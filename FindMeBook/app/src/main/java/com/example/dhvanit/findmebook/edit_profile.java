package com.example.dhvanit.findmebook;

import android.content.Context;
import android.content.SharedPreferences;
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

public class edit_profile extends Fragment {


    EditText name;
    EditText email;
    EditText number;
    Button update;

    String s_name, s_email,s_number,message_id;
    int stop=0,flag_student=0;
    DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");

    private boolean checknet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.edit_profile, container, false);
        getActivity().setTitle("Edit Profile");

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        message_id = pref.getString("message_id",null);

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }
        else {
            name = (EditText) root.findViewById(R.id.name);
            email = (EditText) root.findViewById(R.id.email);
            number = (EditText) root.findViewById(R.id.number);
            update = (Button) root.findViewById(R.id.update);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String dataid = "",dataname="",dataemail="",datanumber="";
                    for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                        dataid = usersnapshot.child("id").getValue(String.class);
                        if (message_id.equals(dataid) && stop == 0) {
                            dataname=usersnapshot.child("name").getValue(String.class);
                            dataemail=usersnapshot.child("mail").getValue(String.class);
                            datanumber=usersnapshot.child("number").getValue(String.class);
                            name.setText(dataname);
                            email.setText(dataemail);
                            number.setText(datanumber);
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checknet()) {
                        Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        stop=0;
                        s_name = name.getText().toString().trim();
                        s_email = email.getText().toString().trim();
                        s_number = number.getText().toString().trim();

                        if (TextUtils.isEmpty(s_name)) {
                            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(s_email)) {
                            Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(s_number)) {
                            Toast.makeText(getContext(), "Enter Number", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            change();
                        }
                    }
                }
            });
        }
        return root;
    }

    private void change() {
        users.child(message_id).child("name").setValue(s_name);
        users.child(message_id).child("mail").setValue(s_email);
        users.child(message_id).child("number").setValue(s_number);
        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
        stop = 1;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new edit_profile()).commit();
    }

}

