package com.example.dhvanit.librarymangementapp;

public class addbook {
    String bookid;
    String booktitle;
    String bookcategory;
    String bookauthor;
    String bookpublisher;
    int numberofbooks;

    public addbook(){

    }

    public addbook(String bookid, String booktitle, String bookcategory, String bookauthor, String bookpublisher, int numberofbooks) {
        this.bookid = bookid;
        this.booktitle = booktitle;
        this.bookcategory = bookcategory;
        this.bookauthor = bookauthor;
        this.bookpublisher = bookpublisher;
        this.numberofbooks = numberofbooks;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getBookcategory() {
        return bookcategory;
    }

    public void setBookcategory(String bookcategory) {
        this.bookcategory = bookcategory;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookpublisher() {
        return bookpublisher;
    }

    public void setBookpublisher(String bookpublisher) {
        this.bookpublisher = bookpublisher;
    }

    public int getNumberofbooks() {
        return numberofbooks;
    }

    public void setNumberofbooks(int numberofbooks) {
        this.numberofbooks = numberofbooks;
    }
}
