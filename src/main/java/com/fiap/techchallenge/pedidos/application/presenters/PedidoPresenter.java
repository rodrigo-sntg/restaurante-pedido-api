package com.fiap.techchallenge.pedidos.application.presenters;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoPresenter implements ApplicationPresenter<PedidoDTO> {

	private final ApplicationPresenter<ClienteDTO> clientePresenter;
	private final ProdutoExternalGateway produtoExternalGateway;

	public PedidoPresenter(ApplicationPresenter<ClienteDTO> clientePresenter,
			ProdutoExternalGateway produtoExternalGateway) {
		this.clientePresenter = clientePresenter;
		this.produtoExternalGateway = produtoExternalGateway;
	}

	@Override
	public PedidoDTO toModelView(Object object) {
		if (!(object instanceof Pedido pedido)) {
			return null;
		}
		PedidoDTO pedidoViewModel = new PedidoDTO();
		pedidoViewModel.setCodigoPedido(pedido.getCodigoPedido());
		pedidoViewModel.setTotal(pedido.getTotal());
		pedidoViewModel.setStatus(tratarStatus(pedido.getStatusAtual()));
		pedidoViewModel.setCliente(
				pedido.getCliente() != null ? clientePresenter.toModelView(pedido.getCliente()) : null);
		pedidoViewModel.setDataAlteracao(pedido.getDataAlteracao());
		pedidoViewModel.setPrevisaoPreparo(formatarData(pedido.getPrevisaoPreparo()));
		pedidoViewModel.setItens(toListViewModel(pedido.getItens()));

		return pedidoViewModel;
	}

	private String formatarData(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH:mm");
		return formater.format(localDateTime);
	}

	private String tratarStatus(StatusPedido statusPedido) {
		String status = statusPedido.name()
				.replace("_", " ");
		return StringUtils.capitalize(status.toLowerCase());
	}

	private List<ItemPedidoDTO> toListViewModel(List<ItemPedido> itensPedido) {
		return itensPedido.stream()
				.map(item -> {
					ProdutoDTO produtoDTO = produtoExternalGateway.buscarProdutoPeloCodigo(item.getCodigoProduto());
					ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();
					itemPedidoDTO.setCustomizacao(item.getCustomizacao());
					itemPedidoDTO.setNomeProduto(produtoDTO.getNome());
					itemPedidoDTO.setPreco(item.getPreco()
							.getValor());
					itemPedidoDTO.setDescricao(produtoDTO.getDescricao());

					return itemPedidoDTO;
				})
				.toList();
	}
}
