package com.izv.androiddraw;

import java.util.Calendar;

import com.izv.androiddraw.ColorPickerDialog.OnColorChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class Principal extends Activity {
	
	Vista vista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vista=new Vista(this);
		setContentView(R.layout.activity_principal);
		LinearLayout ll = (LinearLayout) findViewById(R.id.Lienzo);
		ll.addView(vista);
	}
	
	public void guardar (View v) {
		vista.guardar(generarNombre());
	}
	
	public void borrar (View v) {
		vista.setColor(Color.WHITE);
	}
	
	public void color(View v){
		ColorPickerDialog dialogo;
		dialogo=new ColorPickerDialog(this, new OyenteDialogo(), vista.getColor());
		dialogo.show();
	}
	
	private class OyenteDialogo implements OnColorChangedListener{

		@Override
		public void colorChanged(int color) {
			vista.setColor(color);			
		}
		
	}
	
	public void taponCuadrado(View v){
		vista.setTaponCuadrado();
	}
	
	public void taponCirculo(View v){
		vista.setTaponCirculo();
	}
	
	public void setPincel(View v){
		vista.setPincel();
	}
	
	public String generarNombre(){
		Calendar cal = Calendar.getInstance();	
		String hora, minutos, agno, mes, dia;
		if(cal.get(Calendar.HOUR_OF_DAY)<10)
			hora="0"+cal.get(Calendar.HOUR_OF_DAY);
		else
			hora=""+cal.get(Calendar.HOUR_OF_DAY);
		if(cal.get(Calendar.MINUTE)<10)
			minutos="0"+cal.get(Calendar.MINUTE);
		else
			minutos=""+cal.get(Calendar.MINUTE);
		agno=""+cal.get(Calendar.YEAR);
		if(cal.get(Calendar.MONTH)+1<10)
			mes="0"+cal.get(Calendar.MONTH)+1;
		else
			mes=""+cal.get(Calendar.MONTH)+1;
		if(cal.get(Calendar.DAY_OF_MONTH)<10)
			dia="0"+cal.get(Calendar.DAY_OF_MONTH);
		else
			dia=""+cal.get(Calendar.DAY_OF_MONTH);
		return "IMG_"+agno+mes+dia+hora+minutos;
	}

}