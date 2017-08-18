package com.odde.bbuddy.acceptancetest.steps;

import com.odde.bbuddy.acceptancetest.data.PresentableBudget;
import com.odde.bbuddy.acceptancetest.driver.UiDriver;
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



}
