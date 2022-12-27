package com.example.edb.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.edb.API.ApiInterface;
import com.example.edb.APIUri;
import com.example.edb.DbHelperTransferMoney;
import com.example.edb.R;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edb.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferActivity extends AppCompatActivity {
    User sender;
    User Receiver;
    private ArrayAdapter<String> accountArrayAdapter;
    float newBalanceR=0;
    float newBalanceS=0;
    DbHelperTransferMoney dbTransfer = new DbHelperTransferMoney();
    APIUri apiUri = new APIUri();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        BottomNavigationView bottomNavigationView;
        EditText senderAccNum = findViewById(R.id.sender_acc_txt);
        EditText receiverAccNum = findViewById(R.id.receiver_acc_txt);
        EditText amount = findViewById(R.id.amount_txt);
        Button transferBtn = findViewById(R.id.confirm_btn);
        Intent intent = getIntent();
        sender = (User) intent.getSerializableExtra("user");
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.menu_transfer);
        AutoCompleteTextView senderAutoComplete =findViewById(R.id.sender_acc_txt);
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

        /*
        *  GET ALL USER'S ACCOUNTS FOR THE AUTOCOMPLETE EDIT TEXT
        * */
        try {

            accountArrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, dbTransfer.fetchAccounts(sender));
            senderAutoComplete.setAdapter(accountArrayAdapter);
        }
        catch(Exception e)
        {
            System.out.println("Error Fetching Account IDs for AutoComplete!");
        }

        /*
         *  ON CLICK LISTENER TRANSFER BTN
         * */
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recAccID= receiverAccNum.getText().toString();
                String senderAccountID = senderAutoComplete.getText().toString();
                String RecevierID = receiverAccNum.getText().toString();
                Retrofit retrofitUser = new Retrofit.Builder().baseUrl(apiUri.cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterfaceUser = retrofitUser.create(ApiInterface.class);
                Call<User> callReceiverGetUser = apiInterfaceUser.getUserByAccountId(recAccID);

                callReceiverGetUser.enqueue(new Callback<User>() {
                    @Override
            public void onResponse(Call<User> call, @NonNull Response<User> response) {
            if (response.code() == 200) {
                Receiver=response.body();
                    for (int j = 0; j < Receiver.getAccounts().size(); j++) {
                        // update receiver's balance
                        if (RecevierID.equals(Receiver.getAccounts().get(j).get_id())) {
                            newBalanceR = Receiver.getAccounts().get(j).getBalance() + Float.parseFloat(amount.getText().toString());
                            dbTransfer.UpdateCall(Receiver,String.valueOf(newBalanceR),j);

                            for (int i = 0; i < sender.getAccounts().size(); i++) {
                                if (senderAccountID.equals(sender.getAccounts().get(i).get_id())) {
                                    if (sender.getAccounts().get(i).getBalance()>=Float.parseFloat(amount.getText().toString()))
                                    { //update sender's balance
                                        newBalanceS = sender.getAccounts().get(i).getBalance() - Float.parseFloat(amount.getText().toString());
                                        sender.getAccounts().get(i).setBalance(newBalanceS);
                                        dbTransfer.UpdateCall(sender, String.valueOf(newBalanceS), i);
                                        Toast.makeText(getApplicationContext(), "Transfer Complete", Toast.LENGTH_LONG).show();
                                        Intent ReOpenContext = new Intent(TransferActivity.this, TransferActivity.class);
                                        ReOpenContext.putExtra("user", sender);
                                        startActivity(ReOpenContext);
                                    }
                                }
                           }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error in updating receivers data",Toast.LENGTH_LONG);
                        }
                    }
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("failure in fetching user by id");
                        System.out.println(t.getCause());
                    }
                });




            }

        });

    }
}