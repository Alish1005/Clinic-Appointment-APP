package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class appoint_list extends AppCompatActivity implements SortAppointmentDialog.SortAndFilterListener{

    ArrayList<Appointment> appointments=new ArrayList<>();
    AppointmentAdapter adapter;
    Button sort;
    EditText search;
    TextView no_result,TVcount;
    Switch delete;
    ImageButton Pl,Al,S;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_list);
        search=findViewById(R.id.ETS);
        TVcount=findViewById(R.id.TVC);
        no_result=findViewById(R.id.TVNoResultA);
        delete=findViewById(R.id.SDelete);
        sort=findViewById(R.id.BSortA);

        initSearch();
        initDeleteSwitch();
        initSortAndFilterButton();
        initNavBar();
    }
    @SuppressLint("WrongViewCast")
    public void initNavBar(){
        Pl=findViewById(R.id.imageBtnPatientList);
        Al=findViewById(R.id.imageBtnAppList);
        S=findViewById(R.id.imageButtonSettings);

        Pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appoint_list.this, Patient_List.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appoint_list.this, setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        //remember to add the sorts in the getPatient and getSearch
        String sortField = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("ASortby", "a.Date_time");
        String sortOrder = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("ASortOrder", "Desc");
        ClinicDBDataSource dataSource = new ClinicDBDataSource(this);

        try {
            //>>>For test<<<
            Appointment a=new Appointment();
            dataSource.open();
            appointments = dataSource.getAppointment(sortField, sortOrder);
            dataSource.close();
            //if there are > 0 then show the contact else so to the create contact
            if (appointments.size() > 0) {
                RecyclerView rv = findViewById(R.id.RVA);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                rv.setLayoutManager(layoutManager);
                adapter = new AppointmentAdapter(appointments, appoint_list.this);
                //adapter.setOnItemClickListener(onItemClickListener);
                rv.setAdapter(adapter);
                TVcount.setText("Count : "+String.valueOf(appointments.size()));


            } else {
                Intent intent = new Intent(appoint_list.this, Patient_List.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error in retrieving data",
                    Toast.LENGTH_LONG).show();
        }
    }
    public  void initSearch(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ClinicDBDataSource dataSource = new ClinicDBDataSource(appoint_list.this);
                String sortField = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                        .getString("ASortby", "a.Date_time");
                String sortOrder = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                        .getString("ASortOrder", "Desc");
                try {
                    dataSource.open();
                    appointments = dataSource.getAppointmentSearch(sortField, sortOrder, search.getText().toString());
                    dataSource.close();
                    RecyclerView rv = findViewById(R.id.RVA);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(appoint_list.this);
                    rv.setLayoutManager(layoutManager);
                    adapter = new AppointmentAdapter(appointments, appoint_list.this);
                    //adapter.setOnItemClickListener(onItemClickListener);
                    rv.setAdapter(adapter);
                    if (appointments.size() == 0) {
                        no_result.setVisibility(View.VISIBLE);
                        TVcount.setVisibility(View.INVISIBLE);
                    } else {
                        no_result.setVisibility(View.INVISIBLE);
                        TVcount.setVisibility(View.VISIBLE);
                        TVcount.setText("Count : " + appointments.size());
                    }
                } catch (Exception e) {
                    Toast.makeText(appoint_list.this, "Error in retrieving data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initDeleteSwitch(){
        delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.SetDelete(buttonView.isChecked());
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void initSortAndFilterButton(){
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to create a dialog we must create a FragmentManager
                FragmentManager fm=getSupportFragmentManager();
                //create object from the dialog class we made
                SortAppointmentDialog dialog = new SortAppointmentDialog();
                //to pop up the dialog
                dialog.show(fm,"SortAppo");
            }
        });
    }

    @Override
    public void didFinishSortAndFilterDialog() {
        String sortField = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("ASortby", "a.Date_time");
        String sortOrder = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("ASortOrder", "Desc");
        ClinicDBDataSource dataSource = new ClinicDBDataSource(this);
        try {
            dataSource.open();
            appointments = dataSource.getAppointmentSearch(sortField, sortOrder,search.getText().toString());
            dataSource.close();
            RecyclerView rv = findViewById(R.id.RVA);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rv.setLayoutManager(layoutManager);
            adapter = new AppointmentAdapter(appointments, appoint_list.this);

            rv.setAdapter(adapter);
        }catch (Exception e){

        }
    }

}