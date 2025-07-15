package br.com.selenium.api.apitests;

import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.*;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Feature("API Testing")
public class ApiExampleTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080"; // Base URI for API tests
    }

    @Test
    @Story("Get User by ID")
    @Description("Test to get a user by ID with mocked response")
    public void testGetUserById() {
        // Mock the HTTP response
        Response mockedResponse = Mockito.mock(Response.class);
        io.restassured.response.ResponseBody mockedBody = Mockito.mock(io.restassured.response.ResponseBody.class);
        when(mockedResponse.getStatusCode()).thenReturn(200);
        when(mockedResponse.getBody()).thenReturn(mockedBody);
        when(mockedBody.asString()).thenReturn("{\"id\": 1, \"name\": \"Test User\"}");

        // Perform the request (this part would typically use RestAssured to hit a real endpoint)
        // For this example, we're simulating the response directly
        Response response = mockedResponse; // In a real scenario: RestAssured.get("/users/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"id\": 1, \"name\": \"Test User\"}", response.getBody().asString());
    }

    @Test
    @Story("Create New User")
    @Description("Test to create a new user with mocked response")
    public void testCreateUser() {
        // Mock the HTTP response
        Response mockedResponse = Mockito.mock(Response.class);
        io.restassured.response.ResponseBody mockedBody = Mockito.mock(io.restassured.response.ResponseBody.class);
        when(mockedResponse.getStatusCode()).thenReturn(201);
        when(mockedResponse.getBody()).thenReturn(mockedBody);
        when(mockedBody.asString()).thenReturn("{\"id\": 2, \"name\": \"New User\"}");

        // Perform the request (simulated)
        Response response = mockedResponse; // In a real scenario: RestAssured.given().body("{"name": "New User"}").post("/users");

        assertEquals(201, response.getStatusCode());
        assertEquals("{\"id\": 2, \"name\": \"New User\"}", response.getBody().asString());
    }

    @Test
    @Story("Delete User")
    @Description("Test to delete a user with mocked response")
    public void testDeleteUser() {
        // Mock the HTTP response
        Response mockedResponse = Mockito.mock(Response.class);
        io.restassured.response.ResponseBody mockedBody = Mockito.mock(io.restassured.response.ResponseBody.class);
        when(mockedResponse.getStatusCode()).thenReturn(204);
        when(mockedResponse.getBody()).thenReturn(mockedBody);
        when(mockedBody.asString()).thenReturn("");

        // Perform the request (simulated)
        Response response = mockedResponse; // In a real scenario: RestAssured.delete("/users/1");

        assertEquals(204, response.getStatusCode());
        assertEquals("", response.getBody().asString());
    }



}
