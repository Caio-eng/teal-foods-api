package br.com.foods.teal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.foods.teal.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
