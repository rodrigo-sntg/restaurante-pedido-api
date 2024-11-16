package com.fiap.techchallenge.pedidos.infraestruture.persistence.entities;

import com.fiap.techchallenge.pedidos.domain.valueobjects.FormaPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedidos")
public class PedidoEntity {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pedido")
	private List<ItemPedidoEntity> itens;

	private Double total;

	@Enumerated(EnumType.STRING)
	private StatusPedido status;

	@ManyToOne
	@JoinColumn(name = "clienteid")
	private ClienteEntity cliente;

	@Column(name = "codigopedido")
	private String codigoPedido;

	@Column(name = "datacriacao")
	private LocalDateTime dataCriacao;

	@Column(name = "dataalteracao")
	private LocalDateTime dataAlteracao;

	@Column(name = "datacancelamento")
	private LocalDateTime dataCancelamento;

	@Column(name = "previsaopreparo")
	private LocalDateTime previsaoPreparo;

	@Column(name = "formapagamento")
	private FormaPagamento formaPagamento;

//	@PrePersist
//	protected void onCreate() {
//		String key;
//		if (cliente != null)
//			key = cliente.getCpf();
//		else {
//			key = RandomStringUtils.randomNumeric(11);
//		}
//		this.codigoPedido = UniqueCodeGenerator.generateUniqueCode(key);
//	}
}
