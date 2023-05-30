package com.izv.restaurantejsp.pojo;

	import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class DetallePedido implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id, idPedido, idCarta, cantidad; 
	private float precio;
	
	public DetallePedido(){
		this(0, 0, 0, 0, 0);
	}

	public DetallePedido(long pedido, long carta, long cantidad, float precio) {
		super();
		this.idPedido = pedido;
		this.idCarta = carta;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	public DetallePedido(long id, long pedido, long carta, long cantidad, float precio){
		this.id = id;
		this.idPedido = pedido;
		this.idCarta = carta;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	public DetallePedido (JSONObject json) throws JSONException{
		this (json.getLong ("id"), json.getLong("idPedido"), json.getLong("idCarta"), json.getLong("cantidad"), Float.parseFloat(json.getString("precio")));
	}
	
	public DetallePedido(String[] registro) {
	    set(registro, 0);
	}
	
	public DetallePedido(String[] registro, int inicial) {
	    set(registro, inicial);
	}
	
	public final void set(String[] registro){
	    set(registro, 0);
	}
	
	public final void set(String[] registro, int inicial){
	    if(registro!=null){
	        this.id = Long.parseLong(registro[0+inicial]);
	        this.idPedido = Long.parseLong(registro[1+inicial]);
	        this.idCarta = Long.parseLong(registro[2+inicial]);
	        this.cantidad = Long.parseLong(registro[3+inicial]);
	        this.precio = Float.parseFloat(registro[4+inicial]);
	    }
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPedido() {
		return idPedido;
	}

	public void setPedido(long pedido) {
		this.idPedido = pedido;
	}

	public long getCarta() {
		return idCarta;
	}

	public void setCarta(long carta) {
		this.idCarta = carta;
	}

	public long getCantidad() {
		return cantidad;
	}

	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
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
		if (cantidad != other.cantidad)
			return false;
		if (idCarta != other.idCarta)
			return false;
		if (id != other.id)
			return false;
		if (idPedido != other.idPedido)
			return false;
		if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DetallePedido [id=" + id + ", idPedido=" + idPedido + ", idCarta="
				+ idCarta + ", cantidad=" + cantidad + ", precio=" + precio + "]";
	}


	
}