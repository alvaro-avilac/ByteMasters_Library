package com.library.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.Library.entity.Autor;
import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Titulo;
import com.library.Library.repository.AutorDAO;
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
	private AutorDAO autorDAO;
	
	@GetMapping("/altaTitulo") //endpoint que estamos mapeando
	public String mostrarForm(Model model) {
		
		Titulo titulo = new Titulo();
		List<Autor> autores = (List<Autor>) autorDAO.findAll();
		
		log.info(tituloService.listarTitulos().toString());
		
		model.addAttribute("titulo", titulo);
		model.addAttribute("listaAutores", autores);
				
		return "views/titulos/formAltaTitulo"; //RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute Titulo titulo) {
		
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
	
	@GetMapping("/detalle/{id}")
	public String detallesTitulo(@PathVariable("id") Long tituloId, Model model) {
		
		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		model.addAttribute("titulo", titulo);
		return "/views/titulos/detalleTitulo";
	}
	
	@GetMapping("detalle/edit/{id}")
	public String mostrarFormEditarTitulo(@PathVariable("id") Long tituloId, Model model) {
		
		Titulo titulo = tituloService.buscarTituloPorId(tituloId);
		model.addAttribute("titulo", titulo);
		return "/views/titulos/formAltaTitulo";
	}
	
	@GetMapping("detalle/delete/{id}")
	public String EliminarTitulo(@PathVariable("id") Long tituloId, Model model) {
		
		tituloService.bajaTitulo(tituloId);
		
		return "/views/titulos/mostrarTitulos";
	}
}