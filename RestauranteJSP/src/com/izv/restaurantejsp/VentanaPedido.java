package com.izv.restaurantejsp;

	import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.izv.restaurantejsp.adaptadores.Adaptador_pedido;
import com.izv.restaurantejsp.pojo.Carta;
import com.izv.restaurantejsp.pojo.DetallePedido;
import com.izv.restaurantejsp.pojo.Mesa;
import com.izv.restaurantejsp.pojo.Pedido;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class VentanaPedido extends Activity {
	
	Mesa mesa=new Mesa();
	ArrayList<Pedido> pedidos=new ArrayList<Pedido>();
	ArrayList<Carta> cartas=new ArrayList<Carta>();
	private Adaptador_pedido adaptador;
	String idMesa="";
	private ListView lvVista;
	private ArrayList<DetallePedido> detalles=new ArrayList<DetallePedido>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_pedido);
		lvVista=(ListView) findViewById(R.id.lvPedido);
		Bundle b=getIntent().getExtras();
		Mesa mesa=(Mesa) b.getSerializable("Mesa");
		getPedidosAbiertos hj=new getPedidosAbiertos();
    	hj.execute("http://192.168.208.167:8084/aadcomanda/controlador?" +
    			"op=vistapedidojson&id="+mesa.getId()+"&cerrado=0");
    	idMesa=mesa.getId()+"";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ventana_pedido, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_abrir:
			new setBorrarPedido().execute("http://192.168.208.167:8084/aadcomanda/controlador?"
					+"op=vistaborrarpedidojson&id="+pedidos.get(pedidos.size()-1).getId()
					+"&idMesa="+idMesa);
			Toast.makeText(this, "Pedido Recuperado", Toast.LENGTH_LONG).show();
			finish();
			return true;
		case R.id.action_cerrar:
			new setCerrarPedido().execute("http://192.168.208.167:8084/aadcomanda/controlador?"
					+"op=vistaeditarpedidojson&id="+pedidos.get(pedidos.size()-1).getId()
					+"&idMesa="+idMesa+"&cerrado=1");
			Toast.makeText(this, "Pedido Cerrado", Toast.LENGTH_LONG).show();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class getPedidosAbiertos extends AsyncTask<String, Integer, String>{
		
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
			}catch(Exception e){
			}
			return todo;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			JSONTokener tokener = new JSONTokener(result);
			try{
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("pedidos");
				for (int i = 0; i < lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					Pedido pedido=new Pedido(fila);
					pedidos.add(pedido);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			if(pedidos.isEmpty()==true){
				new setPedidoNuevo().execute("http://192.168.208.167:8084/aadcomanda/controlador?"
						+"op=vistainsertarpedidojson&idMesa="+idMesa+"&cerrado=0");
			}else{
				new getCarta().execute("http://192.168.208.167:8084/aadcomanda/" +
						"controlador?op=vistacartajson");
			}
		}
    	
    }

	private class setPedidoNuevo extends AsyncTask<String, Integer, String>{
		
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
			}catch(Exception e){
			}
			return todo;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			JSONTokener tokener = new JSONTokener(result);
			try{
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("pedidos");
				for (int i = 0; i < lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					Pedido pedido=new Pedido(fila);
					pedidos.add(pedido);
				}
			}catch(Exception e){
			}
			new getCarta().execute("http://192.168.208.167:8084/aadcomanda/" +
					"controlador?op=vistacartajson");
		}
		
	}
	
	private class setCerrarPedido extends AsyncTask<String, Integer, String>{
		
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
			}catch(Exception e){
			}
			return todo;
		}
		
	}
	
	private class setBorrarPedido extends AsyncTask<String, Integer, String>{
		
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
			}catch(Exception e){
			}
			return todo;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			JSONTokener tokener = new JSONTokener(result);
			try{
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("pedidos");
				for (int i = 0; i < lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					Pedido pedido=new Pedido(fila);
					pedidos.add(pedido);
				}
			}catch(Exception e){
			}
			new setCerrarPedido().execute("http://192.168.208.167:8084/aadcomanda/controlador?"
					+"op=vistaeditarpedidojson&id="+pedidos.get(pedidos.size()-1).getId()
					+"&idMesa="+idMesa+"&cerrado=0");
		}
		
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
			}catch(Exception e){
			}
			return todo;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			JSONTokener tokener = new JSONTokener(result);
			try{
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("cartas");
				for (int i = 0; i < lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					Carta carta=new Carta(fila);
					cartas.add(carta);
				}
			}catch(Exception e){
			}
			new getDetalles().execute("http://192.168.208.167:8084/aadcomanda/" +
					"controlador?op=vistadetallejson&idPedido="
					+pedidos.get(pedidos.size()-1).getId());
		}
	}
	
	private class getDetalles extends AsyncTask<String, Integer, String>{
		
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
			}catch(Exception e){
			}
			return todo;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			try{
				JSONTokener tokener = new JSONTokener(result);
				JSONObject raiz = new JSONObject(tokener);
				JSONArray lista=raiz.getJSONArray("detalles");
				for (int i=0; i<lista.length(); i++) {
					JSONObject fila = lista.getJSONObject(i);
					DetallePedido detalle=new DetallePedido(fila);
					detalles.add(detalle);
				}
			}catch(Exception e){
				Log.v("Fallo", e.toString());
			}
			adaptador=new Adaptador_pedido(getApplicationContext(), cartas, detalles,
					pedidos.get(pedidos.size()-1).getId());
			lvVista.setAdapter(adaptador);
		}
    	
    }

}