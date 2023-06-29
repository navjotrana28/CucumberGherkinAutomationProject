Feature: API Tests
  As a user of the API
  I want to perform various API tests
  In order to ensure the API functionality is correct

  Scenario: GET request to retrieve the token
    Given the API endpoint is "/token"
    And the request body contains the following JSON:
      """
      {
      "username":"internal_client",
      "password":"Everwell@123"
      }
      """
    When a POST request is sent to generate token
    Then the response status code should be 200

#
  Scenario: GET request to retrieve user information
    Given the API endpoint is "/v1/Patient?_id=40957993"
    Given the header is added
    When I send a GET request
    Then the response status code should be 200

  Scenario: Create a new user
    Given the API endpoint is "/v1/Patient"
    Given the header is added
    And the request body contains the following JSON:
      """
      {"_page":"0",
       "_count":"10",
       "birthdate":"gt1997-01-01"}
      """
    When a POST request is sent
    Then the response status code should be 200
    And the response body should contain the following JSON:
      """
      Bundle
      """