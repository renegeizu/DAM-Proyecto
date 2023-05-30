package com.izv.restaurante.pojo;

	import java.io.Serializable;

public class DetallePedido implements Serializable{

	private static final long serialVersionUID = 1L;
	private int Id, Pedido, Carta, Cantidad; 
	private float Precio;
	
	public DetallePedido(){
		this(0, 0, 0, 0, 0);
	}

	public DetallePedido(int pedido, int carta, int cantidad, float precio) {
		super();
		Pedido = pedido;
		Carta = carta;
		Cantidad = cantidad;
		Precio = precio;
	}

	public DetallePedido(int id, int pedido, int carta, int cantidad, float precio){
		Id = id;
		Pedido = pedido;
		Carta = carta;
		Cantidad = cantidad;
		Precio = precio;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getPedido() {
		return Pedido;
	}

	public void setPedido(int pedido) {
		Pedido = pedido;
	}

	public int getCarta() {
		return Carta;
	}

	public void setCarta(int carta) {
		Carta = carta;
	}

	public int getCantidad() {
		return Cantidad;
	}

	public void setCantidad(int cantidad) {
		Cantidad = cantidad;
	}

	public float getPrecio() {
		return Precio;
	}

	public void setPrecio(float precio) {
		Precio = precio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (Cantidad ^ (Cantidad >>> 32));
		result = prime * result + (int) (Carta ^ (Carta >>> 32));
		result = prime * result + (int) (Id ^ (Id >>> 32));
		result = prime * result + (int) (Pedido ^ (Pedido >>> 32));
		result = prime * result + Float.floatToIntBits(Precio);
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
		DetallePedido other = (DetallePedido) obj;
		if (Cantidad != other.Cantidad)
			return false;
		if (Carta != other.Carta)
			return false;
		if (Id != other.Id)
			return false;
		if (Pedido != other.Pedido)
			return false;
		if (Float.floatToIntBits(Precio) != Float.floatToIntBits(other.Precio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DetallePedido - Id=" + Id + ", Pedido=" + Pedido + ", Carta="
				+ Carta + ", Cantidad=" + Cantidad + ", Precio=" + Precio;
	}
	
}