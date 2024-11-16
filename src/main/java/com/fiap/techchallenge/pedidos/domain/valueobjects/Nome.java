package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.NomeInvalidoException;
import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

@EqualsAndHashCode
public class Nome {
	private final String valor;

	public Nome(String valor) {
		if (!isValidNome(valor)) {
			throw new NomeInvalidoException("Nome inválido: " + valor);
		}
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	// Método privado para validar o email
	private static boolean isValidNome(String email) {
		String nomeRegex = "^[\\p{L} ]+$";
		Pattern pat = Pattern.compile(nomeRegex);
		if (email == null) {
			return false;
		}
		return pat.matcher(email)
				.matches();
	}
}
