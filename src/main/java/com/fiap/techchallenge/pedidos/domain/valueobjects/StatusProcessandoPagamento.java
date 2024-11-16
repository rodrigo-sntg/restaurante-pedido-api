package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusProcessandoPagamento implements IStatusPedido{
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		pedido.estimarPrazoEntrega();
		return new StatusRecebido();
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusAguardandoPagamento();
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		pedido.atualizarDataCancelada();
		return new StatusCancelado();
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.PROCESSANDO_PAGAMENTO;
	}
}
