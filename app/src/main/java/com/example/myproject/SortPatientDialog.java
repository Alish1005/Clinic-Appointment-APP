package com.example.myproject;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SortPatientDialog extends DialogFragment {
    RadioGroup Sortby,Order;
    RadioButton Name,Age,Add,Asc,Desc;
    Button Done,Cancel;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sort_dialog, container);
        Sortby=view.findViewById(R.id.RGPSortby);
        Order=view.findViewById(R.id.RGSOrder);

        Name=view.findViewById(R.id.RBPName);
        Age=view.findViewById(R.id.RBPAge);
        Add=view.findViewById(R.id.RBPAdd);
        Asc=view.findViewById(R.id.RBAsc);
        Desc=view.findViewById(R.id.RBDecs);
        Done=view.findViewById(R.id.BDone);
        Cancel=view.findViewById(R.id.BCancel);


        initRefresh();
        initDoneButton();
        initCancelButton();
        return view;
    }
    public void initRefresh(){
        String sortField = getContext().getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("PSortby", "addDate");
        String sortOrder = getContext().getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("PSortOrder", "Desc");

        switch (sortField){
            case "name":
                Name.setChecked(true);
                break;
            case "age":
                Age.setChecked(true);
                break;
            case "addDate":
                Add.setChecked(true);
                break;
        }
        if(sortOrder.equalsIgnoreCase("Desc")){
            Desc.setChecked(true);
        }else{
            Asc.setChecked(true);
        }
    }
    public void initDoneButton(){
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.isChecked()){
                    getContext().getSharedPreferences("SortAndFilter",
                                    MODE_PRIVATE).edit()
                            .putString("PSortby","name").apply();
                }else if (Age.isChecked()){
                    getContext().getSharedPreferences("SortAndFilter",
                                    MODE_PRIVATE).edit()
                            .putString("PSortby","age").apply();
                }else{
                    getContext().getSharedPreferences("SortAndFilter",
                                    MODE_PRIVATE).edit()
                            .putString("PSortby","addDate").apply();
                }

                if (Asc.isChecked()){
                    getContext().getSharedPreferences("SortAndFilter",
                                    MODE_PRIVATE).edit()
                            .putString("PSortOrder","Asc").apply();
                }else{
                    getContext().getSharedPreferences("SortAndFilter",
                                    MODE_PRIVATE).edit()
                            .putString("PSortOrder","Desc").apply();
                }
                SortAndFilterListener activity= (SortAndFilterListener) getActivity();
                activity.didFinishSortAndFilterDialog();
                getDialog().dismiss();
            }
        });
    }
    public void initCancelButton(){
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }


    public interface SortAndFilterListener {
        void didFinishSortAndFilterDialog();
    }

    }
