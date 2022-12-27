package com.example.edb.API;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.edb.Controller.LoginActivity;
import com.example.edb.Controller.MainActivity;
import com.example.edb.Controller.UserMapping;
import com.example.edb.Model.Transaction;
import com.example.edb.Model.User;

import java.util.ArrayList;
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
    String cloudDbUrl;
    Retrofit retrofit  ;
    ApiInterface apiInterface ;

    public CallingAPI(){
        cloudDbUrl = ApiUrl.serverUrl;
        retrofit = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public User login(String email, String password){
        HashMap<String,String>userData=new HashMap<>();
        userData.put("Email",email);
        userData.put("Password",password);
        System.out.println("-----------------------------------");
        System.out.println(userData.size());
        Call<User>call=apiInterface.getUserInfoAndLogin(userData);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("Response code: "+response.code());
                if(response.code()==200){
                    user=response.body();
                    UserMapping.user=user;
                    System.out.println(user.getEmail());
                    System.out.println("=======================================");
                }
                else{
                    System.out.println("Wrong Email or Password");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Error at login");
            }
        });
        return user;
    }
    public void updateBalance(String userSSN, String accountId, String newBalance){
        HashMap<String,String>map=new HashMap<>();
        map.put("Balance",newBalance);
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

    public void addTransaction(String userSSN, String accountID, Transaction transactionToAdd){
        retrofit = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
        apiInterface = retrofit.create(ApiInterface.class);
        System.out.println("Add transaction is heeeeeeeeeeeeeeeeeeere");
        Call<Void> call = apiInterface.addTransaction(userSSN,accountID,transactionToAdd);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Response Code : "+response.code());
                if(response.isSuccessful()){
                    System.out.println("Transaction is added Successfully");
                }
                else {
                    System.out.println("Failed to add transaction");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Error at Internet Connection");
            }
        });
    }
    /*
     *  GET ALL USER'S ACCOUNTS FOR THE AUTOCOMPLETE EDIT TEXT
     * */
    public ArrayList<String> fetchAccounts(User sender)
    {
        ArrayList<String> accountID;
        try {
            // getting all the accounts for the user (sender)

            accountID = new ArrayList<>();
            for (int i = 0; i < sender.getAccounts().size(); i++) {

                accountID.add(sender.getAccounts().get(i).get_id());
            }
            // adding them to the senders combobox (autocompleteview)
            return accountID;
        }
        catch(Exception e)
        {
            System.out.println("Error Fetching Account IDs for AutoComplete!");
            return null;
        }
    }
}