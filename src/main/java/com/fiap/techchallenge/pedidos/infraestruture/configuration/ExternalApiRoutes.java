package com.fiap.techchallenge.pedidos.infraestruture.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ExternalApiRoutes {
	@Value("${pagamento.api-url}")
	String pagamentoApiUrl;

	@Value("${produto.api_url}")
	private String produtoApiUrl;

}
