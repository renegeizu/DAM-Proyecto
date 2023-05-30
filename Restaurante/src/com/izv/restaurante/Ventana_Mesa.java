package com.izv.restaurante;

	import com.izv.restaurante.db.GestionMesa;
	import com.izv.restaurante.pojo.Mesa;
	import android.os.Bundle;
	import android.app.Activity;
	import android.app.AlertDialog;
	import android.app.Dialog;
	import android.app.DialogFragment;
	import android.app.FragmentManager;
	import android.content.DialogInterface;
	import android.content.Intent;
	import android.database.Cursor;
	import android.view.Menu;
	import android.view.MenuItem;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.Button;
	import android.widget.EditText;
	import android.widget.GridView;

public class Ventana_Mesa extends Activity {
	
	private GestionMesa gestorMesa;
	private AdaptadorMesa adapMesa;
	private long idBorrar=0;
	private long valor=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventana_mesa);
		gestorMesa = new GestionMesa(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ventana_mesa, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.action_insertar_mesa:
    			final Dialog d=new Dialog(this);
    			d.setContentView(R.layout.dialogo_mesa);
    			d.setTitle("Insertar Nueva Mesa");
    			final EditText et=(EditText) d.findViewById(R.id.etInsertarDialogoMesa);
    			Button bt=(Button)d.findViewById(R.id.btGuardarDialogoMesa);
    			bt.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					String insertar=et.getText().toString();
    					if(!insertar.equals("")){
	    					long r=gestorMesa.insert(new Mesa(insertar));
	    					if (r>0)
	    						refreshMesa();
	    					d.cancel();
    					}
    				}
    			});
    			d.show();
    			return true;
    		case R.id.action_administrar_carta:
    			Intent i = new Intent(this,Ventana_Carta.class);
    			startActivity(i);
    			break;
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onPause() {
		super.onPause();
		gestorMesa.close();
	}

	@Override
	public void onResume() {
		gestorMesa.open();
		super.onResume();
		Cursor c=gestorMesa.getCursor();
		if(gestorMesa.getNumRow()==0){
			for(int cont=1; cont<6; cont++){
				Mesa m=new Mesa("Mesa "+cont);
				gestorMesa.insert(m);
			}
		}
		adapMesa=new AdaptadorMesa(this, c);
		GridView gv=(GridView) findViewById(R.id.gvMesas);
		gv.setAdapter(adapMesa);
	}
	
	public int borrarMesa(int id) {
		Dialogo d=new Dialogo();
		idBorrar=id;
		FragmentManager fm = this.getFragmentManager();
		d.show(fm, "Dialogo");
		return 0;
	}
	
	public void borrarMesaConfirmado(){
		int r= gestorMesa.delete(idBorrar);
		if(r>0){
			Cursor c=gestorMesa.getCursor();
			adapMesa.changeCursor(c);
		}
	}
	
	public static class Dialogo extends DialogFragment{
		
		private int id;
		
		public void setId(int id){
			this.id=id;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			AlertDialog.Builder confirmacion = new AlertDialog.Builder(getActivity());
			confirmacion.setTitle("Importante");  
			confirmacion.setMessage("¿Esta seguro de que desea borrar?");            
			confirmacion.setCancelable(false);
			confirmacion.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface confirmacion, int id) {
	            	Ventana_Mesa p=(Ventana_Mesa) getActivity();
	            	p.borrarMesaConfirmado();
	            }  
	        }); 
			confirmacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface confirmacion, int id) {
	            	dismiss();
	            }  
	        });
			return confirmacion.create();
		}
	}
	
	public void editarMesa(final Mesa m){
		final Dialog d=new Dialog(this);
		d.setContentView(R.layout.dialogo_mesa);
		d.setTitle("Editar Mesa");
		final EditText et=(EditText) d.findViewById(R.id.etInsertarDialogoMesa);
		et.setText(m.getNombre());
		Button bt=(Button)d.findViewById(R.id.btGuardarDialogoMesa);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nuevoNombre=et.getText().toString();
				int r=gestorMesa.update(new Mesa(m.getId(), nuevoNombre));
				if (r>0)
					refreshMesa();
				d.cancel();
			}
		});
		d.show();
	}

	public void refreshMesa(){
		adapMesa.changeCursor(gestorMesa.getCursor());
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savingInstanceState) {
		super.onSaveInstanceState(savingInstanceState);
		savingInstanceState.putLong("idBorrar", idBorrar);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		valor=savedInstanceState.getLong("idBorrar");
	}

}