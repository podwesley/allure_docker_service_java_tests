Feature: API Demo Tests
  As a user
  I want to perform API operations
  So that I can validate the API functionality

  Scenario: Get user details from API
    Given I have a user ID
    When I request user details
    Then I should receive user information

  Scenario: Create a new user via API
    Given I have new user data
    When I send a request to create a user
    Then the user should be created successfully

  Scenario: Delete an existing user via API
    Given I have an existing user ID
    When I send a request to delete the user
    Then the user should be deleted successfully

  Scenario: Update existing user details via API
    Given I have an existing user ID and updated user data
    When I send a request to update the user's details
    Then the user's details should be updated successfully
