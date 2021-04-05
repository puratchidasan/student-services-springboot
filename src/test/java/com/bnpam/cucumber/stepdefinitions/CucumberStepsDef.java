package com.bnpam.cucumber.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import cucumber.api.java.en.Given;
import org.springframework.http.HttpStatus;

import com.bnpam.cucumber.test.SpringIntegrationTest;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CucumberStepsDef{
	 
	 @Given("^the bag is empty$")
	    public void the_bag_is_empty() {
	        assertThat("OK",true);
	    }
	 
	 private int num1;
	    private int num2;
	    private int total;

	    @Given("^I input numbers (\\d+) and (\\d+) into the calculator$")
	    public void I_input_numbers_and_into_the_calculator(int arg1, int arg2)
	            throws Throwable {
	        num1 = arg1;
	        num2 = arg2;
	        assertThat("The total is not zero.", total == 0);
	    }

	    @When("^I press add$")
	    public void I_press_add() throws Throwable {
	        total = num1 + num2;
	    }

	    @Then("^I should get the result (\\d+) on the screen$")
	    public void I_should_get_the_result_on_the_screen(int arg1)
	            throws Throwable {
	    	assertThat("The expected total was 30, but actually is: " + total,
	                total == arg1);
	    }
}