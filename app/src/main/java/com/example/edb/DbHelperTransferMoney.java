package com.example.edb;

import androidx.annotation.NonNull;

import com.example.edb.API.ApiInterface;
import com.example.edb.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DbHelperTransferMoney {
    APIUri apiUri = new APIUri();
   public boolean updateCall = false;
   public User byIdUser=null;



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
    /*
     *  UPDATE REQUEST CALL
     * */
    public void UpdateCall (User user, HashMap<String,String>balance, int index, Float amount)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(apiUri.cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Void> updateBalance = apiInterface.updateBalance(user.getSSN(), user.getAccounts().get(index).get_id(),balance );
        updateBalance.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (user.getAccounts().get(index).getBalance() >= amount) {
                    updateCall = true;
                }
                else
                {
                    updateCall = false;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Failue in Response Update Call");

            }
        });

    }



}
