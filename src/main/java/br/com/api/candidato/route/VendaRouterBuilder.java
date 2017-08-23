package br.com.api.candidato.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.candidato.dto.Compra;
import br.com.api.candidato.dto.RespostaErro;
import br.com.api.candidato.dto.Usuario;
import br.com.api.candidato.dto.Venda;

@Component
public class VendaRouterBuilder extends RouteBuilder  { 
	
	
	@Override
	public void configure() throws Exception {
	
        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");

        
        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/venda/saldo")
							.outType(Venda.class)
								.to("direct:saldoVenda");
        
        from("direct:saldoVenda")
        
          .log("Pesquisar saldo de venda do usuario." + body())
          
	        .doTry()
	        .to("zonaAzulDB:selectSaldoVendaPorUsuario?StatementType=SelectOne")
	        .process(exchange -> {
	        	Venda venda  = null;
	        	try{
	        		
	        		venda = exchange.getIn().getBody(Venda.class);
	        		exchange.getIn().setBody(venda);
	      	        		        		
	        	}catch (Exception e) {
	 		        
	        		 RespostaErro respostaErro = new RespostaErro("VendaRouterBuilder:saldoVenda"
								, "EBZA-003"
								, "O saldo para venda não está disponivel."
								, e.getMessage());
	                 exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
	                 exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
	                 exchange.getOut().setBody(respostaErro.toJson());
	        		 
				}
		        
		      })	        
	        .doCatch(Exception.class)
	            .process(exchange -> {
	            Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                RespostaErro respostaErro = new RespostaErro("CompraRouterBuilder:saldoCompra"
														, "ERZA-003"
														, "Erro ao pesquisar dados de venda do usuario."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
        .end();
	}
}

