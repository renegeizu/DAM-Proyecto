package com.rngds.pong;

	import android.graphics.Paint;

public class Pelota {

    float cx;
    float cy;
    float dx;
    float dy;
    int radio;
    Paint pincel;

    public Pelota(int radio, Paint pincel) {
        this.radio = radio;
        this.pincel = pincel;
    }

}
