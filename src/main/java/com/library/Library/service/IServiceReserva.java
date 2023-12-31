package com.library.Library.service;

import java.util.List;
import java.util.Optional;

import com.library.Library.entity.Reserva;
import com.library.Library.entity.Titulo;

public interface IServiceReserva {
	
	public List<Reserva> listarReservas();
	public void guardarReserva(Reserva reserva);
	public Optional<Reserva> buscarReservaPorId(Long id);
	public void eliminarReserva(Long id);
	public void borrarReservasByTitulo(Titulo titulo);
}