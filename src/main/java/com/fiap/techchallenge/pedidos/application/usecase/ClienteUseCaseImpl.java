package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.CPFInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public class ClienteUseCaseImpl implements ClienteUseCase {
	private final ClienteGateway clienteGateway;

	public ClienteUseCaseImpl(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}

	@Override
	public Cliente criarCliente(Cliente cliente) throws CPFInvalidoException {
		// Aqui poderia haver validação ou lógica de negócio antes de salvar
		return clienteGateway.salvarCliente(cliente);
	}

	@Override
	public Optional<Cliente> buscarClientePorId(Long id) {
		return clienteGateway.buscarClientePorId(id);
	}

	@Override
	public Optional<Cliente> buscarClientePorCpf(String cpf) {
		return clienteGateway.buscarClientePorCpf(cpf);
	}

	@Override
	public Optional<Cliente> buscarClientePorEmail(String email) {
		return clienteGateway.buscarClientePorEmail(email);
	}

	@Override
	public List<Cliente> buscarClientes() {
		return clienteGateway.buscarClientes();
	}
}
