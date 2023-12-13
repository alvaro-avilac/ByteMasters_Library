package com.library.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.Library.entity.Autor;
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

@RequestMapping("/")
@Controller
public class GestorTitulos {
	private static final Logger log = LoggerFactory.getLogger(GestorTitulos.class);

	@Autowired
	private IServiceTitulo tituloService; // Uso interfaz de servicios de titulo

	@Autowired
	private IServiceEjemplar ejemplarService;

	@Autowired
	private IServiceAutor autorService;

	@Autowired
	private IServiceUsuario usuarioService;

	@Autowired
	private IServiceReserva reservaService;

	@Autowired
	private IServicePrestamo prestamoService;

	@GetMapping("/altaTitulo") // endpoint que estamos mapeando
	public String mostrarFormAltaTitulo(Model model) {

		Titulo titulo = new Titulo();
		List<Autor> listaAutores = autorService.listarAutores();

		model.addAttribute("titulo", titulo);
		model.addAttribute("listaAutores", listaAutores);
		return "views/admin/titulos/formAltaTitulo"; // RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN
														// TITULO
	}

	@GetMapping("/altaAutor")
	public String mostrarFormAltaAutor(Model model) {
		Autor autor = new Autor();
		model.addAttribute("autor", autor);
		return "views/admin/autores/formAltaAutores";
	}

	@PostMapping("/autor_saved")
	public String altaAutor(@ModelAttribute Autor autor) {

		autorService.altaAutor(autor);

		return "redirect:/altaTitulo";
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute Titulo titulo, @RequestParam("autoresStr") List<String> autoresStr,
			@RequestParam("numEjemplaresStr") Integer numEjemplaresStr, Model model) {

		log.info("Autores: " + autoresStr.toString());

		List<Autor> autores = new ArrayList<>();

		for (String nombreApellido : autoresStr) {
			String[] partes = nombreApellido.split(" ");
			if (partes.length >= 2) {
				Autor autorExistente = autorService.buscarAutorPorNombreYApellido(partes[0], partes[1]);

				if (autorExistente != null) {
					log.info("encontrado autor existente " + autorExistente.toString());
					autores.add(autorExistente);
				} else {
					Autor autor = new Autor();
					autor.setNombre(partes[0]);
					autor.setApellido(partes[1]);
					autorService.altaAutor(autor);
					log.info("añadiendo nuevo autor " + autor.toString());
					autores.add(autor);
				}

			} else {
				Autor autorExistente = autorService.buscarAutorPorNombre(partes[0]);

				if (autorExistente != null) {
					log.info("autor existente encontrado");
					autores.add(autorExistente);
				} else {
					Autor autor = new Autor();
					autor.setNombre(partes[0]);
					autorService.altaAutor(autor);
					log.info("añadiendo nuevo autor");
					autores.add(autor);
				}
			}
		}

		tituloService.altaTitulo(titulo);

		titulo.setAutores(autores);

		List<Ejemplar> ejemplares = new ArrayList<>();

		for (int i = 0; i < numEjemplaresStr; i++) {
			Ejemplar ejemplar = new Ejemplar();
			ejemplar.setTitulo(titulo);
			ejemplarService.altaEjemplar(ejemplar);
			ejemplares.add(ejemplar);
		}

		titulo.setEjemplares(ejemplares);
		return "redirect:/mostrar";
	}

	@PostMapping("/edited")
	public String EditarTitulo(@ModelAttribute Titulo titulo, @RequestParam("autoresStr") List<String> autoresStr,
			Model model, RedirectAttributes attribute) {

		List<Autor> autores = new ArrayList<>();

		for (String nombreApellido : autoresStr) {
			String[] partes = nombreApellido.split(" ");
			if (partes.length >= 2) {
				Autor autorExistente = autorService.buscarAutorPorNombreYApellido(partes[0], partes[1]);

				if (autorExistente != null) {
					log.info("encontrado autor existente " + autorExistente.toString());
					autores.add(autorExistente);
				} else {
					Autor autor = new Autor();
					autor.setNombre(partes[0]);
					autor.setApellido(partes[1]);
					autorService.altaAutor(autor);
					log.info("añadiendo nuevo autor " + autor.toString());
					autores.add(autor);
				}

			} else {
				Autor autorExistente = autorService.buscarAutorPorNombre(partes[0]);
				if (autorExistente != null) {
					log.info("autor existente encontrado");
					autores.add(autorExistente);
				} else {
					Autor autor = new Autor();
					autor.setNombre(partes[0]);
					autorService.altaAutor(autor);
					log.info("añadiendo nuevo autor");
					autores.add(autor);
				}
			}
		}

		titulo.setAutores(autores);
		tituloService.altaTitulo(titulo);
		attribute.addFlashAttribute("success", "Titulo editado con éxito");
		return "redirect:/detalle/" + titulo.getId();
	}

