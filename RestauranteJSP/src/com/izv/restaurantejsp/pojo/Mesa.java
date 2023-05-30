package com.izv.restaurantejsp.pojo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Mesa implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String nombre;
	
	public Mesa() {
	    this(0,"");
	}
	
	public Mesa(long id, String nombre) {
	    this.id = id;
	    this.nombre = nombre;
	}
	
	public Mesa (JSONObject json) throws JSONException{
		this (json.getLong ("id"), json.getString ("nombre"));
	}
	
	public Mesa(String[] registro) {
	    set(registro, 0);
	}
	
	public Mesa(String[] registro, int inicial) {
	    set(registro, inicial);
	}
	
	public final void set(String[] registro){
	    set(registro, 0);
	}
	
	public final void set(String[] registro, int inicial){
	    if(registro!=null){
	        this.id = Long.parseLong(registro[0+inicial]);
	        this.nombre = registro[1+inicial];
	    }
	}
	
	public long getId() {
	    return id;
	}
	
	public void setId(long id) {
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
	    return "Mesa{" + "cod=" + id + ", nombre=" + nombre + '}';
	}

}
