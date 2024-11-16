package com.fiap.techchallenge.pedidos.infraestruture.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clientes", uniqueConstraints = {
		@UniqueConstraint(name = "UK_cpf", columnNames = "cpf"),
		@UniqueConstraint(name = "UK_email", columnNames = "email")
})
public class ClienteEntity {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column
	private String cpf;

	@Column
	private String email;

	public ClienteEntity(Long id, String nome, String cpf, String email) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
	}

	@CreationTimestamp
	private Date createdAt;

}
