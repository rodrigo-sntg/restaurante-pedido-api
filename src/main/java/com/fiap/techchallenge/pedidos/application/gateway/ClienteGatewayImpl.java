package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.application.repository.ClienteRepository;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public class ClienteGatewayImpl implements ClienteGateway {
	private final ClienteRepository clienteRepository;

	public ClienteGatewayImpl(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public Cliente salvarCliente(Cliente cliente) {
		return clienteRepository.salvarCliente(cliente);
	}

	@Override
	public Optional<Cliente> buscarClientePorId(Long id) {
		return clienteRepository.buscarClientePorId(id);
	}

	@Override
	public Optional<Cliente> buscarClientePorCpf(String cpf) {
		return clienteRepository.buscarClientePorCpf(cpf);
	}

	@Override
	public Optional<Cliente> buscarClientePorEmail(String email) {
		return clienteRepository.buscarClientePorEmail(email);
	}

	@Override
	public List<Cliente> buscarClientes() {
		return clienteRepository.buscarClientes();
	}
}
