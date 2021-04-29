package com.bnpam.cucumber.stepdefinitions;

import static org.junit.Assert.assertEquals;

//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;

import org.springframework.http.HttpStatus;

import com.bnpam.cucumber.test.SpringIntegrationTest;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class VersionStepDefinition extends SpringIntegrationTest{
	 
	
	    @When("^the client calls /baeldung$")
	    public void the_client_issues_POST_hello() throws Throwable {
	        executePost();
	    }

	    @Given("^the client calls /hello$")
	    public void the_client_issues_GET_hello() throws Throwable {
	        executeGet("http://localhost:8083/student-service/hello");
	    }

	    @When("^the client calls /version$")
	    public void the_client_issues_GET_version() throws Throwable {
	        executeGet("http://localhost:8083/student-service/version");
	    }

	    @Then("^the client receives status code of (\\d+)$")
	    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
	        final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
	        //assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
	        assertEquals(statusCode, currentStatusCode.value());
	        
	    }

	    @And("^the client receives server version (.+)$")
	    public void the_client_receives_server_version_body(String version) throws Throwable {
//	        assertThat(latestResponse.getBody(), is(version));
	    	assertEquals(version, latestResponse.getBody());
	    }
}