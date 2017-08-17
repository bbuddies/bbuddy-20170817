@user
Feature: Budget

  Scenario: Add a new budget
    When add a budget with month "2017-08" and amount 1000
    Then the following budget will be listed
      | month   | amount |
      | 2017-08 | 1000   |