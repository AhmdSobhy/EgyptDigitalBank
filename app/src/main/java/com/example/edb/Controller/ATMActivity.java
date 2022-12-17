package com.example.edb.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.edb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ATMActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm);


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menu_wallet:
                    return true;
                case R.id.menu_transfer:
                    startActivity(new Intent(getApplicationContext(), TransferActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                case R.id.menu_profile:
                    return true;
            }
            return true;
        });
    }
}