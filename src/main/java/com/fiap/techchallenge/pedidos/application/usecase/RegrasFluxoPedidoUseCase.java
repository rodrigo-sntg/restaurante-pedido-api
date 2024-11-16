package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.RegrasStatus;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;

public class RegrasFluxoPedidoUseCase implements RegrasStatus {
	@Override
	public void executarRegras(Pedido pedido) {
		if (pedido.getStatusAtual().equals(StatusPedido.AGUARDANDO_PAGAMENTO)
				|| pedido.getStatusAtual().equals(StatusPedido.PROCESSANDO_PAGAMENTO)) {
			throw new StatusInvalidoException("Fluxo do status pedido invalido.");
		}
	}
}
