package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.text.ParseException;
import java.util.ArrayList;
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
    public void calculate_amount_in_whole_month() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        when(mockBudgetRepo.findAll()).thenReturn(getBudgetList());
        double amount = budgets.calculate("2017-06-05", "2017-06-10");
        assertThat(amount).isEqualTo(120);
    }

    @Test
    public void calculate_amount_between_begin_date_and_end_date() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        when(mockBudgetRepo.findAll()).thenReturn(getBudgetList());
        double amount = budgets.calculate("2017-06-05", "2017-07-10");
        assertThat(amount).isEqualTo(620);
    }


    @Test
    public void calculate_amount_begin_date_and_end_date() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        when(mockBudgetRepo.findAll()).thenReturn(getBudgetList());
        double amount = budgets.calculate("2017-06-05", "2017-08-10");
        assertThat(amount).isEqualTo(1030);
    }

    @Test
    public void calculate_amount_current_year_begin_date_and_next_year_end_date() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        when(mockBudgetRepo.findAll()).thenReturn(getBudgetList());
        double amount = budgets.calculate("2017-11-05", "2018-01-10");
        assertThat(amount).isEqualTo(670);
    }

    @Test
    public void calculate_amount_current_year_begin_date_and_next_year_end_date_test() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        when(mockBudgetRepo.findAll()).thenReturn(getBudgetList());
        double amount = budgets.calculate("2017-07-05", "2018-03-10");
        assertThat(amount).isEqualTo(1810);
    }

    @Test
    public void calculate_amount_2016_year_begin_date_and_2017_year_end_date() throws ParseException {
        BudgetRepo mockBudgetRepo = mock(BudgetRepo.class);
        Budgets budgets = new Budgets(mockBudgetRepo);
        List<Budget> budgetList = new ArrayList<>();
        Budget budget1 = new Budget();
        budget1.setAmount(310);
        budget1.setMonth("2016-12");
        budgetList.add(budget1);

        Budget budget2 = new Budget();
        budget2.setAmount(310);
        budget2.setMonth("2017-01");
        budgetList.add(budget2);

        when(mockBudgetRepo.findAll()).thenReturn(budgetList);
        double amount = budgets.calculate("2016-12-20", "2017-01-03");
        assertThat(amount).isEqualTo(150);
    }

    private List<Budget> getBudgetList() {
        // 2017-07-05", "2018-03-10")
        List<Budget> budgetList = new ArrayList<>();
        Budget budget1 = new Budget();
        budget1.setId(2);
        budget1.setAmount(600);
        budget1.setMonth("2017-06");
        budgetList.add(budget1);

        Budget budget2 = new Budget();
        budget2.setId(3);
        budget2.setAmount(310);
        budget2.setMonth("2017-07");
        budgetList.add(budget2);

        Budget budget3 = new Budget();
        budget3.setId(4);
        budget3.setAmount(620);
        budget3.setMonth("2017-08");
        budgetList.add(budget3);

        Budget budget4 = new Budget();
        budget4.setId(5);
        budget4.setAmount(300);
        budget4.setMonth("2017-11");
        budgetList.add(budget4);

        Budget budget5 = new Budget();
        budget5.setId(6);
        budget5.setAmount(310);
        budget5.setMonth("2017-12");
        budgetList.add(budget5);

        Budget budget6 = new Budget();
        budget6.setId(7);
        budget6.setAmount(310);
        budget6.setMonth("2018-01");
        budgetList.add(budget6);

        return budgetList;
    }

}