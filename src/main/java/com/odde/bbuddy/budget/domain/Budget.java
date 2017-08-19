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

    public LocalDate firstDayOfBudgetMonth() {
        return LocalDate.parse(month + "-01");
    }

    public double dailyAmount() {
        return 1.0d * amount / firstDayOfBudgetMonth().lengthOfMonth();
    }

    public boolean containsDate(LocalDate date) {
        return date.withDayOfMonth(1).equals(firstDayOfBudgetMonth());
    }
}
