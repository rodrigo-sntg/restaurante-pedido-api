package com.fiap.techchallenge.pedidos.infraestruture.web.mapper;

import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.PedidoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoMapper {
	private final ItemPedidoMapper itemPedidoMapper;

	public PedidoMapper(ItemPedidoMapper itemPedidoMapper) {
		this.itemPedidoMapper = itemPedidoMapper;
	}

	public PedidoEntity toEntity(Pedido pedido) {
		var pedidoEntity = new PedidoEntity();
		pedidoEntity.setDataCriacao(pedido.getDataCriacao());
		pedidoEntity.setStatus(pedido.getStatusAtual());
		pedidoEntity.setDataAlteracao(pedido.getDataAlteracao());
		pedidoEntity.setItens(itemPedidoMapper.toEntity(pedido.getItens()));
		pedidoEntity.setTotal(pedido.getTotal());
		pedidoEntity.setCodigoPedido(pedido.getCodigoPedido());
		pedidoEntity.setPrevisaoPreparo(pedido.getPrevisaoPreparo());
		if (pedido.getCliente() != null) {
			pedidoEntity.setCliente(ClienteMapper.toEntity(pedido.getCliente()));
		}
		return pedidoEntity;
	}

	public Pedido toDomain(PedidoEntity entity) {
		Cliente cliente = null;
		if (entity.getCliente() != null) {
			cliente = ClienteMapper.toDomain(entity.getCliente());
		}
		return Pedido.builder()
				.itens(itemPedidoMapper.toDomain(entity.getItens()))
				.dataCriacao(entity.getDataCriacao())
				.dataAlteracao(entity.getDataAlteracao())
				.dataCancelamento(entity.getDataCancelamento())
				.formaPagamento(entity.getFormaPagamento())
				.statusPedido(entity.getStatus()
						.getStatusAtual())
				.cliente(cliente)
				.codigoPedido(entity.getCodigoPedido())
				.total(entity.getTotal())
				.previsaoPreparo(entity.getPrevisaoPreparo())
				.build();
	}

	public List<Pedido> toDomain(List<PedidoEntity> pedidoEntities) {
		return pedidoEntities.stream()
				.map(this::toDomain)
				.toList();
	}
}
