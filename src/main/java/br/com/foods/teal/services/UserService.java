package br.com.foods.teal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.foods.teal.model.User;
import br.com.foods.teal.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public User save(User user) {
		return repository.save(user);
	}
}
