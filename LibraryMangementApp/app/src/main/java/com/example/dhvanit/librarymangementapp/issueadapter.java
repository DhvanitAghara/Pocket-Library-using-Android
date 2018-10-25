package com.example.dhvanit.librarymangementapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class issueadapter extends RecyclerView.Adapter<issueadapter.ViewHolder> {

    private ArrayList<issue> listItems;
    private Context context;
    String book_title,bookid;
    ViewHolder viewHolder;
    View v;
    int flag=0;

    public issueadapter(ArrayList<issue> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView studentid;
        public TextView quantity;
        public TextView booktitle;
        public TextView bookid;
        public TextView due_date;
        public TextView due_charge;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            booktitle = itemView.findViewById(R.id.booktitle);
            bookid = itemView.findViewById(R.id.bookid);
            quantity = itemView.findViewById(R.id.quantity);
            studentid = itemView.findViewById(R.id.studentid);
            due_date = itemView.findViewById(R.id.due_date);
            due_charge = itemView.findViewById(R.id.due_charge);

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_record_list,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        issue listitem = listItems.get(i);
        bookid=listitem.getBookid();


        viewHolder.bookid.setText(listitem.getBookid());
        viewHolder.studentid.setText(listitem.getStudentid());
        viewHolder.quantity.setText("" + listitem.getBooks());
        viewHolder.booktitle.setText(listitem.getBooktitle());
        viewHolder.due_date.setText("Due Date : "+listitem.getDue_date());
        viewHolder.due_charge.setText("Due Charge : "+listitem.getDue_charge());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

}
