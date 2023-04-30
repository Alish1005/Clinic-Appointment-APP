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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.api.client.javanet.NetHttpTransport;
import java.util.ArrayList;

public class Patient_List extends AppCompatActivity implements SortPatientDialog.SortAndFilterListener {

    ArrayList<Patient> patients;
    PatientAdapter adapter;
    Button add,sortAndFilter;
    EditText search;
    TextView no_result,TVcount,title;
    Appointment a=new Appointment();
    ImageButton Pl,Al,S;

    private View.OnClickListener onItemClickListenerIB= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder=(RecyclerView.ViewHolder) v.getTag();
            int position=viewHolder.getAdapterPosition();
            Patient p=patients.get(position);
            Intent intent=new Intent(Patient_List.this,MainActivity.class);
            int patientID=patients.get(position).getPatientID();
            intent.putExtra("patientID",patientID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    private View.OnLongClickListener onItemLongClickListener= new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            RecyclerView.ViewHolder viewHolder=(RecyclerView.ViewHolder) v.getTag();
            int position=viewHolder.getAdapterPosition();
            int patientID=patients.get(position).getPatientID();
            Intent intent=new Intent(Patient_List.this,appointment_history.class);
            intent.putExtra("patientID",patientID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return false;
        }
    };
    private View.OnClickListener onItemClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder=(RecyclerView.ViewHolder) v.getTag();
            int position=viewHolder.getAdapterPosition();
            Patient p=patients.get(position);
            Intent intent=new Intent(Patient_List.this,Add_Appointment.class);
            int patientID=patients.get(position).getPatientID();
            intent.putExtra("patientID",patientID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


//            Intent intent = new Intent(Intent.ACTION_INSERT);
//            intent.setData(CalendarContract.Events.CONTENT_URI);
//            intent.putExtra( CalendarContract.Events.TITLE,p.getName());
//            intent.putExtra(CalendarContract.Events.ALL_DAY,false);

            //>>>>>>>>>>>>>>>>>>Test
//            if (intent.resolveActivity(Patient_List.this.getPackageManager())!= null) {
//                startActivity(intent);
//
//            } else {
//                Toast.makeText(Patient_List.this, "There is no app that can support this action",
//                        Toast.LENGTH_SHORT).show();
//                String url = "https://play.google.com/store/apps/details?id=com.google.android.calendar&hl=en&gl=US";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>.test
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        add=findViewById(R.id.BAdd);
        search=findViewById(R.id.ETSearchPat);
        sortAndFilter=findViewById(R.id.BSortPat);
        no_result=findViewById(R.id.TVNoresult);
        no_result.setVisibility(View.INVISIBLE);
        TVcount=findViewById(R.id.TVCount);
        initAddButton();
        initSearch();
        initSortAndFilterButton();
        initNavBar();


    }





    @Override
    protected void onResume() {
        super.onResume();

        //remember to add the sorts in the getPatient and getSearch
        String sortField = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("PSortby", "addDate");
        String sortOrder = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("PSortOrder", "Desc");
        ClinicDBDataSource dataSource = new ClinicDBDataSource(this);
        try {
            dataSource.open();
            patients = dataSource.getPatient(sortField, sortOrder);

            dataSource.close();
            //if there are > 0 then show the contact else so to the create contact
            if (patients.size() > 0) {
                RecyclerView rv = findViewById(R.id.RVPat);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                rv.setLayoutManager(layoutManager);
                adapter = new PatientAdapter(patients, Patient_List.this);
                adapter.setOnItemLongClickListener(onItemLongClickListener);
                adapter.setmOnClickIB(onItemClickListenerIB);
                adapter.setmOnClick(onItemClickListener);
                rv.setAdapter(adapter);
                TVcount.setText("Count : "+patients.size());
            } else {
                Intent intent = new Intent(Patient_List.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error in retrieving data",
                    Toast.LENGTH_LONG).show();
        }
    }
    public  void initAddButton(){
        add=findViewById(R.id.BAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_List.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
    public  void initSearch(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ClinicDBDataSource dataSource = new ClinicDBDataSource(Patient_List.this);
                String sortField = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                        .getString("PSortby", "addDate");
                String sortOrder = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                        .getString("PSortOrder", "Desc");
                try {
                    dataSource.open();
                    patients = dataSource.getPatientSearch(sortField, sortOrder,search.getText().toString());
                    dataSource.close();
                    RecyclerView rv = findViewById(R.id.RVPat);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Patient_List.this);
                    rv.setLayoutManager(layoutManager);
                    adapter = new PatientAdapter(patients, Patient_List.this);
                    adapter.setOnItemLongClickListener(onItemLongClickListener);
                    adapter.setmOnClick(onItemClickListener);
                    rv.setAdapter(adapter);
                    if(patients.size()==0) {
                        no_result.setVisibility(View.VISIBLE);
                        TVcount.setVisibility(View.INVISIBLE);
                    }else {
                        no_result.setVisibility(View.INVISIBLE);
                        TVcount.setVisibility(View.VISIBLE);
                        TVcount.setText("Count : "+patients.size());
                    }
                } catch (Exception e) {
                    Toast.makeText(Patient_List.this, "Error in retrieving data",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void initSortAndFilterButton(){
        sortAndFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to create a dialog we must create a FragmentManager
                FragmentManager fm=getSupportFragmentManager();
                //create object from the dialog class we made
                SortPatientDialog dialog = new SortPatientDialog();
                //to pop up the dialog
                dialog.show(fm,"DatePicker");
            }
        });
    }

    @Override
    public void didFinishSortAndFilterDialog() {
        String sortField = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("PSortby", "addDate");
        String sortOrder = getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                .getString("PSortOrder", "Desc");
        ClinicDBDataSource dataSource = new ClinicDBDataSource(this);
        try {
            dataSource.open();
            patients = dataSource.getPatientSearch(sortField, sortOrder,search.getText().toString());
            dataSource.close();
            RecyclerView rv = findViewById(R.id.RVPat);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rv.setLayoutManager(layoutManager);
            adapter = new PatientAdapter(patients, Patient_List.this);
            adapter.setOnItemLongClickListener(onItemLongClickListener);
            adapter.setmOnClick(onItemClickListener);
            rv.setAdapter(adapter);
        }catch (Exception e){

        }
    }
    @SuppressLint("WrongViewCast")
    public void initNavBar(){
        Pl=findViewById(R.id.imageBtnPatientList);
        Al=findViewById(R.id.imageBtnAppList);
        S=findViewById(R.id.imageButtonSettings);

        Al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Patient_List.this, appoint_list.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Patient_List.this, setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

}