package com.example.dhvanit.librarymangementapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class adduserfragement extends Fragment {

    EditText username;
    EditText password;
    EditText userid;
    EditText email;
    EditText contact;
    Button register;
    Button scan_studentid;

    String name ,id,pass,mail,number;
    int flag=0;
    int stop=0;

    DatabaseReference LibraryManagment;

    private boolean checknet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


       View root = inflater.inflate(R.layout.adduser, container, false);
        getActivity().setTitle("Add Student");

        if(!checknet())
        {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }

        LibraryManagment = FirebaseDatabase.getInstance().getReference("Users");

         username = (EditText) root.findViewById(R.id.username);
         userid = (EditText) root.findViewById(R.id.adminid);
         password = (EditText) root.findViewById(R.id.adminpassword);
         email = (EditText) root.findViewById(R.id.email);
         contact = (EditText) root.findViewById(R.id.contact);
         register = (Button) root.findViewById(R.id.register);
         scan_studentid = (Button) root.findViewById(R.id.scan_bookid);

        userid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                id = userid.getText().toString().trim();
                check();

            }
        });

        scan_studentid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), scan.class);
                startActivityForResult(i,1);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checknet())
                {
                    Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                }
                else {
                    name = username.getText().toString().trim();
                    id = userid.getText().toString().trim();
                    pass = password.getText().toString().trim();
                    mail = email.getText().toString().trim();
                    number = contact.getText().toString().trim();

                    check();

                    if (flag == 0) {
                        adduser();
                    }
                }
            }
        });

        return root;
    }

    public void check()
    {

        LibraryManagment = FirebaseDatabase.getInstance().getReference("Users");
        LibraryManagment.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag=0;
                String dataid="";

                for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                    dataid=usersnapshot.child("id").getValue(String.class);

                    if(id.equals(dataid) && stop==0) {
                        Toast.makeText(getContext(), "Student is already Exist.", 50).show();
                        flag=1;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void adduser(){

        if(TextUtils.isEmpty(id))
        {
            Toast.makeText(getContext(),"Enter ID",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getContext(),"Enter Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getContext(),"Enter Password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(mail))
        {
            Toast.makeText(getContext(),"Enter Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(number))
        {
            Toast.makeText(getContext(),"Enter Number",Toast.LENGTH_SHORT).show();
        }
        else
        {
           // String dataid = LibraryManagment.push().getKey();
            adduser user=new adduser(name,id,pass,mail,number);
            LibraryManagment.child(id).setValue(user);
            Toast.makeText(getContext(), "Student Added", Toast.LENGTH_SHORT).show();
            stop = 1;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new adduserfragement()).commit();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode != RESULT_OK) {
                String scanresult = data.getExtras().getString("scanresult");
                userid.setText(scanresult.toString());
            }
        }
    }
}
