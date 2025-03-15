package br.com.foods.teal.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.foods.teal.dto.UserDTO;
import br.com.foods.teal.model.User;
import br.com.foods.teal.repository.UserRepository;

/**
 * Serviço para Usuário
 * 
 * @author Caio Pereira Leal
 */
@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	/**
	 * Retorna todos os usuário salvos
	 * 
	 * @return usuários
	 */
	public List<UserDTO> findAllUsers() {
        return repository.findAll().stream()
                         .map(user -> UserDTO.formModel(user))
                         .collect(Collectors.toList());
    }
	
	/**
    * Buscar usuário pelo ID
    * 
    * @param id
    *         identificador do usuário
    * 
    * @return usuário encontrado ou null se não encontrado
    */
	public UserDTO findUserById(String id) {
	    User user = repository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

	    return UserDTO.formModel(user);
	}
	
	/**
	 * Salvar novo usuário
	 * 
	 * @param user
	 * 			{@link UserDTO user}
	 * 
	 * @return usuário persistido
	 */
	public UserDTO save(UserDTO user) {	
		return UserDTO.formModel( repository.save(new User( user )) );
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
	public UserDTO update(String id, UserDTO userDTO) {
	    User user = repository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
	    user = new User( userDTO );

	    return UserDTO.formModel(repository.save(user));
	}
	
	/**
	 * Deleta o usuário
	 * 
	 * @param id
	 *         identificador do usuário
	 */
	public void delete(String id) {
	    User user = repository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

	    repository.delete(user);
	}
}
