package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Titulo;


public interface IServiceTitulo {
	public List<Titulo> listarTitulos();
	public void altaTitulo(Titulo t);
	public void bajaTitulo(Long id);
	public Optional<Titulo> buscarTituloPorId(Long id);
}
