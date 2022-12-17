package com.example.edb.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.edb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        BottomNavigationView bottomNavigationView;
        EditText senderAccNum = findViewById(R.id.sender_acc_txt);
        EditText receiverAccNum = findViewById(R.id.receiver_acc_txt);
        EditText amount = findViewById(R.id.amount_txt);
        Button transferBtn = findViewById(R.id.transfer_btn);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.menu_transfer);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menu_wallet:
                    return true;
                case R.id.menu_transfer:
                    return true;
                case R.id.menu_profile:
                    return true;
            }
            return true;
        });

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}