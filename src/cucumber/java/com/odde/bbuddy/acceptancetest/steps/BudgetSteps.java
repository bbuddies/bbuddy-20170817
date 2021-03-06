package com.odde.bbuddy.acceptancetest.steps;

import com.odde.bbuddy.acceptancetest.data.PresentableBudget;
import com.odde.bbuddy.acceptancetest.driver.UiDriver;
import com.odde.bbuddy.budget.domain.Budget;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BudgetSteps {

    @Autowired
    UiDriver uiDriver;

    @When("^add a budget with month \"([^\"]*)\" and amount (\\d+)$")
    public void add_a_budget_with_month_and_amount(String month, int amount) throws Throwable {
        uiDriver.navigateTo("/budgets/add");
        uiDriver.inputTextByName(month, "month");
        uiDriver.inputTextByName(String.valueOf(amount), "amount");
        uiDriver.clickByText("Save");
    }

    @Then("^the following budget will be listed$")
    public void the_following_budget_will_be_listed(List<PresentableBudget> budgets) throws Throwable {
        uiDriver.navigateTo("/budgets/list");
        uiDriver.waitForTextPresent(budgets.get(0).getMonth());
        uiDriver.waitForTextPresent(budgets.get(0).getAmount());
    }

    @Then("^there is an error message \"([^\"]*)\"$")
    public void there_is_an_error_message(String message) throws Throwable {

        uiDriver.waitForTextPresent(message);

        //assertErrorMessageEquals(month, "wrong month");
    }



    @Given("^exist the budget with month \"([^\"]*)\" and amount (\\d+)$")
    public void exist_the_budget_with_month_and_amount(String month, int amount) throws Throwable {
        uiDriver.navigateTo("/budgets/add");
        uiDriver.inputTextByName(month, "month");
        uiDriver.inputTextByName(String.valueOf(amount), "amount");
        uiDriver.clickByText("Save");
    }

    @Then("^the following budget only one month \"([^\"]*)\" and amount is (\\d+)$")
    public void the_following_budget_only_one_month_and_amount_is(String month, int amount) throws Throwable {
        uiDriver.navigateTo("/budgets/list");
        //TODO 怎么拿到页面上的表格的集合
        uiDriver.waitForTextPresent(month);
        uiDriver.waitForTextPresent(String.valueOf(amount));
    }

    @Given("^exist following budget listed$")
    public void exist_following_budget_listed(List<Budget> budgets) throws Throwable {
        for (Budget budget : budgets) {
            add_a_budget_with_month_and_amount(budget.getMonth(), budget.getAmount());
        }

    }

    @When("^calculate beginDate \"([^\"]*)\" endDate \"([^\"]*)\"$")
    public void calculate_beginDate_endDate(String beginDate, String endDate) throws Throwable {
        uiDriver.navigateTo("/budgets/calculate");
        uiDriver.inputTextByName(beginDate, "beginDate");
        uiDriver.inputTextByName(endDate, "endDate");
        uiDriver.clickByText("Calculate");
    }

    @Then("^budget amount is (\\d+)$")
    public void budget_amount_is(int amount) throws Throwable {
        uiDriver.waitForTextPresent(String.valueOf(amount));
    }

}
