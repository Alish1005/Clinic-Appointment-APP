package com.example.myproject;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class ClinicDBDataSource {
    private SQLiteDatabase database;
    private ClinicDBHelper dbHelper;

    public ClinicDBDataSource(Context context){
        dbHelper = new ClinicDBHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }

//insert
    public boolean insertPatient(Patient p){
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("name", p.getName());
            initialValues.put("phonenumber", p.getPhoneNumber());
            initialValues.put("gender",p.getGender());
            initialValues.put("age", p.getAge());
            initialValues.put("addDate", String. valueOf (p.getAddDate().getTimeInMillis()));
//            initialValues.put("patientphoto", String.valueOf(p.getPatientImage()));

            didSucceed = database.insert("patient", null, initialValues) >0;
        }
        catch (Exception e) {
            Log.d(TAG, "insertContact: not complete");
        }
        return didSucceed;
    }

    public boolean insertAppointment(Appointment a){
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("patientID", a.getPatientID());
            initialValues.put("patientCase", a.getPatientCase());
            initialValues.put("discount", a.getDiscount());
            initialValues.put("price", a.getPrice());
            initialValues.put("Date_time", String. valueOf (a.getDateAndTime().getTimeInMillis()));


            didSucceed = database.insert("appointment", null, initialValues) >0;
        }
        catch (Exception e) {
            Log.d(TAG, "insertContact: not complete");
        }
        return didSucceed;
    }

    public boolean deleteAppointment(int id){
        boolean isDelete=false;
        try {
            isDelete=database.delete("appointment","id= "+id,null)>0;
        }catch (Exception e){

        }
        return  isDelete;
    }

    public boolean updatePatient(Patient p){
        boolean didSucceed = false;
        try {
            long rowId = (long) p.getPatientID();
            ContentValues updateValues = new ContentValues();
            updateValues.put("name", p.getName());
            updateValues.put("phonenumber", p.getPhoneNumber());
            updateValues.put("gender",p.getGender());
            updateValues.put("age", p.getAge());

            didSucceed = database.update("patient", updateValues,"id=" + rowId, null)>0;
        }
        catch (Exception e){
            Log.d(TAG, "updateContact: not complete");
        }
        return didSucceed;
    }
    public int getLastPatientId(){
        int lastId;
        try {

            String query = "select MAX(id) from patient";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }

    //Patient
    public Patient getPatientFromID(int id) {
        ArrayList<Patient> patients = new ArrayList<>();

        Patient Patient;
        try {
            String query = "Select * from patient where id = " + id;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            Patient = new Patient();
            Patient.setPatientID(cursor.getInt(0));
            Patient.setName(cursor.getString(1));
            Patient.setGender(cursor.getString(2));
            Patient.setAge(cursor.getString(3));
            Patient.setPhoneNumber(cursor.getString(4));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
            //

            Patient.setAddDate(calendar);
            patients.add(Patient);
            cursor.close();
        } catch (Exception e) {
            Patient = new Patient();
        }
        return Patient;
    }

    public ArrayList<Patient> getPatient(String sortField, String sortOrder) {
        ArrayList<Patient> patients=new ArrayList<>();
        try{
            String query="Select * from patient Order by "+sortField+" "+sortOrder;
            Cursor cursor =database.rawQuery(query,null);
            Patient newPatient;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newPatient=new Patient();
                newPatient.setPatientID(cursor.getInt(0));
                newPatient.setName(cursor.getString(1));
                newPatient.setGender(cursor.getString(2));
                newPatient.setAge(cursor.getString(3));
                newPatient.setPhoneNumber(cursor.getString(4));
                //
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
                newPatient.setAddDate(calendar);
                patients.add(newPatient);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            patients=new ArrayList<Patient>();
        }
        return patients;
    }

    public ArrayList<Patient> getPatientSearch(String sortField, String sortOrder,String search) {
        ArrayList<Patient> patients=new ArrayList<>();
        try{
            String query="Select *from patient Where name like '%"+search+"%' OR gender like '"+search+"%' OR age like '"+search+"%' OR phonenumber like '"+search+"%'  Order by "+sortField+" "+sortOrder;
            Cursor cursor =database.rawQuery(query,null);
            Patient newPatient;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newPatient=new Patient();
                newPatient.setPatientID(cursor.getInt(0));
                newPatient.setName(cursor.getString(1));
                newPatient.setGender(cursor.getString(2));
                newPatient.setAge(cursor.getString(3));
                newPatient.setPhoneNumber(cursor.getString(4));
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
                newPatient.setAddDate(calendar);
                patients.add(newPatient);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            patients=new ArrayList<Patient>();
        }
        return patients;
    }

    //Appointment
    public ArrayList<Appointment> getAppointment(String sortField, String sortOrder) {
        ArrayList<Appointment> appointments=new ArrayList<>();
        try{

            long now= (long) Calendar.getInstance().getTimeInMillis();

            //now+60000*5 that mean the appointment will disappear after 5 min from start the first app if the dr need to see the price
            String query="Select * from appointment as a inner join patient as p on a.patientID=p.id where a.Date_time > "+(long)now+"  Order by "+sortField+" "+sortOrder;
            Cursor cursor =database.rawQuery(query,null);
            Appointment newAppointment;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newAppointment=new Appointment();
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(2)));
                newAppointment.setAppointmentID(cursor.getInt(0));
                newAppointment.setPatientID(cursor.getInt(1));
                newAppointment.setDateAndTime(calendar);
                newAppointment.setPatientCase(cursor.getString(3));
                newAppointment.setDiscount(cursor.getDouble(4));
                newAppointment.setPrice(cursor.getDouble(5));
                appointments.add(newAppointment);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            appointments=new ArrayList<Appointment>();
        }
        return appointments;
    }
    public ArrayList<Appointment> getAppointmentSearch(String sortField, String sortOrder,String search) {
        ArrayList<Appointment> appointments=new ArrayList<>();
        String query;
        long now= (long) Calendar.getInstance().getTimeInMillis();
        try{
            query="Select *from appointment as a inner join patient as p on a.patientID=p.id Where a.Date_time > "+now+" And ( p.name like '%"+search+"%' OR p.age like '"+search+"%' OR p.phonenumber like '"+search+"%')  Order by "+sortField+" "+sortOrder;
            Cursor cursor =database.rawQuery(query,null);
            Appointment newAppointment;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newAppointment=new Appointment();
                newAppointment.setAppointmentID(cursor.getInt(0));
                newAppointment.setPatientID(cursor.getInt(1));
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(2)));
                newAppointment.setDateAndTime(calendar);
                newAppointment.setPatientCase(cursor.getString(3));
                newAppointment.setDiscount(cursor.getInt(4));
                newAppointment.setPrice(cursor.getInt(5));
                appointments.add(newAppointment);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            appointments=new ArrayList<Appointment>();
        }
        return appointments;
    }
    public int getLastAppointmentId(){
        int lastId;
        try {

            String query = "select MAX(id) from appointment";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<Appointment> getAppointmentHistory(int id) {
        ArrayList<Appointment> appointmentsHistory=new ArrayList<>();
        try{
            long now= (long) Calendar.getInstance().getTimeInMillis();

            String query="Select * from appointment where Date_time <= "+now+" And patientID= "+id+"  Order by Date_time Asc";
            Cursor cursor =database.rawQuery(query,null);
            Appointment newAppointment;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newAppointment=new Appointment();
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(2)));
                newAppointment.setAppointmentID(cursor.getInt(0));
                newAppointment.setPatientID(cursor.getInt(1));
                newAppointment.setDateAndTime(calendar);
                newAppointment.setPatientCase(cursor.getString(3));
                newAppointment.setDiscount(cursor.getDouble(4));
                newAppointment.setPrice(cursor.getDouble(5));
                appointmentsHistory.add(newAppointment);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            appointmentsHistory=new ArrayList<Appointment>();
        }
        return appointmentsHistory;
    }
    public boolean isTaken(long time,Context c){
        boolean istaken=false;

        try {
            long now = (long) Calendar.getInstance().getTimeInMillis();
            long duration =Long.parseLong(c.getSharedPreferences("Setting", MODE_PRIVATE)
                    .getString("ClinicDuration", String.valueOf(40*60000)));
            String query = "Select * from appointment where ("+time+" between Date_time And Date_time+duration) OR ("+time+" between Date_time-"+(long)(duration+60000) +" and Date_time)";
            Cursor cursor = database.rawQuery(query, null);


            if (cursor.getCount()>0){
                istaken=true;
            }
        }catch (Exception e){
            istaken=true;
        }

        return  istaken;
    }
}
