package com.library.Library.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Titulo;
import com.library.Library.repository.TituloDAO;
import com.library.Library.repository.EjemplarDAO;
import com.library.Library.service.IServiceTitulo;

@Service
public class ServiceTituloIMPL implements IServiceTitulo{
	
	@Autowired
	private TituloDAO tituloDAO;
	
	@Autowired
	private EjemplarDAO ejemplarDAO;
	
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
		Titulo t = tituloDAO.findById(id).get();
		for (Ejemplar e : t.getEjemplares()) {
			if(e.getTitulo().equals(t)) {
				ejemplarDAO.deleteById(e.getId());
			}
		}
		tituloDAO.deleteById(id);
	}

	@Override
	public Titulo buscarTituloPorId(Long id) {
		Optional <Titulo> value = tituloDAO.findById(id);
		return value.orElse(null);
	}

}
