package com.library.Library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ejemplares")
public class Ejemplar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "titulo_id")
	private Titulo title;

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
	
	public String toString() {
		return String.format("Ejemplar [id=%s, libro=%s, isbn=%s]", id, title.getNombre(), title.getIsbn());
	}

}