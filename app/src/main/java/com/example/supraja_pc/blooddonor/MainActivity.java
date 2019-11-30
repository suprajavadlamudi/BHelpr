package com.example.supraja_pc.blooddonor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); Intent intent = getIntent();
        String activeuser = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        String[] separated = activeuser.split("@");


        TextView user = findViewById(R.id.user);
        user.setText("Hello  "+separated[0] +"\n");

        ImageButton donor=findViewById(R.id.donatebutton);
        ImageButton require = findViewById(R.id.requirebutton);
        ImageButton info = findViewById(R.id.infobutton);

        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DonorActivity.class);
                startActivity(intent);
            }
        });

        require.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,RequireActivity.class);
                startActivity(intent1);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,Info.class);
                startActivity(intent2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signout_id:
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();

        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
