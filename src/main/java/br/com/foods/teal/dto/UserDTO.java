package br.com.foods.teal.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.foods.teal.model.User;
import jakarta.validation.constraints.NotBlank;

/**
 *  Classe para informações do usuário
 * 
 * @param id
 *         identificador do usuário
 *        
 * @param name
 *         nome do usuário
 *        
 * @param email
 *         email do usuário
 *         
 * @param phone
 *         telefone do usuário
 *         
 * @param cpf
 *         cpf do usuário  
 *         
 * @param createDate
 *         data de criação do usuário   
 *         
 * @param updateDate
 *         data de atualização do usuário           
 *         
 * @author Caio Pereira Leal
 */
public record UserDTO(
		@NotBlank(message = "O campo id é obrigatório") String id, 
		@NotBlank(message = "O campo nome é obrigatório") String name, 
		@NotBlank(message = "O campo email é obrigatório") String email, 
		@NotBlank(message = "O campo telefone é obrigatório") String phone, 
		@NotBlank(message = "O campo cpf é obrigatório") String cpf,
		@JsonIgnore LocalDateTime createDate,
		@JsonIgnore LocalDateTime updateDate) {

	
	/**
	 * Retorna uma instância preenchida de usuário
	 * 
	 * @param user
	 *         Classe usuário
	 *         
	 * @return novo UserDTO
	 */
	public static final UserDTO fromModel(User user) {
		return new UserDTO( user.getId(), 
						 user.getName(), 
						 user.getEmail(), 
						 user.getPhone(), 
						 user.getCpf(),
						 user.getCreateDate(),
						 user.getUpdateDate() );
	}
}
