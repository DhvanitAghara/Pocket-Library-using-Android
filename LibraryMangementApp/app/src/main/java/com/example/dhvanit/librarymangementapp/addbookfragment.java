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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class addbookfragment extends Fragment {

    EditText bookid;
    EditText booktitle;
    Spinner category;
    EditText bookauthor;
    EditText bookpublisher;
    EditText numberofbooks;
    Button addbook;
    Button scan_bookid;

    String book_id,book_title,book_author,book_publisher,book_category,stringbook;
    int number_of_books=0;
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
        View root =  inflater.inflate(R.layout.addbook,container,false);
        getActivity().setTitle("Add Book");

        if(!checknet()) {
            Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }
        LibraryManagment = FirebaseDatabase.getInstance().getReference("Books Storage");


         bookid = (EditText) root.findViewById(R.id.studentid);
         booktitle = (EditText) root.findViewById(R.id.booktitle);
         bookauthor = (EditText) root.findViewById(R.id.bookauthor);
         bookpublisher = (EditText) root.findViewById(R.id.bookpublisher);
         numberofbooks = (EditText) root.findViewById(R.id.stock);
         category = (Spinner) root.findViewById(R.id.bookcategory);
         addbook=(Button) root.findViewById(R.id.addbook);
         scan_bookid=(Button) root.findViewById(R.id.scan_bookid);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                book_category = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Please Select Book Category", Toast.LENGTH_SHORT).show();
            }
        });

        scan_bookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), scan.class);
                startActivityForResult(i,1);
            }
        });

        bookid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                book_id = bookid.getText().toString().trim();
                check();

            }
        });

        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checknet())
                {
                    Toast.makeText(getContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    book_id = bookid.getText().toString().trim();
                    book_title = booktitle.getText().toString().trim();
                    book_author = bookauthor.getText().toString().trim();
                    book_publisher = bookpublisher.getText().toString().trim();
                    stringbook = numberofbooks.getText().toString().trim();
                    check();

                    if (flag == 0) {
                        addbook();
                    }
                }
            }
        });

        return root;
    }

    public void check()
    {

        LibraryManagment = FirebaseDatabase.getInstance().getReference("Books Storage");
        LibraryManagment.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag=0;
                String dataid="";

                for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){

                    dataid=usersnapshot.child("bookid").getValue(String.class);

                    if(book_id.equals(dataid) && stop==0) {
                        Toast.makeText(getContext(), "Book is already Exist.", 50).show();
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


    public void addbook(){

        if(TextUtils.isEmpty(book_id))
        {
            Toast.makeText(getContext(),"Enter Book ID",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(book_title))
        {
            Toast.makeText(getContext(),"Enter Book Title",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(book_category) || book_category.equals("Book Category"))
        {
            Toast.makeText(getContext(),"Select Book Category",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(book_author))
        {
            Toast.makeText(getContext(),"Enter Book Author",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(book_publisher))
        {
            Toast.makeText(getContext(),"Enter Book Publisher",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(stringbook))
        {
            Toast.makeText(getContext(),"Enter Number of Books",Toast.LENGTH_SHORT).show();
        }
        else {
            number_of_books = Integer.parseInt(stringbook);
        if(number_of_books==0)
        {
            Toast.makeText(getContext(),"Number of books should not be 0",Toast.LENGTH_SHORT).show();
        }
        else {
            addbook book = new addbook(book_id, book_title, book_category, book_author, book_publisher, number_of_books);
            LibraryManagment.child(book_id).setValue(book);
            Toast.makeText(getContext(), "Books Added", Toast.LENGTH_SHORT).show();
            stop = 1;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new addbookfragment()).commit();
                    /*Intent i = new Intent(getActivity(), dashboard.class);
                    startActivity(i);
                    ((Activity) getActivity()).overridePendingTransition(0,0);*/
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode != RESULT_OK) {
                String scanresult = data.getExtras().getString("scanresult");
                    bookid.setText(scanresult.toString());
            }
        }
    }
}
