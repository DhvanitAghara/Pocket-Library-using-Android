package com.example.dhvanit.findmebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText password;
    EditText userid;
    Button login;
    TextView data;
    DatabaseReference LibraryManagment=FirebaseDatabase.getInstance().getReference("Users");;

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
        setContentView(R.layout.activity_main);

        session = new Session(this);
        if (session.loggedin()) {
            Intent studentRecordIntent = new Intent(MainActivity.this,dashboard.class);
            MainActivity.this.startActivity(studentRecordIntent);
        }
            if (!checknet()) {
                Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
            }
            else {

                userid = (EditText) findViewById(R.id.id);
                password = (EditText) findViewById(R.id.password);
                login = (Button) findViewById(R.id.login);


                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!checknet()) {
                            Toast.makeText(getApplicationContext(), "No Internet Connecction.", Toast.LENGTH_SHORT).show();
                        } else {

                            id = userid.getText().toString().trim();
                            pass = password.getText().toString().trim();

                            if (id.equals("")) {
                                Toast.makeText(getApplicationContext(), "Enter ID.", Toast.LENGTH_SHORT).show();
                            } else if (pass.equals("")) {
                                Toast.makeText(getApplicationContext(), "Enter Password.", Toast.LENGTH_SHORT).show();
                            } else {
                                check();
                            }
                        }
                    }
                });
            }
    }

    private void check() {
        LibraryManagment = FirebaseDatabase.getInstance().getReference("Users");
        LibraryManagment.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag = 0;
                String dataid = "", datapass = "";
                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {

                    dataid = usersnapshot.child("id").getValue(String.class);
                    datapass = usersnapshot.child("pass").getValue(String.class);
                    if (id.equals(dataid) && pass.equals(datapass)) {
                        flag = 1;
                        logedin();
                        break;
                    }
                }
                if (flag == 0) {
                    Toast.makeText(getApplicationContext(), "Invalid UserId or Password", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logedin() {
        session.setLoggedin(true);
        if (session.loggedin()) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("message_id", id);
            editor.commit();

            Toast.makeText(getApplicationContext(), "You are loged in.", Toast.LENGTH_SHORT).show();
            Intent studentRecordIntent = new Intent(MainActivity.this, dashboard.class);
            MainActivity.this.startActivity(studentRecordIntent);
        }
     else {
        Toast.makeText(getApplicationContext(), "Invalid ID or Password.", Toast.LENGTH_SHORT).show();
    }
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
