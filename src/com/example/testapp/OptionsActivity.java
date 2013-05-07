package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OptionsActivity extends Activity {

	public final static String OPERATION = "com.example.testapp.OPERATION";
	public final static String LEVEL = "com.example.testapp.LEVEL";
	
	MediaPlayer mediaPlayer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        
        TextView textview = (TextView) findViewById(R.id.textView1);
        Typeface font = Typeface.createFromAsset(getAssets(), "DK Crayon Crumble.ttf");
        textview.setTypeface(font);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
   		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.smartie);
    	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
		mediaPlayer.start();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_options, menu);
        return true;
    }
    
    public void clickButton(View view) {
    	int buttonId = view.getId();
    	Button button = (Button) findViewById(buttonId);
    	String operation = button.getText().toString();
    	
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.putExtra(OPERATION, operation);
    	intent.putExtra(LEVEL, 2);
    	startActivity(intent);
    }    
}
