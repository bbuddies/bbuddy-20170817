@user
Feature: Budget

  Scenario: Add a new budget
    When add a budget with month "2017-08" and amount 1000
    Then the following budget will be listed
      | month   | amount |
      | 2017-08 | 1000   |

  Scenario: validation of adding budget with month failed
    When add a budget with month "wrong format" and amount 1000
    Then there is an error message "wrong month"



  Scenario: Add a new budget
    When add a budget with month "2017-08" and amount 1000
    Then the following budget will be listed
      | month   | amount |
      | 2017-08 | 1000   |

  Scenario: Add a repeat budget
    Given exist the budget with month "2017-08" and amount 1000
    When add a budget with month "2017-08" and amount 400
    Then the following budget only one month "2017-08" and amount is 400

  Scenario: calculate budget amount
    Given exist following budget listed
      | month   | amount |
      | 2017-06 | 600   |
      | 2017-07 | 310   |
    When calculate beginDate "2017-06-05" endDate "2017-07-10"
    Then budget amount is 620
