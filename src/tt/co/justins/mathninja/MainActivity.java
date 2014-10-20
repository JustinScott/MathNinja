package tt.co.justins.mathninja;

import java.util.Arrays;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


	public final static int ADDITION = 1;
	public final static int SUBTRACTION = 2;
	public final static int MULTIPLICATION = 3;
	public final static int DIVISION = 4;
	
	public final static int INCORRECT = 0;
	public final static int CORRECT = 1;
		
	public int operand1;
	public int operand2;
	public int operation;
	public int solution;
	public char opChar;
	
	public int level;
	
	public int correct_count;
	public int incorrect_count;
	
	public MediaPlayer mediaPlayer;
	public Toast toast;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //initilize counts
        correct_count = 0;
        incorrect_count = 0;
        
        //set the font for text views
        TextView textview1 = (TextView) findViewById(R.id.textView1);
        TextView textview2 = (TextView) findViewById(R.id.textView2);
        TextView textview3 = (TextView) findViewById(R.id.textView3);
        TextView textview4 = (TextView) findViewById(R.id.textView4);

        Typeface font = Typeface.createFromAsset(getAssets(), "DK Crayon Crumble.ttf");
        
        textview1.setTypeface(font);
        textview2.setTypeface(font);
        textview3.setTypeface(font);
        textview4.setTypeface(font);
        
        //get the name of the button the user clicked on the previous screen
        Intent intent = getIntent();
        String message = intent.getStringExtra(OptionsActivity.OPERATION);
                
        if(message.equals("Addition"))
        	operation = ADDITION;
        if(message.equals("Subtraction"))
        	operation = SUBTRACTION;
        if(message.equals("Multiplication"))
        	operation = MULTIPLICATION;
        if(message.equals("Division"))
        	operation = DIVISION;
        
        //2 is the default value if a level isnt saved in prefs
        level = getSavedDifficultyLevel(2);
        
        updateDisplay();
    }

    @Override
    public void onResume() {
    	super.onResume();
   		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ninja);
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
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
	    	case R.id.level1:
	    		setSavedDifficultyLevel(1);
	    		level = 1;
	    		updateDisplay();
	    		return true;
	    	case R.id.level2:
	    		setSavedDifficultyLevel(2);
	    		level = 2;
	    		updateDisplay();
	    		return true;
	    	case R.id.level3:
	    		setSavedDifficultyLevel(3);
	    		level = 3;
	    		updateDisplay();
	    		return true;	    		
	    	case R.id.reset:
	    		resetScore();
	    		return true;	    		
			default:
				return super.onOptionsItemSelected(item);
    	}
    }
    
    //reads the saved difficulty level from the activity's saved preferences
    //lvl is the default value
    public int getSavedDifficultyLevel(int lvl) {
    	SharedPreferences sPref = getPreferences(MODE_PRIVATE);
    	return sPref.getInt("level", lvl);
    }
    
    public void setSavedDifficultyLevel(int lvl) {
    	SharedPreferences sPref = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor editor = sPref.edit();
    	editor.putInt("level", lvl);
    	editor.commit();
    }
    
    @TargetApi(12)
	public void clickButton(View view) {
    	int buttonId = view.getId();
    	Button button = (Button) findViewById(buttonId);

    	String button_text = button.getText().toString();
    	int button_value = Integer.parseInt(button_text);
    	
    	view.animate().rotationYBy(360f);
    	
    	int answer = INCORRECT;
    	if(solution == button_value)
    		answer = CORRECT;

    	playAnswerSound(answer);
        updateCounter(answer);
        updateDisplay();
		showAnswerToast(answer);
    }

    public void updateCounter(int answer) {
        if(answer == CORRECT)
            correct_count++;
        else
            incorrect_count++;
    }

    public void playAnswerSound(int answer) {
    	MediaPlayer mediaPlayer;
    	
    	if(answer == CORRECT) { //correct
    		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fast_woosh);
    	} else {
    		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.boing);
    	}
    	
    	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
		mediaPlayer.start();
    }    
	
	public void createProblem(int op, int level) {
		int max , min;
		
		//use the value of level to determine max / min
		if((op == ADDITION) || (op == SUBTRACTION)) {
			min = 1;
		
			//level determines the number of digits
			char[] numarray = new char[level];
			Arrays.fill(numarray, '9');
			String numstring = new String(numarray);
			max = Integer.parseInt(numstring);
			
			//create operands						
			operand1 = min + (int)(Math.random() * ((max - min) + min)); 
			if(op == ADDITION) {
				//make sure the length of operand 2 isn't bigger than operand 1
				do {
					operand2 = min + (int)(Math.random() * ((max - min) + min));
				} while (String.valueOf(operand2).length() > String.valueOf(operand1).length());
			}
			else {
				//make sure the value of operand2 isn't bigger than op1
				do {
					operand2 = min + (int)(Math.random() * ((max - min) + min));
				} while (operand2 > operand1);
			}
		}
		
		if(op == MULTIPLICATION) {
			min = 1;
			max = 9;
			int max2 = 99;
			
			//level 1 = 1 digit
			if(level == 1)
				operand1 = min + (int)(Math.random() * ((max - min) + min));
			else 
				operand1 = min + (int)(Math.random() * ((max2 - min) + min));
			
			operand2 = min + (int)(Math.random() * ((max - min) + min));
			
		}
		 
		if(op == DIVISION) {
			min = 1;
			max = 9;
					
			operand2 = min + (int)(Math.random() * ((max - min) + min));
			operand1 = (min + (int)(Math.random() * ((max - min) + min))) * operand2;
			
		}
		
		//select operation symbol
		if(op == ADDITION) {
			opChar = '+';
			solution = operand1 + operand2;
		}
		if(op == SUBTRACTION) {
			opChar = '-';
			solution = operand1 - operand2;
		}
		if(op == MULTIPLICATION) {
			opChar = 'x';
			solution = operand1 * operand2;
		}
		if(op == DIVISION) {
			opChar = '/';
			solution = operand1 / operand2;
		}
	}
	
	public void displayProblem() {
		TextView textview3 = (TextView) findViewById(R.id.textView3);
        TextView textview4 = (TextView) findViewById(R.id.textView4);
        TextView textview5 = (TextView) findViewById(R.id.textView5);
        
        textview3.setText("" + operand1);
        textview4.setText("" + operand2);
        textview5.setText("" + opChar);
	}
	
	public void updateButtons() {
		int min = Math.max(1, (solution - 20));
		int max = solution + 20;
		
		int randomButtonSelector = 1 + (int)(Math.random() * ((4 - 1) + 1));
		
		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button button3 = (Button) findViewById(R.id.button3);
		Button button4 = (Button) findViewById(R.id.button4);
		
		int value1, value2, value3, value4;
		
		if(randomButtonSelector == 1) {
			value1 = solution;
		}
		else {
			do {
				value1 = (min + (int)(Math.random() * ((max - min) + 1)));
			} while (value1 == solution);
		}
		button1.setText("" + value1);
		
		if(randomButtonSelector == 2) {
			value2 = solution;
		}
		else {
			do {
				value2 = (min + (int)(Math.random() * ((max - min) + 1)));
			} while ((value2 == solution) || (value2 == value1));
		}
		button2.setText("" + value2);
		
		if(randomButtonSelector == 3) {
			value3 = solution;
		}
		else {
			do {
				value3 = (min + (int)(Math.random() * ((max - min) + 1)));
			} while ((value3 == solution) || (value3 == value1) || (value3 == value2));
		}
		button3.setText("" + value3);
		
		if(randomButtonSelector == 4) {
			value4 = solution;
		}
		else {
			do {
				value4 = (min + (int)(Math.random() * ((max - min) + 1)));
			} while ((value4 == value1) || (value4 == value2) || (value4 == value3));
		}
		button4.setText("" + value4);
	}
	
	public void updateScoreText() {
		TextView text1 = (TextView) findViewById(R.id.textView1);
		text1.setText("Score: " + (correct_count - incorrect_count));
	}
	
	public void updateLevelText() {
		TextView text2 = (TextView) findViewById(R.id.textView2);
		text2.setText("Level:" + level);
	}
	
	public void resetScore() {
		correct_count = 0;
		incorrect_count = 0;
		updateScoreText();
	}
	
	public void updateDisplay() {
		createProblem(operation, level);
		displayProblem();
		updateButtons();
		updateScoreText();
		updateLevelText();
	}
	
	public void showAnswerToast(int flag) {
//		LayoutInflater inflater = getLayoutInflater();
//		View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
//
//		ImageView image = (ImageView) layout.findViewById(R.id.image);
//		TextView text = (TextView) layout.findViewById(R.id.text);
//		
		int resId;
		if(flag == INCORRECT) {
//			image.setImageResource(R.drawable.incorrect);
//			text.setText("Wrong answer!");
			resId = R.string.wrong;
		}
		else {
//			image.setImageResource(R.drawable.correct);
//			text.setText("Right answer!");
			resId = R.string.right;
		}
		
		//toast.cancel();
        if(getApplicationContext() != null) {
            toast = Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
    //		toast.setDuration(Toast.LENGTH_SHORT);
    //		toast.setView(layout);
            toast.show();
        }
	}
}

//jebus!!!!