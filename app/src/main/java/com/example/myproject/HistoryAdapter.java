package com.example.myproject;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter{
    private ArrayList<Appointment> appointmentHistories ;
    private View.OnClickListener mOnClick;
    private Context pcontext;

    public HistoryAdapter(ArrayList<Appointment> h, Context context){
        appointmentHistories=h;
        pcontext=context;
    }


    //inner class
    private  class HistoryViewHolder extends RecyclerView.ViewHolder{

        //put all items in rec list
        public TextView tvDate;
        public TextView tvCase,tvDiscount,tvCost;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            //put all items in rec list
            tvDate=itemView.findViewById(R.id.TVDate);
            tvCase=itemView.findViewById(R.id.TVCase);
            tvDiscount=itemView.findViewById(R.id.TVDiscount2);
            tvCost=itemView.findViewById(R.id.TVCost);
            //RecycleView
            itemView.setTag(this);
            //itemView.setOnClickListener(mOnClick);
        }

        public TextView getTvDate() {
            return tvDate;
        }

        public TextView getTvCase() {
            return tvCase;
        }

        public TextView getTvDiscount() {
            return tvDiscount;
        }

        public TextView getTvCost() {
            return tvCost;
        }
    }
    //end inner class
    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnClick=itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_item,parent,false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryViewHolder cvh=(HistoryViewHolder) holder;
        ClinicDBDataSource ds =new ClinicDBDataSource(pcontext);
        ds.open();
        Patient patient=ds.getPatientFromID(appointmentHistories.get(position).getPatientID());
        cvh.getTvDate().setText(DateFormat.format("dd/MM/yyyy",appointmentHistories.get(position).getDateAndTime()));
        cvh.getTvCase().setText(appointmentHistories.get(position).getPatientCase());
        cvh.getTvDiscount().setText(String.valueOf(appointmentHistories.get(position).getDiscount()));
        cvh.getTvCost().setText(String.valueOf(appointmentHistories.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return appointmentHistories.size();
    }
}

