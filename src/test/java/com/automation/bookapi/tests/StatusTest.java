package com.automation.bookapi.tests;

import com.automation.bookapi.config.ApiConfig;
import com.automation.bookapi.utils.ApiRequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StatusTest {

    @Test
    public void testApiStatus() {
        Response response = ApiRequestBuilder.getRequest(ApiConfig.STATUS_ENDPOINT);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Status endpoint should return 200 OK");
        
        String status = response.jsonPath().getString("status");
        Assert.assertNotNull(status, "Status field should not be null");
        Assert.assertTrue(status.equalsIgnoreCase("ok"), "API status should be OK");
    }

}
