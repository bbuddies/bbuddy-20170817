package com.odde.bbuddy.budget.domain;

import java.time.LocalDate;
import java.time.Period;

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
        if (beginDate.isAfter(endDate))
            return 0;

        return Period.between(beginDate, endDate).getDays() + 1;
    }

}
