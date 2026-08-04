package com.automation.bookapi.tests;

import com.automation.bookapi.config.ApiConfig;
import com.automation.bookapi.utils.ApiRequestBuilder;
import com.automation.bookapi.utils.JsonTemplateLoader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class AuthenticationTest {

     private final String clientName = "TestClient_" + System.currentTimeMillis();
     private final String clientEmail = "test" + System.currentTimeMillis() + "@example.com";
     String requestBody;
     int statusCode;
     String accessToken;
     Response response;

    @Test
    public void testRegisterNewApiClient() throws IOException {

        requestBody = buildRegisterClientRequestBody(clientName, clientEmail);

        response = ApiRequestBuilder.postRequest(ApiConfig.API_CLIENTS_ENDPOINT, requestBody);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_CREATED, "Register API client should return 201 Created");

        accessToken = response.jsonPath().getString("accessToken");
        Assert.assertNotNull(accessToken, "Access token should not be null");
        Assert.assertTrue(accessToken.length() > 0, "Access token should not be empty");
    }

    @Test
    public void testRegisterClientWithMissingName() throws IOException {

        requestBody = buildRegisterClientRequestBody("", clientEmail);
        
        response = ApiRequestBuilder.postRequest(ApiConfig.API_CLIENTS_ENDPOINT, requestBody);
        statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode >= 400, "Register client with missing name should return error status");
    }

    @Test
    public void testRegisterClientWithMissingEmail() throws IOException {
        requestBody = buildRegisterClientRequestBody(clientName, "");
        
        response = ApiRequestBuilder.postRequest(ApiConfig.API_CLIENTS_ENDPOINT, requestBody);
        statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode >= 400, "Register client with missing email should return error status");
    }

    private String buildRegisterClientRequestBody(String clientName, String clientEmail) throws IOException {
        return JsonTemplateLoader.load(
                "requests/register-api-client.json",
                Map.of("clientName", clientName, "clientEmail", clientEmail)
        );
    }

}
