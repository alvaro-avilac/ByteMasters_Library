package com.library.Library.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@ManyToOne
	@JoinColumn(name = "autor_id")
	private Autor autor;
	
	@OneToMany(mappedBy = "title")
	private List<Ejemplar> ejemplares;
	
	public Titulo() {
	}

	public Titulo(Long id, String nombre, String isbn, String numReserva, Autor autor,
			List<Ejemplar> ejemplares) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.isbn = isbn;
		this.numReserva = numReserva;
		this.autor = autor;
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

	public Autor getAutores() {
		return autor;
	}

	public void setAutores(Autor autores) {
		this.autor = autores;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public List<Ejemplar> getEjemplares() {
		return ejemplares;
	}

	public void setEjemplares(List<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}
	
	

}