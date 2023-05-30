package com.izv.restaurante.db;

	import android.provider.BaseColumns;

public class Contrato {

	private Contrato (){
	}
	
	//Tabla que guardara las mesas
	public static abstract class Mesa implements BaseColumns{
		public static final String TABLA = "Mesa";
		public static final String NOMBRE = "Nombre";
	}
	
	//Tabla que guardara los elementos de la carta del restaurante
	public static abstract class Carta implements BaseColumns{
		public static final String TABLA = "Carta";
		public static final String NOMBRE = "Nombre";
		public static final String PRECIO= "Precio";
	}
	
	//Tabla que guardara la informacion de los pedidos
	//Se relaciona con la Mesa
	public static abstract class Pedido implements BaseColumns{
		public static final String TABLA = "Pedido";
		public static final String FECHAHORA = "FechaHora";
		public static final String MESA = "Mesa";
		public static final String CERRADO = "Cerrado";
	}
	
	//Tabla con los detalles de los pedidos
	//Se relaciona con el Pedido y la Carta
	public static abstract class DetallePedido implements BaseColumns{
		public static final String TABLA = "DetallePedido";
		public static final String PEDIDO = "Pedido";
		public static final String CARTA = "Carta";
		public static final String CANTIDAD = "Cantidad";
		public static final String PRECIO = "Precio";
	}
	
}