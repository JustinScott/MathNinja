package tt.co.justins.mathninja;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Justin on 10/20/2014.
 */
public class MainActivity2 extends Activity implements ProblemFragment.CommChannel{


    public final static int ADDITION = 1;
    public final static int SUBTRACTION = 2;
    public final static int MULTIPLICATION = 3;
    public final static int DIVISION = 4;

    private int operation;
    private int level;

    private ProblemFragment problemFragment;
    private ButtonsFragment buttonFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        buttonFragment = (ButtonsFragment) getFragmentManager().findFragmentById(R.id.answer_fragment);
        problemFragment = (ProblemFragment) getFragmentManager().findFragmentById(R.id.problem_fragment);

        //get the name of the button the user clicked on the previous screen
        Intent intent = getIntent();
        String message = intent.getStringExtra(OptionsActivity.OPERATION);

        if(message.equals("Subtraction"))
            operation = SUBTRACTION;
        else if(message.equals("Multiplication"))
            operation = MULTIPLICATION;
        else if(message.equals("Division"))
            operation = DIVISION;
        else
            operation = ADDITION;

        //2 is the default value if a level isnt saved in prefs
        //level = getSavedDifficultyLevel(2);
        level = 2;

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("mathninja", "activity on resume called");
        newProblem();
    }

    @Override
    public void newSolution(int soln) {
        buttonFragment.updateButtons(soln);
    }

    @Override
    public void newProblem() {
        problemFragment.createNewProblem(operation, level);
    }
}