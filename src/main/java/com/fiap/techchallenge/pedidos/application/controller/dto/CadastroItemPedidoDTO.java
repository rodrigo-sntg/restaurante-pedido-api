package com.fiap.techchallenge.pedidos.application.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroItemPedidoDTO {
	private String customizacao;
	@NotBlank
	private String codigoProduto;
}
