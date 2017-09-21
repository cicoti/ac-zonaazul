package br.com.api.zonaazul.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import br.com.api.zonaazul.dto.Compra;
import br.com.api.zonaazul.dto.RespostaErro;

@Component
public class CompraRouterBuilder extends RouteBuilder  {


	@Override
	public void configure() throws Exception {

        restConfiguration().component("servlet")
													 .bindingMode(RestBindingMode.json)
													 .dataFormatProperty("prettyPrint", "true");


        rest("/zonaazul").consumes("application/json")
							.produces("application/json")
							.post("/v1/compra/saldo")
							.outType(Compra.class)
								.to("direct:saldoCompra");

        from("direct:saldoCompra")

          	.log("Pesquisar saldo de compra do usuario." + body())
	        .doTry()
	        .to("zonaAzulDB:selectSaldoCompraPorUsuario?StatementType=SelectOne")
	        .log("Resultado pesquisa saldo de compra do usuario." + body())
	        .process(exchange -> {
	        	Compra compra  = null;
	        	try{

	        		compra = exchange.getIn().getBody(Compra.class);
	        		exchange.getIn().setBody(compra);

	        	}catch (Exception e) {
	        		 RespostaErro respostaErro = new RespostaErro("CompraRouterBuilder:saldoCompra"
								, "EN-COM-001"
								, "Saldo n\u00e3o est\u00e1 disponivel."
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
														, "ER-COM-001"
														, "Erro ao pesquisar dados de compra do usuario."
														, throwable.getMessage());
                exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getOut().setBody(respostaErro.toJson());
              })
		    .end()
        .end();
	}
}

