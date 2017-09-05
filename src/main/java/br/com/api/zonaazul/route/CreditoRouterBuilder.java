package br.com.api.zonaazul.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.zonaazul.dto.Credito;
import br.com.api.zonaazul.dto.RespostaErro;

@Component
public class CreditoRouterBuilder extends RouteBuilder  { 
	
	
	@Override
	public void configure() throws Exception {
	
        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");

        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/buscarCredito")
							.outType(Credito.class)
								.to("direct:buscarCredito");
        
        from("direct:buscarCredito")
        
          .log("Pesquisar ultima posição de credito." + body())
          
	        .doTry()
	        .to("zonaAzulDB:selectCredito?StatementType=SelectOne")
	        .process(exchange -> {
	        	Credito credito  = null;
	        	try{
	        		
	        		credito = exchange.getIn().getBody(Credito.class);
	        		exchange.getIn().setBody(credito);
	      	        		        		
	        	}catch (Exception e) {
	 		        
	        		 RespostaErro respostaErro = new RespostaErro("CreditoRouterBuilder:credito"
								, "EN-CRE-001"
								, "Credito não disponivel."
								, e.getMessage());
	                 exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
	                 exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
	                 exchange.getOut().setBody(respostaErro.toJson());
	        		 
				}
		        
		      })	        
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                RespostaErro respostaErro = new RespostaErro("CreditoRouterBuilder:credito"
														, "ER-CRE-001"
														, "Erro ao pesquisar valor do credito."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
        .end();
	}
}

