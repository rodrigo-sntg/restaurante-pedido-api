package com.fiap.techchallenge.pedidos.infraestruture.web.externalapi;

import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ItemPedidoCheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoCheckoutDTO;
import com.fiap.techchallenge.pedidos.application.gateway.PagamentoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.infraestruture.configuration.ExternalApiRoutes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PagamentoExternalGatewayImpl implements PagamentoExternalGateway {
	private final ExternalApiRoutes externalApiRoutes;
	private final RestTemplate restTemplate;

	@Override
	public CheckoutDTO fazerCheckout(Pedido pedido) {
		String url = externalApiRoutes.getPagamentoApiUrl() + "/api/checkouts";

		var itens = pedido.getItens()
				.stream()
				.map(item -> ItemPedidoCheckoutDTO.builder()
						.codigoProduto(item.getCodigoProduto())
						.customizacao(item.getCustomizacao())
						.preco(converteParaCentavos(item.getPreco()
								.getValor()))
						.build())
				.toList();

		PedidoCheckoutDTO pedidoDTO = PedidoCheckoutDTO.builder()
				.itens(itens)
				.codigoPedido(pedido.getCodigoPedido())
				.build();

		ResponseEntity<CheckoutDTO> response = restTemplate.postForEntity(url, pedidoDTO,
				CheckoutDTO.class);
		return response.getBody();
	}

	private int converteParaCentavos(Double value) {
		return (int) (value * 100);
	}
}
