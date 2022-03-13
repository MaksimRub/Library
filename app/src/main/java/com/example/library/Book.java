package com.example.library;

public class Book implements Comparable<Book> {
    String title;
    String author;
    String genre;
    int year;
    int coverId;

    public Book(String title, String author,String genre,int year,int coverId) {
        this.title = title;
        this.genre=genre;
        this.author = author;
        this.year=year;
        this.coverId=coverId;
    }

    @Override
    public String toString() {
        return  title + " "+ author ;
    }

    @Override
    public int compareTo(Book book) {
        if(title.equals(book.title)){
            return 0;
        }
        return -1;
    }


    class Compare implements Comparable <Book>{

        @Override
        public int compareTo(Book book) {
            if(author.equals(book.author)){
                return 0;
            }
            return -1;
        }
    }
}
