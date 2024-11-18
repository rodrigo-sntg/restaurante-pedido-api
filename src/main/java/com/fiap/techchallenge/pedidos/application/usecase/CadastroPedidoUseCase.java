package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PedidoGateway;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.ClienteNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

public class CadastroPedidoUseCase {
	private final PedidoGateway pedidoGateway;
	private final ProdutoExternalGateway produtoGateway;
	private final ClienteGateway clienteGateway;

	public CadastroPedidoUseCase(PedidoGateway pedidoGateway, //
			ProdutoExternalGateway produtoGateway, //
			ClienteGateway clienteGateway) {
		this.pedidoGateway = pedidoGateway;
		this.produtoGateway = produtoGateway;
		this.clienteGateway = clienteGateway;
	}

	public Pedido salvar(CadastroPedidoDTO cadastroPedidoDTO) {
		Pedido pedido = toPedido(cadastroPedidoDTO);
		pedido.executarRegrasStatus(new RegrasInicioPedidoUseCase());
		pedido.proximoStatus();
		return pedidoGateway.salvarPedido(pedido);
	}

	public Pedido toPedido(CadastroPedidoDTO cadastroPedidoDTO) {
		return Pedido.builder()
				.itens(this.toItemPedido(cadastroPedidoDTO.getItens()))
				.cliente(getCliente(cadastroPedidoDTO))
				.build();
	}

	private Cliente getCliente(CadastroPedidoDTO cadastroPedidoDTO) {
		if (!StringUtils.isEmpty(cadastroPedidoDTO.getCpf())) {
			return this.clienteGateway.buscarClientePorCpf(cadastroPedidoDTO.getCpf())
					.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado!"));
		}
		return null;
	}

	public ItemPedido toItemPedido(CadastroItemPedidoDTO cadastroItemPedidoDTO) {
		if (Objects.isNull(cadastroItemPedidoDTO.getCodigoProduto()))
			throw new PedidoInvalidoException("O produto deve ser informado!");
		ProdutoDTO produto = produtoGateway.buscarProdutoPeloCodigo(cadastroItemPedidoDTO.getCodigoProduto());
		if (produto == null)
			throw new PedidoInvalidoException(
					"Produto com o id: " + cadastroItemPedidoDTO.getCodigoProduto() + " não localizado!");
		Preco preco = new Preco(produto.getPreco());
		return new ItemPedido(cadastroItemPedidoDTO.getCustomizacao(), cadastroItemPedidoDTO.getCodigoProduto(), preco,
				produto.getTempoPreparoEmMinutos());
	}

	public List<ItemPedido> toItemPedido(List<CadastroItemPedidoDTO> itensPedidoDTO) {
		return itensPedidoDTO.stream()
				.map(this::toItemPedido)
				.toList();
	}
}
