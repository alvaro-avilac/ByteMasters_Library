package com.library.Library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.library.Library.entity.Usuario;

@Repository
public interface UsuarioDAO extends CrudRepository<Usuario, Long>{

}
