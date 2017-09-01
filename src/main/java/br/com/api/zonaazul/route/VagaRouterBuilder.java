package br.com.api.zonaazul.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.zonaazul.dto.RespostaErro;
import br.com.api.zonaazul.dto.Vaga;
import br.com.api.zonaazul.dto.Venda;

@Component
public class VagaRouterBuilder extends RouteBuilder  { 
	
	
	@Override
	public void configure() throws Exception {
	
        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");

        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/vaga/livre")
							.outType(Venda.class)
								.to("direct:vagaLivre");
        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/vaga")
							.outType(Venda.class)
								.to("direct:vaga");
        
        from("direct:vagaLivre")
        
          .log("Pesquisa vagas livres nesse momento." + body())
          
	        .doTry()
	        .to("zonaAzulDB:selectVagaLivre?StatementType=SelectList")
	        .process(exchange -> {
	        	
	        		ArrayList<Vaga> listaVagaLivre = (ArrayList<Vaga>) exchange.getIn().getBody();
	        		exchange.getIn().setBody(listaVagaLivre);
		        
		      })	        
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                RespostaErro respostaErro = new RespostaErro("VagaRouterBuilder:vagaLivre"
														, "ER-VAG-001"
														, "Erro ao pesquisar vagas."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
        .end();
        
        
        
        
        from("direct:vaga")
        
        .log("Pesquisa todas vagas cadastradas e ativas." + body())
        
	        .doTry()
	        .to("zonaAzulDB:selectVaga?StatementType=SelectList")
	        .process(exchange -> {

	        		ArrayList<Vaga> listaVaga = (ArrayList<Vaga>) exchange.getIn().getBody();
	        		exchange.getIn().setBody(listaVaga);
		        
		      })	        
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
              RespostaErro respostaErro = new RespostaErro("VagaRouterBuilder:vaga"
														, "ER-VAG-002"
														, "Erro ao pesquisar vagas."
														, throwable.getMessage());
              exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
              exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
              exchange.getOut().setBody(respostaErro.toJson());
            })
		    .end()
      .end();
        
        
        
	}
	
	
}

