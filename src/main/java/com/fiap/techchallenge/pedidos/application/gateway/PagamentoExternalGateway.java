package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public interface PagamentoExternalGateway {
	CheckoutDTO fazerCheckout(Pedido pedido);
}
