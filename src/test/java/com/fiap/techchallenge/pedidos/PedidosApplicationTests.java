package com.fiap.techchallenge.pedidos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class PedidosApplicationTests {
	@Test
	public void applicationContextLoaded() {
	}

	@Test
	void applicationContextTest() {
		assertDoesNotThrow(() -> PedidosApplication.main(new String[] {}));
	}

}
