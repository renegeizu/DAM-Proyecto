package com.izv.restaurantejsp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.izv.restaurantejsp.adaptadores.Adaptador_mesa;
import com.izv.restaurantejsp.db.GestionMesa;
import com.izv.restaurantejsp.pojo.Mesa;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class VentanaMesa extends Activity {

	private GestionMesa gestorMesa;
	private Adaptador_mesa adapMesa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_mesa);
		gestorMesa = new GestionMesa(this);
		Button bt=(Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getMesas hj=new getMesas();
		    	hj.execute("http://192.168.208.167:8084/aadcomanda/controlador?op=vistamesajson");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ventana_mesa, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_administrar_carta:
			Intent i = new Intent(this, VentanaCarta.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class getMesas extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			String linea, todo="";
			try{
				URL url=new URL(params[0]);
				BufferedReader in=new BufferedReader(new InputStreamReader(url.openStream()));
				while((linea=in.readLine())!=null){
					todo+=linea;
				}
				in.close();
			}catch(Exception e){}
			return todo;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			gestorMesa.open();
			gestorMesa.delete();
			JSONTokener tokener = new JSONTokener(result);
			try{
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("mesas");
				for (int i = 0; i < lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					Mesa mesa=new Mesa(fila);
					gestorMesa.insert(mesa);
				}
			}catch(Exception e){}
			Toast.makeText(getApplicationContext(), "Has recibido las ultimas mesas", 
					Toast.LENGTH_LONG).show();
			adapMesa.changeCursor(gestorMesa.getCursor());
			gestorMesa.close();
		}
    	
    }
	
	@Override
	public void onResume() {
		gestorMesa.open();
		super.onResume();
		Cursor c=gestorMesa.getCursor();
		adapMesa=new Adaptador_mesa(this, c);
		ListView gv=(ListView) findViewById(R.id.listView1);
		gv.setAdapter(adapMesa);
		gestorMesa.close();
	}

}
