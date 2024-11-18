package com.fiap.techchallenge.pedidos.domain.model;

import com.fiap.techchallenge.pedidos.domain.valueobjects.FormaPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.GeradorCodigo;
import com.fiap.techchallenge.pedidos.domain.valueobjects.IStatusPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.RegrasStatus;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusAguardandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class Pedido {
	private List<ItemPedido> itens;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAlteracao;
	private LocalDateTime dataCancelamento;
	private Cliente cliente;
	private String codigoPedido;
	private IStatusPedido statusPedido;
	private FormaPagamento formaPagamento;
	private Double total;
	private LocalDateTime previsaoPreparo;
	private boolean regrasExecutadas;

	public Pedido(List<ItemPedido> itens, //
			LocalDateTime dataCriacao, //
			LocalDateTime dataAlteracao, //
			LocalDateTime dataCancelamento, //
			Cliente cliente, //
			String codigoPedido, //
			IStatusPedido statusPedido, //
			FormaPagamento formaPagamento, //
			Double total, //
			LocalDateTime previsaoPreparo, //
			boolean regrasExecutadas) {
		this.itens = itens;
		this.total = total;
		this.dataCriacao = dataCriacao;
		this.dataAlteracao = dataAlteracao;
		this.dataCancelamento = dataCancelamento;
		this.cliente = cliente;
		this.codigoPedido = codigoPedido;
		this.statusPedido = statusPedido;
		this.formaPagamento = formaPagamento;
		this.previsaoPreparo = previsaoPreparo;
		this.regrasExecutadas = regrasExecutadas;
	}

	public void estimarPrazoEntrega() {
		itens.stream()
				.map(ItemPedido::getTempoPreparo)
				.max(Integer::compare)
				.ifPresent(min -> this.previsaoPreparo = LocalDateTime.now()
						.plusMinutes(min));

	}

	public double calcularTotal()  {
		return itens.stream()
				.map(ItemPedido::getValorItem)
				.reduce(0d, Double::sum);
	}

	public void iniciarPedido(LocalDateTime dataInicio) {
		this.codigoPedido = GeradorCodigo.gerar(RandomStringUtils.randomAlphabetic(5));
		this.dataCriacao = dataInicio;
		this.dataAlteracao = dataInicio;
		this.total = this.calcularTotal();
	}

	public StatusPedido getStatusAtual() {
		return statusPedido.getStatusAtual();
	}

	public void executarRegrasStatus(RegrasStatus regrasStatusUseCase) {
		regrasStatusUseCase.executarRegras(this);
		this.regrasExecutadas = true;
	}

	public void proximoStatus() {
		if (regrasExecutadas) {
			if (statusPedido == null)
				this.statusPedido = new StatusAguardandoPagamento();
			else
				this.statusPedido = statusPedido.proximo(this);
		}
	}

	public void voltarStatus() {
		this.statusPedido = statusPedido.voltar(this);
	}

	public void cancelarPedido() {
		this.statusPedido = this.statusPedido.cancelar(this);
	}

	public void atualizarDataCancelada() {
		this.dataCancelamento = LocalDateTime.now();
	}

	public void atualizarDataAlteracao() {
		this.dataAlteracao = LocalDateTime.now();
	}
}
