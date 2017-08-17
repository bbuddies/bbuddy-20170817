package com.odde.bbuddy.budget;

import com.odde.bbuddy.budget.domain.Budget;
import com.odde.bbuddy.budget.domain.Budgets;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BudgetControllerTest {

    @Test
    public void submit_add_budget_should_call_budgets_add() {
        Budgets mockBudgets = mock(Budgets.class);
        BudgetController controller = new BudgetController(mockBudgets);

        Budget budget = new Budget();
        budget.setMonth("2017-05");
        budget.setAmount(300);
        controller.submitAddBudget(budget);

        verify(mockBudgets).add(budget);
    }

}