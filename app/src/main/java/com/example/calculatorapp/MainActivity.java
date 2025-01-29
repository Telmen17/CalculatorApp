/**
 * @author: Telmen Enkhtuvshin
 * The MainActivity class which controls the logical backend processes of the Calculator App.
 */
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

    /**
     * Text fields and buttons of the calculator app.
     */
    TextView resultTV, solutionTV;
    MaterialButton buttonC, button_open_bracket, button_closed_bracket;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonMul, buttonPlus, buttonSub, buttonDivide, buttonEquals;
    MaterialButton buttonAC, buttonDot;
    // New Added operator buttons
    MaterialButton button_open_curly, button_closed_curly, buttonExp, buttonSin, buttonCos,
    buttonTan, buttonCot, buttonLN, buttonLG;

    /**
     * Initializes the application window and assigns the
     * text and button elements to the field objects.
     * @param savedInstanceState The state of the display window.
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

        //Assigning the IDs of TextView elements to their objects.
        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);

        //Assigning the IDs of MaterialButton elements to their objects.
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
        // Additional operators
        assignID(button_open_curly, R.id.button_open_curly);
        assignID(button_closed_curly, R.id.button_closed_curly);
        assignID(buttonExp, R.id.button_exponent);
        assignID(buttonSin, R.id.button_sin);
        assignID(buttonCos, R.id.button_cos);
        assignID(buttonTan, R.id.button_tan);
        assignID(buttonCot, R.id.button_cot);
    }

    /**
     * A method to assign the button elements in the window to the field objects for initialization.
     * @param btn MaterialButton class button.
     * @param id Unique integer id of the button.
     */
    private void assignID(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    /**
     * Action listener onClick method that detects a button press on the display screen.
     * If an action is detected, the app reacts accordingly.
     * All of the user interactivity of inputs, calculations, and output display are handled here.
     * @param view View class object that is the whole screen of the app.
     */
    @Override
    public void onClick(View view) {
        //Declaring the variables to be used.
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTV.getText().toString();

        //If the "AC" button is pressed, the two text fields gets reset.
        if (buttonText.equals("AC")) {
            solutionTV.setText("");
            resultTV.setText("0");
            return;
        }
        //If the "=" button is pressed, the data is calculated and displayed on the
        //result text field.
        if (buttonText.equals("=")) {
            String finalResult = getResults(dataToCalculate);
            resultTV.setText(finalResult);
            return;
        }
        //If the "C" button is pressed, then the solution text field's last value is deleted.
        if (buttonText.equals("C")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else {
            //Else, the dataToCalculate is  concatenated with the pressed button text.
            dataToCalculate = dataToCalculate + buttonText;
        }

        //The solutionTV text field is updated with a new String value.
        solutionTV.setText(dataToCalculate);
    }

    /**
     * A helper method that calculates the given arithmetic string data.
     * @param data String type arithmetic data to be calculated.
     * @return Returns a String type number to be displayed on the user screen interface.
     */
    private String getResults(String data) {
        //The arithmetic data is calculated.
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            //String number is returned.
            return context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
        } catch (Exception e) {
            //If there is an exception, return the String "Error".
            return "Error";
        }
    }
}