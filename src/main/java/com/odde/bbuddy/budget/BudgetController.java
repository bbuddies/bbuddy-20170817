package com.odde.bbuddy.budget;

import com.odde.bbuddy.budget.domain.Budget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static java.lang.System.out;

@Controller
@Slf4j
public class BudgetController {

    @GetMapping("/budgets/add")
    public String addBudget() {
        return "/budgets/add";
    }

    @PostMapping("/budgets/add")
    public String submitAddBudget(Budget budget) {
        out.println("month: " + budget.getMonth());
        out.println("amount: " + budget.getAmount());
        return "/budgets/add";
    }

    @GetMapping("/budgets/list")
    public String listBudgets() {
        return "/budgets/list";
    }
}
