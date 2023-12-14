package com.library.Library.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "titulos")
public class Titulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;

	private String isbn;
	
	private String numReserva;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Autor> autores;
	
	@OneToMany(mappedBy = "title", cascade = CascadeType.REMOVE)
	private List<Ejemplar> ejemplares;
	
	public Titulo() {
	}

	public Titulo(Long id, String nombre, String isbn, String numReserva, List<Autor> autores,
			List<Ejemplar> ejemplares) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.isbn = isbn;
		this.numReserva = numReserva;
		this.autores = autores;
		this.ejemplares = ejemplares;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public List<Ejemplar> getEjemplares() {
		return ejemplares;
	}

	public void setEjemplares(List<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}
	
	public String toString() {
		return String.format("Titulo [id=%s, libro=%s, isbn=%s]", id, nombre, isbn);
	}
}