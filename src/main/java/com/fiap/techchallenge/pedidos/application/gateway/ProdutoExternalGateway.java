package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;

public interface ProdutoExternalGateway {
	ProdutoDTO buscarProdutoPeloCodigo(String codigo);
}
