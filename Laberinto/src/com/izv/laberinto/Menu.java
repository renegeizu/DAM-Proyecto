package com.izv.laberinto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bNewGame = (Button)findViewById(R.id.bNew);
        Button bExit = (Button)findViewById(R.id.bExit);
        bNewGame.setOnClickListener(this);
        bExit.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.bExit:
				finish();
				break;
			case R.id.bNew:
				String[] levels = {"Maze 1", "Maze 2", "Maze 3"};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.levelSelect));
				builder.setItems(levels, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	Intent game = new Intent(Menu.this,Game.class);
						Maze maze = MazeCreator.getMaze(item+1);
						game.putExtra("maze", maze);
						startActivity(game);
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				break;
		}
	}
}