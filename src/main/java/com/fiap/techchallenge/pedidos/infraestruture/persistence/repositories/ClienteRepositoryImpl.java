package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.application.repository.ClienteRepository;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;
import com.fiap.techchallenge.pedidos.infraestruture.web.mapper.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {
	private final ClienteJpaRepository clienteJpaRepository;

	@Autowired
	public ClienteRepositoryImpl(ClienteJpaRepository clienteJpaRepository) {
		this.clienteJpaRepository = clienteJpaRepository;
	}

	@Override
	public Cliente salvarCliente(Cliente cliente) {
		ClienteEntity entity = ClienteMapper.toEntity(cliente);
		entity = clienteJpaRepository.save(entity);
		return ClienteMapper.toDomain(entity);
	}

	@Override
	public Optional<Cliente> buscarClientePorId(Long id) {
		return clienteJpaRepository.findById(id)
				.map(ClienteMapper::toDomain);
	}

	@Override
	public Optional<Cliente> buscarClientePorCpf(String cpf) {
		return clienteJpaRepository.findByCpf(cpf)
				.map(ClienteMapper::toDomain);
	}

	@Override
	public Optional<Cliente> buscarClientePorEmail(String email) {
		return clienteJpaRepository.findByEmail(email)
				.map(ClienteMapper::toDomain);
	}

	public List<Cliente> buscarClientes() {
		return clienteJpaRepository.findAll()
				.stream()
				.map(ClienteMapper::toDomain)
				.toList();
	}
}
