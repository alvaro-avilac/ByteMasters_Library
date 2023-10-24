package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Autor;

public interface IServiceAutor {
	public List<Autor> listarAutores();
	public void altaAutor(Autor a);
	public void bajaAutor(Long id);
	public Optional<Autor> buscarAutorPorId(Long id);
	public List<Autor> buscarNombreYApellido(String nombre, String apellido);
}
