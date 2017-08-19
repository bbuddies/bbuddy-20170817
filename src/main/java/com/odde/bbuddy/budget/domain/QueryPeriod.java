package com.odde.bbuddy.budget.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class QueryPeriod {
    private final LocalDate beginDate;
    private final LocalDate endDate;

    public QueryPeriod(LocalDate beginDate, LocalDate endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int dayCount() {
        return Period.between(beginDate, endDate).getDays() + 1;
    }

    public List<LocalDate> getMonths() {
        return IntStream.range(1, monthCount())
                .mapToObj(i -> beginDate.plusMonths(i).withDayOfMonth(1))
                .collect(toList());
    }

    private int monthCount() {
        return Period.between(beginDate.withDayOfMonth(1), endDate.withDayOfMonth(1)).getMonths();
    }

    public boolean contains(Budget budget) {
        return getMonths().contains(budget.firstDayOfBudgetMonth());
    }
}
