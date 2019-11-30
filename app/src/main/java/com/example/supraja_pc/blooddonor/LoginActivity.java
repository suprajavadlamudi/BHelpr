package com.example.supraja_pc.blooddonor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.supraja_pc.blooddonor.MESSAGE";


    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cm.getActiveNetworkInfo();
        if(nf!=null && nf.isConnected()) {
            auth = FirebaseAuth.getInstance();
            setContentView(R.layout.activity_login);
            final EditText email = findViewById(R.id.email);
            final EditText password = findViewById(R.id.pass);
            Button login = findViewById(R.id.login);
            Button signup = findViewById(R.id.signup);

            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            progressDialog = new ProgressDialog(this);
            final FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentuser!=null){
                final String currentemail = currentuser.getEmail().toString();
                intent.putExtra(EXTRA_MESSAGE,currentemail);
                startActivity(intent);
                finish();
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String inputemail = email.getText().toString();
                    final String inputpassword = password.getText().toString();
                    if (TextUtils.isEmpty(inputemail)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(inputpassword)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    }

                    //authenticate user
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(inputemail, inputpassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        progressDialog.dismiss();
                                        if (inputpassword.length() < 6) {
                                            password.setError(getString(R.string.min_password));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        intent.putExtra(EXTRA_MESSAGE,inputemail);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this,"Check your Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }


}
