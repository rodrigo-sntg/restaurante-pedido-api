package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatusRecebidoTest {
	@Test
	void shouldThrowsExceptionWhenVoldarStatusIsInvalido() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusRecebido())
				.build();
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			pedido.voltarStatus();
		});

		assertEquals(StatusMensagemException.PEDIDO_RECEBIDO_ALTERACAO_INDEVIDA.getMensagem(), exception.getMessage());
	}
}
