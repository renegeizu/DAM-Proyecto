package com.izv.reproductormusica;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RepMusica extends Activity {

	private TextView tvCanciones;
	private ListView lvCanciones;
	private TextView tvTitulo;
	private MediaPlayer mp;
	private boolean estado=true;
	private Cursor lista;
	private Adaptador adaptador;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repmusica);
		
		new OnAudioFocusChangeListener() 
		{@Override
		public void onAudioFocusChange(int focusChange) 
			{if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
				{mp.setVolume(1.0f, 1.0f);} 
			else if(focusChange == AudioManager.AUDIOFOCUS_GAIN) 
				{mp.setVolume(0.1f, 0.1f);}}};
				
		mp=new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mp.setOnCompletionListener(new finReproduccionActual());
        
        tvCanciones=(TextView) findViewById(R.id.tvCanciones);
        lvCanciones=(ListView) findViewById(R.id.lvCanciones);
        tvTitulo=(TextView) findViewById(R.id.tvNombre);
        
        lista=cogerCanciones();
        adaptador=new Adaptador(this, lista);
        lvCanciones.setAdapter(adaptador);
        hayCanciones();
        lvCanciones.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> av, View v, int pos, long id) 
				{try
					{mp.reset();}
				catch(Exception e)
					{Log.v("Reproductor", "No habia cancion en reproduccion");}
				lista.moveToPosition(pos);
				String direccion=lista.getString(1);
				tvTitulo.setText(tituloCancion(direccion));
				play(direccion);}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reproductor, menu);
		return true;
	}
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId())
		 	{case R.id.action_grabar:
		 		mp.reset();
 		 		Intent i = new Intent(this, Recorder.class);
 		 		startActivity(i);
 		 		break;}
		 return super.onOptionsItemSelected(item);
	 }
	 
	 @Override
	 public void onResume(){
		 super.onResume();
		 new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://" + Environment.getExternalStorageDirectory()));   
	     IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
	     intentFilter.addDataScheme("file");     
	     sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		 lista=cogerCanciones();
	     adaptador.notifyDataSetChanged();
	     hayCanciones();
	 }
	 
	 @Override
	 public void onBackPressed() {
		 super.onBackPressed();
		 if(mp.isPlaying())
		 	{mp.stop();}
	 }

	@Override
	 public void onDestroy(){
		 super.onDestroy();
		 if(mp.isPlaying())
		 	{mp.stop();}
	 }
	    
	 private class finReproduccionActual implements OnCompletionListener{
	    	@Override
	    	public void onCompletion(MediaPlayer mp) {
	    		try
	    			{lista.moveToPosition(lista.getPosition()+1);
	        		mp.reset();
	        		String uri=lista.getString(1);
	        		tvTitulo.setText(tituloCancion(uri));
	    			play(uri);}
	    		catch(Exception e)
	    			{lista.moveToFirst();
	        		mp.reset();
	        		String direccion=lista.getString(1);
	        		tvTitulo.setText(tituloCancion(direccion));
	    			play(direccion);}
	    	}
	    }
	    
	 private class Reproduccion implements OnPreparedListener {
		 @Override
		 public void onPrepared(MediaPlayer mediaplayer)
		 	{mp.start();}
	 }
	 
	 public void play(String url){
		 try
		 	{mp.setOnPreparedListener(new Reproduccion());
			 mp.setDataSource(url);
			 mp.prepareAsync();}
		 catch(Exception e)
		 	{Log.v("Reproductor", "Error al intentar reproducir");}	
	 }

	public void backward(View v){
		 try
		 	{lista.moveToPosition(lista.getPosition()-1);
			 mp.reset();
			 String uri=lista.getString(1);
			 tvTitulo.setText(tituloCancion(uri));
			 play(uri);}
		 catch(Exception e)
		 	{lista.moveToLast();
			 mp.reset();
			 String direccion=lista.getString(1);
			 tvTitulo.setText(tituloCancion(direccion));
			 play(direccion);}
	 }

	public void stop(View v){
		 try
		 	{mp.reset();}
		 catch(Exception e)
		 	{Log.v("Reproductor", "Error en el boton stop");}
	 }

	public void play(View v){
		 try
		 	{if(estado==true)
		 		{mp.start();
				 estado=false;}}
		 catch(Exception e)
		 	{Log.v("Reproductor", "Error en el boton play");}
	 }

	public void pause(View v){
		 try
		 	{if(estado==false)
		 		{mp.pause();
				 estado=true;}}
		 catch(Exception e)
		 	{Log.v("Reproductor", "Error en el boton pausa");}
	 }

	public void forward(View v){
		 try
		 	{lista.moveToPosition(lista.getPosition()+1);
			 mp.reset();
			 String uri=lista.getString(1);
			 tvTitulo.setText(tituloCancion(uri));
			 play(uri);}
		 catch(Exception e)
		 	{lista.moveToFirst();
			 mp.reset();
			 String direccion=lista.getString(1);
			 tvTitulo.setText(tituloCancion(direccion));
			 play(direccion);}
	 }

	public Cursor cogerCanciones(){
		 Cursor c=getBaseContext().getContentResolver().query(
				 android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, 
				 AudioColumns.IS_MUSIC+"!=0", null, MediaColumns.TITLE);
		 return c;
	 }

	public void hayCanciones(){
		 if(lista.getCount()==0)
		 	{tvCanciones.setText("No tienes ninguna cancion");}
		 else
		 	{tvCanciones.setText("Lista de canciones:");}
	 }

	public String tituloCancion(String uri){
		 return uri.substring(uri.lastIndexOf("/")+1, uri.length());
	 }

}
