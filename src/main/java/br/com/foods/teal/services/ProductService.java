package br.com.foods.teal.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.foods.teal.dto.ProductDTO;
import br.com.foods.teal.model.Product;
import br.com.foods.teal.repository.ProductRepository;

/**
 * Serviço para Produto
 * 
 * @author Caio Pereira Leal
 */
@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	/**
	 * Retorna todos os produtos salvos
	 * 
	 * @return produtos
	 */
	 public List<ProductDTO> findAllProducts() {
		 return repository.findAll().stream()
				 					.map( product -> ProductDTO.fromModel( product ) )
				 					.collect( Collectors.toList() );
	 }
	 
   /**
    * Buscar produto pelo ID
    * 
    * @param id
    * 		  identificador do produto
    * 
    * @return produto encontrado ou null se não encontrado 	 
    */
	public ProductDTO findProductById(Long id) {
		Product product = repository.findById( id )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Produto não encontrado" ) );

		return ProductDTO.fromModel( product );
	}
	
	/**
	 * Busca imagens pelo id do produto
	 * 
	 * @param productId
	 * 			 identidicador do produto
	 * 
	 * @return imagens 
	 */
	public List<String> getProductImages(Long productId) {
		Product product = repository.findById( productId )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND,
						"Produto não encontrado com ID: " + productId ) );

		return product.getImages();
	}
	 
   /**
	* Salvar novo produto
	* 
	* @param productDTO
	*        {@link ProductDTO produto}
	* 
	* @return produto persistido
	* @throws ResponseStatusException se já existir um produto com o mesmo nome
	*/
	public ProductDTO save(ProductDTO productDTO) {
		validProduct( productDTO );
		Product product = new Product( productDTO );
		product.setCreateDate( LocalDateTime.now() );
		return ProductDTO.fromModel( repository.save( product ) );
	}
	
	private void validProduct(ProductDTO productDto) {
		repository.findByNameIgnoreCase( productDto.name() ).ifPresent( existingProduct -> {
			throw new ResponseStatusException( HttpStatus.CONFLICT,
					"Já existe um produto cadastrado com o nome: " + productDto.name() );
		} );
		
		repository.findByDescriptionIgnoreCase( productDto.description() ).ifPresent( existingProduct -> {
			throw new ResponseStatusException( HttpStatus.CONFLICT,
					"Já existe um produto cadastrado com a descrição: " + productDto.description() );
		} );
	}
	
	/**
	 * Atualizar o produto
	 * 
	 * @param id
	 *         identificador do produto
	 *         
	 * @param productDTO
	 *           {@link ProductDTO productDTO}
	 *           
	 * @return produto atualizado
	 */
	public ProductDTO update(Long id, ProductDTO productDTO) {
		Product product = repository.findById( id )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Produto não encontrado" ) );
		LocalDateTime createDate = product.getCreateDate();
		
		product = new Product( productDTO );
		product.setCreateDate( createDate );
		product.setUpdateDate( LocalDateTime.now() );

		return ProductDTO.fromModel( repository.save( product ) );
	}
	
	/**
	 * Deleta o produto
	 * 
	 * @param id
	 *         identificador do produto
	 */
	public void delete(Long id) {
		Product product = repository.findById( id )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Produto não encontrado" ) );

		repository.delete( product );
	}
}
