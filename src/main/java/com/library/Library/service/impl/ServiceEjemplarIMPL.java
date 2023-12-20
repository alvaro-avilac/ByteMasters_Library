package com.library.Library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Ejemplar;
import com.library.Library.repository.EjemplarDAO;
import com.library.Library.service.IServiceEjemplar;

@Service
public class ServiceEjemplarIMPL implements IServiceEjemplar{
	
	@Autowired
	private EjemplarDAO ejemplarDAO;
	
	@Override
	public List<Ejemplar> listarEjemplares() {
		return (List<Ejemplar>) ejemplarDAO.findAll();
	}

	@Override
	public void altaEjemplar(Ejemplar e) {
		ejemplarDAO.save(e);
	}

	@Override
	public void bajaEjemplar(Long id) {
		ejemplarDAO.deleteById(id);
	}

	@Override
	public Optional<Ejemplar> buscarEjemplarPorId(Long id) {
		return ejemplarDAO.findById(id);
	}

	@Override
	public List<Ejemplar> listarEjemplaresPorTitulo(Long id) {
		return ejemplarDAO.findByTitleId(id);
	}
	
}
