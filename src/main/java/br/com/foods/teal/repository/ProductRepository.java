package br.com.foods.teal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.foods.teal.model.Product;

/**
 * Repositório da classe produto
 * 
 * @author Caio Pereira Leal
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Encontra produtos com o nome passado via parametro
	 * 
	 * @param name
	 * 			nome do produto
	 */
	Optional<Product> findByNameIgnoreCase(String name);
	
	/**
	 * Encontra produtos com a descrição passado via parametro
	 * 
	 * @param description
	 * 			descrição do produto
	 */
	Optional<Product> findByDescriptionIgnoreCase(String description);
}
