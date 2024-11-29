package com.fiap.techchallenge.pedidos.bdd;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PedidoSteps {
	private static final String BASE_URL = "http://localhost:8080/api/pedidos";
	private CadastroPedidoDTO pedido;
	private List<CadastroItemPedidoDTO> itensPedido;
	private Response response;
	private String cpf;
	private String pedidoDTO;

	@Dado("um cliente")
	public void um_cliente() {
		itensPedido = new ArrayList<>();
		cpf = "409.265.000-07";
	}

	@Quando("eu adiciono itens ao pedido")
	public void eu_adiciono_itens_ao_pedido() {
		itensPedido.add(CadastroItemPedidoDTO.builder()
				.codigoProduto("prod1")
				.customizacao("Sem cebola")
				.build());

		itensPedido.add(CadastroItemPedidoDTO.builder()
				.codigoProduto("prod2")
				.customizacao("sem gelo")
				.build());

	}

	@Quando("eu confirmo o pedido")
	public void eu_confirmo_o_pedido() {
		pedido = CadastroPedidoDTO.builder()
				.cpf(cpf)
				.itens(itensPedido)
				.build();
		response = given()
				.contentType("application/json")
				.body(pedido)
				.post(BASE_URL);
	}


	@Então("o pedido deve ser criado com sucesso")
	public void o_pedido_deve_ser_criado_com_sucesso() {
		boolean valido = response.getStatusCode() == 200;
		Assertions.assertTrue(valido);
	}
}
