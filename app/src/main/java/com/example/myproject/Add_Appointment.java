package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class Add_Appointment extends AppCompatActivity {

    Patient currentPatient;
    TextView name,gender,age,phone,DateP,TimeP,warning1;
    Button Save,SelectD,SelectT;
    EditText Case,Discount;
    Calendar cal=Calendar.getInstance();
    int Calyear=cal.get(Calendar.YEAR)
            ,Calmonth=cal.get(Calendar.MONTH)
            ,Calday=cal.get(Calendar.DAY_OF_MONTH)
            ,Calh=cal.get(Calendar.HOUR_OF_DAY)
            ,CalMin=cal.get(Calendar.MINUTE);
    Calendar SavedDate=Calendar.getInstance();
    Appointment currentAppointment=new Appointment();

    AlertDialog.Builder b;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        Bundle extras=getIntent().getExtras();
         int patientID=extras.getInt("patientID");
        ClinicDBDataSource ds=new ClinicDBDataSource(Add_Appointment.this);
        ds.open();
        currentPatient=ds.getPatientFromID(patientID);
        ds.close();
        initCreate();


        initDateButton();
        initSaveButton();
        initTimeButton();
    }
    public void initSaveButton(){
        Save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {



                ClinicDBDataSource ds = new ClinicDBDataSource(Add_Appointment.this);
                //***
                ds.open();
                if(ds.isTaken(SavedDate.getTimeInMillis(),Add_Appointment.this)){
                    b=new AlertDialog.Builder(Add_Appointment.this);
                    b.setTitle("Change Time")
                            .setMessage("This Appointment Time is already Taken please change it!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    b.show();
                    return;
                }
                ds.close();

                currentAppointment.setPatientID(currentPatient.getPatientID());
                double price =Double.parseDouble(getSharedPreferences("Setting", MODE_PRIVATE)
                        .getString("ClinicPrice", "120000"));

                currentAppointment.setPrice(price);
                if(!Discount.getText().toString().equalsIgnoreCase("")) {
                    currentAppointment.setDiscount(Double.parseDouble(Discount.getText().toString()));
                    if(Double.parseDouble(Discount.getText().toString())>100||Double.parseDouble(Discount.getText().toString())<0){
                        warning1.setVisibility(View.VISIBLE);
                        return;
                    }else{
                        warning1.setVisibility(View.INVISIBLE);
                    }


                }else
                    currentAppointment.setDiscount(0);
                    if(Discount.getText().toString().equalsIgnoreCase(""))
                        currentAppointment.setPatientCase("not mentioned");
                    else
                        currentAppointment.setPatientCase(Case.getText().toString());

                int duration =Integer.parseInt(getSharedPreferences("Setting", MODE_PRIVATE)
                        .getString("ClinicDuration", String.valueOf(40*60000)));
                    currentAppointment.setDuration(duration);
                    currentAppointment.setDateAndTime(SavedDate);

                boolean wasSucceesful;
                try {
                    ds.open();
                    if (currentAppointment.getAppointmentID() == -1) {
                        wasSucceesful = ds.insertAppointment(currentAppointment);
                        if (wasSucceesful) {
                            int newId = ds.getLastAppointmentId();
                            currentAppointment.setAppointmentID(newId);
                            Snackbar.make(v, "Appointment has been saved successfully", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            Save.setEnabled(false);

                        }else{
                            Snackbar.make(v, "Appointment fail!!!!!!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    ds.close();


                    b=new AlertDialog.Builder(Add_Appointment.this);
                    b.setTitle("Add it as an event")
                            .setMessage("Do you need to add this appointment as an event in your calender app to remind you?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(Add_Appointment.this, appoint_list.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);



                                    intent = new Intent(Intent.ACTION_INSERT);
                                    intent.setData(CalendarContract.Events.CONTENT_URI);
                                    intent.putExtra( CalendarContract.Events.TITLE,currentPatient.getName());
                                    intent.putExtra(CalendarContract.Events.DESCRIPTION,currentAppointment.getPatientCase());
                                    intent.putExtra(CalendarContract.Events.DTSTART,SavedDate.getTimeInMillis());
//                                    long duration =Long.parseLong(getSharedPreferences("Setting", MODE_PRIVATE)
//                                            .getString("ClinicDuration", "2400000"));
//                                    Calendar c= Calendar.getInstance();
//                                    long m=SavedDate.getTimeInMillis()+duration;
//                                    c.setTimeInMillis(m);
//                                    intent.putExtra(CalendarContract.Events.DTEND,c);
                                    intent.putExtra(CalendarContract.Events.ALL_DAY,false);
                                    if (intent.resolveActivity(Add_Appointment.this.getPackageManager())!= null) {
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Add_Appointment.this, "There is no app that can support this action Please install the app", Toast.LENGTH_SHORT).show();
                                        String url = "https://play.google.com/store/apps/details?id=com.google.android.calendar&hl=en&gl=US";
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                        }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Add_Appointment.this, appoint_list.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                    b.show();




                } catch (Exception e) {
                    wasSucceesful = false;
                }



            }
        });
    }
    public void initDateButton(){
        SelectD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp=new DatePickerDialog(Add_Appointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                                String date = i2 + "/" + i1+1 + "/" + i;
                                Calyear = i;
                                Calmonth = i1 ;
                                Calday = i2;

                                Calendar c1=Calendar.getInstance();
                                c1.set(Calendar.YEAR,i);
                                c1.set(Calendar.MONTH,i1);
                                c1.set(Calendar.DAY_OF_MONTH,i2);

                                //optional ***
//                                if (Calendar.getInstance().getTimeInMillis()> c1.getTimeInMillis()){
//                                    b=new AlertDialog.Builder(Add_Appointment.this);
//                                    b.setTitle("Change Date!")
//                                            .setMessage("You can't choose past date !!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                }
//                                            });
//                                    b.show();
//                            }else{
                                    SavedDate.set(Calyear,Calmonth,Calday);
                                    DateP.setText(date);
//                                }

                            }
                        }, Calyear, Calmonth, Calday);
                //***
                dp.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                Calendar c2 =Calendar.getInstance();
                c2.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR)+1);
                dp.getDatePicker().setMaxDate(c2.getTimeInMillis());
                //***
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3496FF")));
                dp.show();
            }
        });
    }


    public void initTimeButton(){
        int Start =Integer.parseInt(getSharedPreferences("Setting", MODE_PRIVATE)
                .getString("ClinicStart", "8"));
        SelectT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog Tp=new TimePickerDialog(Add_Appointment.this
                        , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int Start =Integer.parseInt(getSharedPreferences("Setting", MODE_PRIVATE)
                                .getString("ClinicStart", "8"));
                        int End = Integer.parseInt(getSharedPreferences("Setting", MODE_PRIVATE)
                                .getString("ClinicEnd", "18"));
                        if(hourOfDay>=End||hourOfDay<Start){
                          b=new AlertDialog.Builder(Add_Appointment.this);
                          b.setTitle("Change time!")
                                  .setMessage("Clinic not open at this time !!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                      }
                                  });
                          b.show();
                        }else {
                            Calendar c1=Calendar.getInstance();
                            c1.set(Calendar.YEAR,SavedDate.get(Calendar.YEAR));
                            c1.set(Calendar.MONTH,SavedDate.get(Calendar.MONTH));
                            c1.set(Calendar.DAY_OF_MONTH,SavedDate.get(Calendar.DAY_OF_MONTH));
                            c1.set(Calendar.HOUR_OF_DAY,SavedDate.get(Calendar.HOUR_OF_DAY));
                            c1.set(Calendar.MINUTE,SavedDate.get(Calendar.MINUTE));
                            if (Calendar.getInstance().getTimeInMillis()> c1.getTimeInMillis()){
                                b=new AlertDialog.Builder(Add_Appointment.this);
                                b.setTitle("Change Date!")
                                        .setMessage("You can't choose past date !!").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                b.show();
                            }else{
                                TimeP.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
                                SavedDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                SavedDate.set(Calendar.MINUTE,minute);
                            }
                        }
                    }
                },Start,0,true);
                if(SavedDate.getTimeInMillis()>Calendar.getInstance().getTimeInMillis()){
                    Tp.setTitle("Choose Time");

                }
                        Tp.show();
            }
        });
    }
    public void initCreate(){
        name=findViewById(R.id.TVNameAA);
        gender=findViewById(R.id.TVGenderAA);
        age=findViewById(R.id.TVAgeAA);
        phone=findViewById(R.id.TVPhoneAA);
        Save=findViewById(R.id.BSaveAA);
        SelectD=findViewById(R.id.BSelectDateAA);
        SelectT=findViewById(R.id.BSelectTime);
        DateP=findViewById(R.id.TVDateP);
        TimeP=findViewById(R.id.TVTimeP);
        Case=findViewById(R.id.ETCaseDesAA);
        Discount=findViewById(R.id.ETDiscountAA);
        warning1=findViewById(R.id.TVAWaring);



        name.setText(currentPatient.getName());
        age.setText(currentPatient.getAge());
        gender.setText(currentPatient.getGender());
        phone.setText(currentPatient.getPhoneNumber());
        int Start =Integer.parseInt(getSharedPreferences("Setting", MODE_PRIVATE)
                .getString("ClinicStart", "8"));
        DateP.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1)+"/"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        TimeP.setText(String.valueOf(Start)+":00");
        SavedDate.set(Calendar.HOUR_OF_DAY,Start);
        SavedDate.set(Calendar.MINUTE,0);
    }

}
