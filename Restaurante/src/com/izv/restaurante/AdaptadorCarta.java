package com.izv.restaurante;

	import com.izv.restaurante.pojo.Carta;
	import android.content.Context;
	import android.database.Cursor;
	import android.util.Log;
	import android.view.LayoutInflater;
	import android.view.MenuInflater;
	import android.view.MenuItem;
	import android.view.View;
	import android.view.ViewGroup;
	import android.view.View.OnLongClickListener;
	import android.widget.CursorAdapter;
	import android.widget.LinearLayout;
	import android.widget.PopupMenu;
	import android.widget.PopupMenu.OnMenuItemClickListener;
	import android.widget.TextView;

public class AdaptadorCarta extends CursorAdapter {

	private Context ctx;
	
	public AdaptadorCarta (Context co, Cursor cu) {
		super(co, cu, true);
		ctx=co;
	}
	
	@Override
	public void bindView(View v, Context co, Cursor cu) {
		TextView tvNombre=(TextView) v.findViewById(R.id.tvElementoDetalleCarta);
		TextView tvPrecio=(TextView) v.findViewById(R.id.tvPrecioDetalleCarta);
		tvNombre.setText(cu.getString(1));
		tvPrecio.setText(cu.getFloat(2)+"");
		final Carta cta=new Carta(cu.getInt(0), cu.getString(1), cu.getFloat(2));
		LinearLayout cc=(LinearLayout) v.findViewById(R.id.LinearLayout2);
		cc.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				PopupMenu popup = new PopupMenu (ctx, v);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.menu_contextual_carta, popup.getMenu());
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch(item.getItemId()){
							case R.id.action_editar_carta:
								Log.v("Editar Carta", cta.toString());
						        ((Ventana_Carta)ctx).editarCarta(cta);
								break;
							case R.id.action_borrar_carta:
						        ((Ventana_Carta)ctx).borrarCarta(cta.getId());
								break;
						}
						return true;
					}
				});
				popup.show();
				return true;
			}
		});
	}

	@Override
	public View newView(Context co, Cursor cu, ViewGroup vg) {
		LayoutInflater i = LayoutInflater.from(vg.getContext());
		View v = i.inflate(R.layout.detalle_carta, vg, false);
		return v;
	}
	
}