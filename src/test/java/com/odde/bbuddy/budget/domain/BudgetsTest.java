package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
        budgets.add(budget,()->{},()->{});

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
        budgets.add(budget,()->{},()->{});

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

}