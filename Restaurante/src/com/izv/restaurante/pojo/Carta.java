package com.izv.restaurante.pojo;

	import java.io.Serializable;

public class Carta implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private float precio;
	
	public Carta() {
		this(0, "Carta", 0);
	}

	public Carta(String nombre) {
		super();
		this.nombre = nombre;
	}

	public Carta(String nombre, float precio) {
		super();
		this.nombre = nombre;
		this.precio = precio;
	}

	public Carta(int id, String nombre, float precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
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

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "Carta - Id=" + id + ", Nombre=" + nombre + ", Precio=" + precio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + Float.floatToIntBits(precio);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carta other = (Carta) obj;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
			return false;
		return true;
	}

	public int compareTo(String s) {
		return nombre.compareTo(s);
	}
	
}