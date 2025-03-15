package br.com.foods.teal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.foods.teal.model.User;

/**
 * Repositório da classe usuário
 * 
 * @author Caio Pereira Leal
 */
public interface UserRepository extends JpaRepository<User, String> {

}
