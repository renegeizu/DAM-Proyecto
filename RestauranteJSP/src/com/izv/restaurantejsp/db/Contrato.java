package com.izv.restaurantejsp.db;

	import android.provider.BaseColumns;

public class Contrato {

	private Contrato (){
	}
	
	//Tabla que guardara las mesas
	public static abstract class Mesa implements BaseColumns{
		public static final String TABLA = "Mesa";
		public static final String IDMESA = "idMesa";
		public static final String NOMBRE = "Nombre";
	}
	
	//Tabla que guardara los elementos de la carta del restaurante
	public static abstract class Carta implements BaseColumns{
		public static final String TABLA = "Carta";
		public static final String IDCARTA = "idCarta";
		public static final String NOMBRE = "Nombre";
		public static final String PRECIO= "Precio";
	}
	
}