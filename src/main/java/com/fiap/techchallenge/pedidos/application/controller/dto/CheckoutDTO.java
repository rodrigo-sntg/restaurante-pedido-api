package com.fiap.techchallenge.pedidos.application.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDTO {
	private Long id;
	private String checkoutId;
	private String referenceId;
	private LocalDateTime createdAt;
	private String status;
	private String payment;
	private String paymentUrl;
}
