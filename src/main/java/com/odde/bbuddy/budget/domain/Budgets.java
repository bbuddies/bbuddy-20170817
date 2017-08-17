package com.odde.bbuddy.budget.domain;

import org.springframework.stereotype.Component;

import static java.lang.System.out;

@Component
public class Budgets {
    public void add(Budget budget) {
        out.println("month: " + budget.getMonth());
        out.println("amount: " + budget.getAmount());
    }
}
