package com.rngds.pong;

	import android.content.Context;
	import android.content.res.Resources;
	import android.content.res.TypedArray;
	import android.graphics.Canvas;
	import android.graphics.Color;
	import android.graphics.DashPathEffect;
	import android.graphics.Paint;
	import android.os.Bundle;
	import android.os.Handler;
	import android.os.Message;
	import android.os.SystemClock;
	import android.util.AttributeSet;
	import android.util.Log;
	import android.view.MotionEvent;
	import android.view.SurfaceHolder;
	import android.view.View;
	import java.util.Random;

public class PongThread extends Thread {

    public static final int ESTADO_PAUSA=0;
    public static final int ESTADO_LISTO=1;
    public static final int ESTADO_CORRIENDO=2;
    public static final int ESTADO_PERDIDO=3;
    public static final int ESTADO_GANADO=4;
    private static final int VELOCIDAD_PELOTA=23;
    private static final int VELOCIDAD_PALA=23;
    private static final int FPS=60;
    private static final double ANGULO=5 * Math.PI / 12;
    private static final int FRAMES_COLISION=5;
    private static final String DATOS_JUGADOR="humanPlayer";
    private static final String DATOS_CPU="computerPlayer";
    private static final String DATOS_PELOTA="ball";
    private static final String ESTADO_JUEGO="state";
    private static final String TAG="PongThread";
    private final SurfaceHolder mSurfaceHolder;
    private final Handler mStatusHandler;
    private final Handler mScoreHandler;
    private final Context mContext;
    private boolean mRun;
    private final Object  mRunLock;
    private int mEstado;
    private Jugador mJugador;
    private Jugador mCPU;
    private Pelota mPelota;
    private Paint mPintarLinea;
    private Paint mCanvasBoundsPaint;
    private int mCanvasAlto;
    private int mCanvasAncho;
    private Random mRandom;
    private float mMoverCPU;
    
    public PongThread(final SurfaceHolder surfaceHolder, final Context context, 
    		final Handler statusHandler,  final Handler scoreHandler, final AttributeSet attributeSet){
        mSurfaceHolder=surfaceHolder;
        mStatusHandler=statusHandler;
        mScoreHandler=scoreHandler;
        mContext=context;
        mRun=false;
        mRunLock=new Object();
        TypedArray a=context.obtainStyledAttributes(attributeSet, R.styleable.PongVista);
        int altoPala=a.getInt(R.styleable.PongVista_palaAlto, 85);
        int anchoPala=a.getInt(R.styleable.PongVista_palaAncho, 25);
        int radioPelota=a.getInt(R.styleable.PongVista_pelotaRadio, 15);
        a.recycle();
        Paint pincelJugador=new Paint();
        pincelJugador.setAntiAlias(true);
        pincelJugador.setColor(Color.BLUE);
        mJugador=new Jugador(anchoPala, altoPala, pincelJugador);
        Paint pincelCPU=new Paint();
        pincelCPU.setAntiAlias(true);
        pincelCPU.setColor(Color.RED);
        mCPU=new Jugador(anchoPala, altoPala, pincelCPU);
        Paint pincelPelota=new Paint();
        pincelPelota.setAntiAlias(true);
        pincelPelota.setColor(Color.GREEN);
        mPelota=new Pelota(radioPelota, pincelPelota);
        mPintarLinea=new Paint();
        mPintarLinea.setAntiAlias(true);
        mPintarLinea.setColor(Color.YELLOW);
        mPintarLinea.setAlpha(80);
        mPintarLinea.setStyle(Paint.Style.FILL_AND_STROKE);
        mPintarLinea.setStrokeWidth(2.0f);
        mPintarLinea.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        mCanvasBoundsPaint=new Paint();
        mCanvasBoundsPaint.setAntiAlias(true);
        mCanvasBoundsPaint.setColor(Color.YELLOW);
        mCanvasBoundsPaint.setStyle(Paint.Style.STROKE);
        mCanvasBoundsPaint.setStrokeWidth(1.0f);
        mCanvasAlto=1;
        mCanvasAncho=1;
        mRandom=new Random();
        mMoverCPU=0.6f;
    }

    /* Bucle del Juego */
    
