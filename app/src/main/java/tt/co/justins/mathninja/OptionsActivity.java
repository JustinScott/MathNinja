package tt.co.justins.mathninja;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

public class OptionsActivity extends Activity {

    public boolean FIRSTRUN = true;

    public final static String TAG = "OptionsActivity";
	public final static String OPERATION = "com.example.testapp.OPERATION";
	public final static String LEVEL = "com.example.testapp.LEVEL";
	
	MediaPlayer mediaPlayer;
    private String operation;

    private Button button4;
    private Button button3;
    private Button button2;
    private Button button1;

    private AnimationSet animationSet1;
    private AnimationSet animationSet2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        Typeface font = Typeface.createFromAsset(getAssets(), "DK Crayon Crumble.ttf");

        button1.setTypeface(font);
        button2.setTypeface(font);
        button3.setTypeface(font);
        button4.setTypeface(font);
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

            introAnimation();

            FIRSTRUN = false;
        } else {
            if(animationSet2 != null && animationSet1 != null) {
                animationSet1.setFillAfter(false);
                animationSet2.setFillAfter(false);
            }
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
    	operation = button.getText().toString();
        outroAnimation(view);

        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.katana_gesture1);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }

    private void startMainActivity() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        int level = settings.getInt("level", 2);

        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra(OPERATION, operation);
        intent.putExtra(LEVEL, level);

        // reset score
        Log.d(TAG, "onCreate: resetting score");
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("score", 0);
        edit.apply();

        startActivity(intent);
    }

    private void introAnimation() {
        TranslateAnimation lrAnimation = new TranslateAnimation(-500f, 0f, 0f, 0f);
        TranslateAnimation rlAnimation = new TranslateAnimation(500f, 0f, 0f, 0f);
        AlphaAnimation aAnimation = new AlphaAnimation(0.0f, 0.6f);

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

    }

    private void outroAnimation(View view) {
        TranslateAnimation lrAnimation = new TranslateAnimation(0f, 500f, 0f, 0f);
        TranslateAnimation rlAnimation = new TranslateAnimation(0f, -500f, 0f, 0f);
        AlphaAnimation aAnimation = new AlphaAnimation(1.0f, 0.0f);

        animationSet1 = new AnimationSet(true);
        animationSet1.setDuration(2000);
        animationSet1.addAnimation(aAnimation);
        animationSet1.addAnimation(lrAnimation);
        animationSet1.setFillAfter(true);

        animationSet2 = new AnimationSet(false);
        animationSet2.setDuration(2000);
        animationSet2.addAnimation(aAnimation);
        animationSet2.addAnimation(rlAnimation);
        animationSet2.setFillAfter(true);
        animationSet2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        //button1.setAnimation(animationSet2);
        button1.startAnimation(animationSet2);

        button2.setAnimation(animationSet2);
        //button2.setVisibility(View.VISIBLE);

        button3.setAnimation(animationSet2);
        //button3.setVisibility(View.);

        button4.setAnimation(animationSet2);
        //button4.setVisibility(View.VISIBLE);

        view.setAnimation(animationSet1);
    }
}
