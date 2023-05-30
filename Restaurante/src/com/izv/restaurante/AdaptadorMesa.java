package com.izv.restaurante;

	import com.izv.restaurante.pojo.Mesa;
	import android.content.Context;
	import android.content.Intent;
	import android.database.Cursor;
	import android.os.Bundle;
	import android.view.LayoutInflater;
	import android.view.MenuInflater;
	import android.view.MenuItem;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.view.View.OnLongClickListener;
	import android.view.ViewGroup;
	import android.widget.Button;
	import android.widget.CursorAdapter;
	import android.widget.PopupMenu;
	import android.widget.PopupMenu.OnMenuItemClickListener;

public class AdaptadorMesa extends CursorAdapter {
	
	private Context ctx;
	
	public AdaptadorMesa (Context co, Cursor cu) {
		super(co, cu, true);
		ctx=co;
	}

	@Override
	public void bindView(View v, Context co, Cursor cu) {
		Button bt=(Button) v.findViewById(R.id.btDetalleMesa);
		bt.setText(cu.getString(1));
		String nombre=cu.getString(1);
		int id=cu.getInt(0);
		final Mesa m=new Mesa(id, nombre);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ctx,Ventana_Pedido.class);
				Bundle b = new Bundle();
				b.putSerializable("Mesa", m);
				i.putExtras(b);
				ctx.startActivity(i);
			}
		});
		bt.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				PopupMenu popup = new PopupMenu (ctx, v);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.menu_contextual_mesa, popup.getMenu());
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch(item.getItemId()){
							case R.id.action_editar_mesa:
						        ((Ventana_Mesa)ctx).editarMesa(m);								
								break;
							case R.id.action_borrar_mesa:
						        ((Ventana_Mesa)ctx).borrarMesa(m.getId());
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
		View v = i.inflate(R.layout.detalle_mesa, vg, false);
		return v;
	}

}