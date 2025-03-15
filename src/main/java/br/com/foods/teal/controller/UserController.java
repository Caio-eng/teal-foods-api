package br.com.foods.teal.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.foods.teal.dto.UserDTO;
import br.com.foods.teal.model.User;
import br.com.foods.teal.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/**
 * Classe para definição de endpont de {@link User}
 * 
 * @author Caio Pereira Leal
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService service;
	
	/**
	 * Lista todos os usuário salvos
	 * 
	 * @return todos usuários
	 */
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return ResponseEntity.ok( service.findAllUsers() );
	}
	
	/**
	 * Buscar usuário pelo ID
	 * 
	 * @param id
	 *         identificador do usuário
	 *         
	 * @return usuário
	 */
	@GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(service.findUserById(id));
    }
	
	/**
	 * Salvar novo usuário
	 * 
	 * @param user
	 * 			{@link UserDTO user}
	 * @param urBuilder
	 *          {@link UriComponentsBuilder}
	 * 
	 * @return usuário persistido
	 */
	@PostMapping
	@Transactional
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user, UriComponentsBuilder urBuilder) {
        UserDTO userPersitido = service.save( user );
        
        URI uri = urBuilder.path( "/user/{id}" ).buildAndExpand( userPersitido.id() ).toUri();
        
        return ResponseEntity.created( uri ).body( userPersitido );
    }
	
	/**
	 * Atualizar o usuário
	 * 
	 * @param id
	 *         identificador do usuário
	 *         
	 * @param userDTO
	 *           {@link UserDTO userDTO}
	 *           
	 * @return usuário atualizado
	 */
	@PutMapping("/{id}")
	@Transactional
    public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable String id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok( service.update(id, userDTO) );
    }
	
	/**
	 * Deleta o usuário
	 * 
	 * @param id
	 *         identificador do usuário
	 */
	@DeleteMapping("/{id}")
	@Transactional
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
