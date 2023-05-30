package com.izv.restaurante;

	import com.izv.restaurante.db.GestionCarta;
import com.izv.restaurante.db.GestionDetallePedido;
import com.izv.restaurante.pojo.Carta;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdaptadorPedido extends CursorAdapter {
	
	private Context c;
	GestionDetallePedido gestionDetalle;
	GestionCarta gestorCarta;
	Carta crt=new Carta();
	
	public AdaptadorPedido(Context contexto, Cursor c){
		super(contexto, c, true);
		this.c=contexto;
	}

	@Override
	public void bindView(View v, Context contexto, final Cursor c) {
		try{
		gestionDetalle=new GestionDetallePedido(contexto);
		gestionDetalle.open();
		gestorCarta=new GestionCarta(contexto);
		gestorCarta.open();
		TextView nombre=(TextView) v.findViewById(R.id.tvNombreDetallePedido);
		final TextView cantidad=(TextView) v.findViewById(R.id.tvCantidadDetallePedido);
		ImageButton sumar=(ImageButton) v.findViewById(R.id.ibSumarDetallePedido);
		ImageButton restar=(ImageButton) v.findViewById(R.id.ibRestarDetallePedido);
		crt=gestorCarta.getRow(c.getInt(2));
		nombre.setText(crt.getNombre());
		final int idDetalle=c.getInt(0);
		Log.v("Cantidad", c.getInt(3)+"");
		cantidad.setText(c.getInt(3)+"");
		sumar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int cant=Integer.parseInt(cantidad.getText().toString());
				cant++;
				cantidad.setText(cant+"");
				gestionDetalle.update(idDetalle, cant);
			}
		});
		restar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int cant=Integer.parseInt(cantidad.getText().toString());
				if(cant==0){
					cantidad.setText(cant+"");
				}else{
					cant--;
					cantidad.setText(cant+"");
					gestionDetalle.update(idDetalle, cant);
				}
			}
		});
		}catch(Exception e){}
	}

	@Override
	public View newView(Context co, Cursor cu, ViewGroup vg) {
		LayoutInflater i=LayoutInflater.from(vg.getContext());
		View v = i.inflate(R.layout.detalle_pedido, vg, false);
		return v;
	}

}