package br.com.foods.teal.controller;

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

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsErros(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		List<String> messages = bindingResult.getAllErrors().stream()
				.map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toList());
		return new ApiErrors(messages);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity handleResponseStatusExeption(ResponseStatusException ex) {
		String messageError = ex.getMessage();
		HttpStatus codeStatus = (HttpStatus) ex.getStatusCode();
		ApiErrors apiErrors = new ApiErrors(messageError);
		return new ResponseEntity<>(apiErrors, codeStatus);
	}
}
