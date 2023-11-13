package com.library.Library.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	private LocalDate fechaGlobal = LocalDate.now();

	@Autowired
	IServiceTitulo tituloService;

	@Autowired
	IServiceEjemplar ejemplarService;

	@Autowired
	IServicePrestamo prestamoService;

	@Autowired
	IServiceUsuario usuarioService;

	@GetMapping("/prestarTitulo")
	public String titulosDisponiblesParaPrestamo(Model model) {
		
		
		List<Titulo> listadoTitulos = tituloService.listarTitulos();
		List<Prestamo> listadoPrestamos = prestamoService.listarPrestamos();

		for (Titulo t : listadoTitulos) {
			List<Ejemplar> ejemplaresDisponibles = new ArrayList<>();

			for (Ejemplar e : t.getEjemplares()) {
				boolean disponible = true;
				for (Prestamo p : listadoPrestamos) {
					if (p.getEjemplar() == e
							&& p.getFechaFinal()
									.after(Date.from(fechaGlobal.atStartOfDay(ZoneId.systemDefault()).toInstant()))
							&& p.isActivo()) {
						disponible = false;
						break;
					}
				}
				if (disponible) {
					ejemplaresDisponibles.add(e);
				}
			}

			t.setEjemplares(ejemplaresDisponibles);
		}

		model.addAttribute("titulos", listadoTitulos);
		model.addAttribute("nombre", "Listado de titulos disponibles para prestar");
		return "views/prestamos/selectTituloPrestamoUsuario";
	}

	@GetMapping("/prestamo/{id}")
	public String mostrarFormPrestamo(@PathVariable("id") Long tituloId, Model model) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);

		List<Ejemplar> ejemplaresDisponibles = new ArrayList<>();
		List<Prestamo> listadoPrestamos = prestamoService.listarPrestamos();

		for (Ejemplar e : titulo.getEjemplares()) {
			boolean disponible = true;
			for (Prestamo p : listadoPrestamos) {
				if (p.getEjemplar() == e
						&& p.getFechaFinal()
								.after(Date.from(fechaGlobal.atStartOfDay(ZoneId.systemDefault()).toInstant()))
						&& p.isActivo()) {
					disponible = false;
					break;
				}
			}
			if (disponible) {
				ejemplaresDisponibles.add(e);
			}
		}

		titulo.setEjemplares(ejemplaresDisponibles);

		log.info("Ejemplares disponibles: " + ejemplaresDisponibles.toString());

		model.addAttribute("titulo", titulo);
		model.addAttribute("listaEjemplares", ejemplaresDisponibles);

		return "views/prestamos/formHacerPrestamoUsuario";
	}

	@PostMapping("/savedPrestamo")
	public String guardarPrestamo(@ModelAttribute Prestamo prestamo,
			@RequestParam("selected_ejemplares") Long idEjemplar) {

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

	@GetMapping("/devolucion")
	public String mostrarEjemplaresPrestados(Model model) {
		Usuario user = usuarioService.buscarUsuarioPorId((long) 1).get();
		model.addAttribute("nombreDeUsuario", user.getNombre() + " " + user.getApellidos());

		List<Prestamo> listadoDePrestamos = user.getPrestamos();

		List<Prestamo> prestamosActivos = listadoDePrestamos.stream().filter(Prestamo::isActivo).collect(Collectors.toList());

		model.addAttribute("listadoDePrestamos", prestamosActivos);
		return "/views/prestamos/mostrarPrestamoDeUsuario";
	}

	@GetMapping("/registrarDevolucion/{id}")
	public String realizarDevolucion(@PathVariable("id") Long prestamoId, Model model) {

		Usuario user = usuarioService.buscarUsuarioPorId((long) 1).get();
		Prestamo prestamo = prestamoService.buscarPrestamoPorId(prestamoId).get();

		prestamo.setActivo(false);
		prestamoService.guardarPrestamo(prestamo);

		return "redirect:/";
	}
}