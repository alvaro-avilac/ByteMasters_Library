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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.Library.entity.Autor;
import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Titulo;
import com.library.Library.entity.Usuario;
import com.library.Library.service.IServiceAutor;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServiceTitulo;
import com.library.Library.service.IServiceUsuario;

public class GestorTitulosTest {

    private GestorTitulos gestorTitulos;
    private IServiceTitulo tituloService;
    private IServiceEjemplar ejemplarService;
    private IServiceAutor autorService;
    private IServiceUsuario usuarioService;
    

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
    public void testMostrarForm() {
        // Crear modelo
        Model model = mock(Model.class);

        // Llamar al método que quieres probar
        //String result = gestorTitulos.mostrarForm(model);

        // Verificar el resultado
        //assertEquals("views/admin/titulos/formAltaTitulo", result);

        // Verificar que se haya añadido correctamente el título al modelo
        Titulo tituloEnModelo = (Titulo) model.asMap().get("titulo");
        assertEquals(new Titulo(), tituloEnModelo);
    }
}
////    @Test
////    public void testAltaTitulo() {
////        // Configurar datos de prueba
////        Titulo titulo = new Titulo();
////        List<String> autoresStr = new ArrayList<>();
////        autoresStr.add("Autor1 Apellido1");
////        autoresStr.add("Autor2 Apellido2");
////        Integer numEjemplaresStr = 3;
////        autorService.buscarAutorPorNombre(null);
////
////        // Configurar comportamiento esperado para los mocks
////        when(autorService.buscarAutorPorNombreYApellido("Autor1", "Apellido1")).thenReturn(null);
////        when(autorService.buscarAutorPorNombreYApellido("Autor2", "Apellido2")).thenReturn(null);
////        when(autorService.buscarAutorPorNombre("Autor1")).thenReturn(null);
////        when(autorService.buscarAutorPorNombre("Autor2")).thenReturn(null);
////        when(tituloService.altaTitulo(any(Titulo.class))).thenReturn(null);
////        when(ejemplarService.altaEjemplar(any(Ejemplar.class))).thenReturn(null);
////
////        // Llamar al método que quieres probar
////        String result = gestorTitulos.altaTitulo(titulo, autoresStr, numEjemplaresStr);
////
////        // Verificar el resultado
////        assertEquals("redirect:/mostrar", result);
////
////        // Verificar llamadas a los métodos de los servicios
////        verify(autorService).altaAutor(any(Autor.class));
////        verify(tituloService).altaTitulo(any(Titulo.class));
////        verify(ejemplarService).altaEjemplar(any(Ejemplar.class));
////    }
//    
//    @Test
//    public void testEditarTitulo() {
//        // Configurar datos de prueba
//    	Model model = mock(Model.class);
//        Titulo titulo = new Titulo();
//        titulo.setId(1L);
//        List<String> autoresStr = new ArrayList<>();
//        autoresStr.add("Autor1 Apellido1");
//        autoresStr.add("Autor2 Apellido2");
//
//        // Configurar comportamiento esperado para los mocks
//        when(autorService.buscarAutorPorNombreYApellido("Autor1", "Apellido1")).thenReturn(null);
//        when(autorService.buscarAutorPorNombreYApellido("Autor2", "Apellido2")).thenReturn(null);
//        when(autorService.buscarAutorPorNombre("Autor1")).thenReturn(null);
//        when(autorService.buscarAutorPorNombre("Autor2")).thenReturn(null);
//        // Llamar al método que quieres probar
//        
//        String result = gestorTitulos.EditarTitulo(titulo, autoresStr);
//
//        // Verificar el resultado
//        assertEquals("redirect:/detalle/1", result);
//
//        // Verificar llamadas a los métodos de los servicios
//        verify(autorService).altaAutor(any(Autor.class));
//        verify(tituloService).altaTitulo(any(Titulo.class));
//    }
//    
//    @Test
//    public void testProcessLoginForm() {
//        // Configurar datos de prueba
//        String nombreUsuario = "Luis Miguel";
//        String apellidosUsuario = "Barreiro ";
//        String rol = "usuario";
//        Model model = mock(Model.class);
//
//        // Configurar comportamiento esperado para el mock de usuarioService
//        when(usuarioService.buscarUsuarioPorNombreyApellido(nombreUsuario, apellidosUsuario)).thenReturn(null);
//
//        // Llamar al método que quieres probar
//        String result = gestorTitulos.processLoginForm(nombreUsuario, apellidosUsuario, rol, model);
//
//        // Verificar el resultado
//        assertEquals("redirect:/user", result);
//
//        // Verificar llamadas a los métodos de usuarioService
//        verify(usuarioService).guardarUsuario(any(Usuario.class));
//        verify(usuarioService).setGlobalUsuario(any(Usuario.class));
//
//        // Verificar el modelo
//        verify(model).addAttribute("usuario", any(Usuario.class));
//    }
//    @Test
//    public void testMostrarTitulosConTituloEsperado() {
//        // Configurar datos de prueba
//        List<Titulo> listadoTitulos = new ArrayList<>();
//        Titulo tituloEsperado = new Titulo();
//        listadoTitulos.add(tituloEsperado);
//
//        // Configurar comportamiento esperado para el mock de tituloService
//        when(tituloService.listarTitulos()).thenReturn(listadoTitulos);
//
//        // Crear modelo
//        Model model = mock(Model.class);
//
//        // Llamar al método que quieres probar
//        String result = gestorTitulos.mostrarTitulos(model);
//
//        // Verificar el resultado
//        assertEquals("/views/admin/titulos/mostrarTitulos", result);
//
//        // Verificar que se haya agregado correctamente la lista de títulos al modelo
//        assertTrue(model.containsAttribute("nombre"));
//        assertTrue(model.containsAttribute("titulos"));
//        assertEquals("Lista de titulos", model.getAttribute("nombre"));
//        assertEquals(listadoTitulos, model.getAttribute("titulos"));
//
//        // Verificar llamadas a los métodos de tituloService
//        when(tituloService.listarTitulos()).thenReturn(listadoTitulos);
//        if (listadoTitulos.contains(tituloEsperado)) {
//            // Verificar que no se añadió el mensaje de "título no esperado"
//            assertFalse(model.containsAttribute("mensaje"));
//        } else {
//            // Verificar que se añadió el mensaje de "título no esperado"
//            assertEquals("Se encontró un título no esperado.", model.getAttribute("mensaje"));
//        }
//    }
//        
//        @Test
//        public void testDetallesTitulo() {
//            // Configurar datos de prueba
//            Long tituloId = 1L;
//            Titulo titulo = new Titulo(); // Puedes ajustar según tus necesidades
//            titulo.setId(tituloId);
//            List<Ejemplar> listaEjemplares = new ArrayList<>(); // Puedes ajustar según tus necesidades
//            listaEjemplares.add(new Ejemplar());
//
//
//            // Configurar comportamiento esperado para el mock de tituloService
//            when(tituloService.buscarTituloPorId(tituloId)).thenReturn(titulo);
//
//            // Configurar comportamiento esperado para el mock de ejemplarService
//            when(ejemplarService.listarEjemplaresPorTitulo(tituloId)).thenReturn(listaEjemplares);
//
//            // Crear modelo
//            Model model = mock(Model.class);
//
//            // Llamar al método que quieres probar
//            String result = gestorTitulos.detallesTitulotest(tituloId, model);
//
//            // Verificar el resultado
//            assertEquals("/views/admin/titulos/detalleTitulo", result);
//
//            // Verificar que se haya agregado correctamente la información al modelo
//            assertEquals(titulo, model.getAttribute("titulo"));
//            assertEquals(titulo.getAutores().toString(), model.getAttribute("autores"));
//            assertEquals(titulo.getEjemplares().size(), model.getAttribute("numEjemplares"));
//            assertEquals(listaEjemplares, model.getAttribute("listaEjemplares"));
//
//            // Verificar llamadas a los métodos de tituloService y ejemplarService
//            when(tituloService.buscarTituloPorId(tituloId)).thenReturn(titulo);
//            when(ejemplarService.listarEjemplaresPorTitulo(tituloId)).thenReturn(listaEjemplares);
//            
//            // titulo no encontrado 
//            when(tituloService.buscarTituloPorId(tituloId)).thenReturn(null);
//            model = mock(Model.class);
//            result = gestorTitulos.detallesTitulotest(tituloId, model);
//
//            assertEquals("/views/admin/titulos/detalleTitulo", result);
//            assertNull(model.getAttribute("titulo"));
//            assertNull(model.getAttribute("autores"));
//            assertNull(model.getAttribute("numEjemplares"));
//            assertNull(model.getAttribute("listaEjemplares"));
//            assertTrue(model.containsAttribute("mensaje"));
//            
//            
//            //Lista de ejemplares vacía
//            when(tituloService.buscarTituloPorId(tituloId)).thenReturn(titulo);
//            when(ejemplarService.listarEjemplaresPorTitulo(tituloId)).thenReturn(new ArrayList<>());
//            model = mock(Model.class);
//            result = gestorTitulos.detallesTitulotest(tituloId, model);
//
//            assertEquals("/views/admin/titulos/detalleTitulo", result);
//            assertEquals(titulo, model.getAttribute("titulo"));
//            assertEquals(titulo.getAutores().toString(), model.getAttribute("autores"));
//            assertEquals(titulo.getEjemplares().size(), model.getAttribute("numEjemplares"));
//            assertEquals(new ArrayList<>(), model.getAttribute("listaEjemplares"));
//        }
//        @Test
//        public void testMostrarFormEditarTitulo() {
//            // Caso típico con título existente
//            Long tituloId = 1L;
//            Titulo titulo = new Titulo();
//            titulo.setId(tituloId);
//            titulo.setAutores(new ArrayList<>());
//            List<Ejemplar> listaEjemplares = new ArrayList<>();
//            listaEjemplares.add(new Ejemplar());
//
//            when(tituloService.buscarTituloPorId(tituloId)).thenReturn(titulo);
//
//            Model model = mock(Model.class);
//            String result = gestorTitulos.mostrarFormEditarTitulo(tituloId, model);
//
//            assertEquals("/views/admin/titulos/formEditarTitulo", result);
//            assertEquals(titulo, model.getAttribute("titulo"));
//            assertEquals(titulo.getAutores().toString().substring(1, titulo.getAutores().toString().length() - 1),
//                    model.getAttribute("autoresStr"));
//            assertEquals(titulo.getEjemplares().size(), model.getAttribute("numEjemplares"));
//
//            // Caso de título no encontrado
//            when(tituloService.buscarTituloPorId(tituloId)).thenReturn(null);
//            model = mock(Model.class);
//            result = gestorTitulos.mostrarFormEditarTitulo(tituloId, model);
//
//            assertEquals("/views/admin/titulos/formEditarTitulo", result);
//            assertNull(model.getAttribute("titulo"));
//            assertNull(model.getAttribute("autores"));
//            assertNull(model.getAttribute("numEjemplares"));
//        }
//        
//        @Test
//        public void testEliminarTitulo() {
//        	
//        	 Model model = mock(Model.class);
//            // Configurar el escenario
//            Long tituloId = 1L;
//            String result = gestorTitulos.EliminarTitulo(tituloId,model);
//            assertEquals("redirect:/mostrar", result);
//            // Verificar que el servicio de títulos se llamó con el método adecuado
//            verify(tituloService).bajaTitulo(tituloId);
//        }
//        
//        @Test
//        public void testEliminarEjemplares() {
//            // Configurar el escenario
//            Long idTitle = 1L;
//            List<Long> selectedEjemplares = new ArrayList<>();
//            selectedEjemplares.add(1L);
//            selectedEjemplares.add(2L);
//
//            Titulo titulo = new Titulo();
//            titulo.setId(idTitle);
//
//            when(tituloService.buscarTituloPorId(idTitle)).thenReturn(titulo);
//            // método que queremos probar
//            String result = gestorTitulos.EliminarEjemplares(idTitle, selectedEjemplares, mock(Model.class));
//            // Verifica el resultado (redirección)
//            assertEquals("redirect:/detalle/" + idTitle, result);
//            // Verifica que el servicio de ejemplares se llamó con el método adecuado
//            verify(ejemplarService).bajaEjemplar(1L);
//            verify(ejemplarService).bajaEjemplar(2L);
//        }
//        
//        @Test
//        public void testAgregarEjemplares() {
//            // Configurar el escenario
//            Integer numeroEjemplares = 3;
//            Long idTitle = 1L;
//
//            Titulo titulo = new Titulo();
//            titulo.setId(idTitle);
//
//            when(tituloService.buscarTituloPorId(idTitle)).thenReturn(titulo);
//
//            // Llamar al método que quieres probar
//            String result = gestorTitulos.AgregarEjemplares(numeroEjemplares, idTitle, mock(Model.class));
//
//            // Verificar el resultado (redirección)
//            assertEquals("redirect:/detalle/" + idTitle, result);
//
//            // Verificar que el servicio de ejemplares se llamó con el método adecuado 
//            verify(ejemplarService, times(3)).altaEjemplar(any(Ejemplar.class));
//        }
//        @Test
//        public void testMostrarTitulosUser() {
//            // Configurar el escenario
//            List<Titulo> listadoTitulos = new ArrayList<>();
//            // Agrega tus objetos Titulo necesarios para el test
//            when(tituloService.listarTitulos()).thenReturn(listadoTitulos);
//
//            // Crear modelo
//            Model model = mock(Model.class);
//
//            // Llamar al método que quieres probar
//            String result = gestorTitulos.mostrarTitulosUser(model);
//
//            // Verificar el resultado
//            assertEquals("/views/Usuario/MostrarTitulosUser", result);
//
//            // Verificar que se haya agregado correctamente la lista de títulos al modelo
//			List<Titulo> titulosModel = (List<Titulo>) model.getAttribute("titulo");
//            assertEquals(listadoTitulos, titulosModel);
//        }
//        
//        @Test
//        public void testMostraMainWindowUser() {
//            // Configurar el escenario
//            Usuario usuarioMock = new Usuario();
//            // Configura el objeto Usuario según tus necesidades para el test
//            when(usuarioService.getUsuario()).thenReturn(usuarioMock);
//            // Crear modelo
//            Model model = mock(Model.class);
//            // Llamar al método que quieres probar
//            String result = gestorTitulos.mostraMainWindowUser(model);
//            // Verificar el resultado
//            assertEquals("/views/Usuario/MenuUsuario", result);
//            // Verificar que se haya agregado correctamente el usuario al modelo
//            Usuario usuarioEnModelo = (Usuario) model.getAttribute("usuario");
//            assertEquals(usuarioMock, usuarioEnModelo);
//        }
//        @Test
//        public void testMostraMainWindowAdmin() {
//            // Crear una instancia de la clase que contiene el método que quieres probar
//            gestorTitulos = new GestorTitulos();
//            // Llamar al método que quieres probar
//            String result = gestorTitulos.mostraMainWindowAdmin();
//            // Verificar el resultado
//            assertEquals("/views/admin/titulos/MenuAdmin", result);
//        
//        }
//        
//        @Test
//        public void testMostraMainWindowBibliotecario() {
//        	// Configurar el escenario
//            gestorTitulos = new GestorTitulos();
//
//            // Llamar al método que quieres probar
//            String result = gestorTitulos.mostraMainWindowBibliotecario();
//
//            // Verificar el resultado (ajustar según la lógica de tu aplicación)
//            assertEquals("redirect:/bibliotecario", result);
//        }
//  }
//        
//              
//        
//        
//        
//      
//        
//  
