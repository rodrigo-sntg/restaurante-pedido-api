package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public interface RegrasStatus {
	void executarRegras(Pedido pedido);
}
