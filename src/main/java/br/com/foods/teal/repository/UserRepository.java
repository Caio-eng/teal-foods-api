package br.com.foods.teal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.foods.teal.model.User;

/**
 * Repositório da classe usuário
 * 
 * @author Caio Pereira Leal
 */
public interface UserRepository extends JpaRepository<User, String> {

	/**
	 * Encontra usuário com o email passado via parametro
	 * 
	 * @param email
	 * 			email do usuário
	 */
	Optional<User> findByEmailIgnoreCase(String email);
	
	/**
	 * Encontra usuário com o telefone passado via parametro
	 * 
	 * @param phone
	 * 			telefone do usuário
	 */
	Optional<User> findByPhoneIgnoreCase(String phone);
	
	/**
	 * Encontra usuário com o cpf passado via parametro
	 * 
	 * @param cpf
	 * 			cpf do usuário
	 */
	Optional<User> findByCpfIgnoreCase(String phone);
}
