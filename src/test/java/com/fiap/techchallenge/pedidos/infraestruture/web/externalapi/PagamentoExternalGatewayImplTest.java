package com.fiap.techchallenge.pedidos.infraestruture.web.externalapi;

import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoCheckoutDTO;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusAguardandoPagamento;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PagamentoExternalGatewayImplTest {
	private AutoCloseable openMocks;

	@InjectMocks
	private PagamentoExternalGatewayImpl pagamentoExternalGateway;
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
	void shouldReturnCheckoutDTOWhenCheckoutWasCalled() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.total(42.4)
				.statusPedido(new StatusAguardandoPagamento())
				.codigoPedido("XPTO")
				.build();

		var checkout = CheckoutDTO.builder()
				.id(1L)
				.payment("CREDIT_CARD")
				.paymentUrl("http://pagamento-url.com")
				.referenceId("XPTO")
				.checkoutId("XPTO")
				.build();

		ResponseEntity<CheckoutDTO> responseEntity = ResponseEntity.status(HttpStatus.OK)
				.header("Content-Type", "application/json")
				.body(checkout);

		when(restTemplate.postForEntity(anyString(), any(PedidoCheckoutDTO.class), eq(CheckoutDTO.class))).thenReturn(
				responseEntity);
		when(externalApiRoutes.getPagamentoApiUrl()).thenReturn("http://localhost:8080");

		CheckoutDTO dto = pagamentoExternalGateway.fazerCheckout(pedido);
		assertNotNull(dto);
		verify(restTemplate).postForEntity(anyString(), any(), any());
	}
}
