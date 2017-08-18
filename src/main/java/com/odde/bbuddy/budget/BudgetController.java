package com.odde.bbuddy.budget;

import com.odde.bbuddy.budget.domain.Budget;
import com.odde.bbuddy.budget.domain.Budgets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;

@Controller
public class BudgetController {

    private final Budgets budgets;

    @Autowired
    public BudgetController(Budgets budgets) {
        this.budgets = budgets;
    }

    @GetMapping("/budgets/add")
    public String addBudget() {
        return "/budgets/add";
    }

    @GetMapping("/budgets/calculate")
    public String calculateBudget() {
        return "/budgets/calculate";
    }
     @PostMapping("/budgets/calculate")
    public String calculate(String beginDate,String endDate,Model model) throws ParseException {
         model.addAttribute("amount", budgets.calculate(beginDate,endDate));
        return "/budgets/calculate";
    }
    @PostMapping("/budgets/add")
    public String submitAddBudget(Budget budget,Model model) {
        budgets.add(budget, ()->{
            setErrorMessage(model, "database error");
        }, () -> {
            setErrorMessage(model, "wrong month");
        });

        return "/budgets/add";
    }

    private void setErrorMessage(Model model, String attributeValue) {
        model.addAttribute("error", attributeValue);
    }

    @GetMapping("/budgets/list")
    public String listBudgets(Model model) {
        model.addAttribute("budgets", budgets.getAll());
        return "/budgets/list";
    }

}
