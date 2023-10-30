package com.library.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.Library.entity.Autor;
import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Titulo;
import com.library.Library.service.IServiceAutor;
import com.library.Library.service.IServiceEjemplar;
import com.library.Library.service.IServiceTitulo;

@RequestMapping("/")
@Controller
public class GestorTitulos {
	private static final Logger log = LoggerFactory.getLogger(GestorTitulos.class);
	
	@Autowired
	private IServiceTitulo tituloService; //Uso interfaz de servicios de titulo
	
	@Autowired
	private IServiceEjemplar ejemplarService;
	
	@Autowired 
	private IServiceAutor autorService;
	
	@GetMapping("/altaTitulo") //endpoint que estamos mapeando
	public String mostrarForm(Model model) {
		
		Titulo titulo = new Titulo();
		List<Autor> autores = (List<Autor>) autorService.listarAutores();
		
		log.info(tituloService.listarTitulos().toString());
		
		model.addAttribute("titulo", titulo);
		model.addAttribute("listaAutores", autores);
				
		return "views/titulos/formAltaTitulo"; //RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute Titulo titulo) {
		
		List<String> nombresAutores = Arrays.asList(titulo.getAutoresStr().split("\\s*,\\s*"));
		
		List<Autor> autores = new ArrayList<>();
		
		for (String nombreApellido : nombresAutores) {
			String[] partes = nombreApellido.split(" ");
			if (partes.length >= 2) {
				Autor autor = new Autor();
				autor.setNombre(partes[0]);
				autor.setApellido(partes[1]);
				autorService.altaAutor(autor);
				autores.add(autor);
			} else {
				Autor autor = new Autor();
				autor.setNombre(partes[0]);
				autorService.altaAutor(autor);
				autores.add(autor);
			}
		}
		
		titulo.setAutores(autores);
		Ejemplar ejemplar = new Ejemplar();
		ejemplar.setTitulo(titulo);
		
		tituloService.altaTitulo(titulo);
		ejemplarService.altaEjemplar(ejemplar);
		
		return "redirect:/mostrar";
	}
	
	
	@GetMapping("/mostrar")
	public String mostrarTitulos(Model model) {
		List<Titulo> listadoTitulos = tituloService.listarTitulos();
		model.addAttribute("nombre", "Lista de titulos");
		model.addAttribute("titulos", listadoTitulos);
		return "/views/titulos/mostrarTitulos";
	}
}