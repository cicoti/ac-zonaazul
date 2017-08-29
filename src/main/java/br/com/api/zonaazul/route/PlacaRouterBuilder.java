package br.com.api.zonaazul.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.zonaazul.dto.Placa;
import br.com.api.zonaazul.dto.RespostaErro;
import br.com.api.zonaazul.dto.Usuario;

@Component
public class PlacaRouterBuilder extends RouteBuilder  { 
	
	
	@Override
	public void configure() throws Exception {
	
        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");

        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("v1/placa/usuario")
							.outType(Placa.class)
								.to("direct:pesquisarPlacaUsuario");
        
        from("direct:pesquisarPlacaUsuario")
        
          .log("Verifica se a placa está vinculada ao usuario." + body())
          
	        .doTry()
	        .to("zonaAzulDB:selectPlacaUsuario?StatementType=SelectOne")
	        .process(exchange -> {
	        	Placa placa  = null;
	        	try{
	        		
	        		placa = exchange.getIn().getBody(Placa.class);
	        		exchange.getIn().setBody(placa);
	        		
	        	}catch (Exception e) {

	        		 RespostaErro respostaErro = new RespostaErro("PlacaRouterBuilder:pesquisarPlacaUsuario"
								, "EN-PLA-001"
								, "Placa não vinculada a esse usuario."
								, e.getMessage());
	                 exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
	                 exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
	                 exchange.getOut().setBody(respostaErro.toJson());
	        		 
				}
		      })	        
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
	            
                RespostaErro respostaErro = new RespostaErro("PlacaRouterBuilder:pesquisarPlacaUsuario"
														, "ER-PLA-001"
														, "Erro ao pesquisar a placa."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
        .end();
         
	}
		
}

