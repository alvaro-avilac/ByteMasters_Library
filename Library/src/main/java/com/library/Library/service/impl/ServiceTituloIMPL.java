package com.library.Library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Titulo;
import com.library.Library.repository.TituloDAO;
import com.library.Library.service.IServiceTitulo;

@Service
public class ServiceTituloIMPL implements IServiceTitulo{
	
	@Autowired
	private TituloDAO tituloDAO;
	
	@Override
	public List<Titulo> listarTitulos() {
		return (List<Titulo>) tituloDAO.findAll();
	}

	@Override
	public void altaTitulo(Titulo t) {
		tituloDAO.save(t);
		
	}

	@Override
	public void bajaTitulo(Long id) {
		tituloDAO.deleteById(id);
	}

	@Override
	public Optional<Titulo> buscarTituloPorId(Long id) {
		return tituloDAO.findById(id);
	}

}
