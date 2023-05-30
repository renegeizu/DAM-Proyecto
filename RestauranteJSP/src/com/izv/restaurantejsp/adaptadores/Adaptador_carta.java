package com.izv.restaurantejsp.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.izv.restaurantejsp.R;

public class Adaptador_carta extends CursorAdapter{

	public Adaptador_carta (Context co, Cursor cu) {
		super(co, cu, true);
	}
	
	@Override
	public void bindView(View v, Context co, Cursor cu) {
		TextView tvNombre=(TextView) v.findViewById(R.id.tvPlato);
		TextView tvPrecio=(TextView) v.findViewById(R.id.tvPrecio);
		tvNombre.setText(cu.getString(2));
		tvPrecio.setText(cu.getFloat(3)+"");
	}

	@Override
	public View newView(Context co, Cursor cu, ViewGroup vg) {
		LayoutInflater i = LayoutInflater.from(vg.getContext());
		View v = i.inflate(R.layout.detalle_carta, vg, false);
		return v;
	}

}
