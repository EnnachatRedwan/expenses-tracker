package com.example.expenses_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<String> expenses = new ArrayList<>(
            Arrays.asList(
                    "10Dh Lunch",
                    "20Dh Public transportation",
                    "5Dh Internet",
                    "50Dh Fuel",
                    "30Dh Groceries"
            )
    );

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

        ListView listView = findViewById(R.id.expensesList);
        ArrayAdapter<String> expensesAdapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                expenses
        );
        listView.setAdapter(expensesAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, expenses.get(i), Toast.LENGTH_SHORT).show();
        });

        Button openExpenseFormButton = findViewById(R.id.openExpenseFormButton);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) return;
                    if (result.getData() == null) return;
                    Intent data = result.getData();
                    expenses.add(data.getStringExtra("name"));
                    expensesAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Saved " + data.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                }
        );

        openExpenseFormButton.setOnClickListener(view -> {
            Intent i = new Intent(this, ExpenceForm.class);
            launcher.launch(i);
        });

    }
}