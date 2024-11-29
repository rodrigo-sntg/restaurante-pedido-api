package com.fiap.techchallenge.pedidos.infraestruture.web.mapper;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.ItemPedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ItemPedidoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemPedidoMapper {
	private final ProdutoExternalGateway produtoExternalGateway;

	public ItemPedidoEntity toEntity(ItemPedido itemPedido) {
		ProdutoDTO produtoDTO = produtoExternalGateway.buscarProdutoPeloCodigo(itemPedido.getCodigoProduto());
		if (Objects.isNull(produtoDTO)) {
			throw new ItemPedidoInvalidoException("Produto não encontrado!");
		}
		return ItemPedidoEntity.builder()
				.customizacao(itemPedido.getCustomizacao())
				.preco(itemPedido.getValorItem())
				.nome(produtoDTO.getNome())
				.produtoCodigo(produtoDTO.getCodigo())
				.produtoNome(produtoDTO.getNome())
				.build();
	}

	public List<ItemPedidoEntity> toEntity(List<ItemPedido> itensPedido) {
		return itensPedido.stream()
				.map(this::toEntity)
				.toList();
	}

	public List<ItemPedido> toDomain(List<ItemPedidoEntity> entity) {
		return entity.stream()
				.map(this::toDomain)
				.toList();
	}

	public ItemPedido toDomain(ItemPedidoEntity entity) {
		ProdutoDTO produtoDTO = produtoExternalGateway.buscarProdutoPeloCodigo(entity.getProdutoCodigo());
		if (produtoDTO == null) {
			throw new ItemPedidoInvalidoException("Produto não encontrado!");
		}

		return new ItemPedido(entity.getCustomizacao(), produtoDTO.getCodigo(), new Preco(produtoDTO.getPreco()),
				produtoDTO.getTempoPreparoEmMinutos(), produtoDTO.getNome());
	}
}
