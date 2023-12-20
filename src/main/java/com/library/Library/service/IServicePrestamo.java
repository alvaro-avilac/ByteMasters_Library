package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Prestamo;
import com.library.Library.entity.Titulo;

public interface IServicePrestamo {
	
	public static List<Prestamo> listarPrestamos() {
		// TODO Auto-generated method stub
		return null;
	}
	public void guardarPrestamo(Prestamo prestamo);
	public Optional<Prestamo> buscarPrestamoPorId(Long id);
	public void borrarPrestamosEjemplaresByTitulo(Titulo titulo);
	public void borrarPrestamosByEjemplar(Long ejemplarId);
}
