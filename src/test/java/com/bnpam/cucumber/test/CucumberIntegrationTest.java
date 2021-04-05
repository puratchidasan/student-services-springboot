package com.bnpam.cucumber.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true,dryRun = false,features = "src/test/resource/features", plugin = { "pretty",
		"html:target/cucumber/", "json:target/cucumber/cucumber.json", "usage:target/cucumber/usage.jsonx",
		"junit:target/cucumber/junit.xml"},glue = "com.bnpam.cucumber.stepdefinitions")
public class CucumberIntegrationTest extends SpringIntegrationTest {
}
