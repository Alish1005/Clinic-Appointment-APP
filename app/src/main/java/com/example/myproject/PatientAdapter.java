package com.example.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter{
    private ArrayList<Patient> patients ;
    private View.OnLongClickListener mOnLongClick;
    private View.OnClickListener mOnClick;
    private View.OnLongClickListener mOnLongClickIB;
    private View.OnClickListener mOnClickIB;
    private Context pcontext;



    public PatientAdapter(ArrayList<Patient> patient, Context context){
        patients=patient;
        pcontext=context;
    }


    //inner class
    private  class PatientViewHolder extends RecyclerView.ViewHolder{

        //put all items in rec list
        public TextView tvPatient;
        public TextView tvPhone,tvGender,tvAge;
        public ImageButton IBP;
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            //put all items in rec list
            tvPatient=itemView.findViewById(R.id.TVName);
            tvPhone=itemView.findViewById(R.id.TVPhoneNumber);
            tvAge=itemView.findViewById(R.id.TVAge);
            tvGender=itemView.findViewById(R.id.TVGender);
            IBP=itemView.findViewById(R.id.IBPatient);
            //RecycleView
            IBP.setTag(this);
            IBP.setOnLongClickListener(mOnLongClickIB);
            IBP.setOnClickListener(mOnClickIB);

            itemView.setTag(this);
            itemView.setOnLongClickListener(mOnLongClick);
            itemView.setOnClickListener(mOnClick);
        }

        public ImageButton getIBP() {
            return IBP;
        }

        public TextView getTvPatient() {
            return tvPatient;
        }

        public TextView getTvPhone() {
            return tvPhone;
        }

        public TextView getTvGender() {
            return tvGender;
        }

        public TextView getTvAge() {
            return tvAge;
        }
    }
    //end inner class
    public void setOnItemLongClickListener(View.OnLongClickListener itemClickListener){
        mOnLongClick=itemClickListener;
    }

    public void setmOnLongClickIB(View.OnLongClickListener mOnLongClickIB) {
        this.mOnLongClickIB = mOnLongClickIB;
    }

    public void setmOnClickIB(View.OnClickListener mOnClickIB) {
        this.mOnClickIB = mOnClickIB;
    }

    public void setmOnClick(View.OnClickListener mOnClick) {
        this.mOnClick = mOnClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_patient_item,parent,false);
        return new PatientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PatientViewHolder cvh=(PatientViewHolder) holder;
        cvh.getTvPatient().setText(patients.get(position).getName());
       cvh.getTvAge().setText(patients.get(position).getAge());
        cvh.getTvGender().setText(patients.get(position).getGender());
       cvh.getTvPhone().setText(patients.get(position).getPhoneNumber());
//       if (!(patients.get(position).getPatientImage().equals("")||patients.get(position).getPatientImage().equals(null))){
//           cvh.getIBP().setImageBitmap(patients.get(position).getPatientImage());
//       }
//        cvh.getIBP().setImageBitmap(patients.get(position).getPatientImage());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
