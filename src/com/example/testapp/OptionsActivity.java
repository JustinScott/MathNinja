package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class OptionsActivity extends Activity {

    public boolean FIRSTRUN = true;

	public final static String OPERATION = "com.example.testapp.OPERATION";
	public final static String LEVEL = "com.example.testapp.LEVEL";
	
	MediaPlayer mediaPlayer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        
        //TextView textview = (TextView) findViewById(R.id.textView1);
        //Typeface font = Typeface.createFromAsset(getAssets(), "DK Crayon Crumble.ttf");
        //textview.setTypeface(font);
    }
    
    @Override
    public void onStart() {
    	super.onStart();

        if(FIRSTRUN) {
            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.smartie);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();

            Button button1 = (Button) findViewById(R.id.button1);
            Button button2 = (Button) findViewById(R.id.button2);
            Button button3 = (Button) findViewById(R.id.button3);
            Button button4 = (Button) findViewById(R.id.button4);

            TranslateAnimation lrAnimation = new TranslateAnimation(-500f, 0f, 0f, 0f);
            TranslateAnimation rlAnimation = new TranslateAnimation(500f, 0f, 0f, 0f);
            AlphaAnimation aAnimation = new AlphaAnimation(0.0f, 1.0f);

            AnimationSet animationSet1 = new AnimationSet(true);
            animationSet1.setDuration(3000);
            animationSet1.addAnimation(aAnimation);
            animationSet1.addAnimation(lrAnimation);

            AnimationSet animationSet2 = new AnimationSet(false);
            animationSet2.setDuration(3000);
            animationSet2.addAnimation(aAnimation);
            animationSet2.addAnimation(rlAnimation);

            button1.setAnimation(animationSet1);
            button1.setVisibility(View.VISIBLE);

            button2.setAnimation(animationSet2);
            button2.setVisibility(View.VISIBLE);

            button3.setAnimation(animationSet1);
            button3.setVisibility(View.VISIBLE);

            button4.setAnimation(animationSet2);
            button4.setVisibility(View.VISIBLE);

            FIRSTRUN = false;
        }
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
