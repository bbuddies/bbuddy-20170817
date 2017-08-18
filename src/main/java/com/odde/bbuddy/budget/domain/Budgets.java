package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Budgets {
    private final BudgetRepo budgetRepo;

    @Autowired
    public Budgets(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public void add(Budget budget) {
        Budget queryBudget = budgetRepo.findByMonth(budget.getMonth());
        if (Objects.isNull(queryBudget)) {
            budgetRepo.save(budget);
            return;
        }

        queryBudget.setAmount(budget.getAmount());
        budgetRepo.save(queryBudget);
    }

    public boolean validation(Budget budget){
        String eL = "[0-9]{4}-[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(budget.getMonth());
        return m.matches();
    }

    public List<Budget> getAll() {
        return budgetRepo.findAll();
    }

}
