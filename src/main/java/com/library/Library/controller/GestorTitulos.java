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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.Library.entity.Autor;
import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Formulario;
import com.library.Library.entity.Titulo;
import com.library.Library.entity.Usuario;
import com.library.Library.service.IServiceAutor;
import com.library.Library.service.IServiceEjemplar;
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
	
	@GetMapping("/altaTitulo") // endpoint que estamos mapeando
	public String mostrarForm(Model model) {

		Titulo titulo = new Titulo();
		model.addAttribute("titulo", titulo);

		return "views/admin/titulos/formAltaTitulo"; // RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute Titulo titulo, @RequestParam("autoresStr") List<String> autoresStr,
			@RequestParam("numEjemplaresStr") Integer numEjemplaresStr) {

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
	public String EditarTitulo(@ModelAttribute Titulo titulo, @RequestParam("autoresStr") List<String> autoresStr) {

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

		return "redirect:/detalle/" + titulo.getId();
	}

	@PostMapping("/userLogged")
	public String processLoginForm(@RequestParam("nombreUsuario") String nombreUsuario, @RequestParam("apellidosUsuario") String apellidosUsuario,
			@RequestParam("contrasena") String contrasena, @RequestParam("rol") String rol, Model model) {
		// Lógica para procesar el formulario de inicio de sesión
		// y redirigir a la página adecuada.
		Usuario user = new Usuario();

		if(usuarioService.buscarUsuarioPorNombreyApellido(nombreUsuario, apellidosUsuario) == null) {
			user.setNombre(nombreUsuario);
			user.setApellidos(apellidosUsuario);
			usuarioService.guardarUsuario(user);
			usuarioService.setGlobalUsuario(user);
		}else {
			user = usuarioService.buscarUsuarioPorNombreyApellido(nombreUsuario, apellidosUsuario);
			usuarioService.setGlobalUsuario(user);

		}
		
		if (rol.equals("admin")) {
			return "redirect:/admin";
		} else if (rol.equals("bibliotecario")) {
			return "redirect:/user";
		} else if (rol.equals("usuario")) {
			model.addAttribute("usuario", user);
			return "redirect:/user";
		}
		return "redirect:/";
	}

	@GetMapping("/mostrar")
	public String mostrarTitulos(Model model) {
		List<Titulo> listadoTitulos = tituloService.listarTitulos();
		model.addAttribute("nombre", "Lista de titulos");
		model.addAttribute("titulos", listadoTitulos);

		return "/views/admin/titulos/mostrarTitulos";
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

		model.addAttribute("titulo", titulo);
		model.addAttribute("autoresStr",
				titulo.getAutores().toString().substring(1, titulo.getAutores().toString().length() - 1));
		model.addAttribute("numEjemplares", titulo.getEjemplares().size());

		return "/views/admin/titulos/formEditarTitulo";
	}

	@PostMapping("detalle/delete/{id}")
	public String EliminarTitulo(@PathVariable("id") Long tituloId, Model model) {

		tituloService.bajaTitulo(tituloId);

		return "redirect:/mostrar";
	}

	@PostMapping("/detalle/delete_ejemplares")
	public String EliminarEjemplares(@RequestParam("idTitle") Long idTitle,
			@RequestParam("selected_ejemplares") List<Long> selected_ejemplares, Model model) {
		log.info("Lista Ejemplares seleccionados " + selected_ejemplares);
		Titulo titulo = tituloService.buscarTituloPorId(idTitle);
		for (Long ejemplar : selected_ejemplares) {
			ejemplarService.bajaEjemplar(ejemplar);
		}
		return "redirect:/detalle/" + titulo.getId();
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
		return "";
	}
	
}