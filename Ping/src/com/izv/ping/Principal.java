package com.izv.ping;

	import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.izv.lanzarping.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        Button bt=(Button)findViewById(R.id.button1);
        bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ping();
			}
		});
    }
    
    public void ping(){
    	RadioButton rb=(RadioButton) findViewById(R.id.radio0);
    	TextView tv=(TextView)findViewById(R.id.textView4);
    	EditText et=(EditText)findViewById(R.id.editText1);
    	tv.setText("");
		if(rb.isChecked()){
			tv.append("Comenzando el Ping...");
			PingSimple ps = new PingSimple();
			ps.execute(new String[] {et.getText().toString().trim()});
		}else{
			tv.append("Comenzando el Ping...");
			PingMulti pm = new PingMulti();
			pm.execute(new String[] {et.getText().toString().trim()+"."});
		}
    }
    
    public class PingSimple extends AsyncTask<String,  String,  String> {   	
    	
		@Override
		protected String doInBackground(String...  params){
			String mnsjPing="";
			try{
				Runtime rt=Runtime.getRuntime();
				Process proceso=null;
				proceso=rt.exec(new String[] {"/system/bin/ping","-c 1",params[0]});
				InputStream is=proceso.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				final BufferedReader stdErr=new BufferedReader(new
				InputStreamReader(proceso.getErrorStream()));
				String linea;
				while ((linea=br.readLine())!=null) {
					mnsjPing=linea+"   --> Hay Conexion con"+params[0];
				}
				while ((linea=stdErr.readLine())!=null) {
					mnsjPing=linea+"   --> No Hay Conexion con "+params[0]
							+", Podrias no Estar Conectado a la Red";
				}
				publishProgress(mnsjPing);
				br.close();
				isr.close();
				is.close();
			}catch(Exception e){
			}
			return mnsjPing;
		}
		
		@Override
		protected void onProgressUpdate(String... values){
			String valor=values[values.length-1];
			TextView tv=(TextView)findViewById(R.id.textView4);
			tv.append("\n"+valor);	
		}
		
		@Override
		protected void onPostExecute(String result)  {
			TextView tv=(TextView)findViewById(R.id.textView4);
			tv.append("\n Fin");				
		} 
		
		@Override
		protected void onCancelled()  {
			Toast.makeText(Principal.this, "Ping Cancelado", Toast.LENGTH_SHORT).show();
		}
	}
    
    public class PingMulti extends AsyncTask<String,  String,  String> { 
		
		@Override
		protected String doInBackground(String...  params)  {
			String mnsjPing="";
			try{
				Runtime rt=Runtime.getRuntime();
				Process proceso=null;
				for(int cont=0; cont<256; cont++){
					proceso=rt.exec(new String[] {"/system/bin/ping","-c 1",params[0]+cont});
					InputStream is=proceso.getInputStream();
					InputStreamReader isr=new InputStreamReader(is);
					BufferedReader br=new BufferedReader(isr);
					final BufferedReader stdErr=new BufferedReader(new
					InputStreamReader(proceso.getErrorStream()));
					String linea;
					while ((linea=br.readLine())!=null) {
						mnsjPing=linea+"   --> Hay Conexion con"+params[0]+cont
								+"\n"+mnsjPing;
					}
					while ((linea=stdErr.readLine())!=null) {
						mnsjPing=linea+"   --> No Hay Conexion con "+params[0]+cont
								+", Podrias no Estar Conectado a la Red"+"\n"+mnsjPing;
					}
					mnsjPing="\n \n"+mnsjPing;
					publishProgress(mnsjPing);
					br.close();
					isr.close();
					is.close();
				}
			}catch(Exception e){
			}
			return mnsjPing;
		}
		
		@Override
		protected void onProgressUpdate(String... values){
			String valor=values[values.length-1];
			TextView tv=(TextView)findViewById(R.id.textView4);
			tv.append("\n"+valor);	
		}
		
		@Override
		protected void onPostExecute(String result)  {
			TextView tv=(TextView)findViewById(R.id.textView4);
			tv.append("\n"+result+"\nFin");	
		} 
		
		@Override
		protected void onCancelled() {
			Toast.makeText(Principal.this, "Ping Cancelado", Toast.LENGTH_SHORT).show();
		}
	}
    
}
