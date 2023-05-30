package com.izv.painting;

	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.util.ArrayList;
	import java.util.Calendar;
	import java.util.HashMap;
	import java.util.Map;
	import android.content.Context;
	import android.graphics.Bitmap;
	import android.graphics.Bitmap.CompressFormat;
	import android.graphics.Canvas;
	import android.graphics.Color;
	import android.graphics.Paint;
	import android.graphics.Path;
	import android.os.Environment;
	import android.view.MotionEvent;
	import android.view.View;

public class Vista extends View {

	private static Paint pincel=new Paint();
	private ArrayList<Path> lineas=new ArrayList();
	private int ancho;
	private int alto;
	private static Bitmap mapaDeBits;
	private static Canvas lienzoFondo;
	private Path rectaPrimera=new Path();
	private Path rectaSegunda=new Path();
	private float xp1, xp2;
	private float yp1, yp2;
	private int color=Color.BLACK;
	private static boolean nuevoDibujo=false;
	private Map<Path, Integer> colores = new HashMap<Path, Integer>();

	public Vista(Context context) {
		super(context);
		pincel.setColor(color);
		pincel.setAntiAlias(true);
		pincel.setStrokeWidth(5);
		pincel.setStyle(Paint.Style.STROKE);
	}
	
	public void onDraw(Canvas c){
		if(nuevoDibujo!=true){
			for (Path p : lineas){
		        pincel.setColor(colores.get(p));
		        c.drawPath(p, pincel);
		    }
		    pincel.setColor(color);
			c.drawBitmap(mapaDeBits, 0, 0, null);
			c.drawPath(rectaPrimera, pincel);
			c.drawPath(rectaSegunda, pincel);
		}else{
			c.drawColor(0xffffffff);
			mapaDeBits = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888);
			lienzoFondo = new Canvas(mapaDeBits);
			lienzoFondo.drawColor(0xffffffff);
			rectaPrimera.reset();
			rectaSegunda.reset();
			nuevoDibujo=false;
		}
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		ancho = w;
		alto = h;
		mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		lienzoFondo = new Canvas(mapaDeBits);
		lienzoFondo.drawColor(0xffffffff);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int puntos = event.getPointerCount();
		for (int i = 0; i < puntos; i++) {
			int accion = event.getActionMasked();
			float x = event.getX(i);
			float y = event.getY(i);
			int puntero = event.getPointerId(i);
			switch (accion) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					if(puntero==0){
						xp1=x;
						yp1=y;
						rectaPrimera.moveTo(xp1,  yp1);
					}else if(puntero==1){
						xp2=x;
						yp2=y;
						rectaSegunda.moveTo(xp2,  yp2);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(puntero==0){
						rectaPrimera.quadTo(xp1, yp1, (x+xp1)/2, (y+yp1)/2);
						xp1=x;
						yp1=y;
					}else if(puntero==1){
						rectaSegunda.quadTo(xp2, yp2, (x+xp2)/2, (y+yp2)/2);
						xp2=x;
						yp2=y;
					}
					break;
				case MotionEvent.ACTION_UP:
					lineas.add(rectaPrimera);
					colores.put(rectaPrimera, color);
					lineas.add(rectaSegunda);
					colores.put(rectaSegunda, color);
					lienzoFondo.drawPath(rectaPrimera, pincel);
					lienzoFondo.drawPath(rectaSegunda, pincel);
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					break;
			}
		}
		invalidate();
		return true;
	}

	public static Bitmap getBitmap(){
		return mapaDeBits;
	}
	
	public static void setBitmap(Bitmap mapa){
		mapaDeBits=mapa;
		lienzoFondo = new Canvas(mapaDeBits);
	}
	
	public int getColor(){
		return color;
	}
	
	public void setColor(int color){
		this.color=color;
		pincel.setColor(color);
		rectaPrimera.reset();
		rectaSegunda.reset();
	}
	
	public void Guardar() throws FileNotFoundException{
		Bitmap mapa=mapaDeBits;
		File carpeta=new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getPath());
		File archivo = new File (carpeta, generarNombre()+".png");
		FileOutputStream fos = new FileOutputStream (archivo);
		mapa.compress(CompressFormat.PNG, 90, fos);
	}
	
	public void Goma(){
		nuevoDibujo=true;
		invalidate();
	}
	
	public String generarNombre(){
		String nombreGenerado="";
		Calendar c = Calendar.getInstance();
		String agno = c.get(Calendar.YEAR)+"";
		String mes = c.get(Calendar.MONTH)+1+"";
		if(Integer.parseInt(mes)<10){
			mes="0"+mes;
		}
		String dia = c.get(Calendar.DAY_OF_MONTH)+"";
		if(Integer.parseInt(dia)<10){
			dia="0"+dia;
		}
		String hora = c.get(Calendar.HOUR_OF_DAY)+"";
		if(Integer.parseInt(hora)<10){
			hora="0"+hora;
		}
		String minuto = c.get(Calendar.MINUTE)+"";
		if(Integer.parseInt(minuto)<10){
			minuto="0"+minuto;
		}
		String segundo = c.get(Calendar.SECOND)+"";
		if(Integer.parseInt(segundo)<10){
			segundo="0"+segundo;
		}
		nombreGenerado="/IMG_"+agno+mes+dia+"_"+hora+minuto+segundo+".png";
		return nombreGenerado;
	}
	
}