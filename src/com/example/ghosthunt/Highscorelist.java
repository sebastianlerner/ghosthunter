package com.example.ghosthunt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Highscorelist extends Activity{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscorelist);

		SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		int score = prefs.getInt("GH", -100);
		System.out.println("the score: " + score);
		TextView textView = (TextView) findViewById(R.id.textView1);
		if (score > -100)
			textView.setText("Your High Score is: " + score + ".");
		else
			textView.setText("Your High Score is: n/a.");

	}

}