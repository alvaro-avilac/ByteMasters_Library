package com.library.Library.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	private static final String NOMBRE = "nombre";
	
	private static boolean isBibliotecarioMode = false;

	GestorPenalizaciones gestorPenalizaciones = new GestorPenalizaciones(); 
	
	
	private final IServiceTitulo tituloService;
	private final IServiceEjemplar ejemplarService;
	private final IServicePrestamo prestamoService;
	private final IServiceUsuario usuarioService;
	private final IServiceReserva reservaService;
	
	@Autowired
	public GestorPrestamos(IServiceTitulo tituloService, IServiceEjemplar ejemplarService, IServicePrestamo prestamoService,
			IServiceUsuario usuarioService, IServiceReserva reservaService) {
		this.tituloService = tituloService;
		this.ejemplarService = ejemplarService;
		this.prestamoService = prestamoService;
		this.usuarioService = usuarioService;
		this.reservaService = reservaService;
	}
	
//	@Autowired
//	IServiceTitulo tituloService;

//	@Autowired
//	IServiceEjemplar ejemplarService;

//	@Autowired
//	IServicePrestamo prestamoService;

//	@Autowired
//	IServiceUsuario usuarioService;
	
//	@Autowired
//	IServiceReserva reservaService;

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
		model.addAttribute(NOMBRE, "Listado de titulos disponibles para prestar");
		return "views/prestamos/selectTituloPrestamoUsuario";
	}
		
	@GetMapping("/reserva")
	public String mostrarReservasUsuario(Model model) {
		Usuario user = usuarioService.getUsuario();
		List<Reserva> listadoReservas = reservaService.listarReservas();
		List<Titulo> listadoTitulos = new ArrayList<>();
		List<Prestamo> listadoPrestamos = prestamoService.listarPrestamos();
		
		for(Reserva r : listadoReservas) {
			if(r.getUsuario().getId().equals(user.getId())) {
				listadoTitulos.add(r.getTitulo());
			}
		}
		
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
			log.info("Ejemplares disponibles de " + t + ": " + ejemplaresDisponibles);
			t.setEjemplares(ejemplaresDisponibles);
		}
		
		model.addAttribute("titulos", listadoTitulos);
		model.addAttribute(NOMBRE, "Listado de titulos reservados");
		
		if(isBibliotecarioMode) {
			return"/views/Bibliotecario/MostrarReservas";
		}else {
			return"/views/Usuario/MostrarReservas";
		}
		
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
		
		if(gestorPenalizaciones.comprobarPenalizaciones(user)) {
			log.info("Usuario " + user + "tiene penalizacion hasta " + user.getFechaFinPenalizacion());
	        model.addAttribute("error", "Usuario " + user.getNombre() + " " + user.getApellidos() + " tiene penalización hasta " + user.getFechaFinPenalizacion());
	        model.addAttribute("flag", true);
	        return "views/error";
		}
		
	    if(gestorPenalizaciones.comprobarCupo(user)){
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
			@RequestParam("selected_ejemplares") Long idEjemplar, Model model, RedirectAttributes attribute) {
		
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
		if(isBibliotecarioMode) {
			attribute.addFlashAttribute("success", "Prestamo realizado con éxito");
			return "redirect:/menuBibliotecario";
		}else {
			attribute.addFlashAttribute("success", "Prestamo realizado con éxito");
			return "redirect:/menuUsuario";
		}
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
	public String realizarDevolucion(@PathVariable("id") Long prestamoId, Model model, RedirectAttributes attribute) {
		
		Usuario user = usuarioService.getUsuario();
		
		Optional<Usuario> value = usuarioService.buscarUsuarioPorId(user.getId());
		user = value.orElse(null);
		if(user == null) {
			return "views/error";
		}
		
		Optional<Prestamo> prestamoValue = prestamoService.buscarPrestamoPorId(prestamoId);
		Prestamo prestamo = prestamoValue.orElse(null);
		
		if(prestamo == null) {
			return "views/error";
		}
		
		gestorPenalizaciones.aplicarPenalizaciones(user, fechaGlobal, prestamo);
		prestamo.setActivo(false);
		prestamoService.guardarPrestamo(prestamo);

		if(isBibliotecarioMode) {
			attribute.addFlashAttribute("success", "Devolucion registrada con éxito");
			return "redirect:/menuBibliotecario";
		}else {
			attribute.addFlashAttribute("success", "Devolución registrada realizado con éxito");
			return "redirect:/menuUsuario";
		}
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
        	if (r.getUsuario().getId().equals(user.getId())  && r.getTitulo().getId().equals(titulo.getId())){
        		
        		if(isBibliotecarioMode) {
            		return "/views/Bibliotecario/ReservaNoPosible";
        		}else {
            		return "/views/Usuario/ReservaNoPosible";
        		}
        		
            }
        }
        
        reserva.setTitulo(titulo);
        reserva.setUsuario(user);
        reserva.setFecha(fechaActual);

        reservaService.guardarReserva(reserva);
        
        if (isBibliotecarioMode) {
        	return "/views/Bibliotecario/ReservaRealizadaBibliotecario";
        }else {
    		return "/views/Usuario/ReservaRealizadaUsuario";
        }
	}
	
	@GetMapping("/menuBibliotecario")
	public String menuBibliotecario(Model model) {
		
		isBibliotecarioMode = true;
		log.info("MODO BIBLIOTECARIO. FLAGBIBLIOTECARIO= " + isBibliotecarioMode);
		Usuario user = usuarioService.getUsuario();
		model.addAttribute("usuario", user);
		
		return "/views/Bibliotecario/MenuBibliotecario";
	}
	
	@GetMapping("/menuUsuario")
	public String menuUsuario(Model model) {
		Usuario user = usuarioService.getUsuario();

		log.info("MODO USUARIO. FLAGBIBLIOTECARIO= " + isBibliotecarioMode);

		model.addAttribute("usuario", user);
		model.addAttribute(NOMBRE, user.getNombre());
		return "/views/Usuario/MenuUsuario";
	}
	
	@GetMapping("/reservaEliminada/{id}")
	public String eliminarReserva(@PathVariable("id") Long tituloId, Model model, RedirectAttributes attribute){	
	
	
		Usuario user = usuarioService.getUsuario();
	
		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		List<Reserva> listaReservas = reservaService.listarReservas();
		model.addAttribute("usuario", user);
		
		attribute.addFlashAttribute("warning", "Reserva cancelada");
		
		for (Reserva r : listaReservas) {
	    	if (r.getUsuario().getId() == user.getId() && r.getTitulo().getId() == titulo.getId()){
	    		Long idReserva = r.getId();
	    		
	    		reservaService.eliminarReserva(idReserva);
	    		if(isBibliotecarioMode)
	    			return "/views/Bibliotecario/MenuBibliotecario";
	    		else
	    			return "/views/Usuario/MenuUsuario";
	    	}
	    }
		
		
		if(isBibliotecarioMode)
			return "/views/Bibliotecario/MenuBibliotecario";
		else
			return "/views/Usuario/MenuUsuario";
	}


}