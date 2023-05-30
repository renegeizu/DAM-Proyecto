package com.rngds.pong;

	import android.graphics.Paint;
	import android.graphics.RectF;

class Jugador {

    int anchoPala;
    int altoPala;
    Paint pincel;
    int puntuacion;
    RectF bounds;
    int colision;

    Jugador(int anchoPala, int altoPala, Paint pincel) {
        this.anchoPala = anchoPala;
        this.altoPala = altoPala;
        this.pincel = pincel;
        this.puntuacion = 0;
        this.bounds = new RectF(0, 0, anchoPala, altoPala);
        this.colision = 0;
    }

}
