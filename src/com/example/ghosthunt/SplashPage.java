package com.example.ghosthunt;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SplashPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_page);
		final MediaPlayer btn1Sound = MediaPlayer.create(SplashPage.this,R.raw.beaware);
		final MediaPlayer btn2Sound = MediaPlayer.create(SplashPage.this, R.raw.ghosts);
		final MediaPlayer btn3Sound = MediaPlayer.create(SplashPage.this, R.raw.ghostappear);
		
		
		ImageButton btn1 = (ImageButton) findViewById(R.id.btn_start);
		btn1.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			btn1Sound.start();
			Intent i = new Intent(SplashPage.this, MainActivity.class);
			SplashPage.this.startActivity(i);
			}
		});

		ImageButton btn2 = (ImageButton) findViewById(R.id.btn_instr);
		btn2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				btn2Sound.start();
				Intent i = new Intent(SplashPage.this, Instructions.class);
				SplashPage.this.startActivity(i);
			}
		});

		ImageButton btn3 = (ImageButton) findViewById(R.id.btn_highsc);
		btn3.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				btn3Sound.start();
				Intent i = new Intent(SplashPage.this, Highscorelist.class);
				SplashPage.this.startActivity(i);
			}
		});

		ImageButton btn4 = (ImageButton) findViewById(R.id.btn_setting);
		btn4.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				btn2Sound.start();
				Intent i = new Intent(SplashPage.this, Settings.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}