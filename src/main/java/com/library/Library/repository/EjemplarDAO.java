package com.library.Library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.library.Library.entity.*;

@Repository
public interface EjemplarDAO extends CrudRepository<Ejemplar, Long> {
	List<Ejemplar> findByTitleId(Long tituloId);
}