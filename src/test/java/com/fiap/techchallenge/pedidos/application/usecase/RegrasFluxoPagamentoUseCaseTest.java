package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusAguardandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusProcessandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegrasFluxoPagamentoUseCaseTest {
	@Test
	void shouldNotThrowsErrorWhenIsValidStatus() {
		var regrasCheckout = new RegrasFluxoPagamentoUseCase();
		assertDoesNotThrow(() -> {
			regrasCheckout.executarRegras(Pedido.builder()
					.statusPedido(new StatusAguardandoPagamento())
					.build());
		});
	}

	@Test
	void shouldThrowsExceptionWhenAStatusAguardandoPagamento() {
		var regrasCheckout = new RegrasFluxoPagamentoUseCase();
		var messagemException = "Status do pedido inválido para checkout.";
		var exception = assertThrows(StatusInvalidoException.class, () -> {
			regrasCheckout.executarRegras(Pedido.builder()
					.statusPedido(new StatusRecebido())
					.build());
		});

		assertEquals(messagemException, exception.getMessage());
	}
}
