package com.fiap.techchallenge.pedidos.infraestruture.web.exception;

import com.fiap.techchallenge.pedidos.domain.exceptions.CPFInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.CPFOuEmailObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.exceptions.ClienteNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoNaoEncontradoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {
//	@ExceptionHandler(CPFOuEmailObrigatorioException.class)
//	protected ResponseEntity<Object> handleCpfOuEmailObrigatorio(CPFOuEmailObrigatorioException ex) {
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//	}

	/**
	 * Manipula exceções de argumentos de método não válidos.
	 *
	 * @param ex A exceção capturada.
	 * @return Uma resposta de erro com status HTTP 400.
	 */
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//		Map<String, String> errors = new HashMap<>();
//		ex.getBindingResult().getAllErrors().forEach(error -> {
//			String fieldName = ((FieldError) error).getField();
//			String errorMessage = error.getDefaultMessage();
//			errors.put(fieldName, errorMessage);
//		});
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
//	}

	/**
	 * Manipula exceções genéricas de tempo de execução.
	 *
	 * @param ex A exceção capturada.
	 * @param request O contexto da requisição web.
	 * @return Uma resposta de erro com status HTTP 500.
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Manipula exceções genéricas não capturadas por outros manipuladores.
	 *
	 * @param ex A exceção capturada.
	 * @param request O contexto da requisição web.
	 * @return Uma resposta de erro com status HTTP 500.
	 */
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<String> handleGeneralException(Exception ex, WebRequest request) {
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor: " + ex.getMessage());
//	}

	/**
	 * Manipula exceções de falha de conversão de tipo de argumento.
	 *
	 * @param e A exceção capturada.
	 * @return Uma resposta de erro com status HTTP 400.
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		// Aqui você pode logar a exceção se necessário
		if (e.getCause() instanceof NumberFormatException) {
			return new ResponseEntity<>("Entrada inválida: O valor deve ser numérico.", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de solicitação inválida.");
	}

	/**
	 * Manipula exceções de violação de integridade de dados.
	 *
	 * @param ex A exceção capturada.
	 * @return Uma resposta de erro com status HTTP 400.
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		// Verifique se a exceção é devido à violação de restrição única
		if (ex.getMessage().contains("uk_cpf")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: O CPF já está cadastrado.");
		} else if (ex.getMessage().contains("uk_email")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: O Email já está cadastrado.");
		}
		// Se não for uma violação de restrição única, retorne um erro genérico de servidor
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
	}

	/**
	 * Manipula exceções de leitura de mensagem HTTP inválida.
	 *
	 * @param ex A exceção capturada.
	 * @return Uma resposta de erro com status HTTP 400.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		Throwable rootCause = ex.getMostSpecificCause();
		if (rootCause instanceof CPFInvalidoException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rootCause.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de leitura do JSON: " + rootCause.getMessage());
	}



	/**
	 * Manipula exceções de CPF inválido.
	 *
	 * @param e A exceção capturada.
	 * @return Uma resposta de erro com status HTTP 400.
	 */
//	@ExceptionHandler(CPFInvalidoException.class)
//	public ResponseEntity<String> handleCPFInvalido(CPFInvalidoException e) {
//		return ResponseEntity.badRequest().body(e.getMessage());
//	}

	@ExceptionHandler(ClienteNaoEncontradoException.class)
	protected ResponseEntity<Object> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}


	@ExceptionHandler(PedidoNaoEncontradoException.class)
	protected ResponseEntity<Object> handlePedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}
