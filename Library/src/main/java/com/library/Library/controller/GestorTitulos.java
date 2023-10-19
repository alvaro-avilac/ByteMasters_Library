package com.library.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Titulo;
import com.library.Library.service.IServiceTitulo;

@RequestMapping("/")
@Controller
public class GestorTitulos {
	private static final Logger log = LoggerFactory.getLogger(GestorTitulos.class);
	
	@Autowired
	private IServiceTitulo tituloService; //Uso interfaz de servicios de titulo
	
	
	@GetMapping("/altaTitulo") //endpoint que estamos mapeando
	public String mostrarForm(Model model) {
		
		Titulo titulo = new Titulo();
		log.info(tituloService.listarTitulos().toString());
		model.addAttribute("titulo", titulo);
				
		return "views/titulos/formAltaTitulo"; //RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute Titulo titulo) {
		
		tituloService.altaTitulo(titulo);
		return "redirect:/";
	}

}