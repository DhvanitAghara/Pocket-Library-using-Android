package com.example.dhvanit.librarymangementapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText userid;
    EditText email;
    EditText contact;
    Button register;
    String name ,id,pass,mail,number;

    DatabaseReference LibraryManagment;

    @Override
    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            LibraryManagment = FirebaseDatabase.getInstance().getReference("Users");

            final EditText username = (EditText) findViewById(R.id.username);
            final EditText userid = (EditText) findViewById(R.id.adminid);
            final EditText password = (EditText) findViewById(R.id.adminpassword);
            final EditText email = (EditText) findViewById(R.id.email);
            final EditText contact = (EditText) findViewById(R.id.contact);
            register = (Button) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    name = username.getText().toString().trim();
                    id = userid.getText().toString().trim();
                    pass = password.getText().toString().trim();
                    mail = email.getText().toString().trim();
                    number = contact.getText().toString().trim();
                    adduser();
                }
            });
    }
    public void adduser(){

        if(TextUtils.isEmpty(id))
        {
            Toast.makeText(getApplicationContext(),"Enter ID",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(mail))
        {
            Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(number))
        {
            Toast.makeText(getApplicationContext(),"Enter Number",Toast.LENGTH_LONG).show();
        }
        else
        {
            String dataid = LibraryManagment.push().getKey();
            adduser user=new adduser(name,id,pass,mail,number);
            LibraryManagment.child(id).setValue(user);
            Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show();
            Intent registerIntent = new Intent(register.this,MainActivity.class);
            register.this.startActivity(registerIntent);
        }
    }
}
