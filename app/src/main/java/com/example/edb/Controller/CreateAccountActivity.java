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


        //final Boolean[] UserSuccessfullyCreated = new Boolean[1];


        createAccBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!(passwordTxt.getText().toString().equals(rePasswordTxt.getText().toString()))) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords not matching", Toast.LENGTH_LONG).show();
                    return;
                }
                User user = new User(nationalIDTxt.getText().toString(), fullNameTxt.getText().toString(), emailTxt.getText().toString(),
                        passwordTxt.getText().toString(), genderType.getText().toString(), mobileNumTxt.getText().toString(),addressTxt.getText().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
                Date date = new Date();
                System.out.println(formatter.format(date));
                Account account = new Account(accountType.getText().toString(), 0, "EGY", "Active", (formatter.format(date)));


                String cloudDbUrl = "https://bank-db-api.herokuapp.com/";

                Retrofit retrofit = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);


                Call call = apiInterface.createUser(user);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {

                            Toast.makeText(CreateAccountActivity.this, "User Created Successfully", Toast.LENGTH_LONG).show();

                            HashMap<String, String> map = new HashMap<>();
                            HashMap<String, Account> map1 = new HashMap<>();
                            map.put("SSN", nationalIDTxt.getText().toString());

                            map1.put("Accounts", account);
                            map.put("Accounts", passwordTxt.getText().toString());
                            User user1 = new User(nationalIDTxt.getText().toString(), account);
                            System.out.println(user1.getSSN());
                            System.out.println(accountType.getText().toString());

                            Retrofit retrofit2 = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                            ApiInterface apiInterface2 = retrofit2.create(ApiInterface.class);
                            Call call2 = apiInterface2.createAccount(nationalIDTxt.getText().toString(), account);

                            call2.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, @NonNull Response response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(CreateAccountActivity.this, "User and account Created Successfully", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                        // i.putExtra("user",user);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(CreateAccountActivity.this, "Response code is"+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Toast.makeText(CreateAccountActivity.this, "Error2", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Response not 200", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        System.out.println("errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                        System.out.println(accountType.getText().toString());
                        t.printStackTrace();
                        //t.printStackTrace();
                        Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_LONG).show();

                    }
                });


                //Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                //startActivity(i);
            }
        });


    }
}