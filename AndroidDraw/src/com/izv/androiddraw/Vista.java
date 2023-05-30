package com.izv.androiddraw;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

public class Vista extends View{
	
	private static Paint pincel=new Paint();
	private int ancho;
	private int alto;
	private static Bitmap mapaDeBits;
	private static Canvas lienzoFondo;
	private Path rectaPoligonal1=new Path();
	private Path rectaPoligonal2=new Path();
	private float xp1, xp2;
	private float yp1, yp2;
	private int color=Color.BLACK;
	private ArrayList<Path> paths=new ArrayList();
	private Map<Path, Integer> colorPath = new HashMap<Path, Integer>();
	private Bitmap iconCuadrado=BitmapFactory.decodeResource(this.getResources(), R.drawable.tapon_cuadrado);
	private Bitmap iconCirculo=BitmapFactory.decodeResource(this.getResources(), R.drawable.tapon_circulo);
	private boolean cuadrado=false;
	private boolean circulo=false;
	
	public Vista(Context context){
		super(context);
		pincel.setColor(color);
		pincel.setAntiAlias (true);
		pincel.setStrokeWidth (6);
		pincel.setStyle(Paint.Style.STROKE);
	}

	public void onDraw (Canvas c) {
		for (Path p : paths){
	        pincel.setColor(colorPath.get(p));
	        c.drawPath(p, pincel);
	    }
	    pincel.setColor(color);
		c.drawBitmap(mapaDeBits, 0, 0, null);
		c.drawPath(rectaPoligonal1, pincel);
		c.drawPath(rectaPoligonal2, pincel);
	}
	
	@Override
	public void onSizeChanged (int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		ancho = w;
		alto = h;
		mapaDeBits = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
		lienzoFondo = new Canvas (mapaDeBits);
		lienzoFondo.drawColor(0xffffffff);
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent event){
		int puntos = event.getPointerCount();
		for (int i = 0; i < puntos; i++) {
			int accion = event.getActionMasked();
			float x = event.getX(i);
			float y = event.getY(i);
			int puntero = event.getPointerId (i);
			switch (accion){
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					if(cuadrado!=true && circulo!=true){
						if (puntero == 0) {
							xp1 = x;
							yp1 = y;
							rectaPoligonal1.moveTo(xp1, yp1);
						}else if (puntero == 1){
							xp2 = x;
							yp2 = y;
							rectaPoligonal2.moveTo (xp2, yp2);
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(cuadrado!=true && circulo!=true){
						if (puntero == 0) {
							rectaPoligonal1.quadTo(xp1, yp1, (x+xp1)/2, (y+yp1)/2);
							xp1 = x;
							yp1 = y;						
						}else if (puntero == 1){
							rectaPoligonal2.quadTo(xp2, yp2, (x+xp2)/2, (y+yp2)/2);
							xp2 = x;
							yp2 = y;
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if(cuadrado==true){
						lienzoFondo.drawBitmap(iconCuadrado, x, y, null);
					}else if(circulo==true){
						lienzoFondo.drawBitmap(iconCirculo, x, y, null);
					}else{
						paths.add(rectaPoligonal1);
						colorPath.put(rectaPoligonal1, color);
						paths.add(rectaPoligonal2);
						colorPath.put(rectaPoligonal2, color);
						lienzoFondo.drawPath(rectaPoligonal1, pincel);
						lienzoFondo.drawPath(rectaPoligonal2, pincel);
					}
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					break;
			}
		}
		invalidate();
		return true;
	}
	
	public void guardar (String guardar) {
		try {
			File archivo = Environment.getExternalStorageDirectory();
			String s = archivo.getPath();
			File carpeta = new File (s+"/Dibujos/");
			carpeta.mkdir();			
		}catch (Exception e){
		}
		try {
			Bitmap mapa = mapaDeBits;
			File carpeta = new File (Environment.getExternalStorageDirectory().getPath()+"/Dibujos/");
			File archivo = new File (carpeta, guardar+".png");
			FileOutputStream fos = new FileOutputStream (archivo);
			mapa.compress(CompressFormat.PNG, 90, fos);
		}catch (Exception e) {			
		}
	}
	
	public static Bitmap getBitmap () {
		return mapaDeBits;
	}
	
	public static void setBitmap (Bitmap mapa) {
		mapaDeBits = mapa;
		lienzoFondo = new Canvas(mapaDeBits);
	}
	
	public void setColor (int color) {
		this.color = color;
		pincel.setColor(color);
		rectaPoligonal1.reset();
		rectaPoligonal2.reset();
	}
	
	public int getColor(){
		return color;
	}
	
	public void setTaponCuadrado(){
		if(cuadrado==true)
			cuadrado=false;
		else
			cuadrado=true;
	}
	
	public void setTaponCirculo(){
		if(circulo==true)
			circulo=false;
		else
			circulo=true;
	}
	
	public void setPincel(){
		circulo=false;
		cuadrado=false;
	}
	
}
