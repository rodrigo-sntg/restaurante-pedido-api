package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {
	Optional<ClienteEntity> findByCpf(String cpf);
	Optional<ClienteEntity> findByEmail(String email);
}
