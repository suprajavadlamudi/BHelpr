package com.example.supraja_pc.blooddonor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class DisplayActivity extends AppCompatActivity {
    FirebaseFirestore dd;
    DocumentReference document;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        final TextView textview = findViewById(R.id.details);
        final int[] i = {0};

      Intent intent = getIntent();
            final String PName = intent.getStringExtra("Patient_Name");
            final String PAge = intent.getStringExtra("Patient_Age");
            final String blood_group = intent.getStringExtra("Blood_Group");
        dd = FirebaseFirestore.getInstance();
        document = dd.collection("Donators").document();
        dd.collection("Donators").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                assert queryDocumentSnapshots != null;
                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    String blood = ds.getString("Blood Group");
                    if (blood_group.equals(blood)) {
                        String Name = ds.getString("Name");
                        String Email = ds.getString("Email");
                        String Age = ds.getString("Age");
                        String Gender = ds.getString("Gender");
                        String Blood_Group = ds.getString("Blood Group");
                        String Weight = ds.getString("Weight");
                        String Contact = ds.getString("Contact");
                        String Address = ds.getString("Address");
                        String City = ds.getString("City");
                        String State = ds.getString("State");
                        String Country = ds.getString("Country");
                         textview.append("Person "+(i[0] +1)+"\n\n"+
                                 "Name: "+Name + "\n" +
                                 "Email: "+ Email +"\n"+
                                 "Age: "+ Age + "\n" +
                                 "Gender: "+ Gender + "\n" +
                                 "Blood Group: "+ Blood_Group + "\n" +
                                 "Weight: "+ Weight + "\n" +
                                 "Contact: "+ Contact + "\n" +
                                 "Address: "+ Address + "\n" +
                                 "City: "+ City +"\n" +
                                 "State: "+ State + "\n" +
                                 "Country: "+ Country+"\n"+"\n");
                         i[0]++;

                    }
                }
                if(i[0]==0){
                    textview.setText("No Persons Available");
                }
                else{
                    HashMap<String , Object> receiver = new HashMap<>();
                    receiver.put("Name", PName);
                    receiver.put("Age", PAge);
                    receiver.put("Blood Group",blood_group);

                    document=dd.collection("Receivers").document();
                    document.set(receiver).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(DisplayActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DisplayActivity.this, "Error in Submitting Details", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }
}
