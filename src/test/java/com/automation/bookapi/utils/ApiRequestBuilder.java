package com.automation.bookapi.utils;

import com.automation.bookapi.config.ApiConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiRequestBuilder {
    public static RequestSpecification getBaseRequest(){
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType("application/json")
                .accept("application/json");
    }

public static Response getRequest(String  endpoint){
        return getBaseRequest()
                .when()
                .get(endpoint);
    }

}

