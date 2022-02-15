package com.example.library;

public class Book {
    String title;
    String author;
    int year;
    int coverId;

    public Book(String title, String author,int year,int coverId) {
        this.title = title;
        this.author = author;
        this.year=year;
        this.coverId=coverId;
    }

    @Override
    public String toString() {
        return  title + " "+ author ;
    }
}
