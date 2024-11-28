package com.fiap.techchallenge.pedidos.infraestruture.web.externalapi;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.infraestruture.configuration.ExternalApiRoutes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProdutoExternalGatewayImplTest {
	private AutoCloseable openMocks;

	@InjectMocks
	private ProdutoExternalGatewayImpl produtoExternalGateway;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private ExternalApiRoutes externalApiRoutes;


	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void shouldReturnProdutoDTOWhenBuscarProdutoPeloCodigoIsCalledWithValidData() {
		var produto = ProdutoDTO.builder()
				.tempoPreparoEmMinutos(10)
				.preco(10.0)
				.ativo(true)
				.codigo("XPTO")
				.nome("X-Salada")
				.build();
		ResponseEntity responseEntity = new ResponseEntity<>(produto, null, 200);

		when(restTemplate.getForEntity(anyString(), any())).thenReturn(responseEntity);
		when(externalApiRoutes.getProdutoApiUrl()).thenReturn("http://localhost:8080/produtos");

		ProdutoDTO dto = produtoExternalGateway.buscarProdutoPeloCodigo("XPTO");

		assertEquals(dto.getCodigo(), produto.getCodigo());
	}
}
