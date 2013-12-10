package com.me.mygdxgame;

import java.io.IOException;

import m4ges.controllers.MyGame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		Thread thread = new Thread(new Runnable(){
//			@Override
//			public void run() {
//				AndroidHelp ah = new AndroidHelp(getBaseContext());
//				try {
//					ah.test();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		thread.start();
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;

		initialize(new MyGame(new AndroidHelp(this)), cfg);
	}
}