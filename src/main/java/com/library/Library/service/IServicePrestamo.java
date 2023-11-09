package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Prestamo;

public interface IServicePrestamo {
	
	public List<Prestamo> listarPrestamos();
	public void guardarPrestamo(Prestamo prestamo);
	public Optional<Prestamo> buscarPrestamoPorId(Long id);
}
