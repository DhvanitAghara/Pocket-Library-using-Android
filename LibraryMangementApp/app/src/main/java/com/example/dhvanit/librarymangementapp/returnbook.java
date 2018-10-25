package com.example.dhvanit.librarymangementapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class returnbook extends Fragment {

    EditText bookid;
    EditText studentid;
    EditText numberofbooks;
    Button returnbook;
    Button scan_studentid;
    Button scan_bookid;

    String book_id, student_id, stringbooks,due_date,current_date;
    String scanresult;
    String key = "";
    int books;
    int flag = 0, inc = 0, stop = 0, databooks = 0, new_bookdata, new_bookrecord  ;
    int scan_flag;
    DatabaseReference LibraryManagment = FirebaseDatabase.getInstance().getReference("Issue book record");
    DatabaseReference booksstorage = FirebaseDatabase.getInstance().getReference("Books Storage");
    DatabaseReference users;

    private boolean checknet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.returnbook, container, false);
        getActivity().setTitle("Return Book");

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }

        LibraryManagment = FirebaseDatabase.getInstance().getReference("Issue book record");

        studentid = (EditText) root.findViewById(R.id.studentid);
        bookid = (EditText) root.findViewById(R.id.bookid);
        numberofbooks = (EditText) root.findViewById(R.id.numberofbooks);
        returnbook = (Button) root.findViewById(R.id.returnbook);
        scan_studentid = (Button) root.findViewById(R.id.scan_studentid);
        scan_bookid = (Button) root.findViewById(R.id.scan_bookid);

        scan_studentid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_flag = 0;
                Intent i = new Intent(getActivity(), scan.class);
                startActivityForResult(i, 1);
            }
        });

        scan_bookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_flag = 1;
                Intent i = new Intent(getActivity(), scan.class);
                startActivityForResult(i, 1);
            }
        });

        returnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checknet()) {
                    Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                } else {
                    stop = 0;
                    student_id = studentid.getText().toString().trim();
                    book_id = bookid.getText().toString().trim();
                    stringbooks = numberofbooks.getText().toString().trim();

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
                        } else {
                            checkrecord();
                        }
                    }
                }
            }
        });

        return root;
    }

    public void checkrecord() {
        LibraryManagment = FirebaseDatabase.getInstance().getReference("Issue book record");
        LibraryManagment.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag = 0;
                String dataid = "", dataid2 = "";

                Date date1 = null,date2 = null;

                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                    dataid = usersnapshot.getKey();
                    if (student_id.equals(dataid) && stop == 0) {
                        for (DataSnapshot usersnapshot2 : usersnapshot.getChildren()) {
                            key = usersnapshot2.getKey();
                            dataid2 = usersnapshot2.child("bookid").getValue(String.class);

                            if (book_id.equals(dataid2) && stop == 0) {
                                databooks = usersnapshot2.child("books").getValue(Integer.class);
                                due_date=usersnapshot2.child("due_date").getValue(String.class);

                                try {
                                    date1=new SimpleDateFormat("dd/MM/yyyy").parse(due_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar c = Calendar.getInstance();
                                current_date = sdf.format(c.getTime());
                                try {
                                     date2 = sdf.parse(current_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long different = date2.getTime() - date1.getTime();
                                long elapsedDays = different / 86400000;
                                if(elapsedDays>0)
                                {
                                    AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                                    builder.setTitle("Due Charges:").setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.setMessage(Long.toString(books*elapsedDays));
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    new_bookrecord = databooks - books;
                                    flag = 1;
                                    break;
                                }
                                else {
                                    elapsedDays=0;
                                    AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                                    builder.setTitle("Due Charges:").setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.setMessage(Long.toString(elapsedDays));
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    new_bookrecord = databooks - books;
                                    flag = 1;
                                    break;
                                }

                            }
                        }
                    }
                }
                if (flag == 0) {
                    Toast.makeText(getContext(), "Data not Found", Toast.LENGTH_SHORT).show();
                } else {
                    increment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void increment() {

        booksstorage.addListenerForSingleValueEvent(new ValueEventListener() {
            String dataid = "";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {

                    dataid = usersnapshot.child("bookid").getValue(String.class);

                    if (book_id.equals(dataid) && stop == 0) {

                        int stock = usersnapshot.child("numberofbooks").getValue(Integer.class);
                        new_bookdata = stock + books;
                        booksstorage.child(book_id).child("numberofbooks").setValue(new_bookdata);
                        inc=1;
                        break;
                    }
                }
                return_book();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void return_book() {

        LibraryManagment.child(student_id).child(key).child("books").setValue(new_bookrecord);
        if(new_bookrecord==0)
        {
            LibraryManagment.child(student_id).child(key).removeValue();
        }
        Toast.makeText(getContext(), "Books Returned.", Toast.LENGTH_SHORT).show();
        stop = 1;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new returnbook()).commit();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                scanresult = data.getExtras().getString("scanresult");
                if (scan_flag == 0) {
                    studentid.setText(scanresult.toString());
                } else {
                    bookid.setText(scanresult.toString());
                }
            }
        }
    }
}
