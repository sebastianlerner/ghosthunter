package com.example.ghosthunt;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;




public class Settings extends Activity {
	private RadioGroup rg = null;
	private ImageButton bt = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final MediaPlayer btn3Sound = MediaPlayer.create(Settings.this, R.raw.ghostappear);
		
	    rg = (RadioGroup) findViewById(R.id.radioGroup1);
	    rg.clearCheck();
	    
	    bt = (ImageButton) findViewById(R.id.btnSave);
	    bt.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			btn3Sound.start();	
			Intent i = new Intent(Settings.this, SplashPage.class);
			startActivity(i);
			}
	    });
	} 
	
	 public void onClicked(View v) {
         boolean checked = ((RadioButton) v).isChecked();
	    		
         switch(v.getId()) {
         case R.id.radioE:
        	 if (checked)
        		 MainActivity.setspeed(0.0000010);
        	 break;
         case R.id.radioM:
        	 if (checked)
        		 MainActivity.setspeed(0.0000018);
        	break;
         case R.id.radioH:
        	 if (checked)
        		 MainActivity.setspeed(0.0000025);
        	break;
      
         }       

}
}