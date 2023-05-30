package com.izv.restaurantejsp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.izv.restaurantejsp.adaptadores.Adaptador_carta;
import com.izv.restaurantejsp.db.GestionCarta;
import com.izv.restaurantejsp.pojo.Carta;
import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


public class VentanaCarta extends Activity {
	
	private GestionCarta gestorCarta;
	private Adaptador_carta adapCarta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_carta);
		gestorCarta = new GestionCarta(this);
		Button bt=(Button) findViewById(R.id.buttonAct);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCarta hj=new getCarta();
		    	hj.execute("http://192.168.208.167:8084/aadcomanda/controlador?op=vistacartajson");
			}
		});
	}

	
	private class getCarta extends AsyncTask<String, Integer, String>{
		
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
			gestorCarta.open();
			gestorCarta.delete();
			JSONTokener tokener = new JSONTokener(result);
			try{
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("cartas");
				for (int i = 0; i < lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					Carta carta=new Carta(fila);
					gestorCarta.insert(carta);
				}
			}catch(Exception e){}
			Toast.makeText(getApplicationContext(), "Has recibido las actualizaciones de la carta", 
					Toast.LENGTH_LONG).show();
			adapCarta.changeCursor(gestorCarta.getCursor());
			gestorCarta.close();
		}
    	
    }
	
	@Override
	public void onResume() {
		gestorCarta.open();
		super.onResume();
		Cursor c=gestorCarta.getCursor();
		adapCarta=new Adaptador_carta(this, c);
		GridView gv=(GridView) findViewById(R.id.gvCarta);
		gv.setAdapter(adapCarta);
		gestorCarta.close();
	}

}
