package com.izv.restaurante.db;

	import com.izv.restaurante.pojo.Carta;
	import android.content.ContentValues;
	import android.content.Context;
	import android.database.Cursor;
	import android.database.sqlite.SQLiteDatabase;

public class GestionCarta {

	private Ayudante abd;
	private SQLiteDatabase bd;
	
	//Cerrar la DB
	public void close() {
		abd.close();
	}
	
	//Borrar Objeto de la DB
	//Se pasa el Objeto
	public int delete(Carta obj) {
		return delete(obj.getId());
	}

	//Borrar Objeto de la DB
	//Se Pasa el ID
	public int delete(long id) {
		String condicion = Contrato.Carta._ID + " = ?";
		String[] argumentos = { id + "" };
		int cuenta = bd.delete(Contrato.Carta.TABLA, condicion, argumentos);
		return cuenta;
	}
	
	//Borrar Objeto de la DB
	//Se Pasa el Nombre
	public int delete(String nombre){
		String condicion = Contrato.Carta.NOMBRE + " = ?";
		String[] argumentos = { nombre };
		int cuenta = bd.delete(Contrato.Carta.TABLA, condicion, argumentos);
		return cuenta;
	}

	//Constructor del Objeto GestionCarta
	public GestionCarta(Context c) {
		abd = new Ayudante(c);
	}
	
	//Devuelve un Cursor con todas las Rows
	public Cursor getCursor(){
		return getCursor(null, null);
	}
	
	//Devuelve un Cursor con las Rows que Cumplan la Condicion
	public Cursor getCursor(String condicion, String [] parametrosPrepared){
		Cursor cursor = bd.query(Contrato.Carta.TABLA, null, condicion, parametrosPrepared, 
				null, null, Contrato.Carta.NOMBRE);
		return cursor;
	}
	
	//Devuelve el Numero de Rows de la DB
	public int getNumRow(){
		Cursor c=getCursor();
		return c.getCount();
	}
	
	//Devuelve un Objeto del Cursor
	public Carta getRow(Cursor c) {
		try{
			Carta obj = new Carta();
			obj.setId(c.getInt(0));
			obj.setNombre(c.getString(1));
			obj.setPrecio(c.getInt(2));
			return obj;
		} catch(Exception e){
			return null;
		}
	}

	//Devuelve el Objeto que Tenga la ID que se le Pasa
	public Carta getRow(Long id) {
		String[] proyeccion=null;
		String where = Contrato.Carta._ID + " = ?";
		String[] parametros = new String[] { id+"" };
		String groupby = null; 
		String having = null;
		String orderby = Contrato.Carta.NOMBRE + " ASC";
		Cursor c = bd.query(Contrato.Carta.TABLA, proyeccion, where, parametros, groupby, 
				having, orderby);
		c.moveToFirst();
		Carta obj = getRow(c);
		c.close();
		return obj;
	}
	
	//Devuelve el Objeto que Tenga la ID que se le Pasa
		public Carta getRow(int id) {
			String[] proyeccion=null;
			String where = Contrato.Carta._ID + " = ?";
			String[] parametros = new String[] { id+"" };
			String groupby = null; 
			String having = null;
			String orderby = Contrato.Carta.NOMBRE + " ASC";
			Cursor c = bd.query(Contrato.Carta.TABLA, proyeccion, where, parametros, groupby, 
					having, orderby);
			c.moveToFirst();
			Carta obj = getRow(c);
			c.close();
			return obj;
		}

	//Devuelve el Objeto que Tenga de Nombre el que se le Pasa
	public Carta getRow(String nombre) {
		String[] parametros = new String[] { nombre };
		Cursor c = bd.rawQuery("SELECT * FROM " + Contrato.Carta.TABLA
			+ " WHERE " + Contrato.Carta.NOMBRE+ " = ?", parametros);
		c.moveToFirst();
		Carta obj = getRow(c);
		c.close();
		return obj;
	}

	//Inserta un Objeto en la DB
	public long insert(Carta obj) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.Carta.NOMBRE, obj.getNombre());
		valores.put(Contrato.Carta.PRECIO, obj.getPrecio());
		long id = bd.insert(Contrato.Carta.TABLA, null, valores);
		return id;
	}

	//Abre la DB
	//Permite Leer y Escribir
	public void open() {
		bd = abd.getWritableDatabase();
	}
	
	//Abre la DB
	//Permite Leer
	public void openRead() {
		bd = abd.getReadableDatabase();
	}

	//Permite Hacer un Update del Objeto que se le Pasa
	public int update(Carta obj) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.Carta.NOMBRE, obj.getNombre());
		valores.put(Contrato.Carta.PRECIO, obj.getPrecio());
		String condicion = Contrato.Carta._ID + " = ?";
		String[] argumentos = { obj.getId() + "" };
		int cuenta = bd.update(Contrato.Carta.TABLA, valores, condicion, argumentos);
		return cuenta;
	}	
	
}