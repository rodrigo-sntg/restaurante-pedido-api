package com.fiap.techchallenge.pedidos.infraestruture.web.mapper;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.domain.exceptions.CPFOuEmailObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.exceptions.NomeObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;

import java.util.Optional;

public class ClienteMapper {
	private ClienteMapper() {

	}

	public static Cliente toDomain(ClienteEntity entity) {
		if (entity.getCpf() == null && entity.getEmail() == null) {
			throw new CPFOuEmailObrigatorioException("CPF ou e-mail é obrigatório");
		}

		// Usa Optional para criar objetos de domínio de maneira segura e expressiva
		Nome nome = Optional.ofNullable(entity.getNome())
				.map(Nome::new)
				.orElse(null);
		CPF cpf = Optional.ofNullable(entity.getCpf())
				.map(CPF::new)
				.orElse(null);
		Email email = Optional.ofNullable(entity.getEmail())
				.map(Email::new)
				.orElse(null);

		return new Cliente(nome, cpf, email);
	}

	public static ClienteEntity toEntity(Cliente cliente) {
//		validarInformacoesObrigatorias(cliente);

		ClienteEntity entity = new ClienteEntity();
		Optional.ofNullable(cliente.getNome())
				.map(Nome::getValor)
				.ifPresent(entity::setNome);
		Optional.ofNullable(cliente.getCpf())
				.map(CPF::getNumero)
				.ifPresent(entity::setCpf);
		Optional.ofNullable(cliente.getEmail())
				.map(Email::getEndereco)
				.ifPresent(entity::setEmail);
		return entity;
	}

	public static Cliente toDomain(ClienteDTO dto) {
		if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
			throw new NomeObrigatorioException("Nome não pode ser nulo ou vazio");
		}

		Nome nome = new Nome(dto.getNome());
		CPF cpf = dto.getCpf() != null ? new CPF(dto.getCpf()) : null;
		Email email = dto.getEmail() != null ? new Email(dto.getEmail()) : null;

		return new Cliente(nome, cpf, email);
	}
}
