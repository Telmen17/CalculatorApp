package com.example.calculatorapp;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    // Material button elements
    MaterialButton buttonGoback, buttonStart;
    TextView textResult;

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
        textResult.setMovementMethod(new ScrollingMovementMethod());
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

        }
    }


}