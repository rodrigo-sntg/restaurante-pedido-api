package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.application.usecase.RegrasFluxoPagamentoUseCase;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusProcessandoPagamentoTest {
	@Test
	void shouldNotThrowsExceptionWhenProximoStatusIsCalled() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
				.statusPedido(new StatusProcessandoPagamento())
				.codigoPedido("XPTO")
				.build();

		assertEquals(StatusPedido.PROCESSANDO_PAGAMENTO, pedido.getStatusAtual());
		assertDoesNotThrow(() -> {
			pedido.executarRegrasStatus(new RegrasFluxoPagamentoUseCase());
			pedido.proximoStatus();
		});
	}

	@Test
	void shouldNotThrowsExceptionWhenCancelarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusProcessandoPagamento())
				.build();

		assertDoesNotThrow(() -> {
			pedido.cancelarPedido();
		});
	}

	@Test
	void shouldThrowsExceptionWhenVoltarStatusIsCalled() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusProcessandoPagamento())
				.build();

		assertDoesNotThrow(() -> {
			pedido.voltarStatus();
		});

	}
}
