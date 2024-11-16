package com.fiap.techchallenge.pedidos.infraestruture.web.externalapi;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProdutoExternalGatewayImpl implements ProdutoExternalGateway {
	private final RestTemplate restTemplate;

	@Value("${produto.api_url}")
	private String produtoClient;

	public ProdutoDTO buscarProdutoPeloCodigo(String codigo) {
		String url = produtoClient + "/produtos/" + codigo;
		ResponseEntity<ProdutoDTO> response = restTemplate.getForEntity(url, ProdutoDTO.class);
		return response.getBody();
	}
}
