package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class appointment_history extends AppCompatActivity {

    ArrayList<Appointment> appointmentsHistory=new ArrayList<>();
    HistoryAdapter adapter;
    int patientID;
    Patient currentPatient=new Patient();

    TextView name,gender,age,phone,count;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);
        name=findViewById(R.id.TVNameH);
        gender=findViewById(R.id.TVGenderH);
        age=findViewById(R.id.TVAgeH);
        phone=findViewById(R.id.TVPNH);
        count=findViewById(R.id.TVCountAA);


    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras=getIntent().getExtras();
        patientID=extras.getInt("patientID");
        //remember to add the sorts in the getPatient and getSearch
        ClinicDBDataSource dataSource = new ClinicDBDataSource(this);

        try {
            //>>>For test<<<
            Appointment a=new Appointment();
            dataSource.open();
            appointmentsHistory = dataSource.getAppointmentHistory(patientID);
            currentPatient= dataSource.getPatientFromID(patientID);
            dataSource.close();
            name.setText(currentPatient.getName());
            gender.setText(currentPatient.getGender());
            age.setText(currentPatient.getAge());
            phone.setText(currentPatient.getPhoneNumber());
            //if there are > 0 then show the contact else so to the create contact
            if (appointmentsHistory.size() > 0) {
                RecyclerView rv = findViewById(R.id.recyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                rv.setLayoutManager(layoutManager);
                adapter = new HistoryAdapter(appointmentsHistory, appointment_history.this);

                rv.setAdapter(adapter);
                count.setText(String.valueOf(appointmentsHistory.size()));

            } else {
                Toast.makeText(this, "No History appointment",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(appointment_history.this, Patient_List.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error in retrieving data",
                    Toast.LENGTH_LONG).show();
        }
    }
}