package com.francois.tetris.activity;

import com.francois.tetris.R;
import com.francois.tetris.customView.TetrisView;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity 
{
	private TetrisView tetrisView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_main);
		this.tetrisView = (TetrisView) this.findViewById(R.id.tetrisGrid);
	}
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus) 
	{
		this.tetrisView.setGameFocus(hasFocus);
    	super.onWindowFocusChanged(hasFocus);
    }
}
