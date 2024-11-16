package com.fiap.techchallenge.pedidos.application.controller;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;

import java.util.List;

public interface ClienteController {
	ClienteDTO criarCliente(ClienteDTO inputData);

	List<ClienteDTO> buscarClientes();
}