	@PostMapping("/userLogged")
	public String processLoginForm(@RequestParam("nombreUsuario") String nombreUsuario,
			@RequestParam("apellidosUsuario") String apellidosUsuario, @RequestParam("rol") String rol, Model model) {
		// Lógica para procesar el formulario de inicio de sesión
		// y redirigir a la página adecuada.
		Usuario user = new Usuario();

		if (usuarioService.buscarUsuarioPorNombreyApellido(nombreUsuario, apellidosUsuario) == null) {
			user.setNombre(nombreUsuario);
			user.setApellidos(apellidosUsuario);
			usuarioService.guardarUsuario(user);
			usuarioService.setGlobalUsuario(user);
		} else {
			user = usuarioService.buscarUsuarioPorNombreyApellido(nombreUsuario, apellidosUsuario);
			usuarioService.setGlobalUsuario(user);
		}

		if (rol.equals("admin")) {
			return "redirect:/admin";
		} else if (rol.equals("bibliotecario")) {
			return "redirect:/bibliotecario";
		} else if (rol.equals("usuario")) {
			model.addAttribute("usuario", user);
			return "redirect:/user";
		}
		return "redirect:/";
	}

	@PostMapping("/selectedUser")
	public String buscarUsuarioPorId(@RequestParam("idUsuario") long idUsuario) {
		Optional<Usuario> optionalUser = usuarioService.buscarUsuarioPorId(idUsuario);

		if (optionalUser.isPresent()) {
			Usuario user = optionalUser.get();
			usuarioService.setGlobalUsuario(user);

			return "/views/Bibliotecario/MenuBibliotecario";
		} else {
			// TODO manejo de error
			return "redirect:/";
		}
	}

	@GetMapping("/mostrar")
	public String mostrarTitulos(Model model) {
		List<Titulo> listadoTitulos = tituloService.listarTitulos();
		model.addAttribute("nombre", "Lista de titulos");
		model.addAttribute("titulos", listadoTitulos);

		return "/views/admin/titulos/mostrarTitulos";
	}

	@GetMapping("/mostrarAutores")
	public String mostrarAutores(Model model) {
		List<Autor> listadoAutores = autorService.listarAutores();
		model.addAttribute("nombre", "Lista de Autores");
		model.addAttribute("autores", listadoAutores);
		return "/views/admin/autores/mostrarAutores";
	}

	@GetMapping("/detalle/{id}")
	public String detallesTitulo(@PathVariable("id") Long tituloId, Model model) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		List<Ejemplar> listaEjemplares = ejemplarService.listarEjemplaresPorTitulo(tituloId);

		model.addAttribute("titulo", titulo);
		model.addAttribute("autoresStr", titulo.getAutores().toString());
		model.addAttribute("numEjemplares", titulo.getEjemplares().size());
		model.addAttribute("listaEjemplares", listaEjemplares);

