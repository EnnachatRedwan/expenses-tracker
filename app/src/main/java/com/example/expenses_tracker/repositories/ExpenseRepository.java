package com.example.expenses_tracker.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.expenses_tracker.entities.Expense;
import com.example.expenses_tracker.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ExpenseRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, expense.getName());
        values.put(DatabaseHelper.COLUMN_AMOUNT, expense.getAmount());
        return database.insert(DatabaseHelper.TABLE_EXPENSES, null, values);
    }

    public void deleteExpense(Expense expense) {
        long id = expense.getId();
        database.delete(DatabaseHelper.TABLE_EXPENSES, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public void updateExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, expense.getName());
        values.put(DatabaseHelper.COLUMN_AMOUNT, expense.getAmount());
        database.update(DatabaseHelper.TABLE_EXPENSES, values, DatabaseHelper.COLUMN_ID + " = " + expense.getId(), null);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXPENSES,
                null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Expense expense = cursorToExpense(cursor);
                expenses.add(expense);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return expenses;
    }

    private Expense cursorToExpense(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
        Double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
        return new Expense(id, name, amount);
    }
}
