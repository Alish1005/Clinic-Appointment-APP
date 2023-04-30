package com.example.myproject;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 101;
    EditText firstName, LastName, Age, PhoneNB;
    TextView warning;
    Button Save,b;
    RadioGroup GenderRG;
    RadioButton MRB,FRB;
    Switch s;
    ImageButton Pl,Al,S,ib;
    ScrollView scrollView;
    Bundle extras;

    Patient currentPatient = new Patient();
    Appointment currentAppointment=new Appointment();
    private AppCompatActivity binding;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                        ib.setImageBitmap(scaledPhoto);
                        currentPatient.setPatientImage(photo);
                    }
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.editFirstName);
        LastName = findViewById(R.id.editLastName);
        Age = findViewById(R.id.editAge);
        PhoneNB =findViewById(R.id.editPhoneNB);
        Save = findViewById(R.id.buttonSave);
        FRB=findViewById(R.id.RBFemale);
        MRB=findViewById(R.id.RBMale);
        warning=findViewById(R.id.TVWarning);
        ib=findViewById(R.id.imagePatient);
        scrollView=findViewById(R.id.scrollView2);
        //warning.setVisibility(View.INVISIBLE);
        MRB.setChecked(true);
        s=findViewById(R.id.switch1);
        b=findViewById(R.id.button);
        s.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);

        extras=getIntent().getExtras();


        if(extras!=null){
            int patientID=extras.getInt("patientID");
            initContact(patientID);
            firstName.setEnabled(false);
            LastName.setEnabled(false);
        }
        initSaveButton();
        initImagebutton();
        initNavBar();
        //>>>>>>>>>>>>>>>>>>>test
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, appoint_list.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, Patient_List.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        //>>>>>>>>>>>>>>>>>>>>test
    }
    public void initContact(int id){
        ClinicDBDataSource ds=new ClinicDBDataSource(MainActivity.this);
        ds.open();
        currentPatient=ds.getPatientFromID(id);
        ds.close();
        currentPatient.setPatientID(id);
        firstName.setText(currentPatient.getName());
        Age.setText(currentPatient.getAge());
        if(currentPatient.getGender().equalsIgnoreCase("male")){
            MRB.setChecked(true);
        }else
            FRB.setChecked(true);
        PhoneNB.setText(currentPatient.getPhoneNumber());
    }

    public void initSaveButton(){
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(extras==null) {
                    if (LastName.getText().toString().equalsIgnoreCase("")) {
                        warning.setVisibility(View.VISIBLE);
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                        return;
                    }
                }
                //>>>>>>>>>>>>>>>>>>>>test
                if (s.isChecked()) {
                    currentAppointment.setPatientID(Integer.parseInt(LastName.getText().toString()));
                    currentAppointment.setPrice(Integer.parseInt(Age.getText().toString()));
                    currentAppointment.setDiscount(Integer.parseInt(PhoneNB.getText().toString()));
                    ClinicDBDataSource ds = new ClinicDBDataSource(MainActivity.this);
                    boolean wasSucceesful;
                    try {
                        ds.open();
                        if (currentAppointment.getAppointmentID() == -1) {
                            wasSucceesful = ds.insertAppointment(currentAppointment);
                            if (wasSucceesful) {
                                int newId = ds.getLastAppointmentId();
                                currentAppointment.setAppointmentID(newId);
                                Snackbar.make(view, "Appointment has been saved successfully", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                Save.setEnabled(false);
                                //Thread.sleep(3000);

                            }else{
                                Snackbar.make(view, "Appointment fail!!!!!!!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } else {
                             wasSucceesful = ds.updatePatient(currentPatient);

                        }
                        ds.close();
                        //To delay the trans
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Save.setEnabled(true);
                                refresh();
                                Intent intent = new Intent(MainActivity.this, Patient_List.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }, 650);

                    } catch (Exception e) {
                        wasSucceesful = false;
                    }
                } else {
                    if(extras==null)
                    if (firstName.getText().toString().equalsIgnoreCase("") || LastName.getText().toString().equalsIgnoreCase("")) {
                    warning.setVisibility(View.VISIBLE);
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    return;
                }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>test

                    currentPatient.setName(firstName.getText().toString() + " " + LastName.getText().toString());
                    currentPatient.setAge(Age.getText().toString());
                    currentPatient.setPhoneNumber(PhoneNB.getText().toString());
                    if (MRB.isChecked())
                        currentPatient.setGender("Male");
                    else
                        currentPatient.setGender("Female");

                    boolean wasSucceesful;
                    ClinicDBDataSource ds = new ClinicDBDataSource(MainActivity.this);
                    try {
                        ds.open();
                        if (currentPatient.getPatientID() == -1) {
                            wasSucceesful = ds.insertPatient(currentPatient);
                            if (wasSucceesful) {
                                int newId = ds.getLastPatientId();
                                currentPatient.setPatientID(newId);
                                Snackbar.make(view, "Patient has been saved successfully", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                Save.setEnabled(false);
                                //Thread.sleep(3000);

                            }
                        } else {
                            wasSucceesful = ds.updatePatient(currentPatient);
                            Snackbar.make(view, "Patient has been Updated successfully", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        ds.close();
                        //To delay the trans
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Save.setEnabled(true);
                                refresh();
                                Intent intent = new Intent(MainActivity.this, Patient_List.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }, 650);

                    } catch (Exception e) {
                        wasSucceesful = false;
                    }

                }
            }
        });
    }
    public  void refresh(){
        firstName.setText("");
        LastName.setText("");
        Age.setText(""); PhoneNB.setText("");
        MRB.setChecked(true);
        currentPatient=new Patient();

    }
    private void initImagebutton() {
        //   ImageButton ib = (ImageButton) findViewById(R.id.imagePatient);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
    }

    private void checkCameraPermission() {

        if (Build.VERSION.SDK_INT < 23) {
            takePhoto();
            return;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            takePhoto();
            return;
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            return;
        }
        Snackbar.make(findViewById(R.id.imagePatient), "The app requires permission to take contact photos"
                        , Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                    }
                })
                .show();
        currentPatient.setPatientImage(ib.getDrawingCache());

    }
    private void takePhoto() {
        activityResultLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    @SuppressLint("WrongViewCast")
    public void initNavBar(){
        Pl=findViewById(R.id.imageBtnPatientList);
        Al=findViewById(R.id.imageBtnAppList);
        S=findViewById(R.id.imageButtonSettings);

        Pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Patient_List.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, appoint_list.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


}