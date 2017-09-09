package br.com.api.zonaazul.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.zonaazul.dto.RespostaErro;

@Component
public class SolicitaRouterBuilder extends RouteBuilder  { 
	
	
	@Override
	public void configure() throws Exception {
	
        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");

        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/solicita/efetivar")
							.outType(String.class)
								.to("direct:efetivarSolicitacao");
        
        from("direct:efetivarSolicitacao")
        
          .log("Gravar na base solicitacao de vaga." + body())
          
	        .doTry()
	        .to("zonaAzulDB:insertSolicita?StatementType=Insert")
	        .process(exchange -> {
	        		exchange.getIn().setBody("{}");
		      })
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
	            	throwable.printStackTrace();
                RespostaErro respostaErro = new RespostaErro("SolicitaRouterBuilder:efetivarSolicitacao"
														, "ER-SOL-001"
														, "Erro ao efetivar a solicitação."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
		    
        .end();
	}
}

