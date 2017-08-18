package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BudgetsTest {

    private BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
    private Budgets budgets = new Budgets(mockBudgetRepo);

    @Test
    public void budgets_repeat_add_should_update_budget() {
        //given
        Budget budget = new Budget();
        budget.setMonth("2017-08");
        budget.setAmount(400);

        Budget existBudget = new Budget();
        existBudget.setId(11L);
        existBudget.setMonth("2017-08");
        existBudget.setAmount(1000);

        //when
        when(mockBudgetRepo.findByMonth(budget.getMonth())).thenReturn(existBudget);
        budgets.add(budget, () -> {
        }, () -> {
        });

        //then
        ArgumentCaptor<Budget> captor = ArgumentCaptor.forClass(Budget.class);
        verify(mockBudgetRepo).save(captor.capture());
        assertThat(400).isEqualTo(captor.getValue().getAmount());
        assertThat("2017-08").isEqualTo(captor.getValue().getMonth());
        assertThat(11L).isEqualTo(captor.getValue().getId());

    }

    @Test
    public void budgets_add_should_save_budget() {
        Budget budget = new Budget();
        budget.setMonth("2017-02");
        budget.setAmount(200);
        budgets.add(budget, () -> {
        }, () -> {
        });

        when(mockBudgetRepo.findByMonth(budget.getMonth())).thenReturn(null);

        ArgumentCaptor<Budget> captor = ArgumentCaptor.forClass(Budget.class);
        verify(mockBudgetRepo).save(captor.capture());
        assertThat("2017-02").isEqualTo(captor.getValue().getMonth());
        assertThat(200).isEqualTo(captor.getValue().getAmount());
        assertThat(0L).isEqualTo(captor.getValue().getId());

    }


    @Test
    public void budgets_add_should_validate_month() {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);

        Budget budget = new Budget();
        budget.setMonth("201705");
        budget.setAmount(200);
        boolean result = budgets.validation(budget);

        assertThat(result).isEqualTo(false);
    }

    @Test
    public void get_all_should_find_all_budgets() {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        List<Budget> allBudgets = Arrays.asList(new Budget());
        when(mockBudgetRepo.findAll()).thenReturn(allBudgets);

        assertThat(budgets.getAll()).isEqualTo(allBudgets);
    }

    @Test
    public void total_months_test() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        double amount = budgets.calculate("2017-06-05", "2017-07-10");
        assertThat(amount).isEqualTo(620);
    }

    @Test
    @Ignore
    public void calculate_amount_between_begin_date_and_end_date() {
        String beginDate = "2017-06-05";
        String endDate = "2017-07-10";
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        Budget budget1 = new Budget();
        budget1.setId(2);
        budget1.setAmount(600);
        budget1.setMonth("2017-06");
        Budget budget2 = new Budget();
        budget2.setId(3);
        budget2.setAmount(310);
        budget2.setMonth("2017-07");
        List<Budget> allBudgets = Arrays.asList(budget1, budget2);

        when(mockBudgetRepo.findAll()).thenReturn(allBudgets);
    }

}