package com.fiap.techchallenge.pedidos.infraestruture.web.externalapi;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.infraestruture.configuration.ExternalApiRoutes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoExternalGatewayImpl implements ProdutoExternalGateway {
	private final RestTemplate restTemplate;

	private final ExternalApiRoutes externalApiRoutes;

	public ProdutoDTO buscarProdutoPeloCodigo(String codigo) {
		String url = externalApiRoutes.getProdutoApiUrl() + "/produtos/" + codigo;
		log.info("Buscando produto na API externa: {}", url);
		ResponseEntity<ProdutoDTO> response = restTemplate.getForEntity(url, ProdutoDTO.class);
		return response.getBody();
	}
}
