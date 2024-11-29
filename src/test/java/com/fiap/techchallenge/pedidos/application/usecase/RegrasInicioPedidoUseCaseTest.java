package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.domain.exceptions.ItemPedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegrasInicioPedidoUseCaseTest {
	@Test
	void shouldNotThrowsExceptionWhenNewPedido(){
		var produto = getProduto();
		var item = getItemPedido(produto);

		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusRecebido())
				.build();
		RegrasInicioPedidoUseCase regras = new RegrasInicioPedidoUseCase();
		assertDoesNotThrow(()->{
			pedido.executarRegrasStatus(regras);
		});
	}

	@Test
	void shouldThrowsExceptionWhenNewPedido_HasNoItens(){
		var pedido = Pedido.builder()
				.itens(null)
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusRecebido())
				.build();
		RegrasInicioPedidoUseCase regras = new RegrasInicioPedidoUseCase();
		var exception = assertThrows(PedidoInvalidoException.class, () -> {
			pedido.executarRegrasStatus(regras);
		});
		assertEquals("Deve ser informado pelo menos um item no pedido!", exception.getMessage());
	}

	@Test
	void shouldThrowsExceptionWhenNewPedido_WhenCodigoProdutoIsEmpty(){
		var produto = ProdutoDTO.builder()
				.preco(24.4)
				.tempoPreparoEmMinutos(10)
				.build();
		var item = getItemPedido(produto);
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(null)
				.statusPedido(new StatusRecebido())
				.build();
		RegrasInicioPedidoUseCase regras = new RegrasInicioPedidoUseCase();
		var exception = assertThrows(ItemPedidoInvalidoException.class, () -> {
			pedido.executarRegrasStatus(regras);
		});
		assertEquals("O Item do pedido deve ter um produto!", exception.getMessage());
	}

	private ProdutoDTO getProduto() {
		return ProdutoDTO.builder()
				.ativo(true)
				.categoria("LANCHE")
				.descricao("Lanche saboroso")
				.nome("X-Salada")
				.preco(24.4)
				.tempoPreparoEmMinutos(10)
				.codigo("XPTO")
				.build();
	}

	private ItemPedido getItemPedido(ProdutoDTO produto) {
		return new ItemPedido(null, produto.getCodigo(), new Preco(produto.getPreco()),
				produto.getTempoPreparoEmMinutos(), produto.getNome());
	}

}
