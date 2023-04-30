package com.example.myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter {
    private ArrayList<Appointment> appointments ;
    private View.OnClickListener mOnClick;
    private boolean isDeleting;
    private Context pcontext;


    public  AppointmentAdapter(ArrayList<Appointment> appointment, Context context){
        appointments=appointment;
        pcontext=context;
    }
    //inner class
    private  class AppointmentViewHolder extends RecyclerView.ViewHolder{

        //put all items in rec list
        public TextView tvName;
        public TextView tvDate,tvPrice,tvDiscount;
        public Button deleteButton;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            //put all items in rec list
            tvName=itemView.findViewById(R.id.TVName);
            tvDate=itemView.findViewById(R.id.TVDate);
            tvPrice=itemView.findViewById(R.id.TVPrice);
            tvDiscount=itemView.findViewById(R.id.TVDiscount);
            deleteButton=itemView.findViewById(R.id.DeleteBtn);
            itemView.setTag(this);
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvDate() {
            return tvDate;
        }


        public TextView getTvPrice() {
            return tvPrice;
        }

        public TextView getTvDiscount() {
            return tvDiscount;
        }
    }
    //end inner class

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnClick=itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointment_item,parent,false);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AppointmentViewHolder cvh=(AppointmentViewHolder) holder;
        ClinicDBDataSource ds =new ClinicDBDataSource(pcontext);
        ds.open();
        Patient patient=ds.getPatientFromID(appointments.get(position).getPatientID());
        cvh.getTvName().setText(patient.getName());
        cvh.getTvDate().setText(DateFormat.format("dd/MM/yyyy hh:mm a",appointments.get(position).getDateAndTime()));
        cvh.getTvPrice().setText(String.valueOf(appointments.get(position).getPrice()));
        cvh.getTvDiscount().setText(String.valueOf(appointments.get(position).getDiscount()));
        if(isDeleting)
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
        else
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position);
            }
        });

    }
    public void SetDelete(boolean s){
        isDeleting=s;
    }

    private  void deleteItem(int position){
        Appointment a=appointments.get(position);
        ClinicDBDataSource ds=new ClinicDBDataSource(pcontext);
        boolean didDelete;
        try{
            ds.open();
            didDelete=ds.deleteAppointment(a.getAppointmentID());
            ds.close();
            if(didDelete){
                appointments.remove(position);
                Toast.makeText(pcontext, "Appointment has been Deleted", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }else{
                Toast.makeText(pcontext, "Delete Fail !!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(pcontext, "Delete Fail !!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

}
