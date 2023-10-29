package com.library.Library.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.library.Library.entity.*;

@Repository
public interface AutorDAO extends CrudRepository<Autor, Long> {
	Optional<Autor> findByNombreAndApellido(String nomrbe, String apellido);
}