package com.example.dhvanit.librarymangementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {

    Button adduser;
    Button addbook;
    Button bookdetails;
    Button issuebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        adduser = (Button) findViewById(R.id.adduser);
        addbook = (Button) findViewById(R.id.addbook);
        bookdetails = (Button) findViewById(R.id.bookdetails);
        issuebook = (Button) findViewById(R.id.issuebook);

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(home.this,register.class);
                home.this.startActivity(registerIntent);
            }
        });
    }
}

