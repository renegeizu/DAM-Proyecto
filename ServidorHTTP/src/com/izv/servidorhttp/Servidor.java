package com.izv.servidorhttp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.StringTokenizer;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class Servidor extends Activity {

	private TextView tv;
	public static final int PUERTO=7557;
	private ServerSocket servicio;
	private boolean arrancado=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servidor);
		tv=(TextView) findViewById(R.id.textView1);
	}
	
	public void start(View v){	
		if(arrancado)
			return;
		tv.append("Servicio Arrancado\n");
		arrancado=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					servicio = new ServerSocket(PUERTO);
					while (arrancado) {
						new HiloSocket(servicio.accept()).start();
					}
					servicio.close();
				}catch(Exception e){
					append("Star Run: "+e.toString());
				}
				append("Servicio Parado");
			}
		}).start();
	}
	
	public void stop(View v){
		arrancado=false;
		tv.append("Parando Servicio...\n");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					Socket cliente = new Socket("localhost", PUERTO);
				}catch(Exception e){
					append("Stop Run: "+e.toString());
				}
			}
		}).start();
	}
	
	private void append(final String s){
		tv.post(new Runnable() {
			@Override
			public void run() {
				tv.append(s+"\n");
			}
		});
	}
	
	private static void append(final TextView tv, final String s){
		tv.post(new Runnable() {
			@Override
			public void run() {
				tv.append(s+"\n");
			}
		});
	}
	
	@Override
	protected void onPause() {
		arrancado=false;
		super.onPause();
	}
	
	private class HiloSocket extends Thread{
		
		private Socket socket;
		private BufferedReader in;
		private DataOutputStream out;
		
		HiloSocket(Socket s){
			this.socket=s;
			try{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new DataOutputStream(socket.getOutputStream());
			}catch(Exception e){}
		}
		
		@Override
		public void run() {
			try{
				String peticion = in.readLine();
				String archivo="";
				String cadena=peticion.substring(peticion.indexOf("/")+1, 
						peticion.lastIndexOf(" ")).trim();
				if(peticion!=null){
					StringTokenizer token = new  StringTokenizer(peticion);
					token.nextToken();
					archivo = token.nextToken();
					archivo = archivo.replaceFirst("/", "");
					archivo = URLDecoder.decode(archivo, "utf-8");
				}
				while (in.ready()) {
					peticion = in.readLine();
				}
				if(cadena.equals("contactos")==true){
					out = new DataOutputStream(socket.getOutputStream());
					out.writeBytes("HTTP/1.1 200 OK\r\n");
					out.writeBytes("Server: Android ACME HTTP Server\r\n");
					out.writeBytes("Content-Type: text/html\r\n");
					String respuesta="<html><head><title>Contactos</title></head><body>" +
							"<h1>Listado de Contactos</h1><br/>"+contactos()+"</body></html>";
					out.writeBytes("Content-Length: "+respuesta.length()+"\r\n");
					out.writeBytes("Connection: close\r\n");
					out.writeBytes("\r\n");
					out.writeBytes(respuesta);
					append("Contactos Actuales Mostrados");
				}else if(cadena.equals("llamadas")==true){
					out = new DataOutputStream(socket.getOutputStream());
					out.writeBytes("HTTP/1.1 200 OK\r\n");
					out.writeBytes("Server: Android ACME HTTP Server\r\n");
					out.writeBytes("Content-Type: text/html\r\n");
					String respuesta="<html><head><title>Llamadas</title></head><body>" +
							"<h1>Listado de Llamadas</h1><br/>"+llamadas()+"</body></html>";
					out.writeBytes("Content-Length: "+respuesta.length()+"\r\n");
					out.writeBytes("Connection: close\r\n");
					out.writeBytes("\r\n");
					out.writeBytes(respuesta);
					append("Listado de Llamadas Mostrado");
				}
				in.close();
				out.flush();
				out.close();
				socket.close();
				append("Peticion Atendida");
			}catch(Exception e){
				append("HiloSocket Run: "+e.toString());
				e.printStackTrace();
			}
		}	
	}
	
	public String contactos(){	
		String contactos="";
		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
        while (phones.moveToNext()) {
            String nombre = phones.getString(phones.getColumnIndex(
            		ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String numero = phones.getString(phones.getColumnIndex(
            		ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactos=contactos+"<h4>Nombre: "+nombre+"</h4><h4>Numero: "+numero+"</h4><br/>";
        }
        phones.close();
		return contactos;
	}
	
	public String llamadas(){
		StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
            	dir = "Realizada";
                break;
            case CallLog.Calls.INCOMING_TYPE:
                dir = "Recibida";
                break;
            case CallLog.Calls.MISSED_TYPE:
                dir = "Perdida";
                break;
            }
            sb.append("<h4>Numero de Telefono: "+phNumber+"</h4><h4>Tipo de Llamada: "
                    +dir+"</h4><h4>Fecha de la Llamada: "+callDayTime
                    + "</h4><h4>Duracion de la Llamada: "+callDuration);
            sb.append("<br/><br/><br/>");
        }
        managedCursor.close();
        return sb.toString();
	}

}
