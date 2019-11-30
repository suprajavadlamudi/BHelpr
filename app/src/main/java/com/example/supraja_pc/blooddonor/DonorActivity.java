package com.example.supraja_pc.blooddonor;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class DonorActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.supraja_pc.blooddonor.MESSAGE";
    FirebaseFirestore db;
    DocumentReference documentReference;
    private ProgressDialog progressDialog;

    EditText user_name,user_email,user_age,user_weight,user_contact,user_address,user_city,user_state,user_country;
    Spinner user_gender,user_blood_group;
    Button submit1;
    String text;
    Boolean flag1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_age = findViewById(R.id.age);
        user_gender = findViewById(R.id.sex);
        user_blood_group = findViewById(R.id.bloodgroup);
        user_weight = findViewById(R.id.weight);
        user_contact = findViewById(R.id.contact);
        user_address = findViewById(R.id.addresss);
        user_city = findViewById(R.id.city);
        user_state = findViewById(R.id.state);
        user_country = findViewById(R.id.country);
        submit1 = findViewById(R.id.submit);
        progressDialog = new ProgressDialog(this);
        final int[] i = new int[1];

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name,email,age,gender,blood_group,weight,contact,address,city,state,country;

                name = user_name.getText().toString();
                email = user_email.getText().toString();
                age = user_age.getText().toString();
                final int a = Integer.parseInt(age);
                gender = user_gender.getSelectedItem().toString();
                blood_group = user_blood_group.getSelectedItem().toString();
                weight = user_weight.getText().toString();
                final int w = Integer.parseInt(weight);
                contact = user_contact.getText().toString();
                address = user_address.getText().toString();
                city = user_city.getText().toString();
                state = user_state.getText().toString();
                country = user_country.getText().toString();

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();
                if(name.isEmpty() || email.isEmpty()||age.isEmpty()||weight.isEmpty()|| contact.isEmpty()||address.isEmpty()||city.isEmpty()||state.isEmpty()||country.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(DonorActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
                if(w<50 || a<18){
                    progressDialog.dismiss();
                    Toast.makeText(DonorActivity.this,"You are Not Eligible for Donating Blood. Please Go Through Info",Toast.LENGTH_SHORT).show();
                }
                if(contact.length()!=10){
                    progressDialog.dismiss();
                    Toast.makeText(DonorActivity.this,"Incorrect Contact",Toast.LENGTH_SHORT).show();
                }
                else {
                    flag1 = false;
                    db = FirebaseFirestore.getInstance();
                    documentReference = db.collection("Donators").document();
                    db.collection("Donators").addSnapshotListener(new EventListener<QuerySnapshot>() {
                      @Override
                      public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                          if (queryDocumentSnapshots != null) {
                              OUTERMOST:for(DocumentSnapshot ds : queryDocumentSnapshots){
                                  String demail = ds.getString("Email");
                                  assert demail != null;
                                  if(demail.equals(email)){
                                      flag1=true;
                                      Log.d("flag1", String.valueOf(flag1));
                                     break OUTERMOST;
                                  }
                              }
                          }
                      }
                  });

                  if(flag1){
                      Toast.makeText(DonorActivity.this,"Email already exists",Toast.LENGTH_SHORT).show();
                  }
                   else{
                        HashMap<String , Object> donor = new HashMap<>();
                        donor.put("Name", name);
                        donor.put("Email", email);
                        donor.put("Age", age);
                        donor.put("Gender", gender);
                        donor.put("Blood Group", blood_group);
                        donor.put("Weight", weight);
                        donor.put("Contact", contact);
                        donor.put("Address", address);
                        donor.put("City", city);
                        donor.put("State", state);
                        donor.put("Country", country);

                      final FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                        documentReference.set(donor).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                progressDialog.dismiss();
                                Toast.makeText(DonorActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DonorActivity.this, MainActivity.class);
                                intent.putExtra(EXTRA_MESSAGE,currentuser.getEmail());
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(DonorActivity.this, "Error in Submitting Details", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DonorActivity.this, MainActivity.class);
                                intent.putExtra(EXTRA_MESSAGE,currentuser.getEmail());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }

            }
        });
        // Spinner for sex

        List<String> categories = new ArrayList<String>();
        //categories.add("Gender");
        categories.add("Male");
        categories.add("Female");
        categories.add("Others");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_gender.setAdapter(dataAdapter);

        //user_gender.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        //gender = text;

        //spinner for blood group

        List<String> categories1 = new ArrayList<String>();

        //categories1.add("Blood Group");
        categories1.add("A+");
        categories1.add("A-");
        categories1.add("B+");
        categories1.add("B-");
        categories1.add("O+");
        categories1.add("O-");
        categories1.add("AB+");
        categories1.add("AB-");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        user_blood_group.setAdapter(dataAdapter1);

       // user_blood_group.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
       // blood_group = text;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // Showing selected spinner item
       // text = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
