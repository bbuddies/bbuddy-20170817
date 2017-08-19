package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.MONTHS;

@Component
public class Budgets {
    private final BudgetRepo budgetRepo;

    @Autowired
    public Budgets(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public void add(Budget budget, Runnable afterAddFailed, Runnable validationFailed) {
        if (!validation(budget)) {
            validationFailed.run();
            return;
        }

        try {
            Budget queryBudget = budgetRepo.findByMonth(budget.getMonth());
            if (Objects.isNull(queryBudget)) {
                budgetRepo.save(budget);
                return;
            }
            queryBudget.setAmount(budget.getAmount());
            budgetRepo.save(queryBudget);
        } catch (DataAccessException e) {
            afterAddFailed.run();
        }
    }

    public boolean validation(Budget budget) {
        String eL = "[0-9]{4}-[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(budget.getMonth());
        return m.matches();
    }

    public List<Budget> getAll() {
        return budgetRepo.findAll();
    }

    public double calculate(String beginDate, String endDate) throws ParseException {
        //拉取全部数据

        List<Budget> budgets = budgetRepo.findAll();
        double[] totalAmount = {0D};

        //计算时间段内有多少个月

        //第一个月的金额
        LocalDate firstDay = LocalDate.parse(beginDate);
        LocalDate beginLocalDate = LocalDate.parse(endDate);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        if(firstDay.getYear() == beginLocalDate.getYear() && firstDay.getMonth() == beginLocalDate.getMonth()) {
            lastDay = LocalDate.parse(endDate);
            totalAmount[0] += getAmount(firstDay, lastDay, beginDate, budgets);
            return totalAmount[0];
        }
        totalAmount[0] += getAmount(firstDay, lastDay, beginDate, budgets);

        //中间月的金额总和
        List<String> months = getMonths(beginDate, endDate);
        budgets.stream().filter(budget -> months.contains(budget.getMonth()))
                .forEach(budget -> {
                    totalAmount[0] += budget.getAmount();
                });

        // 最后一个月的金
        LocalDate endLastDay = LocalDate.parse(endDate);
        LocalDate endFirstDay = LocalDate.of(endLastDay.getYear(), endLastDay.getMonth(), 1);
        totalAmount[0] += getAmount(endFirstDay, endLastDay, endDate, budgets);

        return totalAmount[0];
    }

    private double getAmount(LocalDate firstDay, LocalDate lastDay, String date, List<Budget> budgets) throws ParseException {
        Budget beginDateBudget = budgets.stream().filter(budget -> date.contains(budget.getMonth())).findFirst().orElse(null);
        if (beginDateBudget != null) {
            int totalDays = Period.between(firstDay, lastDay).getDays()+1;
            int days = getDayOfMonth(date);
            return totalDays * beginDateBudget.getAmount() / days;
        }
        return 0;
    }

    public List<String> getMonths(String beginDate, String endDate) throws ParseException {
        List<String> months = new ArrayList<>();
        LocalDate begin = LocalDate.parse(beginDate);
        LocalDate end = LocalDate.parse(endDate);
        Period period = Period.between(begin, end);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        for (int i = 1; i < (period.getYears() * 12 + period.getMonths()); i++) {
            Date curDate = Date.from(begin.plus(i, MONTHS).atStartOfDay(ZoneId.systemDefault()).toInstant());
            months.add(sdf.format(curDate));
        }
        return months;
    }

    //java获取当前月的天数
    public int getDayOfMonth(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = sdf.parse(dateString);
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(begin);
        return aCalendar.getActualMaximum(Calendar.DATE);
    }
}
