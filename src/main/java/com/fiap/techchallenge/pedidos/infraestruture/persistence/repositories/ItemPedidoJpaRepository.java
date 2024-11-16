package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoJpaRepository extends JpaRepository<ItemPedidoEntity, Long> {
}
