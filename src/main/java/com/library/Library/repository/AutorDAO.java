package com.library.Library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.library.Library.entity.*;

@Repository
public interface AutorDAO extends CrudRepository<Autor, Long> {
	Autor findByNombreAndApellido(String nombre, String apellido);
	Autor findByNombre(String nombre);
}