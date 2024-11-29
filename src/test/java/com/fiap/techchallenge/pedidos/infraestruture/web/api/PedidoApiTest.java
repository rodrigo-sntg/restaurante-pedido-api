package com.fiap.techchallenge.pedidos.infraestruture.web.api;

import com.fiap.techchallenge.pedidos.application.controller.PedidoController;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.infraestruture.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PedidoApiTest {
	private MockMvc mockMvc;
	@Mock
	private PedidoController pedidoController;
	private static final String BASE_URL = "/api/pedidos";
	private AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
		PedidoApi controller = new PedidoApi(pedidoController);

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new GlobalExceptionHandler())
				.addFilters(filter)
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Nested
	class CriarPedido {
		@Test
		void shouldCreateValidPedido() throws Exception {
			var pedido = CadastroPedidoDTO.builder()
					.itens(Collections.singletonList(CadastroItemPedidoDTO.builder()
							.codigoProduto("XPTO")
							.build()))
					.build();

			var pedidoModelView = getTemplatePedido();
			when(pedidoController.criarPedido(any(CadastroPedidoDTO.class))).thenReturn(pedidoModelView);

			mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
							.content(JsonHelper.asJsonString(pedido)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.codigoPedido").value("XPTO"))
					.andExpect(jsonPath("$.status").value("Aguardando Pagamento"))
					.andExpect(jsonPath("$.total").value("24.4"))
					.andExpect(jsonPath("$.itens[0].nomeProduto").value("X-Salada"))
					.andExpect(jsonPath("$.itens[0].preco").value("24.4"));
		}
	}

	@Nested
	class BuscarPedidos {

		@Test
		void shouldReturnValidPedido() throws Exception {
			var pedidoModelView = getTemplatePedido();
			var codigo = "XPTO";
			when(pedidoController.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(pedidoModelView));

			mockMvc.perform(get(BASE_URL + "/{codigo}", codigo))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.codigoPedido").value("XPTO"))
					.andExpect(jsonPath("$.status").value("Aguardando Pagamento"))
					.andExpect(jsonPath("$.total").value("24.4"))
					.andExpect(jsonPath("$.itens[0].nomeProduto").value("X-Salada"))
					.andExpect(jsonPath("$.itens[0].preco").value("24.4"));
		}

		@Test
		void shouldReturnPedido_WhenNotFoundThrowsError() throws Exception {
			var codigo = "XPTO";
			when(pedidoController.buscarPedidoPeloCodigo(any())).thenReturn(Optional.empty());

			mockMvc.perform(get(BASE_URL + "/{codigo}", codigo))
					.andDo(print())
					.andExpect(status().isNotFound())
					.andExpect(content().string("Não foi possível encontrar o pedido com esse id: XPTO"));
		}
	}

	@Nested
	class BuscarListaPedidos {
		@Test
		void shouldReturnValidListOfPedidos() throws Exception {
			var pedidoModelView = getTemplatePedido();
			when(pedidoController.buscarPedidos(null)).thenReturn(Collections.singletonList(pedidoModelView));

			mockMvc.perform(get(BASE_URL))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.[0].codigoPedido").value("XPTO"))
					.andExpect(jsonPath("$.[0].status").value("Aguardando Pagamento"))
					.andExpect(jsonPath("$.[0].total").value("24.4"))
					.andExpect(jsonPath("$.[0].itens[0].nomeProduto").value("X-Salada"))
					.andExpect(jsonPath("$.[0].itens[0].preco").value("24.4"));
		}
	}

	@Nested
	class BuscarPedidosEmProgresso {
		@Test
		void shouldReturnValidListOfPedidosStatusEmProgresso() throws Exception {
			var pedidoModelView = getTemplatePedido();
			pedidoModelView.setStatus("Pronto");
			when(pedidoController.buscarPedidosEmProgresso()).thenReturn(Collections.singletonList(pedidoModelView));

			mockMvc.perform(get(BASE_URL + "/em-progresso"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.[0].codigoPedido").value("XPTO"))
					.andExpect(jsonPath("$.[0].status").value("Pronto"))
					.andExpect(jsonPath("$.[0].total").value("24.4"))
					.andExpect(jsonPath("$.[0].itens[0].nomeProduto").value("X-Salada"))
					.andExpect(jsonPath("$.[0].itens[0].preco").value("24.4"));
		}
	}

	@Nested
	class ApagarPedido {
		@Test
		void shouldDeletePedido() throws Exception {
			var codigo = "XPTO";
			doNothing().when(pedidoController)
					.cancelarPedido(any());

			mockMvc.perform(delete(BASE_URL + "/{codigo", codigo))
					.andExpect(status().isNoContent());
		}
	}

	@Nested
	class AtualizarPedido {
		@Test
		void shouldDeletePedido() throws Exception {
			var codigo = "XPTO";
			var pedidoModelView = getTemplatePedido();
			when(pedidoController.atualizarStatusPedidoPago(any(String.class))).thenReturn(pedidoModelView);

			mockMvc.perform(patch(BASE_URL + "/{codigo}", codigo))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.codigoPedido").value("XPTO"))
					.andExpect(jsonPath("$.status").value("Aguardando Pagamento"))
					.andExpect(jsonPath("$.total").value("24.4"))
					.andExpect(jsonPath("$.itens[0].nomeProduto").value("X-Salada"))
					.andExpect(jsonPath("$.itens[0].preco").value("24.4"));
		}
	}

	@Nested
	class VoltarPedidoStatusAnterior {
		@Test
		void shouldDeletePedido() throws Exception {
			var codigo = "XPTO";
			var pedidoDTO = getTemplatePedido();
			when(pedidoController.voltarStatus(any(String.class))).thenReturn(pedidoDTO);

			mockMvc.perform(patch(BASE_URL + "/{codigo}/voltar", codigo))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.codigoPedido").value("XPTO"))
					.andExpect(jsonPath("$.status").value("Aguardando Pagamento"))
					.andExpect(jsonPath("$.total").value("24.4"))
					.andExpect(jsonPath("$.itens[0].nomeProduto").value("X-Salada"))
					.andExpect(jsonPath("$.itens[0].preco").value("24.4"));
		}
	}

	@Nested
	class FazerCheckout {
		@Test
		void shouldDeletePedido() throws Exception {
			var codigo = "XPTO";
			var checkout = CheckoutDTO.builder()
					.id(1L)
					.payment("CREDIT_CARD")
					.paymentUrl("http://pagamento-url.com")
					.referenceId("XPTO")
					.checkoutId("XPTO")
					.build();
			when(pedidoController.checkout(any(String.class))).thenReturn(checkout);

			mockMvc.perform(patch(BASE_URL + "/{codigo}/checkout", codigo))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value("1"))
					.andExpect(jsonPath("$.payment").value("CREDIT_CARD"))
					.andExpect(jsonPath("$.paymentUrl").value("http://pagamento-url.com"))
					.andExpect(jsonPath("$.referenceId").value("XPTO"))
					.andExpect(jsonPath("$.checkoutId").value("XPTO"));
		}
	}

	private PedidoDTO getTemplatePedido() {
		var pedido = new PedidoDTO();
		pedido.setCodigoPedido("XPTO");
		pedido.setTotal(24.4);
		pedido.setStatus("Aguardando Pagamento");
		var itemPedido = new ItemPedidoDTO();
		itemPedido.setNomeProduto("X-Salada");
		itemPedido.setPreco(24.4);
		pedido.setItens(Collections.singletonList(itemPedido));
		return pedido;
	}
}
