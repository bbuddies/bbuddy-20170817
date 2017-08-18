package com.odde.bbuddy.budget;

import com.odde.bbuddy.budget.domain.Budget;
import com.odde.bbuddy.budget.domain.Budgets;
import org.junit.Test;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BudgetControllerTest {

    @Test
    public void submit_add_budget_and_throw_database_exception_should_return_error_message() {
        Budgets mockBudgets = mock(Budgets.class);
        Model mockModel = mock(Model.class);
        BudgetController controller = new BudgetController(mockBudgets);

        //mock exception

        Budget budget = new Budget();
        budget.setMonth("2017-05");
        budget.setAmount(300);
        controller.submitAddBudget(budget,mockModel);

    }

    @Test
    public void submit_add_budget_should_call_budgets_add() {
        Budgets mockBudgets = mock(Budgets.class);
        Model mockModel = mock(Model.class);
        BudgetController controller = new BudgetController(mockBudgets);

        Budget budget = new Budget();
        budget.setMonth("2017-05");
        budget.setAmount(300);
        controller.submitAddBudget(budget,mockModel);

        verify(mockBudgets).add(budget,()->{},()->{});
    }
    
    @Test
    public void list_budget_should_set_budget_list_for_view() {
        Budgets stubBudgets = mock(Budgets.class);
        BudgetController controller = new BudgetController(stubBudgets);
        List<Budget> allBudgets = Arrays.asList(new Budget());
        when(stubBudgets.getAll()).thenReturn(allBudgets);

        Model mockModel = mock(Model.class);
        controller.listBudgets(mockModel);

        verify(mockModel).addAttribute("budgets", allBudgets);
    }

}