package com.example.dhvanit.librarymangementapp;

public class adduser {
    String name;
    String id;
    String pass;
    String mail;
    String number;

    public adduser() {
    }

    public adduser(String name, String id, String pass, String mail, String number) {
        this.name = name;
        this.id = id;
        this.pass = pass;
        this.mail = mail;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }

    public String getMail() {
        return mail;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
