package com.fiap.techchallenge.pedidos.infraestruture.web.api;

import com.fiap.techchallenge.pedidos.application.controller.PedidoController;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoNaoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Validated
public class PedidoApi {
	private final PedidoController pedidoController;

	@Autowired
	public PedidoApi(PedidoController pedidoController) {
		this.pedidoController = pedidoController;
	}

	@PostMapping
	@Operation(summary = "Cria novo pedido", description = "Adiciona novo pedido ao sistema e retorna o pedido criado.", responses = {
			@ApiResponse(responseCode = "200", description = "Pedido criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(name = "Exemplo de Pedido", value = "{\"itens\": [{\"customizacao\": \"Sem cebola\", \"codigoProduto\": \"PROD123\"}], \"cpf\": \"12345678900\"}"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<PedidoDTO> criarPedido(@RequestBody @Valid CadastroPedidoDTO dto) {
		PedidoDTO pedidoDTO = pedidoController.criarPedido(dto);
		return ResponseEntity.ok(pedidoDTO);
	}

	@GetMapping("/{codigo}")
	@Operation(summary = "Pesquisa pedido por id", description = "Procura se existe um pedido pelo número do id", responses = {
			@ApiResponse(responseCode = "200", description = "Pedido localizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(name = "Exemplo de Pedido", value = "{\"codigo\": \"PED123\", \"status\": \"RECEBIDO\", \"itens\": [{\"customizacao\": \"Sem cebola\", \"codigoProduto\": \"PROD123\"}], \"cpf\": \"12345678900\"}"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<PedidoDTO> buscarPedido(@PathVariable("codigo") String codigo) {
		var pedido = pedidoController.buscarPedidoPeloCodigo(codigo)
				.orElseThrow(() -> new PedidoNaoEncontradoException(
						"Não foi possível encontrar o pedido com esse id: " + codigo));
		return ResponseEntity.ok(pedido);
	}

	@GetMapping
	@Operation(summary = "Pesquisa todos os pedidos", description = "Retorna todos os pedidos existentes no banco", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de pedidos localizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(name = "Exemplo de Lista de Pedidos", value = "[{\"codigo\": \"PED123\", \"status\": \"RECEBIDO\", \"itens\": [{\"customizacao\": \"Sem cebola\", \"codigoProduto\": \"PROD123\"}], \"cpf\": \"12345678900\"}, {\"codigo\": \"PED124\", \"status\": \"EM_ELABORACAO\", \"itens\": [{\"customizacao\": \"Extra queijo\", \"codigoProduto\": \"PROD124\"}], \"cpf\": \"98765432100\"}]"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<List<PedidoDTO>> buscarPedidos(
			@RequestParam(required = false, value = "status") String status) {
		List<PedidoDTO> pedidoViewModels = pedidoController.buscarPedidos(status);
		return ResponseEntity.ok(pedidoViewModels);
	}

	@GetMapping("/em-progresso")
	@Operation(summary = "Busca pedidos em progresso", description = "Retorna pedidos com status Recebido, Em Preparação ou Pronto", responses = {
			@ApiResponse(responseCode = "200", description = "Pedidos localizados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(name = "Exemplo de Pedidos em Progresso", value = "[{\"codigo\": \"PED123\", \"status\": \"RECEBIDO\", \"itens\": [{\"customizacao\": \"Sem cebola\", \"codigoProduto\": \"PROD123\"}], \"cpf\": \"12345678900\"}, {\"codigo\": \"PED124\", \"status\": \"EM_ELABORACAO\", \"itens\": [{\"customizacao\": \"Extra queijo\", \"codigoProduto\": \"PROD124\"}], \"cpf\": \"98765432100\"}]"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<List<PedidoDTO>> buscarPedidosEmProgresso() {
		List<PedidoDTO> pedidoViewModels = pedidoController.buscarPedidosEmProgresso();
		return ResponseEntity.ok(pedidoViewModels);
	}

	@DeleteMapping("/{codigo}")
	@Operation(summary = "Cancela o pedido", description = "Cancela o pedido", responses = {
			@ApiResponse(responseCode = "204", description = "Pedido alterado para status cancelado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<Void> cancelarPedido(@PathVariable("codigo") String codigo) {
		pedidoController.cancelarPedido(codigo);
		return ResponseEntity.noContent()
				.build();
	}

	@PatchMapping("/{codigo}")
	@Operation(summary = "Atualiza o status do pedido", description = "Retorna o objeto com a informação atualizada", responses = {
			@ApiResponse(responseCode = "200", description = "Atualiza o status do pedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(name = "Exemplo de Pedido Atualizado", value = "{\"codigo\": \"PED123\", \"status\": \"PRONTO\", \"itens\": [{\"customizacao\": \"Sem cebola\", \"codigoProduto\": \"PROD123\"}], \"cpf\": \"12345678900\"}"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<PedidoDTO> atualizarStatus(@PathVariable("codigo") String codigo) {
		var pedido = pedidoController.atualizarStatusPedidoPago(codigo);
		return ResponseEntity.ok(pedido);
	}

	@PatchMapping("/{codigo}/voltar")
	@Operation(summary = "Retorna o status do pedido", description = "Retorna o objeto com a informação atualizada", responses = {
			@ApiResponse(responseCode = "200", description = "Retorna o status do pedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(name = "Exemplo de Pedido com Status Revertido", value = "{\"codigo\": \"PED123\", \"status\": \"EM_ELABORACAO\", \"itens\": [{\"customizacao\": \"Sem cebola\", \"codigoProduto\": \"PROD123\"}], \"cpf\": \"12345678900\"}"))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	public ResponseEntity<PedidoDTO> voltarStatus(@PathVariable("codigo") String codigo) {
		var pedido = pedidoController.voltarStatus(codigo);
		return ResponseEntity.ok(pedido);
	}
}
