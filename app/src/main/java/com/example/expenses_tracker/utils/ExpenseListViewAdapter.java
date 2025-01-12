package com.example.expenses_tracker.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;

import com.example.expenses_tracker.EditExpense;
import com.example.expenses_tracker.R;
import com.example.expenses_tracker.entities.Expense;
import com.example.expenses_tracker.repositories.ExpenseRepository;

import java.util.List;

public class ExpenseListViewAdapter extends BaseAdapter {

    private final List<Expense> expenses;
    private final Context context;
    private final ExpenseRepository expenseRepository;

    private ActivityResultLauncher<Intent> editExpenseLauncher;

    public ExpenseListViewAdapter(Context context, List<Expense> expenses, ExpenseRepository expenseRepository) {
        this.expenses = expenses;
        this.context = context;
        this.expenseRepository = expenseRepository;
    }

    public void setEditExpenseLauncher(ActivityResultLauncher<Intent> editExpenseLauncher) {
        this.editExpenseLauncher = editExpenseLauncher;
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int i) {
        return expenses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return expenses.get(i).getId();
    }

    private void deleteExpense(int position) {
        Expense expense = expenses.get(position);
        expenses.remove(position);
        expenseRepository.deleteExpense(expense);
        notifyDataSetChanged();
    }

    private void updateExpense(Expense expense) {
        if (editExpenseLauncher == null) return;
        Intent i = new Intent(context, EditExpense.class);
        i.putExtra("id", expense.getId());
        i.putExtra("name", expense.getName());
        i.putExtra("amount", expense.getAmount().toString());
        editExpenseLauncher.launch(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.expense_item, viewGroup, false);
            holder = new ViewHolder();
            holder.txtName = view.findViewById(R.id.expenseItemName);
            holder.txtAmount = view.findViewById(R.id.expenseItemAmount);
            holder.deleteButton = view.findViewById(R.id.deleteExpenseItem);
            holder.editExpense = view.findViewById(R.id.editExpenseItem);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Expense e = (Expense) getItem(i);
        holder.txtName.setText(e.getName() != null ? e.getName() : "unnamed");
        holder.txtAmount.setText(e.getAmount() != null ? e.getAmount().toString() : "no price");
        holder.deleteButton.setOnClickListener(v -> deleteExpense(i));
        holder.editExpense.setOnClickListener(v -> updateExpense(e));
        return view;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtAmount;
        ImageButton deleteButton;
        ImageButton editExpense;
    }
}
