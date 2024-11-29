package com.fiap.techchallenge.pedidos.infraestruture.configuration;

import com.fiap.techchallenge.pedidos.application.controller.PedidoController;
import com.fiap.techchallenge.pedidos.application.controller.PedidoControllerImpl;
import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PagamentoExternalGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PedidoGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PedidoGatewayImpl;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.application.presenters.ApplicationPresenter;
import com.fiap.techchallenge.pedidos.application.presenters.PedidoPresenter;
import com.fiap.techchallenge.pedidos.application.repository.PedidoRepository;
import com.fiap.techchallenge.pedidos.application.usecase.PedidoUseCase;
import com.fiap.techchallenge.pedidos.application.usecase.PedidoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoConfiguration {
	@Bean
	public PedidoGateway pedidoGateway(PedidoRepository pedidoRepository) {
		return new PedidoGatewayImpl(pedidoRepository);
	}

	@Bean
	public PedidoUseCase pedidoUseCase(PedidoGateway pedidoGateway, //
			ProdutoExternalGateway produtoGateway, //
			PagamentoExternalGateway pagamentoGateway, //
			ClienteGateway clienteGateway) {
		return new PedidoUseCaseImpl(pedidoGateway, //
				produtoGateway, //
				pagamentoGateway, //
				clienteGateway);
	}

	@Bean
	public ApplicationPresenter<PedidoDTO> pedidoPresenter(ApplicationPresenter<ClienteDTO> clientePresenter,
			ProdutoExternalGateway produtoExternalGateway) {
		return new PedidoPresenter(clientePresenter, produtoExternalGateway);
	}

	@Bean
	public PedidoController pedidoController(PedidoUseCase pedidoUseCase,
			ApplicationPresenter<PedidoDTO> pedidoPresenter) {
		return new PedidoControllerImpl(pedidoUseCase, pedidoPresenter);
	}
}
