package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.application.usecase.RegrasFluxoPedidoUseCase;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusProntoTest {
	@Test
	void shouldNotThrowsExceptionWhenProximoStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusPronto())
				.build();
		assertEquals(StatusPedido.PRONTO, pedido.getStatusAtual());
		assertDoesNotThrow(() -> {
			pedido.executarRegrasStatus(new RegrasFluxoPedidoUseCase());
			pedido.proximoStatus();
		});
	}

	@Test
	void shouldNotThrowsExceptionWhenCancelarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusPronto())
				.build();

		assertDoesNotThrow(() -> {
			pedido.cancelarPedido();
		});
	}

	@Test
	void shouldThrowsExceptionWhenVoltarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusPronto())
				.build();

		assertDoesNotThrow(() -> {
			pedido.voltarStatus();
		});

	}
}
