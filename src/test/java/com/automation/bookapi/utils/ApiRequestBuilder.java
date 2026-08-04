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

    public static RequestSpecification getBaseRequestWithAuth(String token){
        return getBaseRequest()
                .header("Authorization", "Bearer " + token);
    }

    public static Response getRequest(String endpoint){
        return getBaseRequest()
                .when()
                .get(endpoint);
    }

    public static Response postRequest(String endpoint, String body){
        return getBaseRequest()
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response postRequestWithAuth(String endpoint, String body, String token){
        return getBaseRequestWithAuth(token)
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response patchRequestWithAuth(String endpoint, String body, String token){
        return getBaseRequestWithAuth(token)
                .body(body)
                .when()
                .patch(endpoint);
    }

    public static Response deleteRequestWithAuth(String endpoint, String token){
        return getBaseRequestWithAuth(token)
                .when()
                .delete(endpoint);
    }

}
