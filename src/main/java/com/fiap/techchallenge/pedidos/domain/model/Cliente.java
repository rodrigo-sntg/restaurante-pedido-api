package com.fiap.techchallenge.pedidos.domain.model;

import com.fiap.techchallenge.pedidos.domain.exceptions.NomeObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import lombok.Getter;

@Getter
public class Cliente {
	private Nome nome;
	private CPF cpf;
	private Email email;

	public Cliente(Nome nome, //
			CPF cpf, //
			Email email) {
		if (nome == null)
			throw new NomeObrigatorioException("Nome não pode ser nulo");
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
	}
}
