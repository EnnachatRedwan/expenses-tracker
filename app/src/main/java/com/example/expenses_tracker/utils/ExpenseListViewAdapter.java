package com.example.expenses_tracker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expenses_tracker.R;
import com.example.expenses_tracker.entities.Expense;

import java.util.List;

public class ExpenseListViewAdapter extends BaseAdapter {

    private final List<Expense> expenses;
    private final Context context;

    public ExpenseListViewAdapter(Context context, List<Expense> expenses) {
        this.expenses = expenses;
        this.context = context;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.expense_item, viewGroup, false);
            holder = new ViewHolder();
            holder.txtName = view.findViewById(R.id.expenseItemName);
            holder.txtDescription = view.findViewById(R.id.expenseItemDescr);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Expense e = (Expense) getItem(i);
        holder.txtName.setText(e.getName() != null ? e.getName() : "unnamed");
        holder.txtDescription.setText(e.getDescr() != null ? e.getDescr() : "no decription");
        return view;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
    }
}
