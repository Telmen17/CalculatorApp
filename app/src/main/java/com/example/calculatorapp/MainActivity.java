package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTV, solutionTV;
    MaterialButton buttonC, button_open_bracket, button_closed_bracket;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonMul, buttonPlus, buttonSub, buttonDivide, buttonEquals;
    MaterialButton buttonAC, buttonDot;
    /**
     * Initializes the application window
     * @param savedInstanceState The state of the window
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);

        assignID(buttonC, R.id.button_c);
        assignID(button_open_bracket, R.id.button_open_bracket);
        assignID(button_closed_bracket, R.id.button_closed_bracket);
        assignID(button0, R.id.button_0);
        assignID(button1, R.id.button_1);
        assignID(button2, R.id.button_2);
        assignID(button3, R.id.button_3);
        assignID(button4, R.id.button_4);
        assignID(button5, R.id.button_5);
        assignID(button6, R.id.button_6);
        assignID(button7, R.id.button_7);
        assignID(button8, R.id.button_8);
        assignID(button9, R.id.button_9);
        assignID(buttonMul, R.id.button_multiply);
        assignID(buttonPlus, R.id.button_plus);
        assignID(buttonSub, R.id.button_minus);
        assignID(buttonDivide, R.id.button_divide);
        assignID(buttonAC, R.id.button_ac);
        assignID(buttonDot, R.id.button_dot);
        assignID(buttonEquals, R.id.button_equal);
    }

    /**
     *
     * @param btn
     * @param id
     */
    void assignID(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTV.getText().toString();

        if (buttonText.equals("AC")) {
            solutionTV.setText("");
            resultTV.setText("0");
            return;
        }
        if (buttonText.equals("=")) {
            //solutionTV.setText(resultTV.getText());
            String finalResult = getResults(dataToCalculate);
            resultTV.setText(finalResult);
//            if (!finalResult.equals("Error")) {
//                resultTV.setText(finalResult);
//            } else {
//                resultTV.setText(finalResult);
//            }

            return;
        }
        if (buttonText.equals("C")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }

        solutionTV.setText(dataToCalculate);
    }

    String getResults(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            return context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
        } catch (Exception e) {
            return "Error";
        }
    }
}