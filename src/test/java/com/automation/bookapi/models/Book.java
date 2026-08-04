package com.automation.bookapi.models;

public class Book {

    private int id;
    private String name;
    private String type;
    private boolean available;
    private String author;
    private double price;

    public Book() {
    }

    public Book(int id, String name, String type, boolean available, String author, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.available = available;
        this.author = author;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
