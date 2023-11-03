package com.library.Library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ejemplares")
public class Ejemplar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "titulo_id", nullable=false)
	private Titulo title;

	/* @OneToOne
	 @JoinColumn(name = "prestamo_id", unique=true)
	 private Prestamo prestamo;*/
	
	public Ejemplar() {
		super();
	}

	public Ejemplar(Long id, Titulo titulo) {
		super();
		this.id = id;
		this.title = titulo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Titulo getTitulo() {
		return title;
	}

	public void setTitulo(Titulo titulo) {
		this.title = titulo;
	}

}