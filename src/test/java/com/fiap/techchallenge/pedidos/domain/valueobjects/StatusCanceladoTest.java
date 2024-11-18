package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.application.usecase.RegrasFluxoPedidoUseCase;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatusCanceladoTest {
	@Test
	void shouldThrowsExceptionWhenProximoStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusCancelado())
				.build();
		assertEquals(StatusPedido.CANCELADO, pedido.getStatusAtual());
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			pedido.executarRegrasStatus(new RegrasFluxoPedidoUseCase());
			pedido.proximoStatus();
		});
		assertEquals(StatusMensagemException.PEDIDO_JA_CANCELADO.getMensagem(), exception.getMessage());
	}

	@Test
	void shouldThrowsExceptionWhenVoltarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusCancelado())
				.build();
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			pedido.voltarStatus();
		});
		assertEquals(StatusMensagemException.PEDIDO_JA_CANCELADO.getMensagem(), exception.getMessage());

	}

	@Test
	void shouldThrowsExceptionWhenCancelarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusCancelado())
				.build();
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			pedido.cancelarPedido();
		});
		assertEquals(StatusMensagemException.PEDIDO_JA_CANCELADO.getMensagem(), exception.getMessage());

	}
}
