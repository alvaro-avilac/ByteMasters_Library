package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Usuario;

public interface IServiceUsuario {
	public List<Usuario> listarUsuarios();
	public Optional<Usuario> buscarUsuarioPorId(Long id);
	public void guardarUsuario(Usuario user);
	public Usuario getUsuario();
	public void setGlobalUsuario(Usuario usuario);
	public Usuario buscarUsuarioPorNombreyApellido(String nombre, String apellidos);
 
	
}
