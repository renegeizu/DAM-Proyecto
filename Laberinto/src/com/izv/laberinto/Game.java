package com.izv.laberinto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Game extends Activity {
	Maze maze;
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		this.maze = (Maze)getLastNonConfigurationInstance();
		if(this.maze == null) {
			this.maze = (Maze)extras.get("maze");
		}
		GameView view = new GameView(this);
		view.setMaze(this.maze);
		setContentView(view);
	}
	public Object onRetainNonConfigurationInstance() {
		return this.maze;
	}
}
