package com.example.edb.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edb.API.ApiInterface;
import com.example.edb.API.ApiUrl;
import com.example.edb.Model.Account;
import com.example.edb.Model.User;
import com.example.edb.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateAccountActivity extends AppCompatActivity {

    ArrayAdapter<String> accountTypes, genderTypes;

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
        AutoCompleteTextView genderType = findViewById(R.id.gender_txt);
        AutoCompleteTextView accountType = findViewById(R.id.acc_type_txt);

        String[] gender = {"Male", "Female"};
        String[] items = {"Saving", "Current"};

        genderTypes = new ArrayAdapter<String>(this, R.layout.list_item, gender);
        genderType.setAdapter(genderTypes);


        accountTypes = new ArrayAdapter<String>(this, R.layout.list_item, items);
        accountType.setAdapter(accountTypes);

        createAccBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //checking if passwords are not matching
                if (!(passwordTxt.getText().toString().equals(rePasswordTxt.getText().toString()))) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords not matching", Toast.LENGTH_LONG).show();
                    return;
                }

                //checking if there is an empty field
                if(fullNameTxt.getText().toString().isEmpty() || addressTxt.getText().toString().isEmpty() || mobileNumTxt.getText().toString().isEmpty() ||
                        emailTxt.getText().toString().isEmpty() || nationalIDTxt.getText().toString().isEmpty() ||passwordTxt.getText().toString().isEmpty()
                || rePasswordTxt.getText().toString().isEmpty() || genderType.getText().toString().isEmpty() || accountType.getText().toString().isEmpty() )
                {
                    Toast.makeText(CreateAccountActivity.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User(nationalIDTxt.getText().toString(), fullNameTxt.getText().toString(), emailTxt.getText().toString(),
                        passwordTxt.getText().toString(), genderType.getText().toString(), mobileNumTxt.getText().toString(),addressTxt.getText().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
                Date date = new Date();

                Account account = new Account(accountType.getText().toString(), 0, "EGY", "Active", (formatter.format(date)));


                String cloudDbUrl = ApiUrl.serverUrl;

                Retrofit retrofit = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);


                Call call = apiInterface.createUser(user);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {

                            Toast.makeText(CreateAccountActivity.this, "User Created Successfully", Toast.LENGTH_LONG).show();

                            Retrofit retrofit2 = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                            ApiInterface apiInterface2 = retrofit2.create(ApiInterface.class);
                            Call call2 = apiInterface2.createAccount(nationalIDTxt.getText().toString(), account);

                            call2.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, @NonNull Response response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(CreateAccountActivity.this, "Response code is"+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Response not 200", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                        System.out.println(accountType.getText().toString());
                        t.printStackTrace();

                        Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_LONG).show();

                    }
                });



            }
        });


    }
}