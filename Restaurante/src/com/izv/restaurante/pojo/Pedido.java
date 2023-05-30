package com.izv.restaurante.pojo;

	import java.io.Serializable;

public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;
	private int Id; 
	private String FechaHora;
	private int Mesa, Cerrado;
	
	public Pedido(){
	}
	
	public Pedido(String fechaHora, int mesa, int cerrado) {
		super();
		FechaHora = fechaHora;
		Mesa = mesa;
		Cerrado = cerrado;
	}

	public Pedido(int id, String fechaHora, int mesa, int cerrado) {
		super();
		Id = id;
		FechaHora = fechaHora;
		Mesa = mesa;
		Cerrado = cerrado;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFechaHora() {
		return FechaHora;
	}

	public void setFechaHora(String fechaHora) {
		FechaHora = fechaHora;
	}

	public int getMesa() {
		return Mesa;
	}

	public void setMesa(int mesa) {
		Mesa = mesa;
	}

	public int getCerrado() {
		return Cerrado;
	}

	public void setCerrado(int cerrado) {
		Cerrado = cerrado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Cerrado;
		result = prime * result
				+ ((FechaHora == null) ? 0 : FechaHora.hashCode());
		result = prime * result + Id;
		result = prime * result + Mesa;
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
		Pedido other = (Pedido) obj;
		if (Cerrado != other.Cerrado)
			return false;
		if (FechaHora == null) {
			if (other.FechaHora != null)
				return false;
		} else if (!FechaHora.equals(other.FechaHora))
			return false;
		if (Id != other.Id)
			return false;
		if (Mesa != other.Mesa)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pedido " + Id + " en la Fecha y Hora " + FechaHora + " para la Mesa "
				+ Mesa;
	}
	
}