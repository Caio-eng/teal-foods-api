package br.com.foods.teal.services;

import java.time.LocalDateTime;
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
                         .map( user -> UserDTO.fromModel( user ) )
                         .collect( Collectors.toList() );
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
		User user = repository.findById( id )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuário não encontrado" ) );

		return UserDTO.fromModel( user );
	}
	
	/**
	 * Salvar novo usuário
	 * 
	 * @param user
	 * 			{@link UserDTO user}
	 * 
	 * @return usuário persistido
	 */
	public UserDTO save(UserDTO userDto) {	
		validUser( userDto );
		User user = new User( userDto );
		user.setCreateDate( LocalDateTime.now() );
		return UserDTO.fromModel( repository.save( user ) );
	}
	
	private void validUser(UserDTO userDto) {
		repository.findByEmailIgnoreCase( userDto.email() ).ifPresent( existingProduct -> {
			throw new ResponseStatusException( HttpStatus.CONFLICT,
					"Já existe um usuário cadastrado com o email: " + userDto.email() );
		} );
		
		repository.findByPhoneIgnoreCase( userDto.phone() ).ifPresent( existingProduct -> {
			throw new ResponseStatusException( HttpStatus.CONFLICT,
					"Já existe um usuário cadastrado com o telefone: " + userDto.phone() );
		} );
		
		repository.findByCpfIgnoreCase( userDto.cpf() ).ifPresent( existingProduct -> {
			throw new ResponseStatusException( HttpStatus.CONFLICT,
					"Já existe um usuário cadastrado com o cpf: " + userDto.cpf() );
		} );
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
		User user = repository.findById( id )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuário não encontrado" ) );
		LocalDateTime createDate = user.getCreateDate();
		
		user = new User( userDTO );
		user.setCreateDate( createDate );
		user.setUpdateDate( LocalDateTime.now() );

		return UserDTO.fromModel( repository.save( user ) );
	}
	
	/**
	 * Deleta o usuário
	 * 
	 * @param id
	 *         identificador do usuário
	 */
	public void delete(String id) {
		User user = repository.findById( id )
				.orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuário não encontrado" ) );

		repository.delete( user );
	}
}
