package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.domain.exceptions.ItemPedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.RegrasStatus;

import java.time.LocalDateTime;

public class RegrasInicioPedidoUseCase implements RegrasStatus {

	@Override
	public void executarRegras(Pedido pedido) {
		ValidadorHelper.validar(pedido.getItens(),
				() -> new PedidoInvalidoException("Deve ser informado pelo menos um item no pedido!"));

		pedido.getItens()
				.forEach(item -> ValidadorHelper.validar(item.getCodigoProduto(),
						() -> new ItemPedidoInvalidoException("O Item do pedido deve ter um produto!")));

		pedido.iniciarPedido(LocalDateTime.now());

		//		ValidadorHelper.validar(pedido.getDataCriacao(),
		//				() -> new PedidoInvalidoException("A data do pedido deve ser informada!"));

	}
}
