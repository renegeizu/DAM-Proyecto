package com.izv.reproductormusica;

	import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Adaptador extends CursorAdapter{

	public Adaptador(Context co, Cursor cu) {
		super(co, cu, true);
	}

	@Override
	public void bindView(View v, Context co, Cursor cu) {
		String cancion = cu.getString(cu.getColumnIndex(MediaColumns.TITLE));
		TextView tv=(TextView) v.findViewById(R.id.tvName);
		tv.setText(cancion);
	}

	@Override
	public View newView(Context co, Cursor cu, ViewGroup vg) {
		LayoutInflater i = LayoutInflater.from(vg.getContext());
		View v = i.inflate(R.layout.detalle, vg, false);
		return v;
	}
	
}