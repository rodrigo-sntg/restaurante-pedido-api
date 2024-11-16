package com.fiap.techchallenge.pedidos.application.controller.dto;

import com.fiap.techchallenge.pedidos.application.presenters.ModelView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO implements ModelView {
	private String nome;
	private String email;
	private String cpf;
}
