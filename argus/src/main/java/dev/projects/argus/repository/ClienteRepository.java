package dev.projects.argus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.projects.argus.model.Cliente;
import dev.projects.argus.model.User;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	Cliente findByUser(User user);
	
}
