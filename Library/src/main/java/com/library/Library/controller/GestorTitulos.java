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
import com.library.Library.service.IServiceAutor;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServiceTitulo;

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

	@GetMapping("/altaTitulo") // endpoint que estamos mapeando
	public String mostrarForm(Model model) {

		Titulo titulo = new Titulo();
		model.addAttribute("titulo", titulo);

		return "views/titulos/formAltaTitulo"; // RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute Titulo titulo, @RequestParam("autoresStr") List<String> autoresStr, @RequestParam("numEjemplaresStr") Integer numEjemplaresStr) {

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

	@GetMapping("/mostrar")
	public String mostrarTitulos(Model model) {
		List<Titulo> listadoTitulos = tituloService.listarTitulos();
		model.addAttribute("nombre", "Lista de titulos");
		model.addAttribute("titulos", listadoTitulos);
		return "/views/titulos/mostrarTitulos";
	}

	@GetMapping("/detalle/{id}")
	public String detallesTitulo(@PathVariable("id") Long tituloId, Model model) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		List<Ejemplar> listaEjemplares = ejemplarService.listarEjemplaresPorTitulo(tituloId);

		model.addAttribute("titulo", titulo);
		model.addAttribute("autoresStr", titulo.getAutores().toString());
		model.addAttribute("numEjemplares", titulo.getEjemplares().size());
		model.addAttribute("listaEjemplares", listaEjemplares);

		return "/views/titulos/detalleTitulo";
	}

	@GetMapping("detalle/edit/{id}")
	public String mostrarFormEditarTitulo(@PathVariable("id") Long tituloId, Model model) {

		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		
		model.addAttribute("titulo", titulo);
		model.addAttribute("autoresStr", titulo.getAutores().toString());
		model.addAttribute("numEjemplares", titulo.getEjemplares().size());
		
		return "/views/titulos/formEditarTitulo";
	}

	@PostMapping("detalle/delete/{id}")
	public String EliminarTitulo(@PathVariable("id") Long tituloId, Model model) {

		tituloService.bajaTitulo(tituloId);
		
		return "/views/titulos/mostrarTitulos";
	}
	
	@PostMapping("/detalle/delete_ejemplares")
	public String EliminarEjemplares(@ModelAttribute Titulo titulo, @RequestParam("selected_ejemplares") List<Long> selected_ejemplares, Model model) {
		log.info("Lista Ejemplares seleccionados " + selected_ejemplares.toString());

		for (Long ejemplar : selected_ejemplares) {
			ejemplarService.bajaEjemplar(ejemplar);
		}
		
		return "redirect: /mostrar";
	}
	
	@PostMapping("/detalle/agregar_ejemplares")
	public String AgregarEjemplares(@ModelAttribute Titulo titulo,@RequestParam("numEjemplares") Integer numEjemplares, Model model) {
		log.info("Lista Ejemplares seleccionados " + numEjemplares.toString());
		
		
		List<Ejemplar> ejemplares = new ArrayList<>();

		for (int i = 0; i < numEjemplares; i++) {
			Ejemplar ejemplar = new Ejemplar();
			ejemplar.setTitulo(titulo);
			ejemplarService.altaEjemplar(ejemplar);
			ejemplares.add(ejemplar);
		}
		titulo.setEjemplares(ejemplares);
		
		return "redirect: /mostrar";
	}
}