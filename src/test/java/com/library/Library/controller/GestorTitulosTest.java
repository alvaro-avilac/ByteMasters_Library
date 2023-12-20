package com.library.Library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.Library.entity.Autor;
import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Prestamo;
import com.library.Library.entity.Titulo;
import com.library.Library.entity.Usuario;
import com.library.Library.service.IServiceAutor;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServicePrestamo;
import com.library.Library.service.IServiceTitulo;
import com.library.Library.service.IServiceUsuario;

public class GestorTitulosTest {

	private GestorTitulos gestorTitulos;
	private IServiceTitulo tituloService;
	private IServiceEjemplar ejemplarService;
	private IServiceAutor autorService;
	private IServiceUsuario usuarioService;
	private IServicePrestamo prestamoService;
	
	
	@InjectMocks
    private GestorPrestamosTest gestorTest;




	@Before
	public void setUp() {
		// Configuración de servicios sin Mockito
		tituloService = mock(IServiceTitulo.class);
		ejemplarService = mock(IServiceEjemplar.class);
		autorService = mock(IServiceAutor.class);
		usuarioService = mock(IServiceUsuario.class);
		Model model = Mockito.mock(Model.class);

		gestorTitulos = new GestorTitulos();
	}

	@Test
	public void testSeleccionarEndpointPorRol() {
		// Configurar el escenario
		GestorTitulos gestor = new GestorTitulos();

		// Caso de prueba 1
		String resultUsuario = gestor.seleccionarEndpointPorRol("usuario");
		assertEquals("redirect:/user", resultUsuario);

		// Caso de prueba 2
		String resultAdmin = gestor.seleccionarEndpointPorRol("admin");
		assertEquals("redirect:/admin", resultAdmin);

		// Caso de prueba 3
		String resultBibliotecario = gestor.seleccionarEndpointPorRol("bibliotecario");
		assertEquals("redirect:/bibliotecario", resultBibliotecario);

		// Caso de prueba 4
		String resultDefault = gestor.seleccionarEndpointPorRol("otro");
		assertEquals("redirect:/", resultDefault);
	}

//    @Test
//    public void testEjemplarTieneReservasPrestamos_NoPrestamosActivos() {
//        // Configurar el escenario
//        Ejemplar ejem= new Ejemplar();
//        Titulo mititulo=new Titulo();
//        List<Prestamo> listadoPrestamos = new ArrayList<>();
//        Prestamo p1 = new Prestamo();
//        ejem.setTitulo(mititulo);
//        p1.setActivo(false);
//        p1.setEjemplar(ejem);
//        Prestamo p2 = new Prestamo();
//        Prestamo p3 = new Prestamo();
//        p2.setActivo(true);
//        Prestamo p4 = new Prestamo();
//        p3.setActivo(true);
//        p4.setActivo(false);
//        listadoPrestamos.add(p1);
//        listadoPrestamos.add(p2);
//        listadoPrestamos.add(p3);
//        
//        boolean resultado = gestorTitulos.tituloTieneReservasPrestamos(mititulo);
//        // Verificar el resultado
//        assertFalse(resultado);
//    }
	
	 @Test
	    public void testEjemplarTieneReservasPrestamos_NoPrestamosActivos() {
	        // Configurar el escenario
	        Ejemplar ejem = new Ejemplar();
	        Titulo mititulo = new Titulo();
	        
	        // Crear préstamos
	        Prestamo p1 = new Prestamo();
	        p1.setActivo(false);
	        p1.setEjemplar(ejem);
	        
	        Prestamo p2 = new Prestamo();
	        p2.setActivo(true);
	        
	        Prestamo p3 = new Prestamo();
	        p3.setActivo(true);
	        
	        Prestamo p4 = new Prestamo();
	        p4.setActivo(false);
	        
	        // Lista de préstamos
	        List<Prestamo> listadoPrestamos = Arrays.asList(p1, p2, p3, p4);
	       
	        // Llamar al método del gestorTitulos
	        boolean resultado = gestorTitulos.tituloTieneReservasPrestamos(mititulo);
	        
	        // Verificar el resultado
	        assertFalse(resultado);
	    }


	@Test
	public void testEjemplarTieneReservasPrestamos_ConPrestamoActivo() {
		// Configurar el escenario
		Long ejemplarId = 1L;
		Ejemplar ejemplar = new Ejemplar();
		ejemplar.setId(ejemplarId);

		Prestamo prestamoActivo = new Prestamo();
		prestamoActivo.setActivo(true);
		prestamoActivo.setEjemplar(ejemplar);

		List<Prestamo> listadoPrestamos = List.of(prestamoActivo);

		// Llamar al método que quieres probar
		boolean resultado = gestorTitulos.tituloTieneReservasPrestamos((Titulo) listadoPrestamos);

		// Verificar el resultado
		assertTrue(resultado);
	}
	
	@Test
    public void testTituloTieneReservasPrestamos_ConPrestamoInactivo() {
        // Configurar el escenario
        Titulo titulo = new Titulo();
        Prestamo prestamoInactivo = new Prestamo();
        prestamoInactivo.setActivo(false);

        // Llamar al método que quieres probar
        boolean resultado = GestorTitulos.tituloTieneReservasPrestamos(titulo);

        // Verificar el resultado
        assertFalse(resultado);
   }
	 @Test
	    public void testDetallesTitutlo_TituloExistente() {
	        // Configurar el escenario
	        Long tituloId = 1L;
	        Model model = mock(Model.class);
	        tituloService = mock(IServiceTitulo.class);
	        ejemplarService =  mock(IServiceEjemplar.class);
	        
	        Titulo titulo = new Titulo();
	        titulo.setId(tituloId);

	        when(tituloService.buscarTituloPorId(tituloId)).thenReturn(titulo);
	        when(ejemplarService.listarEjemplaresPorTitulo(tituloId)).thenReturn(new ArrayList<>());
	        
	        String result = gestorTitulos.detallesTitutloTest(tituloId, model);

	        // Verificar el resultado
	        assertEquals("/views/admin/titulos/detalleTitulo", result);

	        // Verificar que se haya agregado correctamente la información al modelo
	        verify(model).addAttribute("titulo", titulo);
	        verify(model).addAttribute("autoresStr", titulo.getAutores().toString());
	        verify(model).addAttribute("numEjemplares", titulo.getEjemplares().size());
	        verify(model).addAttribute("listaEjemplares", new ArrayList<>());
	    }
	 
	 

	
	
	
}
