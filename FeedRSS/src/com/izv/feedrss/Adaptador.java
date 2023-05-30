package com.izv.feedrss;

	import java.util.ArrayList;
	import android.content.Context;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.ArrayAdapter;
	import android.widget.TextView;

public class Adaptador extends ArrayAdapter<ItemRss>{
	private Context contexto;
	private ArrayList<ItemRss> lista;

	public Adaptador(Context contexto, ArrayList<ItemRss> elementos){
		super(contexto, R.layout.detalle_lv, elementos);
		this.contexto=contexto;
		this.lista=elementos;
	}

	@Override
	public View getView(int posicion, View vista, ViewGroup padre){
		if(vista==null){
			LayoutInflater i=(LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vista=i.inflate(R.layout.detalle_lv, null);
		}
		TextView tv=(TextView) vista.findViewById(R.id.tvDetalle);
		ItemRss n=lista.get(posicion);
		tv.setText(n.getTitulo().trim());
		return vista;
	}

}
