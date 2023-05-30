package com.izv.restaurante.pojo;

	import java.io.Serializable;

public class Mesa implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	
	public Mesa() {
		this(0, "Mesa");
	}

	public Mesa(String nombre) {
		super();
		this.nombre = nombre;
	}

	public Mesa(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Mesa " + id + " de nombre " + nombre;
	}	

}