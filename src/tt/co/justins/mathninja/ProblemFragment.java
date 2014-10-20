package tt.co.justins.mathninja;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by Justin on 10/20/2014.
 */
public class ProblemFragment extends Fragment {

    public final static int ADDITION = 1;
    public final static int SUBTRACTION = 2;
    public final static int MULTIPLICATION = 3;
    public final static int DIVISION = 4;

    public int operand1;
    public int operand2;
    public int operation;
    public int solution;
    public char opChar;

    private TextView textview3;
    private TextView textview4;
    private TextView textview5;

    CommChannel comm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textview3 = (TextView) getActivity().findViewById(R.id.textView3);
        textview4 = (TextView) getActivity().findViewById(R.id.textView4);
        textview5 = (TextView) getActivity().findViewById(R.id.textView5);

        comm = (CommChannel) getActivity();
    }

    public interface CommChannel {
        public void newSolution(int soln);
        public void newProblem();
    }

    public void createNewProblem(int op, int level){
        createProblem(op, level);
        displayProblem();
        comm.newSolution(solution);
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

        if(textview3 == null || textview4 == null || textview5 == null) {
            Log.v("mathninja", "textviews are null");
            return;
        }

        textview3.setText("" + operand1);
        textview4.setText("" + operand2);
        textview5.setText("" + opChar);
    }

}