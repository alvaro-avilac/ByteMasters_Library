package com.library.Library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Autor;
import com.library.Library.repository.AutorDAO;
import com.library.Library.service.IServiceAutor;

@Service
public class ServiceAutorIMPL implements IServiceAutor{
	
	@Autowired
	private AutorDAO autorDAO;
	
	@Override
	public List<Autor> listarAutores() {
		return (List<Autor>) autorDAO.findAll();
	}
	
	@Override
	public void altaAutor(Autor a) {
		autorDAO.save(a);
	}
	
	@Override
	public void bajaAutor(Long id) {
		autorDAO.deleteById(id);
	}
	
	@Override
	public Optional<Autor> buscarAutorPorId(Long id) {
		return autorDAO.findById(id);
	}

	@Override
	public Autor buscarAutorPorNombreYApellido(String n, String a) {
		return autorDAO.findByNombreAndApellido(n, a);
	}
	
	@Override
	public Autor buscarAutorPorNombre(String n) {
		return autorDAO.findByNombre(n);
	}
	
}
