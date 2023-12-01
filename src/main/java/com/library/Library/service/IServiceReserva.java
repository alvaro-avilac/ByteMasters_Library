package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Reserva;

public interface IServiceReserva {
	
	public List<Reserva> listarReservas();
	public void guardarReserva(Reserva reserva);
	public Optional<Reserva> buscarReservaPorId(Long id);
}