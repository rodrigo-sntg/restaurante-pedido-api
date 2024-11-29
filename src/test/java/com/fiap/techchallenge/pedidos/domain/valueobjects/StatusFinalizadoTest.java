package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.application.usecase.RegrasFluxoPedidoUseCase;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusFinalizadoTest {

	@Test
	void shouldThrowsExceptionWhenProximoStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusFinalizado())
				.build();
		assertEquals(StatusPedido.FINALIZADO, pedido.getStatusAtual());
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			pedido.executarRegrasStatus(new RegrasFluxoPedidoUseCase());
			pedido.proximoStatus();
		});
		assertEquals(StatusMensagemException.PEDIDO_JA_FINALIZADO.getMensagem(), exception.getMessage());
	}

	@Test
	void shouldThrowsExceptionWhenVoltarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusFinalizado())
				.build();
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			pedido.voltarStatus();
		});
		assertEquals(StatusMensagemException.PEDIDO_FINALIZADO_STATUS_INDEVIDO.getMensagem(), exception.getMessage());

	}

	@Test
	void shouldNotThrowsExceptionWhenCancelarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusFinalizado())
				.build();
		assertDoesNotThrow(() -> {
			pedido.cancelarPedido();
		});

	}
}
