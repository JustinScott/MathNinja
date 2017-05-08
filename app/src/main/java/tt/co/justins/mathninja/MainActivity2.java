package tt.co.justins.mathninja;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity2 extends Activity implements ProblemFragment.CommChannel{
    public static final String TAG = "MainActivity2";

    public final static int ADDITION = 1;
    public final static int SUBTRACTION = 2;
    public final static int MULTIPLICATION = 3;
    public final static int DIVISION = 4;

    private int operation;
    private int level;

    private ProblemFragment problemFragment;
    private ButtonsFragment buttonFragment;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        buttonFragment = (ButtonsFragment) getFragmentManager().findFragmentById(R.id.answer_fragment);
        problemFragment = (ProblemFragment) getFragmentManager().findFragmentById(R.id.problem_fragment);

        //get the name of the button the user clicked on the previous screen
        Intent intent = getIntent();
        String message = intent.getStringExtra(OptionsActivity.OPERATION);

        switch (message) {
            case "Subtraction":
                operation = SUBTRACTION;
                break;
            case "Multiplication":
                operation = MULTIPLICATION;
                break;
            case "Division":
                operation = DIVISION;
                break;
            default:
                operation = ADDITION;
                break;
        }

        //2 is the default value if a level isnt saved in prefs
        level = getSavedDifficultyLevel(2);
        problemFragment.updateLevelText(level);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume: activity on resume called");
        newProblem();

        // start background music
        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ninja);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop background music
        mediaPlayer.release();
    }

    @Override
    public void newSolution(int soln) {
        buttonFragment.updateButtons(soln);
    }

    @Override
    public void newProblem() {
        problemFragment.createNewProblem(operation, level);
    }

    @Override
    public void updateCounter(int answer) {
        problemFragment.updateCounter(answer);
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
                problemFragment.updateLevelText(level);
                problemFragment.createNewProblem(operation, level);
                return true;
            case R.id.level2:
                setSavedDifficultyLevel(2);
                level = 2;
                problemFragment.updateLevelText(level);
                problemFragment.createNewProblem(operation, level);
                return true;
            case R.id.level3:
                setSavedDifficultyLevel(3);
                level = 3;
                problemFragment.updateLevelText(level);
                problemFragment.createNewProblem(operation, level);
                return true;
            case R.id.reset:
                problemFragment.resetScore();
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
        editor.apply();
    }
}