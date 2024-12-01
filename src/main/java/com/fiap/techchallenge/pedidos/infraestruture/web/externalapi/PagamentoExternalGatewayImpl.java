package com.fiap.techchallenge.pedidos.infraestruture.web.externalapi;

import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ItemPedidoCheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoCheckoutDTO;
import com.fiap.techchallenge.pedidos.application.gateway.PagamentoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.infraestruture.configuration.ExternalApiRoutes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoExternalGatewayImpl implements PagamentoExternalGateway {
	private final ExternalApiRoutes externalApiRoutes;
	private final RestTemplate restTemplate;

	@Override
	public CheckoutDTO fazerCheckout(Pedido pedido) {
		String url = externalApiRoutes.getPagamentoApiUrl() + "/api/checkouts";
		log.info("Fazendo checkout na API externa: {}", url);
		var itens = pedido.getItens()
				.stream()
				.map(item -> ItemPedidoCheckoutDTO.builder()
						.codigoProduto(item.getCodigoProduto())
						.customizacao(item.getCustomizacao())
						.preco(converteParaCentavos(item.getPreco()
								.getValor()))
						.nome(item.getNomeProduto())
						.build())
				.toList();

		PedidoCheckoutDTO pedidoDTO = PedidoCheckoutDTO.builder()
				.itens(itens)
				.codigoPedido(pedido.getCodigoPedido())
				.build();

		ResponseEntity<CheckoutDTO> response = restTemplate.postForEntity(url, pedidoDTO,
				CheckoutDTO.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		throw new RuntimeException("Erro ao fazer checkout");
	}

	private int converteParaCentavos(Double value) {
		return (int) (value * 100);
	}
}
