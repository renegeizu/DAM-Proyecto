package com.izv.restaurante.db;

	import com.izv.restaurante.pojo.Pedido;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GestionPedido {

	private Ayudante abd;
	private SQLiteDatabase bd;
	
	//Cerrar la DB
	public void close() {
		abd.close();
	}
	
	//Borrar Objeto de la DB
	//Se pasa el Objeto
	public int delete(Pedido pdd) {
		return delete(pdd.getId());
	}

	//Borrar Objeto de la DB
	//Se Pasa el ID
	public int delete(long id) {
		String condicion = Contrato.Pedido._ID + " = ?";
		String[] argumentos = { id + "" };
		int cuenta = bd.delete(Contrato.Pedido.TABLA, condicion, argumentos);
		return cuenta;
	}

	//Constructor del Objeto GestionPedido
	public GestionPedido(Context c) {
		abd = new Ayudante(c);
	}
	
	//Devuelve un Cursor con todas las Rows
	public Cursor getCursor(){
		return getCursor(null, null);
	}
	
	//Devuelve un Cursor con las Rows que Cumplan la Condicion
	public Cursor getCursor(String condicion, String [] parametrosPrepared){
		Cursor cursor = bd.query(Contrato.Pedido.TABLA, null, condicion, parametrosPrepared, 
				null, null, Contrato.Pedido.FECHAHORA);
		return cursor;
	}
	
	//Devuelve el Numero de Rows de la DB
	public int getNumRow(){
		Cursor c=getCursor();
		return c.getCount();
	}
	
	//Devuelve un Cursor con los Pedidos Abiertos para la Mesa que le Pasamos
	public Cursor getPedido(String[] id){
		String consulta="SELECT p.* FROM "+Contrato.Pedido.TABLA+" p WHERE p.Cerrado=0 AND p.Mesa = ? ";
		Cursor c=bd.rawQuery(consulta, id);
		c.moveToFirst();
		return c;
	}
	
	//Devuelve un Cursor con los Pedidos Cerrados para la Mesa que le Pasamos
		public Cursor getPedidos(String[] id){
			String consulta="SELECT p.* FROM "+Contrato.Pedido.TABLA+" p WHERE p.Cerrado=1 AND p.Mesa = ? ";
			Cursor c=bd.rawQuery(consulta, id);
			c.moveToFirst();
			return c;
		}
	
	//Devuelve un Objeto del Cursor
	public Pedido getRow(Cursor c) {
		try{
			Pedido pdd = new Pedido();
			pdd.setId(c.getInt(0));
			pdd.setFechaHora(c.getString(1));
			pdd.setMesa(c.getInt(2));
			pdd.setCerrado(c.getInt(3));
			return pdd;
		} catch(Exception e){
			return null;
		}
	}

	//Devuelve el Objeto que Tenga la ID que se le Pasa
	public Pedido getRow(Long id) {
		String[] proyeccion=null;
		String where = Contrato.Pedido._ID + " = ?";
		String[] parametros = new String[] { id+"" };
		String groupby = null; 
		String having = null;
		String orderby = Contrato.Pedido.FECHAHORA + " ASC";
		Cursor c = bd.query(Contrato.Pedido.TABLA, proyeccion, where, parametros, groupby, 
				having, orderby);
		c.moveToFirst();
		Pedido pdd = getRow(c);
		c.close();
		return pdd;
	}

	//Inserta un Objeto en la DB
	public long insert(Pedido pdd) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.Pedido.FECHAHORA, pdd.getFechaHora().toString());
		valores.put(Contrato.Pedido.MESA+"", pdd.getMesa());
		valores.put(Contrato.Pedido.CERRADO+"", pdd.getCerrado());
		long id = bd.insert(Contrato.Pedido.TABLA, null, valores);
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
	public int update(Pedido pdd) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.Pedido.FECHAHORA, pdd.getFechaHora().toString());
		valores.put(Contrato.Pedido.MESA+"", pdd.getMesa());
		valores.put(Contrato.Pedido.CERRADO+"", pdd.getCerrado());
		String condicion = Contrato.Pedido._ID + " = ?";
		String[] argumentos = { pdd.getId() + "" };
		int cuenta = bd.update(Contrato.Pedido.TABLA, valores, condicion, argumentos);
		return cuenta;
	}
	
	//Permite Hacer un Update del Objeto para Cerrarlo o Abrirlo Unicamente
	public int updateCerrado(Pedido pdd) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.Pedido.CERRADO+"", pdd.getCerrado());
		String condicion = Contrato.Pedido._ID + " = ?";
		String[] argumentos = { pdd.getId() + "" };
		int cuenta = bd.update(Contrato.Pedido.TABLA, valores, condicion, argumentos);
		return cuenta;
	}
	
}