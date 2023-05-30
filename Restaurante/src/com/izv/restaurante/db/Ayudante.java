package com.izv.restaurante.db;

	import android.content.Context;
	import android.database.sqlite.SQLiteDatabase;
	import android.database.sqlite.SQLiteOpenHelper;
	import android.util.Log;

public class Ayudante extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "Comanda.db";
	public static final int DATABASE_VERSION = 1;
	
	public Ayudante(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//Se crea la Tabla Mesa
		String sql="CREATE TABLE IF NOT EXISTS "+Contrato.Mesa.TABLA+" ("
			               +Contrato.Mesa._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			               +Contrato.Mesa.NOMBRE+" VARCHAR(20) NOT NULL UNIQUE)";
		Log.v("SQL Mesa", sql);
		db.execSQL(sql);
		//Se crea la Tabla Carta
		sql="CREATE TABLE IF NOT EXISTS "+Contrato.Carta.TABLA+" ("
	               +Contrato.Carta._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
	               +Contrato.Carta.NOMBRE+" VARCHAR(80) NOT NULL UNIQUE, "
	               +Contrato.Carta.PRECIO+" DECIMAL(6,2) NOT NULL)";
		Log.v("SQL Carta", sql);
		db.execSQL(sql);
		//Se crea la tabla Pedido
		sql="CREATE TABLE IF NOT EXISTS "+Contrato.Pedido.TABLA+" ("
	               +Contrato.Pedido._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
	               +Contrato.Pedido.FECHAHORA+" TEXT NOT NULL, "
	               +Contrato.Pedido.MESA+" INTEGER NOT NULL, "
	               +Contrato.Pedido.CERRADO+" INTEGER NOT NULL)";
		Log.v("SQL Pedido", sql);
		db.execSQL(sql);
		//Se crea la tabla DetallePedido
		sql="CREATE TABLE IF NOT EXISTS "+Contrato.DetallePedido.TABLA+" ("
	               +Contrato.DetallePedido._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
	               +Contrato.DetallePedido.PEDIDO+" INTEGER NOT NULL REFERENCES "
	               		+Contrato.Pedido.TABLA+"("+Contrato.Pedido._ID+"), "
	               +Contrato.DetallePedido.CARTA+" INTEGER NOT NULL REFERENCES "
	               		+Contrato.Carta.TABLA+"("+Contrato.Carta._ID+"), "
	               +Contrato.DetallePedido.CANTIDAD+" INTEGER NOT NULL, "
	               +Contrato.DetallePedido.PRECIO+" DECIMAL(6,2) NOT NULL, "
	               +"UNIQUE ("+Contrato.DetallePedido.PEDIDO+", "+Contrato.DetallePedido.CARTA+"))";
		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
		onCreate(db);
	}
	
}