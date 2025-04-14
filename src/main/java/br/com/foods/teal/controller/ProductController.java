package br.com.foods.teal.controller;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foods.teal.dto.ProductDTO;
import br.com.foods.teal.model.Product;
import br.com.foods.teal.services.ImageStorageService;
import br.com.foods.teal.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/**
 * Classe para definição de endpoint de {@link Product}
 * 
 * @author Caio Pereira Leal
 */
@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ImageStorageService imageStorageService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Lista todos os produtos salvos
	 * 
	 * @return todos produtos
	 */
	@GetMapping
	@Operation(summary = "Rota busca todos os produtos")
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		return ResponseEntity.ok( service.findAllProducts() );
	}
	
	/**
	 * Buscar produto pelo ID
	 * 
	 * @param id
	 *         identificador do produto
	 *         
	 * @return produto
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Rota responsável por buscar o produto pelo identificador")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok( service.findProductById( id ) );
    }
	
	/**
     * Busca uma imagem do produto
     * @param filename Nome do arquivo da imagem
     * @return Imagem como Resource
     */
	@GetMapping("/images/{filename:.+}")
	@Operation(summary = "Rota vê a imagem")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
	    return service.getImage( filename );
	}
	
	/**
	 * Clase para verificar imagens salvas no diretório 
	 * 
	 * @return imagens
	 * 
	 * @throws IOException
	 * 				Exceção para erro
	 */
	@GetMapping("/check-images")
	@Operation(summary = "Rota checa as imagens")
	public ResponseEntity<List<String>> listImages() throws IOException {
	    return ResponseEntity.ok( service.listImages() );
	}

    /**
     * Busca todas as imagens de um produto
     * @param id ID do produto
     * @return Lista de URLs das imagens
     */
    @GetMapping("/{id}/images")
    @Operation(summary = "Rota buscar imagens do produto")
    public ResponseEntity<List<String>> getProductImages(@PathVariable Long id) {
        ProductDTO product = service.findProductById(id);
        
        return ResponseEntity.ok( product.images() );
    }
    
    /**
	 * Lista de Produtos por usuário
	 * 
	 * @param userId
	 * 			identificador do usuário
	 * 
	 * @return user
	 */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Rota buscar o produto pelo usuário")
    public ResponseEntity<List<ProductDTO>> getProductsByUser(@PathVariable String userId) {
    	return ResponseEntity.ok( service.getProductsByUser( userId ) );
    }

	/**
	 * Salvar novo produto
	 * 
	 * @param product
	 *        {@link ProductDTO produto}
	 * @param uriBuilder
	 *        {@link UriComponentsBuilder}
	 *
	 * @param images
	 * 			imagens do produto
	 * 
	 * @param userId
	 * 			identificador do usuário
	 * 
	 * @return produto persistido
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	@Operation(summary = "Rota responsável por criar um produto")
	public ResponseEntity<ProductDTO> createProduct(@RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam String userId,
            UriComponentsBuilder uriBuilder,
            HttpServletRequest request) throws JsonProcessingException {
        
		ProductDTO productDTO = mapper.readValue( productJson, ProductDTO.class );
		List<String> imageUrls = processImages( images );
        
        ProductDTO savedProduct = service.save(
            new ProductDTO(
                productDTO.id(),
                productDTO.name(),
                productDTO.categories(),
                productDTO.description(),
                productDTO.quantity(),
                imageUrls,
                productDTO.price(),
                userId,
                LocalDateTime.now(),
                productDTO.updateDate()
            )
        );
        
        URI uri = uriBuilder.path( "/products/{id}" ).buildAndExpand( savedProduct.id() ).toUri();
        return ResponseEntity.created( uri ).body( savedProduct );
    }

    private List<String> processImages(List<MultipartFile> images) {
        if ( images == null || images.isEmpty() ) {
            return Collections.emptyList();
        }
        return imageStorageService.storeImages( images );
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
	 * @param images
	 * 			imagens do produto
	 * 
	 * @param userId
	 * 			identificador do usuário
	 *           
	 * @return produto atualizado
	 */
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	@Operation(summary = "Rota responsável por atualizar o produto com a imagem")
	public ResponseEntity<ProductDTO> updateProductWithImages(
	        @PathVariable Long id,
	        @RequestPart("product") String productJson,
	        @RequestPart(value = "images", required = false) List<MultipartFile> images,
	        @RequestParam String userId,
	        UriComponentsBuilder uriBuilder) throws JsonProcessingException {
	    
	    ProductDTO productDTO = mapper.readValue( productJson, ProductDTO.class );
	    
	    List<String> imageUrls = processImages( images );
	    
	    ProductDTO updatedProduct = service.update(
	        id,
	        new ProductDTO(
	            productDTO.id(),
	            productDTO.name(),
	            productDTO.categories(),
	            productDTO.description(),
	            productDTO.quantity(),
	            imageUrls,
	            productDTO.price(),
	            userId,
	            productDTO.createDate(),
	            LocalDateTime.now()
	        )
	    );
	    
	    return ResponseEntity.ok( updatedProduct );
	}
	
	/**
	 * Deleta o produto
	 * 
	 * @param id
	 *         identificador do produto
	 */
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Rota responsável por deletar o produto")
	public ResponseEntity<Void> deleteProduct(@Valid @PathVariable Long id) {
		service.delete( id );
		return ResponseEntity.noContent().build();
	}
}
