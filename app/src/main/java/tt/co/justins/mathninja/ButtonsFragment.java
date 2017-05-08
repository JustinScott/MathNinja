package tt.co.justins.mathninja;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class ButtonsFragment extends Fragment implements View.OnClickListener{

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    public final static int INCORRECT = 0;
    public final static int CORRECT = 1;

    private int solution;

    ProblemFragment.CommChannel comm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buttons, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set onclicklistener to this fragment, it defaults to sending the click to the activity
        button1 = (Button) getActivity().findViewById(R.id.button1);
        button2 = (Button) getActivity().findViewById(R.id.button2);
        button3 = (Button) getActivity().findViewById(R.id.button3);
        button4 = (Button) getActivity().findViewById(R.id.button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        comm = (ProblemFragment.CommChannel) getActivity();
    }

    @Override
    public void onClick(View view) {
            Button button = (Button) view;

            String button_text = button.getText().toString();
            int button_value = Integer.parseInt(button_text);

            view.animate().rotationYBy(360f);

            int answer;
            if(solution == button_value)
                answer = CORRECT;
            else
                answer = INCORRECT;

            playAnswerSound(answer);
            comm.updateCounter(answer);
            showAnswerToast(answer);
            comm.newProblem();
    }

    public void updateButtons(int soln) {
        Log.v("mathninja", "updating buttons with new soln: " + soln);
        solution = soln;
        int min = Math.max(1, (solution - 20));
        int max = solution + 20;

        int randomButtonSelector = 1 + (int)(Math.random() * ((4 - 1) + 1));

        int value1, value2, value3, value4;

        if(randomButtonSelector == 1) {
            value1 = solution;
        }
        else {
            do {
                value1 = (min + (int)(Math.random() * ((max - min) + 1)));
            } while (value1 == solution);
        }
        button1.setText(String.format(Locale.ENGLISH, "%d", value1));

        if(randomButtonSelector == 2) {
            value2 = solution;
        }
        else {
            do {
                value2 = (min + (int)(Math.random() * ((max - min) + 1)));
            } while ((value2 == solution) || (value2 == value1));
        }
        button2.setText(String.format(Locale.ENGLISH, "%d", value2));

        if(randomButtonSelector == 3) {
            value3 = solution;
        }
        else {
            do {
                value3 = (min + (int)(Math.random() * ((max - min) + 1)));
            } while ((value3 == solution) || (value3 == value1) || (value3 == value2));
        }
        button3.setText(String.format(Locale.ENGLISH, "%d", value3));

        if(randomButtonSelector == 4) {
            value4 = solution;
        }
        else {
            do {
                value4 = (min + (int)(Math.random() * ((max - min) + 1)));
            } while ((value4 == value1) || (value4 == value2) || (value4 == value3));
        }
        button4.setText(String.format(Locale.ENGLISH, "%d", value4));
    }

    public void playAnswerSound(int answer) {
        MediaPlayer mediaPlayer;

        if(answer == CORRECT) { //correct
            mediaPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.fast_woosh);
        } else {
            mediaPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.boing);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        mediaPlayer.start();
    }

    public void showAnswerToast(int flag) {
        int resId;
        if(flag == INCORRECT) {
            resId = R.string.wrong;
        }
        else {
            resId = R.string.right;
        }

        if(getActivity().getApplicationContext() != null) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), resId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            //		toast.setView(layout);
            toast.show();
        }
    }
}