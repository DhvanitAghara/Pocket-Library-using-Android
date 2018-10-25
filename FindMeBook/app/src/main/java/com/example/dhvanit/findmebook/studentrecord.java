package com.example.dhvanit.findmebook;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class studentrecord extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<issue> listitem;
    issue book;
    String current_date,due_date;
    Date date1 = null,date2 = null;

    DatabaseReference record_database = FirebaseDatabase.getInstance().getReference("Issue book record");

    private boolean checknet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkinfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkinfo != null && activeNetworkinfo.isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.studentrecord,container,false);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String message_id = pref.getString("message_id",null);

        getActivity().setTitle("Student Record");

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listitem = new ArrayList<issue>();

        if (!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        } else {


            record_database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listitem.clear();

                    for (DataSnapshot booksnapshot : dataSnapshot.getChildren()) {

                        for (DataSnapshot booksnapshot2 : booksnapshot.getChildren()) {

                            book = booksnapshot2.getValue(issue.class);
                            if(book.getStudentid().equals(message_id))
                            {
                                due_date=book.getDue_date();
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
                                    book.setDue_charge((int) elapsedDays);
                                }

                                listitem.add(book);
                                adapter = new issueadapter(listitem, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        }
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

