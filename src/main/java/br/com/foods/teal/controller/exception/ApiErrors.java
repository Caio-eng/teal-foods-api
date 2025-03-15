package br.com.foods.teal.controller.exception;

import java.util.Arrays;
import java.util.List;

/**
 * Classe que recebe erros da aplicação
 * 
 * @author Caio Pereira Leal
 */
public class ApiErrors {

	 private List<String> errors;
        
	    /**
	     * Classe recebe uma lista de erros
	     * 
	     * @param errors
	     * 			erros recebido pela lista
	     */
	    public ApiErrors(List<String> errors) {
	        this.errors = errors;
	    }

	    /**
	     * Classe recebe uma mensagem expecifica de erro
	     * 
	     * @param message
	     * 			mensagem de erro
	     */
	    public ApiErrors(String message) {
	        this.errors = Arrays.asList(message);
	    }

	    /**
	     * Classe retorna uma lista de erros
	     * 
	     * @return erros
	     */
		public List<String> getErrors() {
			return errors;
		}
	    
}
