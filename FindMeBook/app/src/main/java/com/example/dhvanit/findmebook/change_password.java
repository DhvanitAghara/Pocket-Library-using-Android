package com.example.dhvanit.findmebook;

import android.content.Context;
import android.content.Intent;
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

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class change_password extends Fragment {


    EditText oldpassword;
    EditText newpassword;
    EditText confirmpassword;
    Button update;

    String old_password, new_password,confirm_password,message_id;
    int stop=0,flag=0;
    DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");

    private boolean checknet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.change_password, container, false);
        getActivity().setTitle("Change Password");

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        message_id = pref.getString("message_id",null);

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }
        else{
            oldpassword = (EditText) root.findViewById(R.id.old_password);
            newpassword = (EditText) root.findViewById(R.id.new_password);
            confirmpassword = (EditText) root.findViewById(R.id.confirm_password);
            update = (Button) root.findViewById(R.id.update);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checknet()) {
                        Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        stop=0;
                        old_password = oldpassword.getText().toString().trim();
                        new_password = newpassword.getText().toString().trim();
                        confirm_password = confirmpassword.getText().toString().trim();

                        if (TextUtils.isEmpty(old_password)) {
                            Toast.makeText(getContext(), "Enter Old Password", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(new_password)) {
                            Toast.makeText(getContext(), "Enter New Password", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(confirm_password)) {
                            Toast.makeText(getContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                        }
                        else if(!new_password.equals(confirm_password))
                            {
                                Toast.makeText(getContext(), "New & Confirm Password Not Matched", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                check();

                            }
                    }
                }
            });
        }
        return root;
    }

    private void check() {

        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag=0;
                String dataid="",datapass="";

                for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                    dataid=usersnapshot.child("id").getValue(String.class);
                    if(message_id.equals(dataid) && stop==0  ) {
                        datapass=usersnapshot.child("pass").getValue(String.class);
                        if(old_password.equals(datapass)) {
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 0) {
                    Toast.makeText(getContext(), "Invalid Old Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    change();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void change() {
        users.child(message_id).child("pass").setValue(new_password);
        Toast.makeText(getContext(), "Password Changed.", Toast.LENGTH_SHORT).show();
        stop = 1;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new change_password()).commit();
    }

}

