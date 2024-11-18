package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.RegrasStatus;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;

public class RegrasFluxoPagamentoUseCase implements RegrasStatus {
	@Override
	public void executarRegras(Pedido pedido) {
		if (pedido.getStatusAtual() != StatusPedido.AGUARDANDO_PAGAMENTO
				&& pedido.getStatusAtual() != StatusPedido.PROCESSANDO_PAGAMENTO) {
			throw new StatusInvalidoException("Status do pedido inválido para checkout.");
		}
	}
}
