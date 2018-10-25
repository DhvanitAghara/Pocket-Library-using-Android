package com.example.dhvanit.librarymangementapp;

public class issue {
    String bookid, studentid, booktitle, issue_date, due_date;
    int books,due_charge;

    public issue() {

    }

    public issue(String bookid, String studentid, String booktitle, String issue_date, String due_date, int books,int due_charge) {
        this.bookid = bookid;
        this.studentid = studentid;
        this.booktitle = booktitle;
        this.issue_date = issue_date;
        this.due_date = due_date;
        this.books = books;
        this.due_charge=due_charge;
    }

    public int getDue_charge() {
        return due_charge;
    }

    public void setDue_charge(int due_charge) {
        this.due_charge = due_charge;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public int getBooks() {
        return books;
    }

    public void setBooks(int books) {
        this.books = books;
    }
}