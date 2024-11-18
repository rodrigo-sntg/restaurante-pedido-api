package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.application.usecase.RegrasFluxoPagamentoUseCase;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusAguardandoPagamentoTest {
	@Test
	void shouldNotThrowsExceptionWhenProximoStatusIsCalled(){
		var pedido = Pedido.builder()
				.statusPedido(new StatusAguardandoPagamento())
				.build();

		assertDoesNotThrow(()->{
			pedido.executarRegrasStatus(new RegrasFluxoPagamentoUseCase());
			pedido.proximoStatus();
		});
	}
	@Test
	void shouldNotThrowsExceptionWhenCancelarStatusIsCalled(){
		var pedido = Pedido.builder()
				.statusPedido(new StatusAguardandoPagamento())
				.build();

		assertDoesNotThrow(()->{
			pedido.cancelarPedido();
		});
	}

	@Test
	void shouldThrowsExceptionWhenVoltarStatusIsCalled(){
		var pedido = Pedido.builder()
				.statusPedido(new StatusAguardandoPagamento())
				.build();

		var exception = assertThrows(StatusInvalidoException.class, ()->{
			pedido.voltarStatus();
		});

		assertEquals(StatusMensagemException.PEDIDO_FINALIZADO_RECEBIDO_INDEVIDO.getMensagem(), exception.getMessage());
	}
}
