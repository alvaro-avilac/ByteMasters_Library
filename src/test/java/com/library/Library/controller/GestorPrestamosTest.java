package com.library.Library.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Prestamo;
import com.library.Library.entity.Usuario;

import java.util.Date;

public class GestorPrestamosTest {

	@Test
	void testConstructorAndGetters() {
		// Arrange
		Long id = 1L;
		Usuario usuario = new Usuario(id, "John Doe", null, null, null, null); // Asegúrate de tener una clase Usuario válida
		Date fechaInicio = new Date();
		Date fechaFinal = new Date();
		boolean activo = true;
		Ejemplar ejemplar = new Ejemplar(); // Asegúrate de tener una clase Ejemplar válida

		// Act
		Prestamo prestamo = new Prestamo(id, usuario, fechaInicio, fechaFinal, activo, ejemplar);

		// Assert
		assertEquals(id, prestamo.getId());
		assertEquals(usuario, prestamo.getUsuario());
		assertEquals(fechaInicio, prestamo.getFechaInicio());
		assertEquals(fechaFinal, prestamo.getFechaFinal());
		assertTrue(prestamo.isActivo());
		assertEquals(ejemplar, prestamo.getEjemplar());
	}

	@Test
	void testSetters() {
		// Arrange
		Prestamo prestamo = new Prestamo();
		Long newId = 2L;
		Usuario newUsuario = new Usuario (newId, "Jane Doe", null, null, null, null);
		Date newFechaInicio = new Date();
		Date newFechaFinal = new Date();
		boolean newActivo = false;
		Ejemplar newEjemplar = new Ejemplar();

		// Act
		prestamo.setId(newId);
		prestamo.setUsuario(newUsuario);
		prestamo.setFechaInicio(newFechaInicio);
		prestamo.setFechaFinal(newFechaFinal);
		prestamo.setActivo(newActivo);
		prestamo.setEjemplar(newEjemplar);

		// Assert
		assertEquals(newId, prestamo.getId());
		assertEquals(newUsuario, prestamo.getUsuario());
		assertEquals(newFechaInicio, prestamo.getFechaInicio());
		assertEquals(newFechaFinal, prestamo.getFechaFinal());
		assertFalse(prestamo.isActivo());
		assertEquals(newEjemplar, prestamo.getEjemplar());
	}
// Agrega más pruebas 

}
