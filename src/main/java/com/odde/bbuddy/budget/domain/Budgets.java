package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
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

    public void add(Budget budget, Runnable afterAddFailed, Runnable validationFailed) {
        if (!validation(budget)) {
            validationFailed.run();
            return;
        }

        try {
            Budget queryBudget = budgetRepo.findByMonth(budget.getMonth());
            if (Objects.isNull(queryBudget)) {
                budgetRepo.save(budget);
                return;
            }
            queryBudget.setAmount(budget.getAmount());
            budgetRepo.save(queryBudget);
        } catch (DataAccessException e) {
            afterAddFailed.run();
        }
    }

    public boolean validation(Budget budget) {
        String eL = "[0-9]{4}-[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(budget.getMonth());
        return m.matches();
    }

    public List<Budget> getAll() {
        return budgetRepo.findAll();
    }

    public double calculate(String beginDateString, String endDateString) throws ParseException {
        LocalDate beginDate = LocalDate.parse(beginDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        return calculate(new QueryPeriod(beginDate, endDate));
    }

    private double calculate(QueryPeriod queryPeriod) throws ParseException {
        List<Budget> budgets = budgetRepo.findAll();
        double totalAmount = 0D;

        if (queryPeriod.getBeginDate().getYear() == queryPeriod.getEndDate().getYear() && queryPeriod.getBeginDate().getMonth() == queryPeriod.getEndDate().getMonth()) {
            return getAmount(budgets, queryPeriod);
        }

        //第一个月的金额
        totalAmount += getAmount(budgets, new QueryPeriod(queryPeriod.getBeginDate(), queryPeriod.getBeginDate().withDayOfMonth(queryPeriod.getBeginDate().lengthOfMonth())));

        //中间月的金额总和
        totalAmount += budgets.stream().filter(budget -> queryPeriod.contains(budget))
                .mapToDouble(budget -> budget.getAmount())
                .sum();

        // 最后一个月的金
        totalAmount += getAmount(budgets, new QueryPeriod(queryPeriod.getEndDate().withDayOfMonth(1), queryPeriod.getEndDate()));

        return totalAmount;
    }

    private double getAmount(List<Budget> budgets, QueryPeriod period) {
        return budgets.stream().filter(budget -> budget.containsDate(period.getBeginDate()))
                .mapToDouble(budget -> period.dayCount() * budget.dailyAmount())
                .sum();
    }

}
