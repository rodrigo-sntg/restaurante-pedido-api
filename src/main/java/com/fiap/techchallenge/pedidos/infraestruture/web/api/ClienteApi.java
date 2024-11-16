package com.fiap.techchallenge.pedidos.infraestruture.web.api;

import com.fiap.techchallenge.pedidos.application.controller.ClienteController;
import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteApi {
	private final ClienteController clienteController;

	@Autowired
	public ClienteApi(ClienteController clienteController) {
		this.clienteController = clienteController;
	}

	@PostMapping
	@Operation(summary = "Cria um novo cliente", description = "Adiciona um novo cliente ao sistema e retorna o cliente criado.", responses = {
			@ApiResponse(responseCode = "200", description = "Cliente criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class), examples = @ExampleObject(name = "Exemplo de Cliente", value = "{\"nome\": \"João Silva\", \"cpf\": \"12345678900\", \"email\": \"joao.silva@example.com\"}"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<ClienteDTO> criarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
		return ResponseEntity.ok(clienteController.criarCliente(clienteDTO));
	}

	@GetMapping
	@Operation(summary = "Busca todos os clientes", description = "Retorna uma lista de todos os clientes registrados no sistema.")
	@ApiResponse(responseCode = "200", description = "Operação bem-sucedida", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClienteDTO.class))))
	public ResponseEntity<List<ClienteDTO>> buscarTodosClientes() {
		return ResponseEntity.ok(clienteController.buscarClientes());
	}
}
