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

public class EditExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText expenseNameEditText = findViewById(R.id.editExpenseName);
        EditText expenseAmountEditText = findViewById(R.id.editExpenseAmount);

        expenseNameEditText.setText(getIntent().getStringExtra("name"));
        expenseAmountEditText.setText(getIntent().getStringExtra("amount"));

        Button editExpenseButton = findViewById(R.id.editExpenseButton);
        editExpenseButton.setOnClickListener(view -> {
            if (expenseNameEditText.getText().toString().isEmpty() || expenseAmountEditText.getText().toString().isEmpty()) return;
            Intent resultIntent = new Intent();
            resultIntent.putExtra("id", getIntent().getStringExtra("id"));
            resultIntent.putExtra("name", expenseNameEditText.getText().toString());
            resultIntent.putExtra("amount", expenseAmountEditText.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        Button cancelEditingExpenseButton = findViewById(R.id.cancelEditingExpenseButton);
        cancelEditingExpenseButton.setOnClickListener(view -> {
            finish();
        });
    }
}