package com.library.Library.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.library.Library.entity.Reserva;
import com.library.Library.entity.Titulo;
import com.library.Library.entity.Usuario;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServicePrestamo;
import com.library.Library.service.IServiceTitulo;
import com.library.Library.service.IServiceUsuario;
import com.library.Library.service.IServiceReserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GestorPrestamos {

	private static final Logger log = LoggerFactory.getLogger(GestorPrestamos.class);

	private static LocalDate fechaGlobal = LocalDate.now();
	
	GestorPenalizaciones gestorPenalizaciones = new GestorPenalizaciones(); 
	
	@Autowired
	IServiceTitulo tituloService;

	@Autowired
	IServiceEjemplar ejemplarService;

	@Autowired
	IServicePrestamo prestamoService;

	@Autowired
	IServiceUsuario usuarioService;
	
	@Autowired
	IServiceReserva reservaService;

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
							&& p.isActivo() || isTituloReservado(t)) {
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
	
	public boolean isTituloReservado(Titulo titulo) {
		
		List<Reserva> listaReservas = reservaService.listarReservas();
		
		for (Reserva r : listaReservas) {
			if(r.getTitulo() == titulo) {
				return true;
			}
		}
		
		
		return false;
	}

	@GetMapping("/prestamo/{id}")
	public String mostrarFormPrestamo(@PathVariable("id") Long tituloId, Model model) {
		
		Usuario user = usuarioService.getUsuario();
		
		Optional<Usuario> value = usuarioService.buscarUsuarioPorId(user.getId());
		user = value.orElse(null);
		if(user == null) {
			return "views/error";
		}
		
		if(!gestorPenalizaciones.comprobarPenalizaciones(user)) {
			log.info("Usuario " + user + "tiene penalizacion hasta " + user.getFechaFinPenalizacion());
	        model.addAttribute("error", "Usuario " + user.getNombre() + " " + user.getApellidos() + " tiene penalización hasta " + user.getFechaFinPenalizacion());
	        model.addAttribute("flag", true);
	        return "views/error";
		}
		
	    if(!gestorPenalizaciones.comprobarCupo(user)){
			log.info("Usuario tiene cupo completo de prestamos cubierto");
	        model.addAttribute("error", "Usuario tiene cupo completo de préstamos cubierto"); 
	        return "views/error";
		}
		
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
			@RequestParam("selected_ejemplares") Long idEjemplar, Model model) {
		
		Usuario user = usuarioService.getUsuario();
		
		Optional<Usuario> value = usuarioService.buscarUsuarioPorId(user.getId());
		user = value.orElse(null);
		
		
		Optional<Ejemplar> e_value = ejemplarService.buscarEjemplarPorId(idEjemplar);
		Ejemplar ejemplar = e_value.orElse(null);
		prestamo.setEjemplar(ejemplar);
		
		if(user == null || ejemplar == null) {
			return "views/error";
		}
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
		Usuario user = usuarioService.getUsuario();
		
		Optional<Usuario> value = usuarioService.buscarUsuarioPorId(user.getId());
		user = value.orElse(null);
		if(user == null) {
			return "views/error";
		}
		model.addAttribute("nombreDeUsuario", user.getNombre() + " " + user.getApellidos());

		List<Prestamo> listadoDePrestamos = user.getPrestamos();
		List<Prestamo> prestamosActivos = listadoDePrestamos.stream().filter(Prestamo::isActivo).collect(Collectors.toList());
		model.addAttribute("listadoDePrestamosActivos", prestamosActivos);
		model.addAttribute("listadoDePrestamos", listadoDePrestamos);
		
		return "/views/prestamos/mostrarPrestamoDeUsuario";
	}

	@GetMapping("/registrarDevolucion/{id}")
	public String realizarDevolucion(@PathVariable("id") Long prestamoId, Model model) {
		
		Usuario user = usuarioService.getUsuario();
		
		Optional<Usuario> value = usuarioService.buscarUsuarioPorId(user.getId());
		user = value.orElse(null);
		if(user == null) {
			return "views/error";
		}
		Optional<Prestamo> prestamoValue = prestamoService.buscarPrestamoPorId(prestamoId);
		Prestamo prestamo = prestamoValue.orElse(null);
		
		gestorPenalizaciones.aplicarPenalizaciones(user, fechaGlobal, prestamo);
		prestamo.setActivo(false);
		prestamoService.guardarPrestamo(prestamo);

		return "redirect:/user";
	}
	@GetMapping("/reserva/{id}")
	public String hacerReserva(@PathVariable("id") Long tituloId, Model model) {
		Usuario user = usuarioService.getUsuario();
		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		
		List<Reserva> listaReservas = reservaService.listarReservas();

		long tiempoActual = System.currentTimeMillis();
        Date fechaActual = new Date(tiempoActual);
        
        Reserva reserva = new Reserva();
        for (Reserva r : listaReservas) {
        	if (r.getUsuario().getId() == user.getId() && r.getTitulo().getId() == titulo.getId()){
        		return "/views/Bibliotecario/ReservaNoPosible";
            }
        }
        
        reserva.setTitulo(titulo);
        reserva.setUsuario(user);
        reserva.setFecha(fechaActual);

        reservaService.guardarReserva(reserva);

		return "/views/Bibliotecario/ReservaRealizadaBibliotecario";
	}
	@GetMapping("/reservado")
	public String reservaHecha() {
		return "/views/Bibliotecario/MenuBibliotecario";
	}
	@GetMapping("/no_reservado")
	public String reservaNoPosible() {
		return "/views/Bibliotecario/MenuBibliotecario";
	}
	
	
	
}