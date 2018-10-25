package com.example.dhvanit.librarymangementapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class bookadapter extends RecyclerView.Adapter<bookadapter.ViewHolder> {

    private ArrayList<addbook> listItems;
    private Context context;
    View v;

    public bookadapter(ArrayList<addbook> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView booktitle;
        public TextView bookid;
        public TextView bookauthor;
        public TextView bookstock;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            booktitle = itemView.findViewById(R.id.booktitle);
            bookid = itemView.findViewById(R.id.studentid);
            bookauthor = itemView.findViewById(R.id.bookauthor);
            bookstock = itemView.findViewById(R.id.bookstock);

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_details_list,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        addbook listitem = listItems.get(i);
        viewHolder.booktitle.setText(listitem.getBooktitle());
        viewHolder.bookid.setText(listitem.getBookid());
        viewHolder.bookauthor.setText(listitem.getBookauthor());
        viewHolder.bookstock.setText("" + listitem.getNumberofbooks());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

}
