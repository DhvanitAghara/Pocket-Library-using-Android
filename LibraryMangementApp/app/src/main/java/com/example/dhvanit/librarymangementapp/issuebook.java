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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class issuebook extends Fragment {

    EditText bookid;
    EditText studentid;
    EditText numberofbooks;
    Button issue;
    Button scan_studentid;
    Button scan_bookid;

    String book_id, student_id, stringbooks,book_title,issue_date,due_date;
    String scanresult;
    String key;
    int books;
    int flag_book = 0,flag_student=0, stop = 0, databooks=0,new_bookdata,update_data,due_charge=0;
    int scan_flag;
    DatabaseReference LibraryManagment;
    DatabaseReference booksstorage;
    DatabaseReference users;

    private boolean checknet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.issuebook, container, false);
        getActivity().setTitle("Issue Book");
/*
        Bundle bundle = getActivity().getIntent().getExtras();
        String scanresult = bundle.getString("scanresult");
        Toast.makeText(getContext(), scanresult, Toast.LENGTH_SHORT).show();
*/
        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }

        LibraryManagment = FirebaseDatabase.getInstance().getReference("Issue book record");

         studentid = (EditText) root.findViewById(R.id.studentid);
         bookid = (EditText) root.findViewById(R.id.bookid);
         numberofbooks = (EditText) root.findViewById(R.id.numberofbooks);
         issue = (Button) root.findViewById(R.id.issue);
         scan_studentid = (Button) root.findViewById(R.id.scan_studentid);
         scan_bookid=(Button)root.findViewById(R.id.scan_bookid);

        scan_studentid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_flag=0;
                Intent i = new Intent(getActivity(), scan.class);
                startActivityForResult(i,1);
            }
        });

        scan_bookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_flag=1;
                Intent i = new Intent(getActivity(), scan.class);
                startActivityForResult(i,1);
            }
        });

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checknet()) {
                    Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                }
                else {
                    stop=0;
                    student_id = studentid.getText().toString().trim();
                    book_id = bookid.getText().toString().trim();
                    stringbooks = numberofbooks.getText().toString().trim();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();
                    issue_date = sdf.format(c.getTime());


                    c.add(Calendar.DATE, 21);
                    due_date = sdf.format(c.getTime());


                    if (TextUtils.isEmpty(student_id)) {
                        Toast.makeText(getContext(), "Enter Student ID", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(book_id)) {
                        Toast.makeText(getContext(), "Enter Book ID", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(stringbooks)) {
                        Toast.makeText(getContext(), "Enter Number of Books", Toast.LENGTH_SHORT).show();
                    } else {
                        books = Integer.parseInt(stringbooks);
                        if (books == 0) {
                            Toast.makeText(getContext(), "Number of books should not be 0", Toast.LENGTH_SHORT).show();
                        }
                        else
                         {
                            checkstudentid();
                        }
                    }
                }
            }
        });
        return root;
    }

    public void checkstudentid()
    {
        users = FirebaseDatabase.getInstance().getReference("Users");
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
                    checkbookid();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void checkbookid()
    {
        booksstorage = FirebaseDatabase.getInstance().getReference("Books Storage");
        booksstorage.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag_book=0;
                String dataid="";
                databooks=0;

                for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                    dataid=usersnapshot.child("bookid").getValue(String.class);
                    if(book_id.equals(dataid) && stop==0)

                    {
                        databooks=usersnapshot.child("numberofbooks").getValue(Integer.class);
                        book_title=usersnapshot.child("booktitle").getValue(String.class);
                        //Toast.makeText(getContext(), String.valueOf(databooks), Toast.LENGTH_SHORT).show();
                        new_bookdata=databooks-books;
                        flag_book = 1;
                        break;
                    }
                }
                if(flag_book==0)
                {
                    Toast.makeText(getContext(), "Book is not Available.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    checkrecord();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void checkrecord() {
        LibraryManagment = FirebaseDatabase.getInstance().getReference("Issue book record");
        LibraryManagment.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag = 0;
                String dataid = "", dataid2 = "",dataid3="";
                databooks = 0;

                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                    dataid = usersnapshot.getKey();
                    if (student_id.equals(dataid) && stop == 0) {
                        for (DataSnapshot usersnapshot2 : usersnapshot.getChildren()) {
                            key = usersnapshot2.getKey();
                            dataid2 = usersnapshot2.child("bookid").getValue(String.class);
                            dataid3=usersnapshot2.child("issue_date").getValue(String.class);

                            if (book_id.equals(dataid2) && stop == 0 && dataid3.equals(issue_date)) {
                                databooks = usersnapshot2.child("books").getValue(Integer.class);
                                update_data=databooks+books;
                                flag = 1;
                                break;
                            }
                        }
                    }
                }
                if (flag == 1) {

                    LibraryManagment.child(student_id).child(key).child("books").setValue(update_data);
                    decrementbook();
                    Toast.makeText(getContext(), "Books Issued.", Toast.LENGTH_SHORT).show();
                    stop = 1;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new issuebook()).commit();
                }
                else {
                    issue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void issue() {
        LibraryManagment = FirebaseDatabase.getInstance().getReference("Issue book record");
                String record_id = LibraryManagment.push().getKey();
                issue record = new issue(book_id, student_id,book_title,issue_date,due_date,books,due_charge);
                LibraryManagment.child(student_id).push().setValue(record);
                //LibraryManagment.child(record_id).setValue(record);
                Toast.makeText(getContext(), "Books Issued.", Toast.LENGTH_SHORT).show();
                decrementbook();
                stop = 1;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new issuebook()).commit();
        }
        public void decrementbook(){

            booksstorage.child(book_id).child("numberofbooks").setValue(new_bookdata);
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                if(resultCode != RESULT_OK) {
                     scanresult = data.getExtras().getString("scanresult");
                     if(scan_flag==0) {
                         studentid.setText(scanresult.toString());
                     }
                     else{
                         bookid.setText(scanresult.toString());
                     }
                }
            }
        }
}

