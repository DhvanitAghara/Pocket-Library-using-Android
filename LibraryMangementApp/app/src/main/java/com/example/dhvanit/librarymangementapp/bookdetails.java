package com.example.dhvanit.librarymangementapp;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bookdetails extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<addbook> listitem;
    addbook book;

    DatabaseReference books_database = FirebaseDatabase.getInstance().getReference("Books Storage");

    private boolean checknet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bookdetails, container, false);

        getActivity().setTitle("Book Details");

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listitem = new ArrayList<addbook>();

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        } else {


            books_database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listitem.clear();

                    for (DataSnapshot booksnapshot : dataSnapshot.getChildren()) {
                        book = booksnapshot.getValue(addbook.class);

                        listitem.add(book);
                        adapter = new bookadapter(listitem, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
        return root;
    }
}
