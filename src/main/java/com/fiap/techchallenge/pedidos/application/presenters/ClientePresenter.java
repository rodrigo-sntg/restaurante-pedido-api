package com.fiap.techchallenge.pedidos.application.presenters;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;

import java.util.Optional;

public class ClientePresenter implements ApplicationPresenter<ClienteDTO>{
	@Override
	public ClienteDTO toModelView(Object object) {
		if (!(object instanceof Cliente cliente)) {
			return null; // Considere lançar uma exceção se o contexto exigir
		}

		ClienteDTO dto = new ClienteDTO();
		// Usa Optional para tratar possíveis nulos no nome, CPF e e-mail
		Optional.ofNullable(cliente.getNome())
				.map(Nome::getValor)
				.ifPresent(dto::setNome);
		Optional.ofNullable(cliente.getCpf())
				.map(CPF::getNumero)
				.ifPresent(dto::setCpf);
		Optional.ofNullable(cliente.getEmail())
				.map(Email::getEndereco)
				.ifPresent(dto::setEmail);
		return dto;
	}
}
