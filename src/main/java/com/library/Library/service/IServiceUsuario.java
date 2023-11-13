package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Usuario;

public interface IServiceUsuario {
	public List<Usuario> listarUsuarios();
	public Optional<Usuario> buscarUsuarioPorId(Long id);
	public void guardarUsuarioporId(Usuario user);
	
}
