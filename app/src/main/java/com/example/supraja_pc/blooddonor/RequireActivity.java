package com.example.supraja_pc.blooddonor;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RequireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    //public static final String EXTRA_MESSAGE = "com.example.supraja_pc.blooddonor.MESSAGE";

    EditText user_patient_name,user_patient_age;
    Spinner spinner1;
    Button proceed;
    //String [][]strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_require);


        user_patient_name = findViewById(R.id.name);
        user_patient_age = findViewById(R.id.age);
        spinner1 = findViewById(R.id.bloodgroup1);
        proceed = findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String patient_name,patient_age,blood_group1;
                patient_name = user_patient_name.getText().toString();
                patient_age = user_patient_age.getText().toString();
                blood_group1 = spinner1.getSelectedItem().toString();
                if(user_patient_name.getText().toString().isEmpty() || user_patient_age.getText().toString().isEmpty()){
                    Toast.makeText(RequireActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(RequireActivity.this, DisplayActivity.class);
                    intent.putExtra("Patient_Name",patient_name);
                    intent.putExtra("Patient_Age",patient_age);
                    intent.putExtra("Blood_Group",blood_group1);
                    startActivity(intent);
                    finish();
                }
            }
        });

        spinner1.setOnItemSelectedListener(this);
        List<String> categories1 = new ArrayList<>();

        categories1.add("A+");
        categories1.add("A-");
        categories1.add("B+");
        categories1.add("B-");
        categories1.add("O+");
        categories1.add("O-");
        categories1.add("AB+");
        categories1.add("AB-");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories1);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter1);

    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // Showing selected spinner item
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
