package com.library.Library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Library.entity.Reserva;
import com.library.Library.repository.ReservaDAO;
import com.library.Library.service.IServiceReserva;

@Service
public class ServiceReservaIMPL implements IServiceReserva{
	
	@Autowired
	private ReservaDAO reservaDAO;
	
	@Override
	public List<Reserva> listarReservas() {
		return (List<Reserva>) reservaDAO.findAll();
	}

	@Override
	public Optional<Reserva> buscarReservaPorId(Long id) {
		return reservaDAO.findById(id) ;
	}

	@Override
	public void guardarReserva(Reserva reserva) {
		reservaDAO.save(reserva);
	}
	
	public void eliminarReserva(Long id) {
		reservaDAO.deleteById(id);
	}

}
