package com.odde.bbuddy.budget.repo;

import com.odde.bbuddy.budget.domain.Budget;
import org.springframework.data.repository.Repository;
import sun.awt.image.BufferedImageDevice;

import java.util.List;

public interface BudgetRepo extends Repository<Budget, Long> {
    void save(Budget budget);

    List<Budget> findAll();

    Budget findByMonth(String s);
}
