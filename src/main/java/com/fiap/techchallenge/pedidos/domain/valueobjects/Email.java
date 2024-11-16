package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.EmailInvalidoException;
import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;
@EqualsAndHashCode
public class Email {
	private final String endereco;

	// Construtor que inicializa o endereço de email validando-o
	public Email(String endereco) {
		if (!isValidEmail(endereco)) {
			throw new EmailInvalidoException("Endereço de email inválido: " + endereco);
		}
		this.endereco = endereco;
	}

	public String getEndereco() {
		return endereco;
	}

	// Método privado para validar o email
	private static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null) {
			return false;
		}
		return pat.matcher(email).matches();
	}
}
