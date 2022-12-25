package com.example.edb.API;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.edb.Controller.LoginActivity;
import com.example.edb.Controller.MainActivity;
import com.example.edb.Model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Path;

public class CallingAPI {
    User user;
    String cloudDbUrl = ApiUrl.serverUrl;



    public  void updateBalance(String userSSN, String accountId,  HashMap<String,String>map){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Void> call = apiInterface.updateBalance(userSSN,accountId,map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("Update Balance is Done ::)");
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Update is failed ");
            }
        });
    }

    public User getUserByAccountId(String accountID){
        System.out.println("================================================");
        System.out.println("Account ID "+ accountID);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<User> call = apiInterface.getUserByAccountId(accountID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, @NonNull Response<User> response) {
                System.out.println("Response code: "+response.code());
                if (response.code() == 200) {
                    user= response.body();
                }
                else
                    user=null;
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Failure to get reciver user");
                System.out.println(t.getCause());
            }
        });
        return user;
    }
}
