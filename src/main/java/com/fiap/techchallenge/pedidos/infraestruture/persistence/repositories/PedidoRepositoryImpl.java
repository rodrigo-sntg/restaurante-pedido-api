package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.application.repository.PedidoRepository;
import com.fiap.techchallenge.pedidos.domain.exceptions.ClienteNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.PedidoEntity;
import com.fiap.techchallenge.pedidos.infraestruture.web.mapper.PedidoMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepositoryImpl implements PedidoRepository {
	private final PedidoJpaRepository repository;
	private final ItemPedidoJpaRepository itemPedidoJpaRepository;
	private final ClienteJpaRepository clienteJpaRepository;
	private final PedidoMapper pedidoMapper;

	@Autowired
	public PedidoRepositoryImpl(PedidoJpaRepository repository, ItemPedidoJpaRepository itemPedidoJpaRepository,
			ClienteJpaRepository clienteJpaRepository, PedidoMapper pedidoMapper) {
		this.repository = repository;
		this.itemPedidoJpaRepository = itemPedidoJpaRepository;
		this.pedidoMapper = pedidoMapper;
		this.clienteJpaRepository = clienteJpaRepository;
	}

	@Override
	@Transactional
	public Pedido salvarPedido(Pedido pedido) {
		PedidoEntity entity = pedidoMapper.toEntity(pedido);

		if (pedido.getCliente() != null) {
			var cliente = clienteJpaRepository.findByCpf(pedido.getCliente()
							.getCpf()
							.getNumero())
					.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado ao atrelar pedido!"));
			entity.setCliente(cliente);
		}

		itemPedidoJpaRepository.saveAll(entity.getItens());
		PedidoEntity saved = repository.save(entity);
		entity.getItens()
				.forEach(item -> item.setPedido(saved));
		return pedidoMapper.toDomain(saved);
	}

	@Override
	public Optional<Pedido> buscarPedidoPeloCodigo(String codigo) {
		var pedido = pesquisarEntidadePeloCodigo(codigo);
		return Optional.of(pedidoMapper.toDomain(pedido));
	}

	@Override
	public Optional<List<Pedido>> buscarPedidos(StatusPedido statusPedido) {
		List<PedidoEntity> listaPedidos;
		if (statusPedido == null)
			listaPedidos = repository.findByStatusNot(StatusPedido.CANCELADO);
		else
			listaPedidos = repository.findByStatus(statusPedido);

		return Optional.of(pedidoMapper.toDomain(listaPedidos));
	}

	@Override
	public Pedido atualizarStatus(Pedido pedido) {
		PedidoEntity pedidoEntity = pesquisarEntidadePeloCodigo(pedido.getCodigoPedido());
		pedidoEntity.setStatus(pedido.getStatusAtual());
		pedidoEntity.setDataAlteracao(pedido.getDataAlteracao());
		pedidoEntity.setPrevisaoPreparo(pedido.getPrevisaoPreparo());
		PedidoEntity updated = repository.save(pedidoEntity);
		return pedidoMapper.toDomain(updated);
	}

	private PedidoEntity pesquisarEntidadePeloCodigo(String codigo) {
		return repository.findByCodigoPedido(codigo)
				.orElseThrow(() -> new PedidoNaoEncontradoException(
						"O Pedido com código " + codigo + " não foi encontrado!"));
	}

	@Override
	public void cancelarPedido(Pedido pedido) {
		PedidoEntity pedidoEntity = pesquisarEntidadePeloCodigo(pedido.getCodigoPedido());
		pedidoEntity.setStatus(pedido.getStatusAtual());
		pedidoEntity.setDataCancelamento(pedido.getDataCancelamento());
		repository.save(pedidoEntity);
	}

	@Override
	public List<Pedido> buscarPedidosPorStatus(EnumSet<StatusPedido> status) {
		return repository.findByStatusInAndDataCancelamentoIsNullOrderByDataCriacaoAsc(status).stream()
				.map(pedidoMapper::toDomain)
				.toList();
	}
}
