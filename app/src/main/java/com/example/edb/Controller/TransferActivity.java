package com.example.edb.Controller;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.edb.API.CallingAPI;
import com.example.edb.Model.Account;
import com.example.edb.R;
import android.widget.EditText;

import com.example.edb.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class TransferActivity extends AppCompatActivity {
    User sender;
    User Receiver;
    CallingAPI callingAPI=new CallingAPI();
    private ArrayList<String> accountID;
    private ArrayAdapter<String> accountArrayAdapter;
    int indexOfAccount=0;
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
        try {
            // getting all the accounts for the user (sender)

            accountID = new ArrayList<>();
            for (int i = 0; i < sender.getAccounts().size(); i++) {

                accountID.add(sender.getAccounts().get(i).get_id());
            }
            // adding them to the senders combobox (autocompleteview)
            accountArrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, accountID);
            senderAutoComplete.setAdapter(accountArrayAdapter);
            //temp check
            System.out.println(sender.getAccounts().get(1).getBalance());
        }
        catch(Exception e)
        {
            System.out.println("Error Fetching Account IDs for AutoComplete!");
        }

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String senderAccountId=senderAutoComplete.getText().toString();
                String reciverAccountId=receiverAccNum.getText().toString();
                String amountString=amount.getText().toString();
                Float amountToTransfer= Float.parseFloat(amountString);
                Receiver=callingAPI.getUserByAccountId(reciverAccountId);

                if(Receiver==null)return;

                HashMap<String,String>mp=new HashMap<>();
                boolean isCoveredBalance=false;
                for (Account account:sender.getAccounts()) {
                    if((account.get_id().equals(senderAccountId))){
                       if(account.getBalance() >=amountToTransfer){
                           isCoveredBalance=true;
                           account.setBalance(account.getBalance()-amountToTransfer);
                           mp.put("Balance",String.valueOf(account.getBalance()));
                           callingAPI.updateBalance(sender.getSSN(),senderAccountId,mp);
                           break;
                       }
                    }
                }
                mp.clear();
                if(isCoveredBalance){
                    for (Account account:Receiver.getAccounts()) {
                        if((account.get_id().equals(reciverAccountId))){
                            account.setBalance(account.getBalance()+amountToTransfer);
                            mp.put("Balance",String.valueOf(account.getBalance()));
                            callingAPI.updateBalance(Receiver.getSSN(),reciverAccountId,mp);
                        }
                    }

                }

            }

        });
    }
}