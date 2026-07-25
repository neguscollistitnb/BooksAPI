package com.automation.bookapi.tests;

import com.automation.bookapi.config.ApiConfig;
import com.automation.bookapi.utils.ApiRequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SingleBookTest_Negus {
    @Test
    public void SingleBookJamieTest() {


        Response response = ApiRequestBuilder.getRequest(ApiConfig.SINGLE_BOOK_ENDPOINT + "2");
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == ApiConfig.HTTP_OK, "Status code is not 200 OK");

    }




}
