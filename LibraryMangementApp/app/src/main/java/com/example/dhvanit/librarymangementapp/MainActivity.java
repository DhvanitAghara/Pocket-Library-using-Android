package com.example.dhvanit.librarymangementapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText password;
    EditText adminid;
    Button login;
    TextView data;
    DatabaseReference LibraryManagment;

    String id;
    String pass;

    int backButtonCount=0;

    private Session session ;


    private boolean checknet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(this);
        if (session.loggedin()) {
            Intent studentRecordIntent = new Intent(MainActivity.this,dashboard.class);
            MainActivity.this.startActivity(studentRecordIntent);
        }
            if (!checknet()) {
                Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
            }

            setContentView(R.layout.activity_main);

            adminid = (EditText) findViewById(R.id.adminid);
            password = (EditText) findViewById(R.id.adminpassword);
            login = (Button) findViewById(R.id.login);


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!checknet()) {
                        Toast.makeText(getApplicationContext(), "No Internet Connecction.", Toast.LENGTH_SHORT).show();
                    } else {


                        id = adminid.getText().toString().trim();
                        pass = password.getText().toString().trim();

                        if (id.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter ID.", Toast.LENGTH_SHORT).show();
                        } else if (pass.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter Password.", Toast.LENGTH_SHORT).show();
                        } else if (id.equals("admin") && pass.equals("1234")) {

                            session.setLoggedin(true);
                            if (session.loggedin()) {
                                Toast.makeText(getApplicationContext(), "You are loged in.", Toast.LENGTH_SHORT).show();
                                Intent studentRecordIntent = new Intent(MainActivity.this, dashboard.class);
                                MainActivity.this.startActivity(studentRecordIntent);
                            }
                        /*
                        Intent studentRecordIntent = new Intent(MainActivity.this, dashboard.class);
                        MainActivity.this.startActivity(studentRecordIntent);*/
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid ID or Password.", Toast.LENGTH_SHORT).show();
                        }
                    }
/*
                LibraryManagment = FirebaseDatabase.getInstance().getReference("Users");
                LibraryManagment.addValueEventListener(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int flag=0;
                        for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                            logo_adduser users= new logo_adduser();

                            users.setId(usersnapshot.getValue(logo_adduser.class).getId());
                            users.setPass(usersnapshot.getValue(logo_adduser.class).getPass());

                            String dataid=users.getId();
                            String datapass=users.getPass();

                            if(id.equals(dataid) && pass.equals(datapass)) {
                                Toast.makeText(register.getContext(), "You are loged in.", Toast.LENGTH_LONG).show();
                                flag=1;
                                Intent studentRecordIntent = new Intent(MainActivity.this,home.class);
                                MainActivity.this.startActivity(studentRecordIntent);
                                break;

                            }

                        }
                        if(flag==0)
                        {
                            Toast.makeText(register.getContext(), "Not authorised user.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                }
            });
        }
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            backButtonCount=0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            backButtonCount++;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backButtonCount=0;
                }
            }, 2000);
        }
    }
}
