package com.library.Library.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "titulos")
public class Titulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	private String isbn;
	private String numReserva;
	
	private List<Autor> autores;

	
	public Titulo() {
		super();
	}

	public Titulo(Long id, String titulo, String isbn, String numReserva, List<Autor> autores,
			List<Ejemplar> ejemplares) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.isbn = isbn;
		this.numReserva = numReserva;
		this.autores = autores;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getNumReserva() {
		return numReserva;
	}

	public void setNumReserva(String numReserva) {
		this.numReserva = numReserva;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}

}