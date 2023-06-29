package api.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Attachment;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ApiTestSetup {

    private String endpoint;
    private String requestBody;
    private int responseStatusCode;
    private Response response;
    public static String token;
    Map<String, String> headerMap;
    public String dataGatewayBaseURl = "https://data-gateway.beta.everwell.org";

    @Given("the API endpoint is {string}")
    @Attachment
    public void setAPIEndpoint(String endpoint) {
        System.out.println("Given called");
        this.endpoint = endpoint;
    }

    @Given("the request body contains the following JSON:")
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @When("I send a GET request")
    public void sendGETRequest() {
        System.out.println("When called");
        RestAssured.baseURI = dataGatewayBaseURl;
        RequestSpecification request = RestAssured.given();
        request.headers(headerMap);
        response = request.get(endpoint);
    }

    @When("a POST request is sent to generate token")
    public void sendPostRequestForToken() {
        RestAssured.baseURI = dataGatewayBaseURl;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", ContentType.JSON);
        response = request.body(requestBody).post(endpoint);
        token = JsonPath.from(response.asString()).get("data");
        System.out.println(token);
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
        System.out.println("Then called: " + response.getStatusCode());
    }

    @Given("the header is added")
    public void setHeaders() {
        headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, "application/json");
        headerMap.put(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }


    @When("a POST request is sent")
    public void sendPostRequest() {
        RestAssured.baseURI = dataGatewayBaseURl;
        RequestSpecification request = RestAssured.given();
        request.headers(headerMap);
        response = request.body(requestBody).post(endpoint);
    }

    @Then("the response body should contain the following JSON:")
    public void verifyResponseBody(String expectedResponseBody) {
        String resourceType = JsonPath.from(response.asString()).get("Data").toString();
//        Assert.assertEquals(resourceType, expectedResponseBody);
    }
//
}