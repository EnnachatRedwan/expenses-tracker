package com.example.expenses_tracker.entities;

public class Expense {
    private Long id;
    private String name;
    private String descr;

    public Expense() {
    }

    public Expense(Long id, String name, String descr) {
        this.id = id;
        this.name = name;
        this.descr = descr;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
