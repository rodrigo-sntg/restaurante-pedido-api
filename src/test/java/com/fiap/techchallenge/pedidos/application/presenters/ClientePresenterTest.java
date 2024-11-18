package com.fiap.techchallenge.pedidos.application.presenters;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClientePresenterTest {
	private AutoCloseable openMocks;
	@InjectMocks
	private ClientePresenter presenter;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void shouldReturnValidDataWhenCreateClientePresenter() {
		Cliente cliente = new Cliente(new Nome("Nome"), new CPF("63656155070"), new Email("email@domain.com"));
		ClienteDTO dto = presenter.toModelView(cliente);
		assertEquals(dto.getNome(), cliente.getNome().getValor() );
	}

	@Test
	void shouldReturnNullWhenNotValidInstance() {
		Object object = new Object();
		ClienteDTO dto = presenter.toModelView(object);
		assertNull(dto);
	}
}
