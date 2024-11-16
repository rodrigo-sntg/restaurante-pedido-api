package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteGateway {
	Cliente salvarCliente(Cliente cliente);

	Optional<Cliente> buscarClientePorId(Long id);

	Optional<Cliente> buscarClientePorCpf(String cpf);

	Optional<Cliente> buscarClientePorEmail(String email);

	List<Cliente> buscarClientes();
}
