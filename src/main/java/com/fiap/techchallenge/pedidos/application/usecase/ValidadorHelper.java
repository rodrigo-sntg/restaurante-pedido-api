package com.fiap.techchallenge.pedidos.application.usecase;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ValidadorHelper {
	private ValidadorHelper(){}

	public static  <X extends RuntimeException>
	void validar(Object objeto, Supplier<? extends X> exception) {
		if (Objects.isNull(objeto))
			throw exception.get();
	}

	public static  <X extends RuntimeException>
	void validar(List<?> lista, Supplier<? extends X> exception) {
		if (CollectionUtils.isEmpty(lista))
			throw exception.get();
	}
}
