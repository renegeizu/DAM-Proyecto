package com.rngds.pong;

	import android.app.Activity;
	import android.os.Bundle;
	import android.view.Menu;
	import android.view.MenuItem;
	import android.widget.TextView;

public class Pong extends Activity {

    private static final int MENU_JUEGO_NUEVO = 1;
    private static final int MENU_PAUSA = 2;
    private static final int MENU_SALIR = 3;
    private PongThread hiloJuego;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong);
        final PongVista vistaPong = (PongVista) findViewById(R.id.main);
        vistaPong.setVistaStatus((TextView) findViewById(R.id.status));
        vistaPong.setVistaScore((TextView) findViewById(R.id.score));
        hiloJuego = vistaPong.getGameThread();
        if (savedInstanceState == null) {
            hiloJuego.setState(PongThread.ESTADO_LISTO);
        } else {
            hiloJuego.restoreState(savedInstanceState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hiloJuego.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hiloJuego.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_JUEGO_NUEVO, 0, R.string.menu_new_game);
        menu.add(0, MENU_PAUSA, 0, R.string.menu_resume);
        menu.add(0, MENU_SALIR, 0, R.string.menu_exit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_JUEGO_NUEVO:
                hiloJuego.startNewGame();
                return true;
            case MENU_SALIR:
                finish();
                return true;
            case MENU_PAUSA:
                hiloJuego.unPause();
                return true;
        }
        return false;
    }

}
