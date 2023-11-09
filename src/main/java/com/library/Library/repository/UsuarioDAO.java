package com.library.Library.repository;

import org.springframework.data.repository.CrudRepository;

import com.library.Library.entity.Usuario;

public interface UsuarioDAO extends CrudRepository<Usuario, Long>{

}
