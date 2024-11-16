package com.fiap.techchallenge.pedidos.application.controller;

import com.fiap.techchallenge.pedidos.application.presenters.ApplicationPresenter;
import com.fiap.techchallenge.pedidos.application.usecase.ClienteUseCase;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.infraestruture.web.mapper.ClienteMapper;

import java.util.List;

public class ClienteControllerImpl implements ClienteController {
	private final ClienteUseCase clienteUseCase;
	private final ApplicationPresenter<ClienteDTO> clientePresenter;

	public ClienteControllerImpl(ClienteUseCase clienteUseCase, ApplicationPresenter<ClienteDTO> clientePresenter) {
		this.clienteUseCase = clienteUseCase;
		this.clientePresenter = clientePresenter;
	}

	public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
		Cliente cliente = ClienteMapper.toDomain(clienteDTO); // Map DTO to Domain
		Cliente criado = clienteUseCase.criarCliente(cliente); // Create the client
		return clientePresenter.toModelView(criado); // Map Domain back to DTO for response
	}

	@Override
	public List<ClienteDTO> buscarClientes() {
		List<Cliente> todosClientes = clienteUseCase.buscarClientes(); // Chama o método do serviço
		return todosClientes.stream()
				.map(clientePresenter::toModelView)
				.toList();
	}
}
