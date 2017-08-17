package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BudgetsTest {
    
    @Test
    public void budgets_add_should_save_budget() {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);

        Budget budget = new Budget();
        budget.setMonth("2017-02");
        budget.setAmount(200);
        budgets.add(budget);

        verify(mockBudgetRepo).save(budget);
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