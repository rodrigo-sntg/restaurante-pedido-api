package com.fiap.techchallenge.pedidos.application.controller.dto;

import com.fiap.techchallenge.pedidos.application.presenters.ModelView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoDTO implements ModelView {
	private List<ItemPedidoDTO> itens;
	private ClienteDTO cliente;
	private Double total;
	private String status;
	private String codigoPedido;
	private LocalDateTime dataAlteracao;
	private String previsaoPreparo;

}
