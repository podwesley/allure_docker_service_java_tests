package br.com.selenium.api.apitests;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.qameta.allure.Step;

public class ApiSteps {

    @Given("^I have a user ID$")
    @Step("Given I have a user ID")
    public void i_have_a_user_ID() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // For now, we'll just print a message and mark as pending
        System.out.println("Step: I have a user ID");
        // throw new PendingException();
    }

    @When("^I request user details$")
    @Step("When I request user details")
    public void i_request_user_details() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: I request user details");
        // throw new PendingException();
    }

    @Then("^I should receive user information$")
    @Step("Then I should receive user information")
    public void i_should_receive_user_information() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: I should receive user information");
        // throw new PendingException();
    }

    @Given("^I have new user data$")
    @Step("Given I have new user data")
    public void i_have_new_user_data() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: I have new user data");
        // throw new PendingException();
    }

    @When("^I send a request to create a user$")
    @Step("When I send a request to create a user")
    public void i_send_a_request_to_create_a_user() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: I send a request to create a user");
        // throw new PendingException();
    }

    @Then("^the user should be created successfully$")
    @Step("Then the user should be created successfully")
    public void the_user_should_be_created_successfully() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: The user should be created successfully");
        // throw new PendingException();
    }

    @Given("^I have an existing user ID$")
    @Step("Given I have an existing user ID")
    public void i_have_an_existing_user_ID() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: I have an existing user ID");
        // throw new PendingException();
    }

    @When("^I send a request to delete the user$")
    @Step("When I send a request to delete the user")
    public void i_send_a_request_to_delete_the_user() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: I send a request to delete the user");
        // throw new PendingException();
    }

    @Then("^the user should be deleted successfully$")
    @Step("Then the user should be deleted successfully")
    public void the_user_should_be_deleted_successfully() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Step: The user should be deleted successfully");
        // throw new PendingException();
    }
}
