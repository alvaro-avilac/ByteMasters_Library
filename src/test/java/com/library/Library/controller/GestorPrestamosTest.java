package com.library.Library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Prestamo;
import com.library.Library.entity.Reserva;
import com.library.Library.entity.Titulo;
import com.library.Library.entity.Usuario;
import com.library.Library.service.IServiceAutor;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServicePrestamo;
import com.library.Library.service.IServiceReserva;
import com.library.Library.service.IServiceTitulo;
import com.library.Library.service.IServiceUsuario;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GestorPrestamosTest {
	private GestorTitulos gestorTitulos;
	private IServiceTitulo tituloService;
	private IServiceEjemplar ejemplarService;
	private IServiceAutor autorService;
	private IServiceUsuario usuarioService;
	private IServicePrestamo prestamoService;

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
	@Test
	public void testTitulosDisponiblesParaPrestamo() {
	    // Configurar el escenario
		GestorPrestamos gestorprestamo = new GestorPrestamos();
	    Model model = mock(Model.class);
	    
	    
	    Titulo titulo1 = new Titulo();
	    titulo1.setId(1L);
	    
	    Titulo titulo2 = new Titulo();
	    titulo2.setId(2L);
	    
	    List<Titulo> listadoTitulos = Arrays.asList(titulo1, titulo2);
	    
	    Ejemplar ejemplar1 = new Ejemplar();
	    ejemplar1.setTitulo(titulo1);
	    
	    Ejemplar ejemplar2 = new Ejemplar();
	    ejemplar2.setTitulo(titulo2);
	    
	    List<Ejemplar> ejemplares = Arrays.asList(ejemplar1, ejemplar2);
	    
	    Prestamo prestamo1 = new Prestamo();
	    prestamo1.setEjemplar(ejemplar1);
	    prestamo1.setFechaFinal(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	    prestamo1.setActivo(true);
	    //when(gestorTitulos(titulo2)).thenReturn(true);

	    // Simular el servicio de préstamos
	    when(IServicePrestamo.listarPrestamos()).thenReturn(Collections.singletonList(prestamo1));

	    // Llamar al método que quieres probar
	    String result = gestorprestamo.titulosDisponiblesParaPrestamo(model);

	    // Verificar el resultado
	    assertEquals("views/prestamos/selectTituloPrestamoUsuario", result);

	    // Verificar que se haya agregado correctamente la información al modelo
	    verify(model).addAttribute("titulos", listadoTitulos);
	    verify(model).addAttribute("nombre", "Listado de títulos disponibles para prestar");
	}
	
	
	
	

}
