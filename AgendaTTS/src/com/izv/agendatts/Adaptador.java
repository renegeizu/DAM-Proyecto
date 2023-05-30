package com.izv.agendatts;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adaptador extends ArrayAdapter<String>{
	private ArrayList<String> lista;
	Context contexto;
	
	public Adaptador (Context c, ArrayList<String> lista){
		super(c, R.layout.detalle_lv,lista);
		this.contexto = c;
		this.lista = lista;
	}
	
	@Override
	public View getView (int posicion, View vista, ViewGroup padre) {
		if (vista== null)
			{LayoutInflater li = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vista= li.inflate(R.layout.detalle_lv, null);
			}
	
		TextView tv = (TextView) vista.findViewById(R.id.textView1);
		tv.setText(lista.get(posicion));
		return vista;
	}
}
