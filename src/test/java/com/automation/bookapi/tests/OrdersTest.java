package com.automation.bookapi.tests;

import com.automation.bookapi.config.ApiConfig;
import com.automation.bookapi.utils.ApiRequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OrdersTest {
    
    private String accessToken;
    private String createdOrderId;

    @BeforeClass
    public void setupAuthentication() {
        String clientName = "OrdersTestClient_" + System.currentTimeMillis();
        String clientEmail = "orders" + System.currentTimeMillis() + "@example.com";
        
        String requestBody = "{\n" +
                "  \"clientName\": \"" + clientName + "\",\n" +
                "  \"clientEmail\": \"" + clientEmail + "\"\n" +
                "}";
        
        Response response = ApiRequestBuilder.postRequest(ApiConfig.API_CLIENTS_ENDPOINT, requestBody);
        accessToken = response.jsonPath().getString("accessToken");
        Assert.assertNotNull(accessToken, "Failed to obtain access token for testing");
    }

    @Test
    public void testSubmitNewOrder() {
        String requestBody = "{\n" +
                "  \"bookId\": 1,\n" +
                "  \"customerName\": \"John Doe\"\n" +
                "}";
        
        Response response = ApiRequestBuilder.postRequestWithAuth(ApiConfig.ORDERS_ENDPOINT, requestBody, accessToken);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_CREATED, "Submit order should return 201 Created");
        
        String orderId = response.jsonPath().getString("orderId");
        Assert.assertNotNull(orderId, "Order ID should not be null");
        Assert.assertTrue(orderId.length() > 0, "Order ID should not be empty");
        
        this.createdOrderId = orderId;
    }

    @Test
    public void testSubmitOrderWithoutAuth() {
        String requestBody = "{\n" +
                "  \"bookId\": 1,\n" +
                "  \"customerName\": \"John Doe\"\n" +
                "}";
        
        Response response = ApiRequestBuilder.postRequest(ApiConfig.ORDERS_ENDPOINT, requestBody);
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode >= 400, "Submit order without auth should return error status");
    }

    @Test
    public void testSubmitOrderMissingBookId() {
        String requestBody = "{\n" +
                "  \"customerName\": \"John Doe\"\n" +
                "}";
        
        Response response = ApiRequestBuilder.postRequestWithAuth(ApiConfig.ORDERS_ENDPOINT, requestBody, accessToken);
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode >= 400, "Submit order without bookId should return error status");
    }

    @Test
    public void testSubmitOrderMissingCustomerName() {
        String requestBody = "{\n" +
                "  \"bookId\": 1\n" +
                "}";
        
        Response response = ApiRequestBuilder.postRequestWithAuth(ApiConfig.ORDERS_ENDPOINT, requestBody, accessToken);
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode >= 400, "Submit order without customerName should return error status");
    }

    @Test
    public void testGetAllOrders() {
        Response response = ApiRequestBuilder.getBaseRequestWithAuth(accessToken)
                .when()
                .get(ApiConfig.ORDERS_ENDPOINT);
        
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get orders should return 200 OK");
        
        Assert.assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Test
    public void testGetSingleOrder() {
        // First, create a new order for this test
        String requestBody = "{\n" +
                "  \"bookId\": 2,\n" +
                "  \"customerName\": \"Jane Smith\"\n" +
                "}";
        
        Response createResponse = ApiRequestBuilder.postRequestWithAuth(ApiConfig.ORDERS_ENDPOINT, requestBody, accessToken);
        int statusCode = createResponse.getStatusCode();
        if (statusCode != ApiConfig.HTTP_CREATED) {
            System.out.println("Order creation response: " + createResponse.getBody().asString());
            System.out.println("Status code: " + statusCode);
        }
        Assert.assertEquals(statusCode, ApiConfig.HTTP_CREATED, "Order creation failed");
        
        String orderId = createResponse.jsonPath().getString("orderId");
        Assert.assertNotNull(orderId, "Order ID from creation response should not be null");
        
        // Now get the specific order
        Response response = ApiRequestBuilder.getBaseRequestWithAuth(accessToken)
                .when()
                .get(ApiConfig.ORDERS_ENDPOINT + "/" + orderId);
        
        int statusCode2 = response.getStatusCode();
        Assert.assertEquals(statusCode2, ApiConfig.HTTP_OK, "Get single order should return 200 OK. Got status: " + statusCode2 + ", Response: " + response.getBody().asString());
        
        String retrievedOrderId = response.jsonPath().getString("id");
        Assert.assertEquals(retrievedOrderId, orderId, "Retrieved order ID should match");
    }

    @Test
    public void testUpdateOrder() {
        // First, create an order
        String requestBody = "{\n" +
                "  \"bookId\": 3,\n" +
                "  \"customerName\": \"Original Name\"\n" +
                "}";
        
        Response createResponse = ApiRequestBuilder.postRequestWithAuth(ApiConfig.ORDERS_ENDPOINT, requestBody, accessToken);
        Assert.assertEquals(createResponse.getStatusCode(), ApiConfig.HTTP_CREATED, "Order creation failed");
        
        String orderId = createResponse.jsonPath().getString("orderId");
        Assert.assertNotNull(orderId, "Order ID from creation response should not be null");
        
        // Update the order
        String updateBody = "{\n" +
                "  \"customerName\": \"Updated Name\"\n" +
                "}";
        
        Response updateResponse = ApiRequestBuilder.patchRequestWithAuth(ApiConfig.ORDERS_ENDPOINT + "/" + orderId, updateBody, accessToken);
        int statusCode = updateResponse.getStatusCode();
        Assert.assertTrue(statusCode == ApiConfig.HTTP_OK || statusCode == ApiConfig.HTTP_NO_CONTENT, 
                "Update order should return 200 OK or 204 No Content, got: " + statusCode);
    }

    @Test
    public void testDeleteOrder() {
        // First, create an order
        String requestBody = "{\n" +
                "  \"bookId\": 4,\n" +
                "  \"customerName\": \"To Be Deleted\"\n" +
                "}";
        
        Response createResponse = ApiRequestBuilder.postRequestWithAuth(ApiConfig.ORDERS_ENDPOINT, requestBody, accessToken);
        Assert.assertEquals(createResponse.getStatusCode(), ApiConfig.HTTP_CREATED, "Order creation failed");
        
        String orderId = createResponse.jsonPath().getString("orderId");
        Assert.assertNotNull(orderId, "Order ID from creation response should not be null");
        
        // Delete the order
        Response deleteResponse = ApiRequestBuilder.deleteRequestWithAuth(ApiConfig.ORDERS_ENDPOINT + "/" + orderId, accessToken);
        int statusCode = deleteResponse.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_NO_CONTENT, "Delete order should return 204 No Content");
    }

}
