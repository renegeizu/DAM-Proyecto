package com.rngds.pong;

	import android.content.Context;
	import android.os.Handler;
	import android.os.Message;
	import android.util.AttributeSet;
	import android.view.MotionEvent;
	import android.view.SurfaceHolder;
	import android.view.SurfaceView;
	import android.widget.TextView;

public class PongVista extends SurfaceView implements SurfaceHolder.Callback {

    private PongThread hiloJuego;
    private TextView vistaStatus;
    private TextView vistaScore;
    private boolean moviendo;
    private float   ultimaPosicionY;

    public PongVista(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        hiloJuego = new PongThread(holder, context,
                new Handler() {
                    @Override
                    public void handleMessage(Message m) {
                        vistaStatus.setVisibility(m.getData().getInt("vis"));
                        vistaStatus.setText(m.getData().getString("text"));
                    }
                },
                new Handler() {
                    @Override
                    public void handleMessage(Message m) {
                        vistaScore.setText(m.getData().getString("text"));
                    }
                },
                attributeSet
        );

        setFocusable(true);
    }

    public void setVistaStatus(TextView textView) {
        vistaStatus = textView;
    }

    public void setVistaScore(TextView textView) {
        vistaScore = textView;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            hiloJuego.pause();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        hiloJuego.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        hiloJuego.setRunning(true);
        hiloJuego.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        hiloJuego.setRunning(false);
        while (retry) {
            try {
                hiloJuego.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (hiloJuego.isBetweenRounds()) {
                    hiloJuego.setState(PongThread.ESTADO_CORRIENDO);
                } else {
                    if (hiloJuego.isTouchOnHumanPaddle(event)) {
                        moviendo = true;
                        ultimaPosicionY = event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (moviendo) {
                    float y = event.getY();
                    float dy = y - ultimaPosicionY;
                    ultimaPosicionY = y;
                    hiloJuego.moveHumanPaddle(dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                moviendo = false;
                break;
        }
        return true;
    }

    public PongThread getGameThread() {
        return hiloJuego;
    }

}