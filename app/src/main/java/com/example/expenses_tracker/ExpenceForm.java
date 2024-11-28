package com.example.expenses_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExpenceForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expence_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText expenseNameEditText = findViewById(R.id.newExpenseName);

        Button addExpenseButton = findViewById(R.id.addExpenseButton);
        addExpenseButton.setOnClickListener(view -> {
            if (expenseNameEditText.getText().toString().isEmpty()) return;
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", expenseNameEditText.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        Button cancelAddingExpenseButton = findViewById(R.id.cancelAddingExpenseButton);
        cancelAddingExpenseButton.setOnClickListener(view -> {
            finish();
        });
    }
}