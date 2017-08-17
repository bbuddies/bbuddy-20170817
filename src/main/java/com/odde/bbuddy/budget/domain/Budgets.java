package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Budgets {
    private final BudgetRepo budgetRepo;

    @Autowired
    public Budgets(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public void add(Budget budget) {
        budgetRepo.save(budget);
    }

    public List<Budget> getAll() {
        Budget budget = new Budget();
        budget.setMonth("2017-08");
        budget.setAmount(1000);
        return Arrays.asList(budget);
    }
}
