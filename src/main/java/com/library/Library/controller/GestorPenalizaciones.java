package com.library.Library.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.Library.entity.Prestamo;
import com.library.Library.entity.Usuario;

public class GestorPenalizaciones {

	private static final Logger log = LoggerFactory.getLogger(GestorPenalizaciones.class);

	public static final double INDICE_PENALIZACION = 2.5;
	public static final double CUPO_MAXIMO = 4;

	public void aplicarPenalizaciones(Usuario user, LocalDate fechaDevolucion, Prestamo prestamoUser) {

		// Verificar si la fecha de devolución es posterior a la fecha límite
		if (fechaDevolucion
				.isAfter(prestamoUser.getFechaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {

			// Calcular la cantidad de días de retraso
			long diasRetraso = ChronoUnit.DAYS.between(
					prestamoUser.getFechaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
					fechaDevolucion);

			double penalizacion = diasRetraso * INDICE_PENALIZACION;
			LocalDateTime finFechaPenalizacion = LocalDateTime.now().plusDays((long) penalizacion);

			// Convertir LocalDateTime a Date y asignarlo a al campo FechaFinPenalizacion
			user.setFechaFinPenalizacion(Date.from(finFechaPenalizacion.atZone(ZoneId.systemDefault()).toInstant()));
			log.info("Usuario " + user.getNombre() + " penalizado con " + penalizacion + " unidades.");
			log.info("Fecha de fin de penalización: " + user.getFechaFinPenalizacion());
		} else {
			log.info("Usuario " + user.getNombre() + " devolvió el libro a tiempo. Sin penalización.");
		}

	}

	public boolean comprobarPenalizaciones(Usuario user) {
		LocalDate fechaActual = LocalDate.now();

		if (user.getFechaFinPenalizacion() != null) {
			LocalDate fechaFinPenalizacion = user.getFechaFinPenalizacion().toInstant()
					.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
			// Comprueba si tiene penalizaciones
			if (fechaActual.isBefore(fechaFinPenalizacion)) {
				return true;
			}
		}

		return false;
	}

	public static boolean comprobarCupo(Usuario user) {
		long prestamosActivos = user.getPrestamos().stream().filter(Prestamo::isActivo).count();

		// Comprueba cupo de prestamos (si ya tiene 4 prestamos no puede pedir prestados
		// mas)
		if (prestamosActivos + 1 > CUPO_MAXIMO) {
			return true;
		}

		return false;
	}
	

}