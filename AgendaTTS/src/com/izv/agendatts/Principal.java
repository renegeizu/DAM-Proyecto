package com.izv.agendatts;

	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.util.ArrayList;
	import java.util.Locale;
	import org.xmlpull.v1.XmlPullParser;
	import org.xmlpull.v1.XmlSerializer;
	import android.os.Bundle;
	import android.app.Activity;
	import android.content.Intent;
	import android.speech.RecognizerIntent;
	import android.speech.tts.TextToSpeech;
	import android.util.Xml;
	import android.view.View;
	import android.widget.AdapterView;
	import android.widget.AdapterView.OnItemClickListener;
	import android.widget.ListView;
	import android.widget.Toast;
	import android.widget.ToggleButton;

public class Principal extends Activity implements TextToSpeech.OnInitListener{

	private static int GRABAR = 1;
	private static int CTE=2;
	private ArrayList<String> listaMnj;
	private TextToSpeech tts;
	private Adaptador adaptador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		final ToggleButton tb=(ToggleButton) findViewById(R.id.tbutton1);
		ListView lv = (ListView) findViewById(R.id.lvMensajes);
		listaMnj = new ArrayList<String>();
		Intent i = new Intent ();
		
		listaMnj.add("Hola");
		listaMnj.add("Adios");
		leer();
		adaptador = new Adaptador(this,listaMnj);
		lv.setAdapter(adaptador);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View vista, int posicion, long id) {
				if(tb.isChecked()==false){
					String contenido= listaMnj.get(posicion);
					tts.speak(contenido, TextToSpeech.QUEUE_FLUSH, null);
				}else{
					listaMnj.remove(posicion);
					adaptador.notifyDataSetChanged();
					escribir();
				}
			}
		});
		
		i.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(i,CTE);
	}

	public void escribir (){
		try {
			FileOutputStream fos = new FileOutputStream(new File(getExternalFilesDir(null), "mensaje.xml"));
			XmlSerializer docxml = Xml.newSerializer();
			docxml.setOutput(fos,"UTF-8");
			docxml.startDocument(null, Boolean.valueOf(true));
			docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			docxml.startTag(null, "Mensajes");
			
			for (String texto: listaMnj)
				{docxml.startTag(null, "Mensaje");
				docxml.text(texto);
				docxml.endTag(null, "Mensaje");
				}
			
			docxml.endDocument();
			docxml.flush();
			fos.close();
		}
		catch (Exception e) {
			Toast.makeText(this, "No ha podido guardarse su mensaje", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void leer () {
		XmlPullParser leerXml = Xml.newPullParser();
		try {
			leerXml.setInput(new FileInputStream (new File(getExternalFilesDir(null), "mensaje.xml")), "UTF-8");
			int posicion = leerXml.getEventType();
			
			while (posicion != XmlPullParser.END_DOCUMENT){ 
				if (posicion == XmlPullParser.START_TAG){
					String nombreTag = leerXml.getName();
					 if (nombreTag.equals("Mensaje")== true){
						 String contenido = leerXml.nextText();
						 listaMnj.add(contenido);
					 	}
					}
				
				posicion = leerXml.next();
				}
		}
		catch (Exception e) {
			
		}
	}

	public void btGrabar(View v){
		Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
		i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Comienza a dictar tu mensaje");
		i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
		startActivityForResult(i, GRABAR);
	}
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data){
		if (requestCode == CTE && resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
			tts = new TextToSpeech (this, this);
		}
		
		if (requestCode == GRABAR){
			ArrayList<String> textoGrabado=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			listaMnj.add(textoGrabado.get(0));
			adaptador.notifyDataSetChanged();
			escribir();
		}
			
	}
	
	@Override
	public void onInit(int status) {
		if(status==TextToSpeech.SUCCESS){
			tts.setLanguage(new Locale("es", "ES"));
		}
	}
	
	@Override
	public void onDestroy(){
		if(tts!=null){
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
}
