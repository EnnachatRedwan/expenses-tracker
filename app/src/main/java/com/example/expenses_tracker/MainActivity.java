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

import com.example.expenses_tracker.entities.Expense;
import com.example.expenses_tracker.utils.ExpenseListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Expense> expenses = new ArrayList<>(
            Arrays.asList(
                    new Expense(1L,"10Dh Lunch","was a delicious luncg"),
                    new Expense(2L,"20Dh Public transportation",null),
                    new Expense(3L,null,null),
                    new Expense(4L,"50Dh Fuel","some descr here!"),
                    new Expense(1L,"30Dh Groceries","some descr here too!")
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
        ExpenseListViewAdapter expenseListViewAdapter = new ExpenseListViewAdapter(this,expenses);
        listView.setAdapter(expenseListViewAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, expenses.get(i).getName()!=null?expenses.get(i).getName():"unnamed", Toast.LENGTH_SHORT).show();
        });

        Button openExpenseFormButton = findViewById(R.id.openExpenseFormButton);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) return;
                    if (result.getData() == null) return;
                    Intent data = result.getData();
//                    expenses.add(data.getStringExtra("name"));
//                    expensesAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Saved " + data.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                }
        );

        openExpenseFormButton.setOnClickListener(view -> {
            Intent i = new Intent(this, ExpenceForm.class);
            launcher.launch(i);
        });

    }
}