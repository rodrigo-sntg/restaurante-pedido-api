package com.fiap.techchallenge.pedidos.infraestruture.web.api;

import com.fiap.techchallenge.pedidos.application.controller.ClienteController;
import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.infraestruture.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClienteApiTest {
	private MockMvc mockMvc;
	@Mock
	private ClienteController clienteController;
	private AutoCloseable openMocks;


	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
		ClienteApi controller = new ClienteApi(clienteController);

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
	class CriarCliente {
		@Test
		void shouldReturnClienteDTO_WhenCriarClienteIsCalled_WithValidData() throws Exception {
			ClienteDTO clienteDTO = new ClienteDTO("Elão dos Foguete", "12345678909", "email@domain.com");
			var clienteModelView = getTemplateDTO();

			when(clienteController.criarCliente(any(ClienteDTO.class))).thenReturn(clienteModelView);

			mockMvc.perform(post("/clientes")
							.contentType(MediaType.APPLICATION_JSON)
							.content(JsonHelper.asJsonString(clienteDTO)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.nome").value("Elão dos Foguete"))
					.andExpect(jsonPath("$.cpf").value("12345678909"))
					.andExpect(jsonPath("$.email").value("email@domain.com"));
		}

		@Test
		void shouldThrowRuntimeException_WhenCriarClienteFails() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new RuntimeException("Expected failure."));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678909\",\"email\":\"email@domain.com\"}"))
					.andExpect(status().isInternalServerError());
		}

		@Test
		void shouldReturnInternalServerError_WhenRuntimeExceptionOccurs() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new RuntimeException("Internal server error"));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Test\",\"cpf\":\"98041680062\",\"email\":\"test@example.com\"}"))
					.andExpect(status().isInternalServerError())
					.andExpect(content().string(containsString("Internal server error")));
		}

		@Test
		void shouldReturnBadRequest_WhenDataIntegrityViolationOccurs() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new DataIntegrityViolationException("Erro interno no servidor."));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Test\",\"cpf\":\"98041680062\",\"email\":\"test@example.com\"}"))
					.andExpect(
							status().isInternalServerError()) // Verifying if status is 500 due to internal server error
					.andExpect(content().string(containsString("Erro interno no servidor.")));
		}

	}

	@Nested
	class BuscarTodosClientes {
		@Test
		void shouldReturnClientesDTO_WhenBuscarTodosClientesIsCalled() throws Exception {

			var cliente1 = getTemplateDTO();
			var cliente2 = new ClienteDTO();
			cliente2.setNome("John Doe");
			when(clienteController.buscarClientes()).thenReturn(Arrays.asList(cliente1, cliente2));

			mockMvc.perform(get("/clientes"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(2)))
					.andExpect(jsonPath("$[0].nome").value("Elão dos Foguete"))
					.andExpect(jsonPath("$[1].nome").value("John Doe"));
		}

		@Test
		void shouldReturnEmptyList_WhenNoClientesAreFound() throws Exception {
			when(clienteController.buscarClientes()).thenReturn(Arrays.asList());

			mockMvc.perform(get("/clientes"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(0)));
		}
	}

	@Nested
	class ExceptionHandler {

//		@Test
		void shouldReturnBadRequest_WhenValidationFails() throws Exception {
			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content(
									"{\"nome\":\"Elão dos Foguete\",\"cpf\":\"invalid\",\"email\":\"invalid\"}")) // intentionally bad data
					.andExpect(status().isBadRequest());
		}

		@Test
		void shouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new NegativeArraySizeException("Erro interno no servidor: Unexpected error"));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678909\",\"email\":\"email@domain.com\"}"))
					.andExpect(status().isInternalServerError())
					.andExpect(content().string(containsString("Erro interno no servidor: Unexpected error")));
		}

		@Test
		void shouldReturnConflict_WhenDataIntegrityViolationOccurs_WithCPf() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new DataIntegrityViolationException("uk_cpf"));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678909\",\"email\":\"email@domain.com\"}"))
					.andExpect(status().isConflict())
					.andExpect(content().string(containsString("Erro: O CPF já está cadastrado.")));
		}

		@Test
		void shouldReturnConflict_WhenDataIntegrityViolationOccurs_WithEmail() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new DataIntegrityViolationException("uk_email"));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678909\",\"email\":\"email@domain.com\"}"))
					.andExpect(status().isConflict())
					.andExpect(content().string(containsString("Erro: O Email já está cadastrado.")));
		}

		@Test
		void shouldReturnBadRequest_WhenHttpMessageNotReadable() throws Exception {
			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{malformed json")) // Intentionally malformed JSON
					.andExpect(status().isBadRequest())
					.andExpect(content().string(containsString("Erro de leitura do JSON")));
		}

//		@Test
		void shouldReturnBadRequest_WhenCPFInvalido() throws Exception {

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678901\",\"email\":\"email@domain.com\"}"))
					.andExpect(status().isBadRequest())
					.andExpect(content().string(containsString("CPF inválido")));
		}

		@Test
		void shouldReturnInternalServerError_WhenUnexpectedExceptionOccurs() throws Exception {
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(
					new RuntimeException("Erro interno no servidor"));

			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content("{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678909\",\"email\":\"email@domain.com\"}"))
					.andExpect(status().isInternalServerError())
					.andExpect(content().string(containsString("Erro interno no servidor")));
		}

		@Test
		void shouldReturnBadRequest_WhenCauseIsNotNumberFormatException() throws Exception {
			// Simulate an exception with a different cause
			MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(null, null, null, null,
					new IllegalArgumentException("Invalid argument"));
			when(clienteController.criarCliente(any(ClienteDTO.class))).thenThrow(ex);

			// Perform the request and verify outcomes
			mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
							.content(
									"{\"nome\":\"Elão dos Foguete\",\"cpf\":\"12345678909\",\"email\":\"email@domain.com\"}")) // Data that does not trigger NumberFormatException
					.andExpect(status().isBadRequest())
					.andExpect(content().string(containsString("Erro de solicitação inválida.")));
		}

	}

	private ClienteDTO getTemplateDTO(){
		var cliente = new ClienteDTO();
		cliente.setCpf("12345678909");
		cliente.setNome("Elão dos Foguete");
		cliente.setEmail("email@domain.com");
		return cliente;
	}
}
