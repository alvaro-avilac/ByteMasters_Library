package com.library.Library.controller;

import com.library.Library.entity.*;
import com.library.Library.repository.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;


@DataJpaTest
public class GestorPenalizacionesTest {

    private Usuario usuario;
    private Prestamo prestamo;
    private static  final int   cupo  =4;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Puedes realizar limpieza después de la ejecución de todas las pruebas
    }

    @Before
    public void setUp() throws Exception {
        // Inicializar objetos necesarios para las pruebas
        usuario = new Usuario();
        usuario.setNombre("LUIS");
        usuario.setApellidos("PASCAL");
        usuario.setFechaFinPenalizacion(null); // Asegúrate de inicializarlo correctamente
        // Otros atributos de Usuario
       
        prestamo = new Prestamo(); 
        prestamo.setFechaInicio(Date.from(LocalDateTime.now().minusDays(10).atZone(ZoneId.systemDefault()).toInstant()));
        prestamo.setFechaFinal(Date.from(LocalDateTime.now().plusDays(5).atZone(ZoneId.systemDefault()).toInstant()));
        // Otros atributos de Prestamo
    }

    @After
    public void tearDown() throws Exception {
        // Limpiar objetos después de cada prueba si es necesario
        usuario = null;
        prestamo = null;
    }

    @Test
    public void testAplicarPenalizacionesSinPenalizacion() {
        // Configurar la fecha de devolución antes o igual a la fecha límite
        LocalDate fechaDevolucion = prestamo.getFechaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        GestorPenalizaciones gestorPenalizaciones = new GestorPenalizaciones();
		// Ejecutar el método de penalización
        gestorPenalizaciones.aplicarPenalizaciones(usuario, fechaDevolucion, prestamo);

        // Verificar que no se aplicó penalización
        assertNull(usuario.getFechaFinPenalizacion());
        
    }

    @Test
    public void testAplicarPenalizacionesConPenalizacion() {
        // Configurar la fecha de devolución después de la fecha límite
        LocalDate fechaDevolucion = prestamo.getFechaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);

        GestorPenalizaciones gestorPenalizaciones = new GestorPenalizaciones();
		// Ejecutar el método de penalización
        gestorPenalizaciones.aplicarPenalizaciones(usuario, fechaDevolucion, prestamo);

        // Verificar que se aplicó penalización
        assertNotNull(usuario.getFechaFinPenalizacion());
       
    }

    @Test
    public void testCalculoPenalizacion() {
        // Configurar la fecha de devolución después de la fecha límite
        LocalDate fechaDevolucion = prestamo.getFechaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(3);

        GestorPenalizaciones gestorPenalizaciones = new GestorPenalizaciones();
		// Ejecutar el método de penalización
        gestorPenalizaciones.aplicarPenalizaciones(usuario, fechaDevolucion, prestamo);

        // Verificar el cálculo de penalización
        assertNotNull(usuario.getFechaFinPenalizacion());
       
    }

    @Test
    public void testConversionLocalDateTimeADate() {
        // Configurar la fecha de devolución después de la fecha límite
        LocalDate fechaDevolucion = prestamo.getFechaFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(2);

        GestorPenalizaciones gestorPenalizaciones = new GestorPenalizaciones();
		// Ejecutar el método de penalización
        gestorPenalizaciones.aplicarPenalizaciones(usuario, fechaDevolucion, prestamo);

        // Verificar la conversión de LocalDateTime a Date
        assertNotNull(usuario.getFechaFinPenalizacion());
        
    }
    @Test
    public void testComprobarCupo_SinPrestamosActivos() {
        // Configurar el escenario
        Usuario usuario = new Usuario();
        usuario.setPrestamos(Arrays.asList(new Prestamo(), new Prestamo(), new Prestamo())); // Sin prestamos activos

         //Llamar al método que quieres probar
        boolean resultado = GestorPenalizaciones.comprobarCupo(usuario);

        // Verificar el resultado
        assertFalse(resultado);
    }
    // PRUEBA CON 4 PRESTAMOS 
//    @Test
//    public void testComprobarCupo_ConCupoExcedido() {
//        // Configurar el escenario
//        Usuario usuario = new Usuario();
//        Prestamo p1 = new Prestamo ();
//        p1.setActivo(true);
//        Prestamo p2 = new Prestamo ();
//        p2.setActivo(true);
//        Prestamo p3 = new Prestamo ();
//        p3.setActivo(true);
//        Prestamo p4 = new Prestamo ();
//        p4.setActivo(true);
//        
//        List<Prestamo>p= new ArrayList<>();
//        p.add(p1);
//        p.add(p2);
//        p.add(p3);
//        p.add(p4);
//        usuario.setPrestamos(p);

        // Llamar al método que quieres probar
        //boolean resultado = GestorPenalizaciones.comprobarCupo(usuario);

        // Verificar el resultado
        //assertTrue("No se esperaba que el cupo estuviera disponible", GestorPenalizaciones.comprobarCupo(usuario));
//    }

    
    @Test
    public void testComprobarCupo_ConCupoDisponible() {
        // Configurar el escenario
        Usuario usuario = new Usuario();
        usuario.setPrestamos(Arrays.asList(new Prestamo(), new Prestamo())); // Dos prestamos activos
        Prestamo p1 = new Prestamo ();
        p1.setActivo(true);
        Prestamo p2 = new Prestamo ();
        p2.setActivo(true);
        
        List<Prestamo>p= new ArrayList<>();
        p.add(p1);
        p.add(p2);
        usuario.setPrestamos(p);

        // Llamar al método que quieres probar
        //boolean resultado = GestorPenalizaciones.comprobarCupo(usuario);

        // Verificar el resultado
        //assertFalse(resultado);
    }
    
    public void testComprobarPenalizaciones(Usuario usuario, boolean resultadoEsperado) {
        // Llamar al método que quieres probar
    	
    	
    	// Caso 1: Usuario con penalización activa (fecha de finalización en el futuro)
        Usuario usuarioConPenalizacion = new Usuario();
        LocalDate fechaActual = LocalDate.now();
        Date fechaFinal =Date.from(LocalDateTime.now().minusDays(10).atZone(ZoneId.systemDefault()).toInstant());
        ComprobarPenalizaciones(usuarioConPenalizacion, true);
        
        // caso 2: Usuario expira 
        Usuario usuarioPenalizacionExpirada = new Usuario();
        usuarioPenalizacionExpirada.setFechaFinPenalizacion(fechaFinal);
        
        ComprobarPenalizaciones(usuarioPenalizacionExpirada, false);
        boolean resultado = new GestorPenalizaciones().comprobarPenalizaciones(usuario);
        Usuario user = new Usuario();
        user.getFechaFinPenalizacion(); 
        
        
    }
    
    
    private void ComprobarPenalizaciones (Usuario usuario, boolean resultadoEsperado) {
        // Llamar al método que quieres probar
        boolean resultado = new GestorPenalizaciones().comprobarPenalizaciones(usuario);

        // Verificar el resultado
        if (resultadoEsperado) {
            assertTrue("Se esperaba que hubiera penalizaciones activas", resultado);
        } else {
            assertFalse("No se esperaba que hubiera penalizaciones activas", resultado);
        }
    }

	
	
}
    
    
 
