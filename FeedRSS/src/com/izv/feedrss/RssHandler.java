package com.izv.feedrss;

import java.util.List;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssHandler extends DefaultHandler{
	private List<ItemRss> lista;
    private ItemRss elemento;
    private StringBuilder texto;
    
    public List<ItemRss> getElemento(){
        return lista;
    }
    
    @Override
    public void characters (char[] ch, int comienzo, int longitud) throws SAXException {
        super.characters(ch, comienzo, longitud);
        if (this.elemento != null){
        	texto.append(ch, comienzo, longitud);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        if (this.elemento != null) {
            if (localName.equals("title")) {
                elemento.setTitulo(texto.toString());
            } else if (localName.equals("link")) {
                elemento.setLink(texto.toString());
            } else if (localName.equals("description")) {
            	elemento.setDescripcion(texto.toString());
            } else if (localName.equals("guid")) {
            	elemento.setGuid(texto.toString());
            } else if (localName.equals("pubDate")) {
            	elemento.setFecha(texto.toString());
            } else if (localName.equals("item")) {
            	lista.add (elemento);
            }
            texto.setLength(0);
        }
    }
 
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        lista = new ArrayList<ItemRss>();
        texto = new StringBuilder();
    }
 
    @Override
    public void startElement(String uri, String localName, String nombre, Attributes atributos) throws SAXException {
        super.startElement(uri, localName, nombre, atributos);
        if (localName.equals("item")) {
            elemento = new ItemRss();
        }
    }
}
