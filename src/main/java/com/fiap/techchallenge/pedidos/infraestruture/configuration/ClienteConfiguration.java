package com.fiap.techchallenge.pedidos.infraestruture.configuration;

import com.fiap.techchallenge.pedidos.application.controller.ClienteController;
import com.fiap.techchallenge.pedidos.application.controller.ClienteControllerImpl;
import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.application.gateway.ClienteGatewayImpl;
import com.fiap.techchallenge.pedidos.application.presenters.ApplicationPresenter;
import com.fiap.techchallenge.pedidos.application.presenters.ClientePresenter;
import com.fiap.techchallenge.pedidos.application.repository.ClienteRepository;
import com.fiap.techchallenge.pedidos.application.usecase.ClienteUseCase;
import com.fiap.techchallenge.pedidos.application.usecase.ClienteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteConfiguration {
	@Bean
	public ClienteGateway clienteGateway(ClienteRepository clienteRepository) {
		return new ClienteGatewayImpl(clienteRepository);
	}

	@Bean
	public ClienteUseCase clienteUseCase(ClienteGateway clienteGateway) {
		return new ClienteUseCaseImpl(clienteGateway);
	}

	@Bean
	public ApplicationPresenter<ClienteDTO> clientePresenter() {
		return new ClientePresenter();
	}

	@Bean
	public ClienteController clienteController(ClienteUseCase clienteUseCase,
			ApplicationPresenter<ClienteDTO> clientePresenter) {
		return new ClienteControllerImpl(clienteUseCase, clientePresenter);
	}

}
