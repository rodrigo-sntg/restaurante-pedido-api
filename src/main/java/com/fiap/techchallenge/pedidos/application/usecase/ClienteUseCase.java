package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.domain.exceptions.CPFInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteUseCase {
	Cliente criarCliente(Cliente cliente) throws CPFInvalidoException;

	Optional<Cliente> buscarClientePorId(Long id);

	Optional<Cliente> buscarClientePorCpf(String cpf);

	Optional<Cliente> buscarClientePorEmail(String email);

	List<Cliente> buscarClientes();
}
