package com.library.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.Library.entity.Ejemplar;
import com.library.Library.entity.Titulo;
import com.library.Library.service.IServiceTitulo;

@Controller
@RequestMapping("/")
public class GestorTitulos {
	
	@Autowired
	private IServiceTitulo tituloService; //Uso interfaz de servicios de titulo
	
	
	@GetMapping("/altaTitulo") //endpoint que estamos mapeando
	public String mostrarForm(Model model) {
		
		Titulo titulo = new Titulo();
		Ejemplar ejemplar = new Ejemplar();
		
		model.addAttribute("titulo", titulo);
		model.addAttribute("ejemplar", ejemplar);
				
		return "views/titulos/formAltaTitulo"; //RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}

	@PostMapping("/saved")
	public String altaTitulo(@ModelAttribute("titulo") Titulo titulo) {
		
		tituloService.altaTitulo(titulo);
		return "redirect:/";
	}

}