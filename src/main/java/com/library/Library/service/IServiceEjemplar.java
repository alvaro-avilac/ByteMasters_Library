package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Ejemplar;

public interface IServiceEjemplar {
	public List<Ejemplar> listarEjemplares();
	public List<Ejemplar> listarEjemplaresPorTitulo(Long id);
	public void altaEjemplar(Ejemplar e);
	public void bajaEjemplar(Long id);
	public Optional<Ejemplar> buscarEjemplarPorId(Long id);
}
