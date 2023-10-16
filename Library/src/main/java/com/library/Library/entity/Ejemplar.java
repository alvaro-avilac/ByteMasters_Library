package com.library.Library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ejemplares")
public class Ejemplar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Titulo titulo;
	private Prestamo prestamos;

	public Ejemplar() {
		super();
	}

	public Ejemplar(Long id, Titulo titulo, Prestamo prestamos) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.prestamos = prestamos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Titulo getTitulo() {
		return titulo;
	}

	public void setTitulo(Titulo titulo) {
		this.titulo = titulo;
	}

	public Prestamo getPrestamos() {
		return prestamos;
	}
	
	public void setPrestamos(Prestamo prestamos) {
		this.prestamos = prestamos;
	}

}