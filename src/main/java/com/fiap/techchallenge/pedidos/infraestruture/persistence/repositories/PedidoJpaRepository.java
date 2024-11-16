package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
	Optional<PedidoEntity> findByCodigoPedido(String codigoPedido);

	List<PedidoEntity> findByStatus(StatusPedido status);

	List<PedidoEntity> findByStatusNot(StatusPedido status);

	List<PedidoEntity> findByStatusInAndDataCancelamentoIsNullOrderByDataCriacaoAsc(EnumSet<StatusPedido> status);
}
