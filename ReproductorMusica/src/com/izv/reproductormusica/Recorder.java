package com.izv.reproductormusica;

import java.io.File;
import java.util.Calendar;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class Recorder extends Activity {
	
	private MediaRecorder recorder;
	private TextView tv;
	private Button btGrabar;
	private Button btParar;
	private ImageView btImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorder);
		
		try
			{File archivo = Environment.getExternalStorageDirectory();
			String existePath = archivo.getPath();
			File carpeta = new File(existePath+"/Record");
			carpeta.mkdir();}
		catch(Exception e)
			{Log.v("Grabador", "La carpeta ya existe");}
		
		tv=(TextView) findViewById(R.id.tvGrabacion);
		btImg=(ImageView) findViewById(R.id.imageView1);
		btGrabar=(Button) findViewById(R.id.btGrabar);
		
		btGrabar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try
					{tv.setText("Grabando...");
					btImg.setImageResource(R.drawable.microfono_on);

					recorder=new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					recorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()
							+"/Record/"+generarNombre());
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					recorder.prepare();
					recorder.start();}
				catch(Exception e)
					{Toast.makeText(v.getContext(), "No se ha podido grabar correctamente", Toast.LENGTH_LONG).show();}
			}
		});
		btParar=(Button)findViewById(R.id.btDetener);
		btParar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try
					{recorder.stop();
					recorder.release();
					recorder = null;
					Toast.makeText(v.getContext(), "Grabacion finalizada correctamente", Toast.LENGTH_LONG).show();
					finish();}
				catch(Exception e)
					{Toast.makeText(v.getContext(), "Error en la grabacion", Toast.LENGTH_LONG).show();}
			}
		});
	}
	
	public String generarNombre(){
		Calendar cal = Calendar.getInstance();	
		String hora, minutos, agno, mes, dia;
		if(cal.get(Calendar.HOUR_OF_DAY)<10)
			{hora="0"+cal.get(Calendar.HOUR_OF_DAY);}
		else
			{hora=""+cal.get(Calendar.HOUR_OF_DAY);}
		if(cal.get(Calendar.MINUTE)<10)
			{minutos="0"+cal.get(Calendar.MINUTE);}
		else
			{minutos=""+cal.get(Calendar.MINUTE);}
		agno=""+cal.get(Calendar.YEAR);
		if(cal.get(Calendar.MONTH)+1<10)
			{mes="0"+cal.get(Calendar.MONTH)+1;}
		else
			{mes=""+cal.get(Calendar.MONTH)+1;}
		if(cal.get(Calendar.DAY_OF_MONTH)<10)
			{dia="0"+cal.get(Calendar.DAY_OF_MONTH);}
		else
			{dia=""+cal.get(Calendar.DAY_OF_MONTH);}
		return "MIC_"+agno+mes+dia+hora+minutos+".mp4";
	}

}
