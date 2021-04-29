@add
Feature: Addition of two numbers
	@add1
  Scenario: Add two numbers
    Given I input numbers 10 and 20 into the calculator
    When I press add
    Then I should get the result 30 on the screen
  @add2
  Scenario: Add two numbers
    Given I input numbers 100 and 220 into the calculator
    When I press add
    Then I should get the result 320 on the screen
  @add3
  Scenario: Add two numbers
    Given I input numbers 1 and 8899 into the calculator
    When I press add
    Then I should get the result 8900 on the screen
  @add4
  Scenario: Add two numbers
    Given I input numbers 16 and 234 into the calculator
    When I press add
    Then I should get the result 250 on the screen
  @add5
  Scenario: Add two numbers
    Given I input numbers 93 and 8907 into the calculator
    When I press add
    Then I should get the result 9000 on the screen
