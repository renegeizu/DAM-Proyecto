package com.izv.restaurante;

	import com.izv.restaurante.db.GestionCarta;
	import com.izv.restaurante.pojo.Carta;
	import android.os.Bundle;
	import android.app.Activity;
	import android.app.AlertDialog;
	import android.app.Dialog;
	import android.app.DialogFragment;
	import android.app.FragmentManager;
	import android.content.DialogInterface;
	import android.database.Cursor;
	import android.view.Menu;
	import android.view.MenuItem;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.Button;
	import android.widget.EditText;
	import android.widget.GridView;

public class Ventana_Carta extends Activity {
	
	private GestionCarta gestorCarta;
	private AdaptadorCarta adapCarta;
	private long idBorrar=0;
	private String valor=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventana_carta);
		gestorCarta = new GestionCarta(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ventana_carta, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.action_insertar_carta:
    			final Dialog d=new Dialog(this);
    			d.setContentView(R.layout.dialogo_carta);
    			d.setTitle("Insertar Nuevo Elemento");
    			final EditText et=(EditText) d.findViewById(R.id.etCartaDialogoCarta);
    			final EditText et1=(EditText) d.findViewById(R.id.etPrecioDialogoCarta);
    			Button bt=(Button)d.findViewById(R.id.btGuardarDialogoCarta);
    			bt.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					String nombre=et.getText().toString();
    					Float precio=Float.parseFloat(et1.getText().toString());
    					if(!nombre.equals("") && !(precio==0)){
	    					long r=gestorCarta.insert(new Carta(nombre, precio));
	    					if (r>0)
	    						refreshCarta();
	    					d.cancel();
    					}
    				}
    			});
    			d.show();
    			return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onPause() {
		super.onPause();
		gestorCarta.close();
	}

	@Override
	public void onResume() {
		gestorCarta.open();
		super.onResume();
		Cursor c=gestorCarta.getCursor();
		if(gestorCarta.getNumRow()==0){
			for(int cont=1; cont<6; cont++){
				Carta m=new Carta("Carta "+cont, cont);
				gestorCarta.insert(m);
			}
		}
		adapCarta=new AdaptadorCarta(this, gestorCarta.getCursor());
		GridView gv=(GridView) findViewById(R.id.gvCarta);
		gv.setAdapter(adapCarta);
	}
	
	public int borrarCarta(long id) {
		Dialogo d=new Dialogo();
		idBorrar=id;
		FragmentManager fm = this.getFragmentManager();
		d.show(fm, "Dialogo");
		return 0;
	}
	
	public void borrarCartaConfirmado(){
		int r= gestorCarta.delete(idBorrar);
		if(r>0){
			Cursor c=gestorCarta.getCursor();
			adapCarta.changeCursor(c);
		}
	}
	
	public static class Dialogo extends DialogFragment{
		
		private long id;
		
		public void setId(long id){
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
	            	Ventana_Carta p=(Ventana_Carta) getActivity();
	            	p.borrarCartaConfirmado();
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
	
	public void editarCarta(final Carta ctax){
		final Dialog d=new Dialog(this);
		d.setContentView(R.layout.dialogo_carta);
		d.setTitle("Editar Mesa");
		final EditText et=(EditText) d.findViewById(R.id.etCartaDialogoCarta);
		final EditText et1=(EditText) d.findViewById(R.id.etPrecioDialogoCarta);
		et.setText(ctax.getNombre()+"");
		et1.setText(ctax.getPrecio()+"");
		Button bt=(Button)d.findViewById(R.id.btGuardarDialogoCarta);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nuevoNombre=et.getText().toString();
				Float nuevoPrecio=Float.parseFloat(et1.getText().toString().trim());
				int r=gestorCarta.update(new Carta(ctax.getId(), nuevoNombre, nuevoPrecio));
				if (r>0)
					refreshCarta();
				d.cancel();
			}
		});
		d.show();
	}

	public void refreshCarta(){
		adapCarta.changeCursor(gestorCarta.getCursor());
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savingInstanceState) {
		super.onSaveInstanceState(savingInstanceState);
		savingInstanceState.putLong("idBorrar", idBorrar);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		valor=savedInstanceState.getString("idBorrar");
	}

}