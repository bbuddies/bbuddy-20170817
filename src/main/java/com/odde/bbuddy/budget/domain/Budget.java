package com.odde.bbuddy.budget.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue
    private long id;

    private String month;
    private int amount;

    private LocalDate firstDayOfBudgetMonth() {
        return LocalDate.parse(month + "-01");
    }

    private double dailyAmount() {
        return 1.0d * amount / firstDayOfBudgetMonth().lengthOfMonth();
    }

    private LocalDate lastDayOfMonth() {
        return firstDayOfBudgetMonth().withDayOfMonth(firstDayOfBudgetMonth().lengthOfMonth());
    }

    public double getOverlappingAmount(QueryPeriod queryPeriod) {
        LocalDate endOfOverlapping = queryPeriod.getEndDate().isBefore(lastDayOfMonth())? queryPeriod.getEndDate() : lastDayOfMonth();
        LocalDate beginOfOverlapping = queryPeriod.getBeginDate().isAfter(firstDayOfBudgetMonth()) ? queryPeriod.getBeginDate() : firstDayOfBudgetMonth();
        QueryPeriod overlappingPeriod = new QueryPeriod(beginOfOverlapping, endOfOverlapping);

        return overlappingPeriod.dayCount() * dailyAmount();
    }
}
