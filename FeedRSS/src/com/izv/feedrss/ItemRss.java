package com.izv.feedrss;

public class ItemRss {
	
	private String titulo;
    private String link;
    private String descripcion;
    private String guid;
    private String fecha;
 
    public String getTitulo() {
        return titulo;
    }
 
    public String getLink() {
        return link;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
 
    public String getGuid() {
        return guid;
    }
 
    public String getFecha() {
        return fecha;
    }
 
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
 
    public void setLink(String link) {
        this.link = link;
    }
 
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
 
    public void setGuid(String guid) {
        this.guid = guid;
    }
 
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
	
}
