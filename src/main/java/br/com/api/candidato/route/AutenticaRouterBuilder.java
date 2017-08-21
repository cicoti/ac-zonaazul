package br.com.api.candidato.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.candidato.dto.RespostaErro;
import br.com.api.candidato.dto.Usuario;

@Component
public class AutenticaRouterBuilder extends RouteBuilder  { 
	
	
	@Override
	public void configure() throws Exception {
	
        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");

        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/autentica")
							.outType(Usuario.class)
								.to("direct:pesquisarUsuario");
        
        from("direct:pesquisarUsuario")
        
          .log("Pesquisar usuario para autenticar." + body())
          
	        .doTry()
	        .to("zonaAzulDB:selectUsuario?StatementType=SelectOne")
	        .process(exchange -> {
	        	Usuario usuario  = null;
	        	try{
	        		
	        		usuario = exchange.getIn().getBody(Usuario.class);
	        		exchange.getIn().setBody(usuario);
	      	        		        		
	        	}catch (Exception e) {
	 		        
	        		 RespostaErro respostaErro = new RespostaErro("AutenticaRouterBuilder:pesquisarUsuario"
								, "EBZA-001"
								, "E-mail ou senha não encontrado."
								, e.getMessage());
	                 exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
	                 exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
	                 exchange.getOut().setBody(respostaErro.toJson());
	        		 
				}
		        
		      })	        
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                RespostaErro respostaErro = new RespostaErro("AutenticaRouterBuilder:pesquisarUsuario"
														, "ERZA-001"
														, "Erro ao pesquisar dados do usuario para autenticar acesso."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
        .end();
	}
}

