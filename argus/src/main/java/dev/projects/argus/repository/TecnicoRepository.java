package dev.projects.argus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.projects.argus.model.Tecnico;
import dev.projects.argus.model.User;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long>{
	
	@Query(value = "select * from tecnico limit 10",
			nativeQuery = true)
	List<Tecnico> findAllTecnicos();

	Tecnico findByUser(User user);
}
