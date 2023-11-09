package com.library.Library.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Prestamo;
import com.library.Library.entity.Titulo;
import com.library.Library.entity.Usuario;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServicePrestamo;
import com.library.Library.service.IServiceTitulo;
import com.library.Library.service.IServiceUsuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GestorPrestamos {
	
	private static final Logger log = LoggerFactory.getLogger(GestorTitulos.class);

	private LocalDate fechaGlobal = LocalDate.now();;
	
	@Autowired
	IServiceTitulo tituloService;

	@Autowired
	IServiceEjemplar ejemplarService;

	@Autowired
	IServicePrestamo prestamoService;
	
	@Autowired
	IServiceUsuario usuarioService;
	
	@GetMapping("/prestarTitulo")
	public String titulosPrestamo(Model model) {
		List<Titulo> listadoTitulos = tituloService.listarTitulos();

		model.addAttribute("titulos", listadoTitulos);

		model.addAttribute("nombre", "Listado de titulos disponibles para prestar");
		return "views/prestamos/selectTituloPrestamoUsuario";
	}

	@GetMapping("/prestamo/{id}")
	public String mostrarFormPrestamo(@PathVariable("id") Long tituloId, Model model) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		List<Ejemplar> listaEjemplares = ejemplarService.listarEjemplaresPorTitulo(tituloId);

		model.addAttribute("titulo", titulo);
		model.addAttribute("listaEjemplares", listaEjemplares);

		return "views/prestamos/formHacerPrestamoUsuario";
	}

	@PostMapping("/savedPrestamo")
	public String guardarPrestamo(@ModelAttribute Prestamo prestamo, @RequestParam("selected_ejemplares") Long idEjemplar) {
		
		Usuario user = usuarioService.buscarUsuarioPorId((long) 1).get();
		
		Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(idEjemplar).get();
		prestamo.setEjemplar(ejemplar);
		prestamo.setActivo(true);
		prestamo.setFechaInicio(Date.from(fechaGlobal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		prestamo.setUsuario(user);
	    LocalDate fechaFinal = fechaGlobal.plusDays(14);
		
	    prestamo.setFechaFinal(Date.from(fechaFinal.atStartOfDay(ZoneId.systemDefault()).toInstant()));

		prestamoService.guardarPrestamo(prestamo);

		return "redirect:/prestarTitulo";
	}
}