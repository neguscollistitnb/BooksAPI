package com.automation.bookapi.tests;

import com.automation.bookapi.config.ApiConfig;
import com.automation.bookapi.models.Book;
import com.automation.bookapi.utils.ApiRequestBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BooksTest {
    Book book;
    Response response;
    int statusCode;
    int bookCount;

    @Test
    public void testGetAllBooks() {
        response = ApiRequestBuilder.getRequest(ApiConfig.BOOKS_ENDPOINT);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get books endpoint should return 200 OK");
        
        bookCount = response.jsonPath().getList("").size();
        Assert.assertTrue(bookCount > 0, "Books list should not be empty");
    }

    @Test
    public void testGetBooksWithFictionFilter() {
        response = ApiRequestBuilder.getRequest(ApiConfig.BOOKS_ENDPOINT + "?type=fiction");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get fiction books should return 200 OK");
        
        bookCount = response.jsonPath().getList("").size();
        Assert.assertTrue(bookCount > 0, "Fiction books list should not be empty");
    }

    @Test
    public void testGetBooksWithNonFictionFilter() {
        response = ApiRequestBuilder.getRequest(ApiConfig.BOOKS_ENDPOINT + "?type=non-fiction");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get non-fiction books should return 200 OK");
        
        bookCount = response.jsonPath().getList("").size();
        Assert.assertTrue(bookCount > 0, "Non-fiction books list should not be empty");
    }

    @Test
    public void testGetBooksWithLimitParameter() {
        response = ApiRequestBuilder.getRequest(ApiConfig.BOOKS_ENDPOINT + "?limit=5");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get books with limit should return 200 OK");
        
        bookCount = response.jsonPath().getList("").size();
        Assert.assertTrue(bookCount <= 5, "Books list should respect limit parameter");
    }

    @Test
    public void testGetSingleBook() {
        response = ApiRequestBuilder.getRequest(ApiConfig.SINGLE_BOOK_ENDPOINT + "1");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get single book should return 200 OK");
        
        int bookId = response.jsonPath().getInt("id");
        Assert.assertEquals(bookId, 1, "Book ID should match the requested ID");
        
        String title = response.jsonPath().getString("name");
        Assert.assertNotNull(title, "Book title should not be null");
    }

    @Test(dataProvider = "singleBookData")
    public void testGetSingleBookDetails(int bookIdToTest, String expectedName, String expectedAuthor, String expectedType, double expectedPrice, boolean expectedAvailability) {

        book = new Book();
        response = ApiRequestBuilder.getRequest(ApiConfig.SINGLE_BOOK_ENDPOINT + bookIdToTest);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ApiConfig.HTTP_OK, "Get single book should return 200 OK");
        
//        int bookId = response.jsonPath().getInt("id");
        book.setId(response.jsonPath().getInt("id"));
        book.setName(response.jsonPath().getString("name"));
        book.setAuthor(response.jsonPath().getString("author"));
        book.setType(response.jsonPath().getString("type"));
        book.setPrice(response.jsonPath().getDouble("price"));
        book.setAvailable(response.jsonPath().getBoolean("available"));

        Assert.assertEquals(book.getId(), bookIdToTest, "Book ID should match");
        Assert.assertEquals(book.getName(), expectedName, "Book name should match");
        Assert.assertEquals(book.getAuthor(), expectedAuthor, "Book author should match");
        Assert.assertEquals(book.getType(), expectedType, "Book type should match");
        Assert.assertEquals(book.getPrice(), expectedPrice, 0.001, "Book price should be 20.33");
        Assert.assertEquals(book.isAvailable(), expectedAvailability, "Book availability should match");
    }

    @DataProvider(name = "singleBookData")
    public Object[][] singleBookData() {
        return new Object[][]{
                {1, "The Russian", "James Patterson and James O. Born", "fiction", 12.98, true},
                {2, "Just as I Am", "Cicely Tyson", "non-fiction", 20.33, false},
                {3, "The Vanishing Half", "Brit Bennett", "fiction", 16.2, true}
        };
    }



}
