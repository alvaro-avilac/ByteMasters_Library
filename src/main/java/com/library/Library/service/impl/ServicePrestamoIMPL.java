package com.library.Library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Prestamo;
import com.library.Library.repository.PrestamoDAO;
import com.library.Library.service.IServicePrestamo;

@Service
public class ServicePrestamoIMPL implements IServicePrestamo{
	
	@Autowired
	private PrestamoDAO prestamoDAO;
	
	@Override
	public List<Prestamo> listarPrestamos() {
		return (List<Prestamo>) prestamoDAO.findAll();
	}

	@Override
	public Optional<Prestamo> buscarPrestamoPorId(Long id) {
		return prestamoDAO.findById(id) ;
	}

	@Override
	public void guardarPrestamo(Prestamo prestamo) {
		prestamoDAO.save(prestamo);
	}

}
