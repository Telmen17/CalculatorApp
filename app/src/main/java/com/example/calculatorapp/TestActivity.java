/**
 * @author Telmen Enkhtuvshin
 * Backend activity class for Test Activity Page.
 */

package com.example.calculatorapp;

import static com.example.calculatorapp.MainActivity.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mariuszgromada.math.mxparser.Expression;

import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    // Material button elements
    MaterialButton buttonGoback, buttonStart;
    TextView textResult;
    EditText inputField;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Assign frontend elements to backend
        assignID(buttonGoback, R.id.button_goback);
        assignID(buttonStart, R.id.button_start);
        textResult = findViewById(R.id.text_result);
//        textResult.setMovementMethod(new ScrollingMovementMethod());
//        textResult.setVerticalScrollBarEnabled(true);

        inputField = findViewById(R.id.input_field);

        for (int i = 0; i < 20; i++) {
            String exp = generateExpression(3);
            Log.d("MyExp","The expression\n" + exp + "\n" + evaluateExpression(exp));
        }
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

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();

        if (buttonText.equals("Go Back")) {
            finish();
        } else if (buttonText.equals("Start")) {
            // Clearing input field
            textResult.setText("");
            // Getting input field value
            String inputText = inputField.getText().toString();
            int size = Integer.parseInt(inputText);

            compareTestCases(size);

        }
    }

    public void compareTestCases(int size) {
        final int MAX_TEST_SIZE = 100000;
        int oracleValidCount = 0;
        int oracleInvalidCount = 0;
        int calcValidCount = 0;
        int calcInvalidCount = 0;

        textResult.append("Example test cases:\n");

        // Generating expression
        for (int i = 0; i < MAX_TEST_SIZE; i++) {
            // Test case
            String testCase = generateExpression(size);
            // Display sample test case on screen
            if (i % 1000 == 0) {
                textResult.append(testCase + "\n");
            }
            // Oracle result
            double oracleResult = evaluateExpression(testCase);
            boolean isValid_calcResult = isValidExpression(testCase);

            // Both oracle case is invalid
            if (Double.isNaN(oracleResult)) {
                oracleInvalidCount++;
                // Calc case is invalid
                if (!isValid_calcResult) {
                    calcInvalidCount++;
                } else {
                    // Incrementing calcValidCount
                    calcValidCount++;
                }
            } else {
                // Oracle case is valid
                oracleValidCount++;
                // Calc case is invalid
                if (!isValid_calcResult) {
                    calcInvalidCount++;
                } else {
                    // Incrementing calcValidCount
                    double oracleValue = evaluateExpression(testCase);
                    double calcValue = evaluatePostfix(infixToPostfix(tokenize(testCase)));
                    double diff = Math.abs(oracleValue - calcValue);
                    // Testing for valid case
                    if (diff <= 0.01) {
                        // Calculator case is valid
                        calcValidCount++;
                    }
                }
            }
        }
        // Displaying results in the screen
        textResult.append("\nTested a total of 100,000 cases of size: " + size + "\n");
        // Logcat info
        Log.d("MyResults", "Valid Oracle Cases: " + oracleValidCount + "\n" +
                "Invalid Oracle Cases: " + oracleInvalidCount + "\n" +
                "Valid Calculator Cases: " + calcValidCount + "\n" +
                "Invalid Calculator Cases: " + calcInvalidCount + "\n"
        );
        // Result in the result text field
        textResult.append("\nValid Oracle Cases: " + oracleValidCount + "\n" +
                        "Invalid Oracle Cases: " + oracleInvalidCount + "\n" +
                        "Valid Calculator Cases: " + calcValidCount + "\n" +
                        "Invalid Calculator Cases: " + calcInvalidCount + "\n\n\n\n"
        );
//        textResult.post(() -> textResult.scrollTo(0, textResult.getBottom()));
    }

    /**
     * A method to calculate the expression with MxParser dependency.
     * @param expression String mathematical expression.
     * @return Returns a double data type result.
     */
    public static double evaluateExpression(String expression) {
        // Declare object
        Expression exp = new Expression(expression);
        // Return result
        return exp.calculate();
    }

private static final String[] OPERATORS = {"+", "-", "*", "/", "^"};
    private static final String[] FUNCTIONS = {"sin", "cos", "tan", "cot", "ln", "log10"};
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] BRACKETS = {'(', ')', '{', '}', '[', ']'};
    private static final char DECIMAL = '.';
    private static final Random RANDOM = new Random();

    public static String generateExpression(int length) {
        StringBuilder expression = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int choice = RANDOM.nextInt(6);

            if (choice == 0) { // Append a digit (0-9)
                expression.append(getRandomDigit());
            } else if (choice == 1) { // Append a decimal point
                expression.append(DECIMAL);
            } else if (choice == 2) { // Append an operator
                expression.append(getRandomOperator());
            } else if (choice == 3) { // Append a function
                expression.append(getRandomFunction());
            } else if (choice == 4) { // Append a closing bracket
                expression.append(getClosingBracket());
            } else if (choice == 5) { // Append an opening bracket
                expression.append(getOpeningBracket());
            }
        }

        // Return final result
        return expression.toString();
    }

    private static char getRandomDigit() {
        return DIGITS[RANDOM.nextInt(DIGITS.length)];
    }

    private static String getRandomOperator() {
        return OPERATORS[RANDOM.nextInt(OPERATORS.length)];
    }

    private static String getRandomFunction() {
        return FUNCTIONS[RANDOM.nextInt(FUNCTIONS.length)];
    }

    private static char getOpeningBracket() {
        return BRACKETS[RANDOM.nextInt(3) * 2]; // Pick '(', '{', or '['
    }

    private static char getClosingBracket() {
        return BRACKETS[RANDOM.nextInt(3) * 2 + 1]; // Pick ')', '}', or ']'
    }

}