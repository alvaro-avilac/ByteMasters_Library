package com.library.Library.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.library.Library.entity.Usuario;

public interface UsuarioDAO extends CrudRepository<Usuario, Long>{
	Usuario findByNombreAndApellidos(String nombre, String apellidos);
}
