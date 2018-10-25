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

public class addexistingbook extends Fragment {

    EditText bookid;
    EditText stock;
    Button add,scan;

    String book_id,books_string;
    int books,ava_books;
    int stop=0,flag=0;
    DatabaseReference books_storages = FirebaseDatabase.getInstance().getReference("Books Storage");

    private boolean checknet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_existing_book, container, false);
        getActivity().setTitle("Add Existing Book");

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }
        else{
            bookid = (EditText) root.findViewById(R.id.studentid);
            stock = (EditText) root.findViewById(R.id.stock);
            add = (Button) root.findViewById(R.id.add);
            scan = (Button) root.findViewById(R.id.scan_bookid);

            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), scan.class);
                    startActivityForResult(i, 1);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checknet()) {
                        Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        stop=0;
                        book_id = bookid.getText().toString().trim();
                        books_string = stock.getText().toString().trim();


                        if (TextUtils.isEmpty(book_id)) {
                            Toast.makeText(getContext(), "Enter Book ID", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(books_string)) {
                            Toast.makeText(getContext(), "Enter Number of books", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            books=Integer.parseInt(books_string);
                            if(books==0){
                                Toast.makeText(getContext(), "Number of books should not be 0", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                checkbookid();
                            }
                        }
                    }
                }
            });
        }
        return root;
    }

    private void checkbookid() {

        books_storages.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag=0;
                String dataid="";

                for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                    dataid=usersnapshot.child("bookid").getValue(String.class);
                    if(book_id.equals(dataid) && stop==0  ) {
                        ava_books=usersnapshot.child("numberofbooks").getValue(Integer.class);
                        books=books+ava_books;
                        flag=1;
                        break;
                    }
                }
                if (flag == 0) {
                    Toast.makeText(getContext(), "Book is not available", Toast.LENGTH_SHORT).show();
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
        books_storages.child(book_id).child("numberofbooks").setValue(books);
        Toast.makeText(getContext(), "Books added.", Toast.LENGTH_SHORT).show();
        stop = 1;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new addexistingbook()).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                String scanresult = data.getExtras().getString("scanresult");
                    bookid.setText(scanresult.toString());
            }
        }
    }
}