    @Override
    public void run() {
        long mNextGameTick=SystemClock.uptimeMillis();
        int skipTicks=1000/FPS;
        while (mRun) {
            Canvas c=null;
            try{
                c=mSurfaceHolder.lockCanvas(null);
                if(c!=null){
                    synchronized (mSurfaceHolder){
                        if(mEstado==ESTADO_CORRIENDO){
                            updatePhysics();
                        }
                        synchronized (mRunLock){
                            if (mRun){
                                updateDisplay(c);
                            }
                        }
                    }
                }
            } finally{
                if (c != null){
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
            mNextGameTick+=skipTicks;
            long tiempoEspera=mNextGameTick-SystemClock.uptimeMillis();
            if (tiempoEspera>0){
                try {
                    Thread.sleep(tiempoEspera);
                }catch(InterruptedException e){}
            }
        }
    }

    void setRunning(boolean running) {
        synchronized (mRunLock) {
            mRun=running;
        }
    }

    void saveState(Bundle map) {
        synchronized (mSurfaceHolder) {
            map.putFloatArray(DATOS_JUGADOR, new float[]{mJugador.bounds.left, mJugador.bounds.top,
            		mJugador.puntuacion});
            map.putFloatArray(DATOS_CPU, new float[]{mCPU.bounds.left, mCPU.bounds.top,
            		mCPU.puntuacion});
            map.putFloatArray(DATOS_PELOTA, new float[]{mPelota.cx, mPelota.cy, mPelota.dx, 
            		mPelota.dy});
            map.putInt(ESTADO_JUEGO, mEstado);
        }
    }

    void restoreState(Bundle map) {
        synchronized (mSurfaceHolder) {
            float[] datosJugador=map.getFloatArray(DATOS_JUGADOR);
            mJugador.puntuacion=(int) datosJugador[2];
            movePlayer(mJugador, datosJugador[0], datosJugador[1]);
            float[] datosCPU=map.getFloatArray(DATOS_CPU);
            mCPU.puntuacion=(int) datosCPU[2];
            movePlayer(mCPU, datosCPU[0], datosCPU[1]);
            float[] datosPelota=map.getFloatArray(DATOS_PELOTA);
            mPelota.cx=datosPelota[0];
            mPelota.cy=datosPelota[1];
            mPelota.dx=datosPelota[2];
            mPelota.dy=datosPelota[3];
            int state=map.getInt(ESTADO_JUEGO);
            setState(state);
        }
    }

    void setState(int mode) {
        synchronized (mSurfaceHolder) {
            mEstado=mode;
            Resources res=mContext.getResources();
            switch (mEstado) {
                case ESTADO_LISTO:
                    setupNewRound();
                    break;
                case ESTADO_CORRIENDO:
                    hideStatusText();
                    break;
                case ESTADO_GANADO:
                    setStatusText(res.getString(R.string.mode_win));
                    mJugador.puntuacion++;
                    setupNewRound();
                    break;
                case ESTADO_PERDIDO:
                    setStatusText(res.getString(R.string.mode_lose));
                    mCPU.puntuacion++;
                    setupNewRound();
                    break;
                case ESTADO_PAUSA:
                    setStatusText(res.getString(R.string.mode_pause));
                    break;
            }
        }
    }

    void pause() {
        synchronized (mSurfaceHolder) {
            if (mEstado==ESTADO_CORRIENDO) {
                setState(ESTADO_PAUSA);
            }
        }
    }

    void unPause() {
        synchronized (mSurfaceHolder) {
            setState(ESTADO_CORRIENDO);
        }
    }

    /* Resetear Puntuacion e Iniciar Juego Nuevo */
    
    void startNewGame() {
        synchronized (mSurfaceHolder) {
            mJugador.puntuacion = 0;
            mCPU.puntuacion = 0;
            setupNewRound();
            setState(ESTADO_CORRIENDO);
        }
    }

    /* Devuelve True si el juego esta en Ganado, Perdido o Pausa */
    
    boolean isBetweenRounds() {
        return mEstado!=ESTADO_CORRIENDO;
    }

    boolean isTouchOnHumanPaddle(MotionEvent event) {
        return mJugador.bounds.contains(event.getX(), event.getY());
    }

    void moveHumanPaddle(float dy) {
        synchronized (mSurfaceHolder) {
            movePlayer(mJugador, mJugador.bounds.left, mJugador.bounds.top + dy);
        }
    }

    void setSurfaceSize(int width, int height) {
        synchronized (mSurfaceHolder) {
            mCanvasAncho = width;
            mCanvasAlto = height;
            setupNewRound();
        }
    }

    /* Recarga las posiciones, comprueba si hay colision, ganado o perdido */
    
    private void updatePhysics() {
        if (mJugador.colision > 0) {
            mJugador.colision--;
        }
        if (mCPU.colision > 0) {
            mCPU.colision--;
        }
        if (collision(mJugador, mPelota)) {
            handleCollision(mJugador, mPelota);
            mJugador.colision=FRAMES_COLISION;
        } else if (collision(mCPU, mPelota)) {
            handleCollision(mCPU, mPelota);
            mCPU.colision=FRAMES_COLISION;
        } else if (ballCollidedWithTopOrBottomWall()) {
            mPelota.dy=-mPelota.dy;
        } else if (ballCollidedWithRightWall()) {
        	//El jugador juega en la izquierda
        	//Juega hacia la derecha
            setState(ESTADO_GANADO);
            return;
        } else if (ballCollidedWithLeftWall()) {
        	//LA CPU juega en la derecha
        	//Juega hacia la izquierda
            setState(ESTADO_PERDIDO);
            return;
        }
        if (mRandom.nextFloat()<mMoverCPU) {
            doAI();
        }
        moveBall();
    }

    private void moveBall() {
        mPelota.cx+=mPelota.dx;
        mPelota.cy+=mPelota.dy;

        if (mPelota.cy<mPelota.radio) {
            mPelota.cy=mPelota.radio;
        } else if (mPelota.cy+mPelota.radio>=mCanvasAlto) {
            mPelota.cy=mCanvasAlto-mPelota.radio-1;
        }
    }

    /* Mueve la pala de la CPU para interceptar la bola */
    
    private void doAI() {
        if (mCPU.bounds.top > mPelota.cy) {
            //Mueve la pala hacia arriba
            movePlayer(mCPU, mCPU.bounds.left, mCPU.bounds.top-VELOCIDAD_PALA);
        } else if (mCPU.bounds.top+mCPU.altoPala<mPelota.cy) {
            //Mueve la pala hacia abajo
            movePlayer(mCPU, mCPU.bounds.left, mCPU.bounds.top+VELOCIDAD_PALA);
        }
    }

    private boolean ballCollidedWithLeftWall() {
        return mPelota.cx<=mPelota.radio;
    }

    private boolean ballCollidedWithRightWall() {
        return mPelota.cx+mPelota.radio>=mCanvasAncho - 1;
    }

    private boolean ballCollidedWithTopOrBottomWall() {
        return mPelota.cy <= mPelota.radio
               || mPelota.cy + mPelota.radio >= mCanvasAlto - 1;
    }

    /**
     * Draws the puntuacion, paddles and the ball.
     */
    private void updateDisplay(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawRect(0, 0, mCanvasAncho, mCanvasAlto, mCanvasBoundsPaint);

        final int middle = mCanvasAncho / 2;
        canvas.drawLine(middle, 1, middle, mCanvasAlto - 1, mPintarLinea);

        setScoreText(mJugador.puntuacion + "    " + mCPU.puntuacion);

        handleHit(mJugador);
        handleHit(mCPU);

        canvas.drawRoundRect(mJugador.bounds, 5, 5, mJugador.pincel);
        canvas.drawRoundRect(mCPU.bounds, 5, 5, mCPU.pincel);
        canvas.drawCircle(mPelota.cx, mPelota.cy, mPelota.radio, mPelota.pincel);
    }

    private void handleHit(Jugador player) {
        if (player.colision > 0) {
            player.pincel.setShadowLayer(player.anchoPala / 2, 0, 0, player.pincel.getColor());
        } else {
            player.pincel.setShadowLayer(0, 0, 0, 0);
        }
    }

    /**
     * Reset players and ball position for a new round.
     */
    private void setupNewRound() {
        mPelota.cx = mCanvasAncho / 2;
        mPelota.cy = mCanvasAlto / 2;
        mPelota.dx = -VELOCIDAD_PELOTA;
        mPelota.dy = 0;

        movePlayer(mJugador,
                   2,
                   (mCanvasAlto - mJugador.altoPala) / 2);

        movePlayer(mCPU,
                   mCanvasAncho - mCPU.anchoPala - 2,
                   (mCanvasAlto - mCPU.altoPala) / 2);
    }

    private void setStatusText(String text) {
        Message msg = mStatusHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("text", text);
        b.putInt("vis", View.VISIBLE);
        msg.setData(b);
        mStatusHandler.sendMessage(msg);
    }

    private void hideStatusText() {
        Message msg = mStatusHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("vis", View.INVISIBLE);
        msg.setData(b);
        mStatusHandler.sendMessage(msg);
    }

    private void setScoreText(String text) {
        Message msg = mScoreHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("text", text);
        msg.setData(b);
        mScoreHandler.sendMessage(msg);
    }

    private void movePlayer(Jugador player, float left, float top) {
        if (left < 2) {
            left = 2;
        } else if (left + player.anchoPala >= mCanvasAncho - 2) {
            left = mCanvasAncho - player.anchoPala - 2;
        }
        if (top < 0) {
            top = 0;
        } else if (top + player.altoPala >= mCanvasAlto) {
            top = mCanvasAlto - player.altoPala - 1;
        }
        player.bounds.offsetTo(left, top);
    }

    private boolean collision(Jugador player, Pelota ball) {
        return player.bounds.intersects(
                ball.cx - mPelota.radio,
                ball.cy - mPelota.radio,
                ball.cx + mPelota.radio,
                ball.cy + mPelota.radio);
    }

    /**
     * Compute ball direction after colision with player paddle.
     */
    private void handleCollision(Jugador player, Pelota ball) {
        float relativeIntersectY = player.bounds.top + player.altoPala / 2 - ball.cy;
        float normalizedRelativeIntersectY = relativeIntersectY / (player.altoPala / 2);
        double bounceAngle = normalizedRelativeIntersectY * ANGULO;

        ball.dx = (float) (-Math.signum(ball.dx) * VELOCIDAD_PELOTA * Math.cos(bounceAngle));
        ball.dy = (float) (VELOCIDAD_PELOTA * -Math.sin(bounceAngle));

        if (player == mJugador) {
            mPelota.cx = mJugador.bounds.right + mPelota.radio;
        } else {
            mPelota.cx = mCPU.bounds.left - mPelota.radio;
        }
    }

}
