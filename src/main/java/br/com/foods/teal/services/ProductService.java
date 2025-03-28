package br.com.foods.teal.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.foods.teal.dto.ProductDTO;
import br.com.foods.teal.model.Product;
import br.com.foods.teal.model.User;
import br.com.foods.teal.repository.ProductRepository;
import br.com.foods.teal.repository.UserRepository;

/**
 * Serviço para Produto
 * 
 * @author Caio Pereira Leal
 */
@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${app.upload.dir:${user.home}}")
	private String uploadDir;
	
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
     * Busca uma imagem do produto
     * @param filename Nome do arquivo da imagem
     * @return Imagem como Resource
     */
	public ResponseEntity<Resource> getImage(String filename) {
	    try {
	        Path uploadPath = Paths.get( uploadDir ).resolve( "product-images" );
	        Path filePath = uploadPath.resolve( filename ).normalize();
	        Resource resource = new UrlResource( filePath.toUri() );
	        
	        if ( resource.exists() && resource.isReadable() ) {
	            String contentType = Files.probeContentType( filePath );
	            return ResponseEntity.ok()
	                    .header( HttpHeaders.CONTENT_TYPE, contentType )
	                    .body( resource );
	        } else {
	            throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Imagem não encontrada: " + filename );
	        }
	    } catch (IOException e) {
	        throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao carregar imagem: " + filename );
	    }
	}
	
	/**
	 * Clase para verificar imagens salvas no diretório 
	 * 
	 * @return imagens
	 * 
	 * @throws IOException
	 * 				Exceção para erro
	 */
	public List<String> listImages() throws IOException {
		Path uploadPath = Paths.get( uploadDir ).resolve( "product-images" );
		List<String> files = Files.list( uploadPath )
	            .map( Path::getFileName )
	            .map( Path::toString )
	            .collect( Collectors.toList() );
	    return files;
	}
	
	/**
	 * Lista de Produtos por usuário
	 * 
	 * @param userId
	 * 			identificador do usuário
	 * 
	 * @return user
	 */
	public List<ProductDTO> getProductsByUser(String userId) {
	    return repository.findByUserId( userId ).stream()
	            .map( ProductDTO::fromModel )
	            .collect( Collectors.toList() );
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
		User user = userRepository.findById( productDTO.userId() )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuário não encontrado" ) );
		Product product = new Product( productDTO );
		product.setUser( user );
		product.setCreateDate( LocalDateTime.now() );
		return ProductDTO.fromModel( repository.save( product ) );
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
		User user = userRepository.findById( productDTO.userId() )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuário não encontrado" ) );
		product.setUser( user );
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
