package com.example.myproject;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Patient extends AppCompatActivity {
    private int patientID;
    private String Name;
    private String phoneNumber;
    private String age;
    private String Gender;
    private Calendar addDate;
    private Bitmap PatientImage;

    public Bitmap getPatientImage() {
        return PatientImage;
    }

    public void setPatientImage(Bitmap patientImage) {
        PatientImage = patientImage;
    }

    public Patient(String name, String phoneNumber, String age, String gender) {
        Name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        Gender = gender;
        patientID = -1;
        addDate=Calendar.getInstance();
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public Patient(){
        patientID = -1;
        addDate=Calendar.getInstance();
    }

    public Calendar getAddDate() {
        return addDate;
    }

    public void setAddDate(Calendar addDate) {
        this.addDate = addDate;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int i) {
        this.patientID = i;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge(){
        return age;
    }
    public void setAge(String a){
        this.age=a;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
