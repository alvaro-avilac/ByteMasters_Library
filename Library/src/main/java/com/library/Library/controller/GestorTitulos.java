package com.library.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.Library.service.IServiceTitulo;

@Controller
@RequestMapping("/")
public class GestorTitulos {
	
	//@Autowired
	//private IServiceTitulo tituloService; //Uso interfaz de servicios de titulo
	
	@GetMapping("/") //endpoint que estamos mapeando
	public String crearTitulo(Model model) {
		
		//Titulo titulo = new Titulo();
		//model.addAttribute("titulo",titulo);
		//mas model atributes en funcion de lo que necesitemos ver en el html
				
		return "views/titulos/formAltaTitulo"; //RUTA A ARCHIVO .HTML DONDE ESTE EL FORMULARIO DE DAR DE ALTA UN TITULO
	}


}