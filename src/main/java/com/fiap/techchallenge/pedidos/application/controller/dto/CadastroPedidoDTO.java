package com.fiap.techchallenge.pedidos.application.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroPedidoDTO {
	private List<CadastroItemPedidoDTO> itens;

//	@Valid(message = "CPF inválido")
	private String cpf;
}
