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

public class change_password extends Fragment {


    EditText studentid;
    EditText password,confirm_password;
    Button update;
    Button scan;

    String student_id, new_password,confirm_pass;
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
        View root = inflater.inflate(R.layout.change_password, container, false);
        getActivity().setTitle("Change Password");

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }
        else{
            studentid = (EditText) root.findViewById(R.id.studentid);
            password = (EditText) root.findViewById(R.id.new_password);
            confirm_password = (EditText) root.findViewById(R.id.confirm_password);
            update = (Button) root.findViewById(R.id.update);
            scan = (Button) root.findViewById(R.id.scan_bookid);

            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), scan.class);
                    startActivityForResult(i, 1);
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
                        student_id = studentid.getText().toString().trim();
                        new_password = password.getText().toString().trim();
                        confirm_pass = confirm_password.getText().toString().trim();

                        if (TextUtils.isEmpty(student_id)) {
                            Toast.makeText(getContext(), "Enter Student ID", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(new_password)) {
                            Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(confirm_pass)) {
                            Toast.makeText(getContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                        }
                        else if (!confirm_pass.equals(new_password)) {
                            Toast.makeText(getContext(), "Passwords are not matched.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                                checkstudentid();
                        }
                    }
                }
            });
        }
        return root;
    }

    private void checkstudentid() {

        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag_student=0;
                String dataid="";

                for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                    dataid=usersnapshot.child("id").getValue(String.class);
                    if(student_id.equals(dataid) && stop==0  ) {
                        flag_student=1;
                        break;
                    }
                }
                if (flag_student == 0) {
                    Toast.makeText(getContext(), "Student is not registerd", Toast.LENGTH_SHORT).show();
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
        users.child(student_id).child("pass").setValue(new_password);
        Toast.makeText(getContext(), "Password Changed.", Toast.LENGTH_SHORT).show();
        stop = 1;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new change_password()).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                String scanresult = data.getExtras().getString("scanresult");
                    studentid.setText(scanresult.toString());
            }
        }
    }
}

