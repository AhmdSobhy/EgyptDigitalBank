package com.example.edb.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.edb.Model.User;
import com.example.edb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class
MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = (User) getIntent().getSerializableExtra("user");
        getIntent().putExtra("user",user);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new HomeFragment()).commit();

        //Bottom Nav Bar---------------------------------------------------------------------------

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
                case R.id.menu_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).addToBackStack("HomeFragment").commit();
                    return true;
            }
            return true;
        });

    }
}