package com.example.expenses_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expenses_tracker.entities.Expense;
import com.example.expenses_tracker.repositories.ExpenseRepository;
import com.example.expenses_tracker.utils.ExpenseListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ExpenseRepository expenseRepository;
    private List<Expense> expenses;

    private void calculateTotal(TextView totalTextView) {
        double total = 0;
        for (Expense expense : expenses) {
            if (expense.getAmount() != null) {
                total += expense.getAmount();
            }
        }
        totalTextView.setText(String.format("%s DH", total));
    }

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

        expenseRepository = new ExpenseRepository(this);
        expenses = expenseRepository.getAllExpenses();

        TextView totalTextView = findViewById(R.id.totalExpenses);
        calculateTotal(totalTextView);

        ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) return;
                    if (result.getData() == null) return;
                    Intent data = result.getData();
                    Expense newExpense = new Expense(data.getLongExtra("id", 0),data.getStringExtra("name"),Double.valueOf(data.getStringExtra("amount")));
                    expenseRepository.updateExpense(newExpense);
                    Expense currentExpense = expenses.stream().filter(expense -> expense.getId() == newExpense.getId()).findFirst().orElse(null);
                    if (currentExpense != null) {
                        currentExpense.setName(newExpense.getName());
                        currentExpense.setAmount(newExpense.getAmount());
                    }
//                    expenseListViewAdapter.notifyDataSetChanged();
                    calculateTotal(totalTextView);
                    Toast.makeText(this, "Edited " + data.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                }
        );

        ListView listView = findViewById(R.id.expensesList);
        ExpenseListViewAdapter expenseListViewAdapter = new ExpenseListViewAdapter(this, expenses, expenseRepository,editLauncher);
        listView.setDivider(null);
        listView.setAdapter(expenseListViewAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, expenses.get(i).getName() != null ? expenses.get(i).getName() : "unnamed", Toast.LENGTH_SHORT).show();
        });

        Button openExpenseFormButton = findViewById(R.id.openExpenseFormButton);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) return;
                    if (result.getData() == null) return;
                    Intent data = result.getData();
                    Expense newExpense = new Expense();
                    newExpense.setName(data.getStringExtra("name"));
                    newExpense.setAmount(Double.valueOf(data.getStringExtra("amount")));
                    expenseRepository.addExpense(newExpense);
                    expenses.add(newExpense);
                    expenseListViewAdapter.notifyDataSetChanged();
                    calculateTotal(totalTextView);
                    Toast.makeText(this, "Saved " + data.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                }
        );

        openExpenseFormButton.setOnClickListener(view -> {
            Intent i = new Intent(this, ExpenceForm.class);
            launcher.launch(i);
        });

    }
}