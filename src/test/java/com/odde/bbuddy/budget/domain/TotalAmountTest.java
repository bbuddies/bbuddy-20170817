package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TotalAmountTest {

    BudgetRepo stubBudgetRepo = mock(BudgetRepo.class);
    Budgets budgets = new Budgets(stubBudgetRepo);

    @Test
    public void no_budgets() throws ParseException {
        givenExistingBudgets();

        assertThat(budgets.calculate("2017-05-10", "2017-06-10")).isEqualTo(0);
    }

    @Test
    public void one_month_budget() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-05-01", "2017-05-31")).isEqualTo(310);
    }
    
    @Test
    public void end_date_earlier_than_one_month_first_day() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-04-10", "2017-04-30")).isEqualTo(0);
    }

    @Test
    public void start_date_later_than_one_month_last_day() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-06-01", "2017-06-15")).isEqualTo(0);
    }
    
    @Test
    public void start_and_end_date_are_same_and_is_one_month_first_day() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-05-01", "2017-05-01")).isEqualTo(10);
    }
    
    @Test
    public void start_and_end_date_are_in_one_month() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-05-05", "2017-05-10")).isEqualTo(60);
    }
    
    @Test
    public void start_before_one_month_first_day() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-04-29", "2017-05-10")).isEqualTo(100);
    }
    
    @Test
    public void end_after_one_month_last_day() throws ParseException {
        givenExistingBudgets(budget("2017-05", 310));

        assertThat(budgets.calculate("2017-05-20", "2017-06-10")).isEqualTo(120);
    }
    
    @Test
    public void two_month_overlapping() throws ParseException {
        givenExistingBudgets(
                budget("2017-05", 310),
                budget("2017-06", 300));

        assertThat(budgets.calculate("2017-05-20", "2017-06-10")).isEqualTo(220);
    }
    
    @Test
    public void two_months_across_year() throws ParseException {
        givenExistingBudgets(
                budget("2016-12", 310),
                budget("2017-01", 310));

        assertThat(budgets.calculate("2016-12-20", "2017-01-03")).isEqualTo(150);
    }
    
    @Test
    public void some_month_have_no_budget() throws ParseException {
        givenExistingBudgets(
                budget("2016-10", 310),
                budget("2016-12", 310),
                budget("2017-01", 310));

        assertThat(budgets.calculate("2016-09-15", "2017-01-10")).isEqualTo(720);
    }
    
    @Test
    public void same_month_but_not_same_year() throws ParseException {
        givenExistingBudgets(
                budget("2016-07", 310),
                budget("2017-07", 310));

        assertThat(budgets.calculate("2016-07-20", "2017-07-03")).isEqualTo(150);
    }
    
    @Test
    public void begin_date_month_day_is_earlier_than_end_date_month_day() throws ParseException {
        givenExistingBudgets(
                budget("2016-11", 300),
                budget("2016-12", 310),
                budget("2017-01", 310));

        assertThat(budgets.calculate("2016-11-15", "2017-01-16")).isEqualTo(630);
    }

    private Budget budget(String month, int amount) {
        Budget budget = new Budget();
        budget.setMonth(month);
        budget.setAmount(amount);
        return budget;
    }

    private void givenExistingBudgets(Budget... budgets) {
        when(stubBudgetRepo.findAll()).thenReturn(Arrays.asList(budgets));
    }

}
