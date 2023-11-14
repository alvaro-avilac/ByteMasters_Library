package com.library.Library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Usuario;
import com.library.Library.repository.UsuarioDAO;
import com.library.Library.service.IServiceUsuario;

@Service
public class ServiceUsuarioIMPL implements IServiceUsuario{
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	private Usuario usuario;

	
	@Override
	public List<Usuario> listarUsuarios() {
		return (List<Usuario>) usuarioDAO.findAll();
	}

	@Override
	public void guardarUsuario(Usuario user) {
		usuarioDAO.save(user);
	}

	@Override
	public Optional<Usuario> buscarUsuarioPorId(Long id) {
		return usuarioDAO.findById(id);
	}
	

    public Usuario getUsuario() {
        return usuario;
    }

    public void setGlobalUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

	@Override
	public Usuario buscarUsuarioPorNombreyApellido(String nombre, String apellidos) {
		return usuarioDAO.findByNombreAndApellidos(nombre, apellidos);
	}

}
