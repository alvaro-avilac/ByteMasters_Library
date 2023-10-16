package com.library.Library.entity;

import java.util.*;

public class Usuario {

	Collection<Prestamo> prestamos;
	Collection<Reserva> reservas;
	private String id;
	private String nombre;
	private String apellidos;
	private Date fechaFinPenalizacion;

}