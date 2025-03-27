package br.com.foods.teal.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.foods.teal.dto.ProductDTO;
import br.com.foods.teal.model.Product;
import br.com.foods.teal.services.ImageStorageService;
import br.com.foods.teal.services.ProductService;
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
	
	@Value("${app.upload.dir:${user.home}}")
	private String uploadDir;
	
	/**
	 * Lista todos os produtos salvos
	 * 
	 * @return todos produtos
	 */
	@GetMapping
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
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok( service.findProductById( id ) );
    }
	
	/**
     * Busca uma imagem do produto
     * @param filename Nome do arquivo da imagem
     * @return Imagem como Resource
     */
	@GetMapping("/images/{filename:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
	    try {
	        Path uploadPath = Paths.get(uploadDir).resolve("product-images");
	        Path filePath = uploadPath.resolve(filename).normalize();
	        
	        System.out.println("Procurando imagem em: " + filePath.toString());
	        
	        Resource resource = new UrlResource(filePath.toUri());
	        
	        if (resource.exists() && resource.isReadable()) {
	            String contentType = Files.probeContentType(filePath);
	            System.out.println("Content-Type detectado: " + contentType);
	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_TYPE, contentType)
	                    .body(resource);
	        } else {
	            System.out.println("Arquivo não encontrado ou não legível");
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem não encontrada: " + filename);
	        }
	    } catch (IOException e) {
	        System.out.println("Erro ao acessar arquivo: " + e.getMessage());
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao carregar imagem: " + filename);
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
	@GetMapping("/check-images")
	public ResponseEntity<List<String>> listImages() throws IOException {
	    Path uploadPath = Paths.get(uploadDir).resolve("product-images");
	    List<String> files = Files.list(uploadPath)
	            .map(Path::getFileName)
	            .map(Path::toString)
	            .collect(Collectors.toList());
	    
	    return ResponseEntity.ok(files);
	}

    /**
     * Busca todas as imagens de um produto
     * @param id ID do produto
     * @return Lista de URLs das imagens
     */
    @GetMapping("/{id}/images")
    public ResponseEntity<List<String>> getProductImages(@PathVariable Long id) {
        ProductDTO product = service.findProductById(id);
        
        return ResponseEntity.ok(product.images());
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
	 * @return produto persistido
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	public ResponseEntity<ProductDTO> createProduct(@RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            UriComponentsBuilder uriBuilder,
            HttpServletRequest request) throws JsonProcessingException {
        
        // Converte o JSON string para DTO
        ObjectMapper mapper = new ObjectMapper();
        ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);
        
        // Processa imagens
        List<String> imageUrls = processImages(images);
        
        ProductDTO savedProduct = service.save(
            new ProductDTO(
                productDTO.id(),
                productDTO.name(),
                productDTO.categories(),
                productDTO.description(),
                productDTO.quantity(),
                imageUrls,
                LocalDateTime.now(),
                productDTO.updateDate()
            )
        );
        
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(savedProduct.id()).toUri();
        return ResponseEntity.created(uri).body(savedProduct);
    }

    private List<String> processImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return imageStorageService.storeImages(images);
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
	 * @return produto atualizado
	 */
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	public ResponseEntity<ProductDTO> updateProductWithImages(
	        @PathVariable Long id,
	        @RequestPart("product") String productJson,
	        @RequestPart(value = "images", required = false) List<MultipartFile> images,
	        UriComponentsBuilder uriBuilder) throws JsonProcessingException {
	    
	    ObjectMapper mapper = new ObjectMapper();
	    ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);
	    
	    List<String> imageUrls = processImages(images);
	    
	    ProductDTO updatedProduct = service.update(
	        id,
	        new ProductDTO(
	            productDTO.id(),
	            productDTO.name(),
	            productDTO.categories(),
	            productDTO.description(),
	            productDTO.quantity(),
	            imageUrls,
	            productDTO.createDate(), // Mantém a data original
	            LocalDateTime.now()     // Atualiza a data de modificação
	        )
	    );
	    
	    return ResponseEntity.ok(updatedProduct);
	}
	
	/**
	 * Deleta o produto
	 * 
	 * @param id
	 *         identificador do produto
	 */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> deleteProduct(@Valid @PathVariable Long id) {
		service.delete( id );
		return ResponseEntity.noContent().build();
	}
}
