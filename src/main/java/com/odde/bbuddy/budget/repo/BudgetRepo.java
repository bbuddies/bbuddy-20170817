package com.odde.bbuddy.budget.repo;

import com.odde.bbuddy.budget.domain.Budget;
import org.springframework.data.repository.Repository;

public interface BudgetRepo extends Repository<Budget, Long> {
    void save(Budget budget);
}
