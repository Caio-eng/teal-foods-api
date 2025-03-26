package br.com.foods.teal.controller;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import br.com.foods.teal.controller.exception.ApiErrors;

/**
 * Classe que vai formartar os erros recebido pela aplicação
 * 
 * @author Caio Pereira Leal
 */
@RestControllerAdvice
public class ApplicationControllerAdvice {

	/**
	 * Classe erros em gerais
	 * 
	 * @param ex
	 *        {@link MethodArgumentNotValidException ex}
	 *        
	 * @return ApiErros formatando a mensagem de erro
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsErros(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		List<String> messages = bindingResult.getAllErrors().stream()
				.map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toList());
		return new ApiErrors(messages);
	}

	/**
	 *  Classe recebe os erro de status ex: 405 - NOT_FOUND
	 *  
	 * @param ex
	 *        {@link ResponseStatusException ex}
	 *        
	 * @return o status com uma mensagem formatada
	 */
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity handleResponseStatusExeption(ResponseStatusException ex) {
		String messageError = ex.getMessage();
		HttpStatus codeStatus = (HttpStatus) ex.getStatusCode();
		ApiErrors apiErrors = new ApiErrors(messageError);
		return new ResponseEntity<>(apiErrors, codeStatus);
	}
	
	@ExceptionHandler(MalformedURLException.class)
	public ResponseEntity<String> handleMalformedURL(MalformedURLException ex) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Erro ao processar caminho da imagem");
	}
}
