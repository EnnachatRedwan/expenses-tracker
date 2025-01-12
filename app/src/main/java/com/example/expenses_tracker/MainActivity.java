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
        ListView listView = findViewById(R.id.expensesList);
        Button openExpenseFormButton = findViewById(R.id.openExpenseFormButton);

        calculateTotal(totalTextView);

        ExpenseListViewAdapter expenseListViewAdapter = new ExpenseListViewAdapter(this, expenses, expenseRepository);

        ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK || result.getData() == null) return;

                    Intent data = result.getData();
                    long id = data.getLongExtra("id", 0);
                    String name = data.getStringExtra("name");
                    double amount = Double.parseDouble(data.getStringExtra("amount"));

                    Expense newExpense = new Expense(id, name, amount);
                    expenseRepository.updateExpense(newExpense);

                    Expense currentExpense = expenses.stream()
                            .filter(expense -> expense.getId() == id)
                            .findFirst()
                            .orElse(null);

                    if (currentExpense != null) {
                        currentExpense.setName(name);
                        currentExpense.setAmount(amount);
                    }

                    expenseListViewAdapter.notifyDataSetChanged();
                    calculateTotal(totalTextView);

                    Toast.makeText(this, "Edited " + name, Toast.LENGTH_SHORT).show();
                }
        );

        expenseListViewAdapter.setEditExpenseLauncher(editLauncher);

        listView.setDivider(null);
        listView.setAdapter(expenseListViewAdapter);


        ActivityResultLauncher<Intent> createLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK || result.getData() == null) return;

                    Intent data = result.getData();
                    String name = data.getStringExtra("name");
                    double amount = Double.parseDouble(data.getStringExtra("amount"));

                    Expense newExpense = new Expense();
                    newExpense.setName(name);
                    newExpense.setAmount(amount);
                    Expense savedExpense = expenseRepository.addExpense(newExpense);

                    expenses.add(savedExpense);
                    expenseListViewAdapter.notifyDataSetChanged();
                    calculateTotal(totalTextView);

                    Toast.makeText(this, "Saved " + name, Toast.LENGTH_SHORT).show();
                }
        );

        openExpenseFormButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExpenceForm.class);
            createLauncher.launch(intent);
        });
    }

}