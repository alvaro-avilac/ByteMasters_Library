package com.library.Library.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Prestamo")
public class Prestamo {
 


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne 
	@JoinColumn (name ="usario_id")
	private Usuario usuario;
	private Date fechaInicio;
	private Date fechaFinal;
	private boolean activo;
	
	@OneToOne 
	private Ejemplar ejemplar;
	
	public Prestamo() {
	}

	public Prestamo(Long id, Date fechaInicio, Date fechaFinal, boolean activo) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.activo = activo;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	
	
}




