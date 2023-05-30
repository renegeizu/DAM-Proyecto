package com.izv.painting;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Principal extends Activity {

	private Vista lienzo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lienzo=new Vista(this);
		setContentView(R.layout.activity_principal);
		/*
		 * Declara el Lienzo y Agregalo
		 * LinearLayout lienzo=(LinearLayout) findViewById(R.id.canvas);
		 * lienzo.addView(apaint);
		 * Declara todos los botones
		 * Para agregarles el metodo correspondiente usa:
		 * lienzo.guardar(); -- Guarda el dibujo actual con nombre generado
		 * lienzo.setColor(); -- Cambia el color, hazlo con el colorpicker
		 * lienzo.Gomar(); -- Pinta de color blanco, como una goma
		 * El color picker necesita el oyente y
		 * ColorPickerDialog dialogo;
		 * dialogo=new ColorPickerDialog(this, new OyenteDialogo(), apaint.getColor());
		 * dialogo.show();
		 */
		
		
	}

}