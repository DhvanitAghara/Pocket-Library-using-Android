
package com.example.dhvanit.findmebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Session session;
    int backButtonCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        session = new Session(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String message_id = pref.getString("message_id",null);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(!session.loggedin()){
            logout();
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        toggle=new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        TextView label = (TextView) headerview.findViewById(R.id.message_id);
        label.setText(message_id);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new studentrecord()).commit();
            navigationView.setCheckedItem(R.id.studentrecord);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.studentrecord:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new studentrecord()).commit();
                break;
            case R.id.bookdetails:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new bookdetails()).commit();
                break;
            case R.id.editprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new edit_profile()).commit();
                break;
            case R.id.changepassword:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new change_password()).commit();
                break;
            case R.id.logout:
                 logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        session.setLoggedin(false);
        finish();
        Intent studentRecordIntent = new Intent(dashboard.this, MainActivity.class);
        dashboard.this.startActivity(studentRecordIntent);
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
