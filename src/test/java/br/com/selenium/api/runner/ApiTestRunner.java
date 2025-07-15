package br.com.selenium.api.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-api", "junit:target/cucumber-api.xml", "io.qameta.allure.cucumberjvm.AllureCucumberJvm"},
        glue = {"br.com.selenium.api.apitests"},
        features = "src/test/resources/features/api-demo.feature")
public class ApiTestRunner {
}
