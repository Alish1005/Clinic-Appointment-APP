package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class setting extends AppCompatActivity {
    ImageButton Pl,Al,S;
    Button save,help;
    EditText Start,End,Duration,Price;
    TextView w1,w2;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        save=findViewById(R.id.BSaveS);
        help=findViewById(R.id.BHelp);
        Start=findViewById(R.id.ETSH);
        End=findViewById(R.id.ETEH);
        Duration=findViewById(R.id.ETAD);
        Price=findViewById(R.id.ETAP);
        w1=findViewById(R.id.TVW1);
        w2=findViewById(R.id.TVW2);
        w1.setVisibility(View.INVISIBLE);
        scrollView=findViewById(R.id.ScrollSetting);
        initOnCreate();
        initSaveButton();
        initNavBar();
        initHelpButton();
    }
    @SuppressLint("WrongViewCast")
    public void initNavBar(){
        Pl=findViewById(R.id.imageBtnPatientList);
        Al=findViewById(R.id.imageBtnAppList);
        S=findViewById(R.id.imageButtonSettings);

        Pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, Patient_List.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, appoint_list.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initOnCreate(){
        String start =getSharedPreferences("Setting", MODE_PRIVATE)
                        .getString("ClinicStart", "8");
        String end =getSharedPreferences("Setting", MODE_PRIVATE)
                .getString("ClinicEnd", "18");
        String price =getSharedPreferences("Setting", MODE_PRIVATE)
                .getString("ClinicPrice", "120000");
        String duration =getSharedPreferences("Setting", MODE_PRIVATE)
                .getString("ClinicDuration", String.valueOf(40*60000));
        Start.setText(start);
        End.setText(end);
        Price.setText(price);
        Duration.setText(String.valueOf(Integer.parseInt(duration)/60000));
    }
    public void initHelpButton(){
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b;
                b=new AlertDialog.Builder(setting.this);
                b.setTitle("Help ❓")
                        .setMessage("[App Description]\n\nOur small clinic mobile application is designed specifically for patients of a Doctor, a highly skilled and experienced healthcare provider. The app allows patients to easily view and manage their upcoming appointments, view their medical information including lab results and medication lists, and securely message the Doctor with any questions or concerns they may have. It also provides easy-to-use symptom checker, which can help patients determine what issues they may need to discuss with Doctor during their appointment.\n\n[To add Patient]\ngo to 'Patient List' > press on 'Add'\n\n[To Add Appointment for a Patient]\ngo to 'Patient list' > short press on the patient you need\n\n[To Show Patient History]\ngo to 'Patient list' > long press on the patient you need\n\n[To Delete Appointment]\ngo to Appointment List > turn 'delete' switch on > press on the 'delete' button of the item you need to delete\n\n[To Change Clinic Setting]\ngo to 'setting' > edit what you need > press 'save'").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                b.show();
            }
        });

    }
    public void initSaveButton(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int start = Integer.parseInt(Start.getText().toString());
                    int end = Integer.parseInt(End.getText().toString());
                    double price = Double.parseDouble(Price.getText().toString());
                    int duration = Integer.parseInt(Duration.getText().toString());
                    boolean isW = false;

                    if (duration > 120) {
                        w2.setVisibility(View.VISIBLE);
                        isW = true;
                    }
                    if (start >= end || (start + (duration / 60000)) >= end || start > 23 || end > 23 || end < 1) {
                        w1.setVisibility(View.VISIBLE);
                        isW = true;
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                    if (isW) {
                        return;
                    }
                    w1.setVisibility(View.INVISIBLE);
                    w2.setVisibility(View.INVISIBLE);

                    getSharedPreferences("Setting", MODE_PRIVATE).edit()
                            .putString("ClinicStart", String.valueOf(start)).apply();

                    getSharedPreferences("Setting", MODE_PRIVATE).edit()
                            .putString("ClinicEnd", String.valueOf(end)).apply();

                    getSharedPreferences("Setting", MODE_PRIVATE).edit()
                            .putString("ClinicPrice", String.valueOf(price)).apply();

                    getSharedPreferences("Setting", MODE_PRIVATE).edit()
                            .putString("ClinicDuration", String.valueOf(duration * 60000)).apply();
                    Snackbar.make(v, "Setting has been saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }catch (Exception e){
                    initOnCreate();
                    Toast.makeText(setting.this, "Fill all the blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}