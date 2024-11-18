package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.application.usecase.RegrasFluxoPedidoUseCase;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StatusEmElaboracaoTest {
	@Test
	void shouldNotThrowsExceptionWhenProximoStatusIsCalled(){
		var pedido = Pedido.builder()
				.statusPedido(new StatusEmElaboracao())
				.build();

		assertDoesNotThrow(()->{
			pedido.executarRegrasStatus(new RegrasFluxoPedidoUseCase());
			pedido.proximoStatus();
		});
	}

	@Test
	void shouldNotThrowsExceptionWhenCancelarStatusIsCalled(){
		var pedido = Pedido.builder()
				.statusPedido(new StatusEmElaboracao())
				.build();

		assertDoesNotThrow(()->{
			pedido.cancelarPedido();
		});
	}
}
