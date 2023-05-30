package com.izv.restaurante.db;

	import com.izv.restaurante.pojo.DetallePedido;
	import android.content.ContentValues;
	import android.content.Context;
	import android.database.Cursor;
	import android.database.sqlite.SQLiteDatabase;

public class GestionDetallePedido {

	private Ayudante abd;
	private SQLiteDatabase bd;
	
	//Cerrar la DB
	public void close() {
		abd.close();
	}
	
	//Borrar Objeto de la DB
	//Se pasa el Objeto
	public int delete(DetallePedido pdd) {
		return delete(pdd.getId());
	}

	//Borrar Objeto de la DB
	//Se Pasa el ID
	public int delete(long id) {
		String condicion = Contrato.DetallePedido._ID + " = ?";
		String[] argumentos = { id + "" };
		int cuenta = bd.delete(Contrato.DetallePedido.TABLA, condicion, argumentos);
		return cuenta;
	}

	//Constructor del Objeto GestionDetallePedido
	public GestionDetallePedido(Context c) {
		abd = new Ayudante(c);
	}
	
	//Devuelve un Cursor con todas las Rows
	public Cursor getCursor(){
		return getCursor(null, null);
	}
	
	//Devuelve un Cursor con las Rows que Cumplan la Condicion
	public Cursor getCursor(String condicion, String [] parametrosPrepared){
		Cursor cursor = bd.query(Contrato.DetallePedido.TABLA, null, condicion, parametrosPrepared, null, null, 
				Contrato.DetallePedido.PEDIDO);
		return cursor;
	}
	
	//Devuelve un Cursor con las Rows de Todos los Elementos de la Carta
	//y la Cantidad de Ellos Que se ha Pedido y Para Que Mesa
	public Cursor getDetalle(String [] args){
		String consulta="SELECT sub._ID, c._ID, c.Precio, c.Nombre, sub.Cantidad FROM "
				+Contrato.Carta.TABLA+" c LEFT JOIN "+"(SELECT dp.* FROM "
				+Contrato.DetallePedido.TABLA+" dp JOIN "+Contrato.Pedido.TABLA+" p ON"
				+" dp.Pedido=p._ID WHERE p.Cerrado=0 AND p.Mesa = ? ) sub ON c._ID=sub.Carta";
		Cursor c=bd.rawQuery(consulta, args);
		c.moveToFirst();
		return c;
	}
	
	//Devuelve un Cursor para un Pedido Concreto
	public Cursor getDetalleTotal(String [] args){
		String consulta="SELECT dp.* FROM "
				+Contrato.DetallePedido.TABLA+" dp WHERE dp.Pedido = ? ";
		Cursor c=bd.rawQuery(consulta, args);
		c.moveToFirst();
		return c;
	}
	
	//Devuelve el Numero de Rows de la DB
	public int getNumRow(){
		Cursor c=getCursor();
		return c.getCount();
	}
	
	//Devuelve un Objeto del Cursor
	public DetallePedido getRow(Cursor c){
		try{
			DetallePedido pdd = new DetallePedido();
			pdd.setId(c.getInt(0));
			pdd.setPedido(c.getInt(1));
			pdd.setCarta(c.getInt(2));
			pdd.setCantidad(c.getInt(3));
			pdd.setPrecio(c.getInt(4));
			return pdd;
		} catch(Exception e){
			return null;
		}
	}

	//Devuelve el Objeto que Tenga la ID que se le Pasa
	public DetallePedido getRow(Long id) {
		String[] proyeccion=null;
		String where = Contrato.DetallePedido._ID + " = ?";
		String[] parametros = new String[] { id+"" };
		String groupby = null; 
		String having = null;
		String orderby = Contrato.DetallePedido._ID + " DESC";
		Cursor c = bd.query(Contrato.DetallePedido.TABLA, proyeccion, where, parametros, 
				groupby , having, orderby);
		c.moveToFirst();
		DetallePedido pdd = getRow(c);
		c.close();
		return pdd;
	}
	
	//Inserta un Objeto en la DB
	public int insert(DetallePedido detalle) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.DetallePedido.PEDIDO, detalle.getPedido());
		valores.put(Contrato.DetallePedido.CARTA, detalle.getCarta());
		valores.put(Contrato.DetallePedido.CANTIDAD, detalle.getCantidad());
		valores.put(Contrato.DetallePedido.PRECIO, detalle.getPrecio());
		int id=(int) bd.insert(Contrato.DetallePedido.TABLA, null, valores);
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
	public int update(DetallePedido pdd) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.DetallePedido.PEDIDO, pdd.getPedido());
		valores.put(Contrato.DetallePedido.CARTA, pdd.getCarta());
		valores.put(Contrato.DetallePedido.CANTIDAD, pdd.getCantidad());
		valores.put(Contrato.DetallePedido.PRECIO, pdd.getPedido());
		String condicion = Contrato.DetallePedido._ID + " = ?";
		String[] argumentos = { pdd.getId() + "" };
		int cuenta = bd.update(Contrato.DetallePedido.TABLA, valores, condicion, argumentos);
		return cuenta;
	}
	
	//Permite Hacer un Update del Objeto Pasandole el ID y la Cantidad
	public int update(int id, int cantidad) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.DetallePedido.CANTIDAD,cantidad);
		String condicion = Contrato.DetallePedido._ID + " = ?";
		String[] argumentos = { id + "" };
		int cuenta = bd.update(Contrato.DetallePedido.TABLA, valores, condicion, argumentos);
		return cuenta;
	}
	
	//Permite Hacer un Update del Objeto Pasandola el ID, el ID de la Carta y la Cantidad
	public int update(long id, long articulo, long cantidad) {
		ContentValues valores = new ContentValues();
		valores.put(Contrato.DetallePedido.CARTA, articulo);
		valores.put(Contrato.DetallePedido.CANTIDAD, cantidad);
		String condicion = Contrato.DetallePedido._ID + " = ?";
		String[] argumentos = { id + "" };
		int cuenta = bd.update(Contrato.DetallePedido.TABLA, valores, condicion, argumentos);
		return cuenta;
	}
	
}