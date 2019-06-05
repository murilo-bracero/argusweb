package dev.projects.argus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.projects.argus.model.Videos;

public interface VideosRepository extends JpaRepository<Videos, Integer>{
	
	@Query(value = "select * from videos where tags like %?1%",
			nativeQuery = true)
	List<Videos> findByTag(String tag);
	
	@Query(value = "select * from videos limit 3",
			nativeQuery = true)
	List<Videos> findAllVideos();
	
}
