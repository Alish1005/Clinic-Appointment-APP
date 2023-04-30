package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Appointment extends AppCompatActivity {
    private int AppointmentID;
    private int PatientID;
    private String PatientCase;
    private double Discount=0;
    private double Price=0;
    private Calendar DateAndTime;
    private int Duration;

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public Appointment() {
        AppointmentID = -1;
        Calendar c=Calendar.getInstance();
        c.set(c.get(1)-2,c.get(2),c.get(3));
        DateAndTime=c;
    }
////>>>For testing<<
    public Appointment(int id,int patientID, String patientCase, double discount, double price,int year,int month,int day,int hour,int min) {
        AppointmentID = id;
        PatientID = patientID;
        PatientCase = patientCase;
        Discount = discount;
        Price = Math.round((price-(price*discount/100)));

        Calendar c=Calendar.getInstance();
        c.set(year,month+1,day,hour,min);
        c.setTimeInMillis(Calendar.getInstance().getTimeInMillis()-1000);
        DateAndTime = c;
    }
    ////>>>For testing<<
    public Appointment(int id,int patientID, String patientCase, double discount, double price) {
        AppointmentID = -1;
        PatientID = patientID;
        PatientCase = patientCase;
        Discount = discount;
        Price = Math.round((price-price*discount/100)*100)/(double)100;
        Calendar c=Calendar.getInstance();
        c.set(2001,7,23,1,5);
        //c.setTimeInMillis(dateAndTime);
        DateAndTime = c;
    }

    public int getAppointmentID() {
        return AppointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        AppointmentID = appointmentID;
    }

    public int getPatientID() {
        return PatientID;
    }

    public void setPatientID(int patientID) {
        PatientID = patientID;
    }

    public String getPatientCase() {
        return PatientCase;
    }

    public void setPatientCase(String patientcase) {
        PatientCase = patientcase;
    }

    public double getDiscount() {
        return Discount;
    }



    public double getPrice() {
        return Math.round(Price);
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public void setPrice(double price) {
        if(Discount!=0)
        Price = Math.round(price*(1-(Discount)/100));
        else
            Price=price;
    }

    public Calendar getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(Calendar dateAndTime) {
        DateAndTime = dateAndTime;
    }
}