		return "/views/admin/titulos/detalleTitulo";
	}

	@GetMapping("detalle/edit/{id}")
	public String mostrarFormEditarTitulo(@PathVariable("id") Long tituloId, Model model) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		List<Autor> listaAutores = autorService.listarAutores();

		model.addAttribute("listaAutores", listaAutores);
		model.addAttribute("titulo", titulo);
		model.addAttribute("autoresStr",
				titulo.getAutores().toString().substring(1, titulo.getAutores().toString().length() - 1));
		model.addAttribute("numEjemplares", titulo.getEjemplares().size());

		return "/views/admin/titulos/formEditarTitulo";
	}

	@GetMapping("detalle/delete/{id}")
	public String EliminarTitulo(@PathVariable("id") Long tituloId, Model model, RedirectAttributes attribute) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		if (tituloTieneReservasPrestamos(titulo)) {
			
			attribute.addFlashAttribute("error", "El titulo que desea borrar tiene algun prestamo o reserva activo");
			return "redirect:/detalle/{id}";
		} else {

		}
		prestamoService.borrarPrestamosEjemplaresByTitulo(titulo);
		titulo.getAutores().clear();
		tituloService.bajaTitulo(tituloId);

		return "redirect:/mostrar";
	} 

	private boolean tituloTieneReservasPrestamos(Titulo titulo) {

		List<Prestamo> listadoPrestamos = prestamoService.listarPrestamos();

		for(Prestamo p: listadoPrestamos) {
			if(p.getEjemplar().getTitulo().equals(titulo)) {
				if (p.isActivo()  &&  p.getEjemplar().getTitulo().equals(titulo)) {
					return true;
				}
			}
		}

		return false;
	}

	@PostMapping("/detalle/delete_ejemplares")
	public String EliminarEjemplares(@RequestParam("idTitle") Long idTitle,
			@RequestParam("selected_ejemplares") List<Long> selected_ejemplares, Model model, RedirectAttributes attribute) {
		log.info("Lista Ejemplares seleccionados " + selected_ejemplares);
		Titulo titulo = tituloService.buscarTituloPorId(idTitle);
		
		for (Long ejemplar : selected_ejemplares) {

			if (ejemplarTieneReservasPrestamos(ejemplar)) {
				attribute.addFlashAttribute("error", "El/los ejemplar que desea borrar tiene algun prestamo o reserva activo");

			} else {
				prestamoService.borrarPrestamosByEjemplar(ejemplar);
				ejemplarService.bajaEjemplar(ejemplar);
			}
		}

		return "redirect:/detalle/" + titulo.getId();
	}

	private boolean ejemplarTieneReservasPrestamos(Long ejemplarId) {

		List<Prestamo> listadoPrestamos = prestamoService.listarPrestamos();
		
		for (Prestamo p : listadoPrestamos) {
		
			if (p.isActivo() && p.getEjemplar().getId().equals(ejemplarId)) {
				return true;
			}
			
		}
		
		return false;
	}

	@PostMapping("/detalle/agregar_ejemplares")
	public String AgregarEjemplares(@RequestParam("numeroEjemplares") Integer numeroEjemplares,
			@RequestParam("idTitle") Long idTitle, Model model) {
		log.info("Numero de Ejemplares seleccionados " + numeroEjemplares.toString());

		Titulo titulo = tituloService.buscarTituloPorId(idTitle);

		log.info("Titulo: " + titulo.toString());

		List<Ejemplar> ejemplares = new ArrayList<>();

		for (int i = 0; i < numeroEjemplares; i++) {
			Ejemplar ejemplar = new Ejemplar();
			ejemplar.setTitulo(titulo);
			ejemplarService.altaEjemplar(ejemplar);
			ejemplares.add(ejemplar);
		}
		titulo.setEjemplares(ejemplares);

		return "redirect:/detalle/" + titulo.getId();
	}

	@GetMapping("/mostrarTitulos")
	public String mostrarTitulosUser(Model model) {
		List<Titulo> listadoTitulos = tituloService.listarTitulos();
		model.addAttribute("nombre", "Lista de titulos");
		model.addAttribute("titulos", listadoTitulos);
		return "/views/Usuario/MostrarTitulosUser";
	}

	@SuppressWarnings("null")
	@GetMapping("/reserva")
	public String mostrarReservas(Model model) {
		Usuario user = usuarioService.getUsuario();
		List<Reserva> listadoReservas = reservaService.listarReservas();
		List<Titulo> listadoTitulos = new ArrayList<>();

		for (Reserva r : listadoReservas) {
			if (r.getUsuario().getId().equals(user.getId())) {
				listadoTitulos.add(r.getTitulo());
			}
		}

		model.addAttribute("nombre", "Lista de reservas");
		model.addAttribute("titulos", listadoTitulos);

		return "/views/Bibliotecario/MostrarReservas";

	}

	@GetMapping("/user")
	public String mostraMainWindowUser(Model model) {
		model.addAttribute("usuario", usuarioService.getUsuario());
		return "/views/Usuario/MenuUsuario";
	}

	@GetMapping("/admin")
	public String mostraMainWindowAdmin() {
		return "/views/admin/titulos/MenuAdmin";
	}

	@GetMapping("/bibliotecario")
	public String mostraMainWindowBibliotecario() {
		return "/views/Bibliotecario/SeleccionUsuario";
	}

}