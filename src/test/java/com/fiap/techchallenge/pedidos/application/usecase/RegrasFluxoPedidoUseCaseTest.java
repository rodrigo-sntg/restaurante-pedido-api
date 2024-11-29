package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusAguardandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegrasFluxoPedidoUseCaseTest {
	@Test
	void shouldNotThrowsErrorWhenIsValidStatus() {
		var regrasCheckout = new RegrasFluxoPedidoUseCase();
		assertDoesNotThrow(() -> {
			regrasCheckout.executarRegras(Pedido.builder()
					.statusPedido(new StatusRecebido())
					.build());
		});
	}

	@Test
	void shouldThrowsExceptionWhenAStatusAguardandoPagamento() {
		var regrasCheckout = new RegrasFluxoPedidoUseCase();
		var messagemException = "Fluxo do status pedido invalido.";
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			regrasCheckout.executarRegras(Pedido.builder()
					.statusPedido(new StatusAguardandoPagamento())
					.build());
		});

		assertEquals(messagemException, exception.getMessage());
	}
}
