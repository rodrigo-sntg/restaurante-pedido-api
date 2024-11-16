package com.fiap.techchallenge.pedidos.application.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProdutoDTO {
	private String codigo;
	private String nome;
	private String categoria;
	private Double preco;
	private String descricao;
	private Boolean ativo;
	private Integer tempoPreparoEmMinutos;
	private String imagem;
}
