package dev.projects.argus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.projects.argus.model.User;

public interface UserRepository extends JpaRepository<User, String>{

	User findByEmail(String email);
	
	User findByCpf(String cpf);
	
}
