package com.automation.bookapi.tests;

import com.automation.bookapi.config.ApiConfig;
import com.automation.bookapi.utils.ApiRequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APIStatusTest {

    @Test
    public void testAPIStatus() {
        // Implement your test logic here
        Response response = ApiRequestBuilder.getRequest(ApiConfig.STATUS_ENDPOINT);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK);
        System.out.println("✓ Test passed: API status endpoint returned 200 OK");
    }



}
