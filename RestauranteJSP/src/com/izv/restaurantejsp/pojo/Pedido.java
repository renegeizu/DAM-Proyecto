package com.izv.restaurantejsp.pojo;

	import java.io.Serializable;
	import org.json.JSONException;
	import org.json.JSONObject;

public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id; 
	private String fechaHora;
	private int idMesa, cerrado;
	
	public Pedido(){
	}
	
	public Pedido(String fechaHora, int mesa, int cerrado) {
		super();
		this.fechaHora = fechaHora;
		this.idMesa = mesa;
		this.cerrado = cerrado;
	}

	public Pedido(long id, String fechaHora, int mesa, int cerrado) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.idMesa = mesa;
		this.cerrado = cerrado;
	}

	public Pedido (JSONObject json) throws JSONException{
		this (json.getLong ("id"), json.getString ("fechaHora"), json.getInt("idMesa"), json.getInt("cerrado"));
	}
	
	public Pedido (String[] registro) {
	    set(registro, 0);
	}
	
	public Pedido (String[] registro, int inicial) {
	    set(registro, inicial);
	}
	
	public final void set(String[] registro){
	    set(registro, 0);
	}
	
	public final void set(String[] registro, int inicial){
	    if(registro!=null){
	        this.id = Long.parseLong(registro[0+inicial]);
	        this.fechaHora = registro[1+inicial];
	        this.idMesa = Integer.parseInt(registro [2+inicial]);
	        this.cerrado = Integer.parseInt(registro [3+inicial]);
	    }
	}
	
	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public int getMesa() {
		return idMesa;
	}

	public void setMesa(int mesa) {
		idMesa = mesa;
	}

	public int getCerrado() {
		return cerrado;
	}

	public void setCerrado(int cerrado) {
		this.cerrado = cerrado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cerrado;
		result = prime * result
				+ ((fechaHora == null) ? 0 : fechaHora.hashCode());
		result = (int) (prime * result + id);
		result = prime * result + idMesa;
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
		if (cerrado != other.cerrado)
			return false;
		if (fechaHora == null) {
			if (other.fechaHora != null)
				return false;
		} else if (!fechaHora.equals(other.fechaHora))
			return false;
		if (id != other.id)
			return false;
		if (idMesa != other.idMesa)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pedido " + id + " en la Fecha y Hora " + fechaHora + " para la idMesa "
				+ idMesa;
	}
	
}