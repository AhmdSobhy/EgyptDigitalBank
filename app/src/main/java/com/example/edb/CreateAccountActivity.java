package com.example.edb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText fullNameTxt = findViewById(R.id.full_name_txt);
        EditText addressTxt = findViewById(R.id.address_txt);
        EditText mobileNumTxt = findViewById(R.id.mobile_num_txt);
        EditText emailTxt = findViewById(R.id.email_txt);
        EditText nationalIDTxt = findViewById(R.id.id_txt);
        EditText passwordTxt = findViewById(R.id.password_txt);
        EditText rePasswordTxt = findViewById(R.id.re_password_txt);
        Button createAccBtn = findViewById(R.id.create_acc_btn);

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}