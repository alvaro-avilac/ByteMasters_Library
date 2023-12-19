package com.library.Library.entity;

import java.util.Date;

import jakarta.persistence.Column;
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
@Table(name = "Prestamo")
public class Prestamo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = true)
	@JoinColumn(name = "usario_id", nullable = false)
	private Usuario usuario;
	private Date fechaInicio;
	private Date fechaFinal;
	private boolean activo;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ejemplar_id", nullable = false)
	private Ejemplar ejemplar;

	public Prestamo() {
	}

	
	public Prestamo(Long id, Usuario usuario, Date fechaInicio, Date fechaFinal, boolean activo, Ejemplar ejemplar) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.activo = activo;
		this.ejemplar = ejemplar;
	}


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Ejemplar getEjemplar() {
		return ejemplar;
	}

	public void setEjemplar(Ejemplar ejemplar) {
		this.ejemplar = ejemplar;
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
	
	public String toString() {
		return String.format("Prestamo [usuario=%s, ejemplar=%s]", usuario.toString(), ejemplar.toString());
	}

}