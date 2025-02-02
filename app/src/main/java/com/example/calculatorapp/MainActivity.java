/**
 * @author: Telmen Enkhtuvshin
 * The MainActivity class which controls the logical backend processes of the Calculator App.
 * Added more features for complex mathematical evaluation.
 */
package com.example.calculatorapp;

import java.util.*;
import java.util.regex.*;

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

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "(\\d+\\.\\d+|\\d+)|([+\\-*/^(){}])|(sin|cos|tan|cot|ln|log10)");

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
        assignID(buttonLN, R.id.button_ln);
        assignID(buttonLG, R.id.button_lg);
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
            // If valid, evaluate the expression
            if (isValidExpression(dataToCalculate)) {
                String finalResult = String.valueOf(evaluatePostfix(infixToPostfix(tokenize(dataToCalculate))));
                resultTV.setText(finalResult);
            // If it is not valid, show error text
            } else {

                resultTV.setText("Error");
            }
            // Final return
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
     * A function to tokenize the string expression for easy evaluation.
     * @param expr String type raw mathematical expression with no processing.
     * @return Returns a tokenized expression that is in infix.
     */
    public static List<String> tokenize(String expr) {

        // ArrayList of tokens
        List<String> tokens = new ArrayList<>();
        // A special matcher class type pattern used for tokenization.
        Matcher matcher = TOKEN_PATTERN.matcher(expr);

        // Assuming an operator before the first token
        boolean lastWasOperator = true;

        while (matcher.find()) {
            String token = matcher.group();

            if (token.matches("\\d+(\\.\\d+)?")) { // Number
                tokens.add(token);
                lastWasOperator = false;
            // Unary minus
            } else if (token.equals("-") && lastWasOperator) {
                // Temporary token for negative numbers
                tokens.add("NEG");
            } else {
                // Operators, parentheses, functions
                tokens.add(token);
                lastWasOperator = !token.matches("\\)");
            }
        }
        // Return the tokens as a Arraylist of Strings
        return tokens;
    }

    /**
     * A method to turn infix expression into a post fix expression as pre-processing.
     * @param tokens Arraylist of tokenized String values that are in infix expression.
     * @return Returns a final Arraylist of post fix expression as Strings.
     */
    public static List<String> infixToPostfix(List<String> tokens) {
        // Precedence Hashmap
        Map<String, Integer> precedence = Map.of(
                "^", 3,
                "*", 2, "/", 2,
                "+", 1, "-", 1
        );

        // Operators stack
        Stack<String> operators = new Stack<>();
        // Final postfix order output list
        List<String> output = new ArrayList<>();

        // Iterating over the tokens
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            // If token is a numeric value
            if (token.matches("\\d+(\\.\\d+)?")) {
                output.add(token);
            // If token is a negative number
            } else if (token.equals("NEG")) {
                // Convert -X to (0 X -)
                output.add("0");
                operators.push("-");
            // If token is a special operator
            } else if (token.matches("sin|cos|tan|cot|ln|log10")) {
                operators.push(token);
            // If token is a openign bracket
            } else if (token.equals("(") || token.equals("{")) {
                operators.push(token);
            // If token is a closing bracket
            } else if (token.equals(")") || token.equals("}")) {
                // Adding the operators withing the brackets
                while (!operators.isEmpty() && !operators.peek().equals("(") && !operators.peek().equals("{")) {
                    output.add(operators.pop());
                }
                operators.pop();
            // Else when tokens are operators
            } else {
                while (!operators.isEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            }
        }

        // Popping the stack until it is empty.
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        // Returning the final output Arraylist of Strings that are in postfix expression
        return output;
    }

    /**
     * A method to evaluate post fix expression with a stack to compute the final result.
     * @param tokens An ArrayList of strings that is in post fix and tokenized.
     * @return Returns the final number value as a double.
     */
    public static double evaluatePostfix(List<String> tokens) {
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            // If the token matches
            if (token.matches("\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("NEG")) {
                // Apply unary minus
                stack.push(-stack.pop());
            // Evaluating special operators
            } else if (token.matches("sin|cos|tan|cot|ln|log10")) {
                double num = stack.pop();
                switch (token) {
                    case "sin" -> stack.push(Math.sin(num));
                    case "cos" -> stack.push(Math.cos(num));
                    case "tan" -> stack.push(Math.tan(num));
                    case "cot" -> stack.push(1 / Math.tan(num));
                    case "ln" -> stack.push(Math.log(num));
                    case "log10" -> stack.push(Math.log10(num));
                }
            // Evaluating regular operators
            } else {
                double b = stack.pop(), a = stack.pop();
                switch (token) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> stack.push(a / b);
                    case "^" -> stack.push(Math.pow(a, b));
                }
            }
        }
        // Return the final value from the stack
        return stack.pop();
    }

    /**
     * A method to validate if the given mathematical expression has correct syntax.
     * @param expression The arithmetic expression to validate.
     * @return Returns true if the expression is valid, otherwise false.
     */
    public static boolean isValidExpression(String expression) {
        // Check for balanced parentheses and curly brackets
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> brackets = Map.of('(', ')', '{', '}');

        for (char ch : expression.toCharArray()) {
            if (brackets.containsKey(ch)) {
                stack.push(ch);
            } else if (brackets.containsValue(ch)) {
                if (stack.isEmpty() || brackets.get(stack.pop()) != ch) {
                    return false; // Unbalanced brackets
                }
            }
        }
        if (!stack.isEmpty()) return false; // Leftover open brackets

        // Allow unary minus cases like "+-", "*-", "/-", "^-", "( -5)"
        if (expression.matches(".*[+*/^]-.*") || expression.matches(".*\\(-.*")) {
            // Unary minus after an operator or within parentheses is allowed
        } else if (expression.matches(".*[+\\-*/^]{2,}.*")) {
            return false; // Other consecutive operators (like `++`, `**`, `//`) are invalid
        }

        // Ensure expression does not start or end with an invalid operator (except unary `-`)
        if (expression.matches("^[+*/^].*") || expression.matches(".*[+*/^]$")) return false;

        // Check for division by zero (excluding decimals like 0.5)
        if (expression.matches(".*\\/\\s*0([^.]|$).*")) return false;

        // Check if expression contains only valid characters
        String cleanedExpr = expression.replaceAll("sin|cos|tan|cot|ln|log10", ""); // Remove function names
        if (!cleanedExpr.matches("[0-9+\\-*/^().{},\\s]+")) return false;

        return true;
    }
}