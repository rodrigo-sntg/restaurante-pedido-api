package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PedidoGateway;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.RegrasStatus;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class PedidoUseCaseImpl implements PedidoUseCase{
	private final PedidoGateway pedidoGateway;
	private final ProdutoExternalGateway produtoGateway;
	private final ClienteGateway clienteGateway;

	public PedidoUseCaseImpl(PedidoGateway pedidoGateway, //
			ProdutoExternalGateway produtoGateway, //
			ClienteGateway clienteGateway) {
		this.pedidoGateway = pedidoGateway;
		this.produtoGateway = produtoGateway;
		this.clienteGateway = clienteGateway;
	}

	@Override
	public Pedido criarPedido(CadastroPedidoDTO inputData) {
		return new CadastroPedidoUseCase(pedidoGateway, produtoGateway, clienteGateway).salvar(inputData);
	}

	@Override
	public Optional<Pedido> buscarPedidoPeloCodigo(String codigo) {
		return pedidoGateway.buscarPedidoPeloCodigo(codigo);
	}

	@Override
	public Optional<List<Pedido>> buscarPedidos(StatusPedido statusPedido) {
		return pedidoGateway.buscarPedidos(statusPedido);
	}

	@Override
	public void cancelarPedido(Pedido pedido) {
		pedido.cancelarPedido();
		this.pedidoGateway.cancelarPedido(pedido);
	}

	@Override
	public Pedido avancarStatus(Pedido pedido, RegrasStatus regrasStatusUseCase) {
		pedido.executarRegrasStatus(regrasStatusUseCase);
		pedido.proximoStatus();
		return this.pedidoGateway.atualizarStatus(pedido);
	}

	@Override
	public Pedido avancarStatus(String codigo) {
		var pedido = pedidoGateway.buscarPedidoPeloCodigo(codigo)
				.orElseThrow(
						() -> new PedidoInvalidoException("Pedido não encontrado para o código: (" + codigo + ")."));
		return this.avancarStatus(pedido, new RegrasFluxoPedidoUseCase());
	}

	@Override
	public Pedido voltarStatus(String codigo) {
		var pedido = this.buscarPedidoPeloCodigo(codigo)
				.orElseThrow(
						() -> new PedidoInvalidoException("Pedido não encontrado para o código: (" + codigo + ")."));
		pedido.voltarStatus();
		return this.pedidoGateway.atualizarStatus(pedido);
	}

	@Override
	public void verificarEAtualizarStatusDosPedidos(LocalDateTime tempoAtual) {
		Optional<List<Pedido>> pedidos = this.pedidoGateway.buscarPedidos(null);

//		if (pedidos.isPresent()) {
//			for (Pedido pedido : pedidos.get()) {
//				long minutosDesdeAtualizacao = ChronoUnit.MINUTES.between(pedido.getDataAlteracao(), tempoAtual);
//				switch (pedido.getStatusAtual()) {
//				case AGUARDANDO_PAGAMENTO, FINALIZADO, CANCELADO:
//					break;
//				case PROCESSANDO_PAGAMENTO:
//					esperarEAvancar(pedido, minutosDesdeAtualizacao, 2, new RegrasCheckoutUseCase());
//					break;
//				case EM_ELABORACAO:
//					processarPedidoEmElaboracao(pedido, new RegrasFluxoPedidoUseCase());
//					break;
//				case PRONTO, RECEBIDO:
//					esperarEAvancar(pedido, minutosDesdeAtualizacao, 1, new RegrasFluxoPedidoUseCase());
//					break;
//				}
//			}
//		}
	}

	@Override
	public List<Pedido> buscarPedidosPorStatus(EnumSet<StatusPedido> status) {
		return pedidoGateway.buscarPedidosPorStatus(status);
	}

//	private void esperarEAvancar(Pedido pedido, long minutosDesdeAtualizacao, long tempoDeEspera, RegrasStatus regras) {
//		if (minutosDesdeAtualizacao >= tempoDeEspera) {
//			this.avancarStatus(pedido, regras);
//		}
//	}
//
//	private void processarPedidoEmElaboracao(Pedido pedido, RegrasStatus regras) {
//		if (pedido.getPrevisaoPreparo()
//				.isBefore(LocalDateTime.now())) {
//			this.avancarStatus(pedido, regras);
//		}
//	}
}
