package com.fiap.techchallenge.pedidos;

import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.infraestruture.configuration.ExternalApiRoutes;
import com.fiap.techchallenge.pedidos.infraestruture.web.externalapi.ProdutoExternalGatewayImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class CucumberSpringConfiguration {
	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private ExternalApiRoutes externalApiRoutes;

	@Bean
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Bean
	public ProdutoExternalGateway produtoExternalGateway() {
		return new ProdutoExternalGatewayImpl(restTemplate, externalApiRoutes);
	}
}
