package com.fiap.techchallenge.pedidos.domain.model;

import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import lombok.Getter;

@Getter
public class ItemPedido {
	private String customizacao;
	private Preco preco;
	private String codigoProduto;
	private int tempoPreparo;
	private String nomeProduto;

	public ItemPedido(String customizacao, //
			String codigoProduto, //
			Preco preco, //
			int tempoPreparo,
			String nomePedido) {
		this.customizacao = customizacao;
		this.codigoProduto = codigoProduto;
		this.preco = preco;
		this.tempoPreparo = tempoPreparo;
		this.nomeProduto = nomePedido;
	}

	//	public ItemPedido(ProdutoNome nome, //
	//			Preco preco, //
	//			String customizacao, //
	//			String codigoProduto) {
	//		this(new Produto(codigoProduto, nome, null, preco, null, true, null, null), customizacao, codigoProduto);
	//	}

	public Double getValorItem() {
		return this.preco.getValor();
	}
}
