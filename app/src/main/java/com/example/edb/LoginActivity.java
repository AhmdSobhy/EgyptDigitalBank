package com.example.edb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edb.API.ApiInterface;
import com.example.edb.Model.User;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import cz.msebera.android.httpclient.entity.StringEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailTxt = findViewById(R.id.email_txt);
        EditText passwordTxt = findViewById(R.id.password_txt);
        Button LoginBtn = findViewById(R.id.login_acc_btn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cloudDbUrl = "https://bank-db-api.herokuapp.com/";

                Retrofit retrofit=new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterface=retrofit.create(ApiInterface.class);

                HashMap<String, String> map = new HashMap<>();
                map.put("Email",emailTxt.getText().toString());
                map.put("Password", passwordTxt.getText().toString());

                Call<User> call= apiInterface.getUserInfo(map);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, @NonNull Response<User> response) {
                        if(response.code()== 200){
                            Toast.makeText(LoginActivity.this, "You have log in successfully ::)", Toast.LENGTH_LONG).show();
                            User user = response.body();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("user",user);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Error at internet connection", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}