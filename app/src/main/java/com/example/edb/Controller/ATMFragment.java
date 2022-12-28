package com.example.edb.Controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.edb.API.CallingAPI;
import com.example.edb.Model.Account;
import com.example.edb.Model.Transaction;
import com.example.edb.Model.User;
import com.example.edb.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ATMFragment extends Fragment {

    LinearLayout trTypeLayout;
    LinearLayout transactionLayout;
    LinearLayout confirmationLayout;
    LinearLayout processingLayout;
    LinearLayout errorLayout;
    AutoCompleteTextView accAutoComplete;
    EditText amountTxt;
    static String transactionType;
    User user = UserMapping.user;
    float newBalance =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atm, container, false);
        trTypeLayout = view.findViewById(R.id.transaction_type_layout);
        transactionLayout = view.findViewById(R.id.transaction_layout);
        processingLayout = view.findViewById(R.id.processing_layout);
        confirmationLayout = view.findViewById(R.id.confirmation_layout);
        errorLayout = view.findViewById(R.id.error_layout);
        accAutoComplete = view.findViewById(R.id.acc_txt);
        amountTxt = view.findViewById(R.id.amount_txt);
        Button depositBtn = view.findViewById(R.id.deposit_btn);
        Button withdrawBtn = view.findViewById(R.id.withdraw_btn);
        Button backBtn = view.findViewById(R.id.back_btn);
        Button confirmBtn = view.findViewById(R.id.confirm_btn);

        // Getting all the accounts for the user
        try {
            ArrayList<String> accountID = new ArrayList<>();
            for (int i = 0; i < user.getAccounts().size(); i++) {

                accountID.add(user.getAccounts().get(i).get_id());
            }
            // adding them to the Accounts (autocomplete)
            ArrayAdapter<String> accountArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item, accountID);
            accAutoComplete.setAdapter(accountArrayAdapter);
            //temp check
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + user.getAccounts().get(1).getBalance());
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error Fetching Account IDs for AutoComplete!");
            e.printStackTrace();
        }

        depositBtn.setOnClickListener(view1 -> {
            trTypeLayout.setVisibility(View.GONE);
            transactionLayout.setVisibility(View.VISIBLE);
            transactionType = depositBtn.getText().toString();
        });

        withdrawBtn.setOnClickListener(view12 -> {
            trTypeLayout.setVisibility(View.GONE);
            transactionLayout.setVisibility(View.VISIBLE);
            transactionType = withdrawBtn.getText().toString();
        });

        backBtn.setOnClickListener(view13 -> {
            transactionLayout.setVisibility(View.GONE);
            trTypeLayout.setVisibility(View.VISIBLE);
            transactionType = withdrawBtn.getText().toString();
        });

        confirmBtn.setOnClickListener(view14 -> {
            String account = accAutoComplete.getText().toString();
            String amount = amountTxt.getText().toString();
            if (!account.isEmpty() && !amount.isEmpty()) {
                confirmTransaction(account, Float.parseFloat(amount));
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    transactionLayout.setVisibility(View.GONE);
                    processingLayout.setVisibility(View.GONE);
                    confirmationLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    trTypeLayout.setVisibility(View.VISIBLE);
                }, 13000);

            } else
                Toast.makeText(getContext(), "Invalid Account or Amount", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    private void confirmTransaction(String accountId, float amount) {
        try {
            transactionLayout.setVisibility(View.GONE);
            processingLayout.setVisibility(View.VISIBLE);
            String description="";
            // for loop to get the index of the chosen account to get it's balance
            for (Account acc:user.getAccounts()) {
                if (accountId.equals(acc.get_id())) {
                    float senderBalance =acc.getBalance();
                    if (transactionType.equals("Deposit")) {
                        newBalance = senderBalance + amount;
                        description="Deposit at ATM";
                    }
                    else {
                        // checking if the Account's balance is less than the amount to be Withdrawn
                        if (acc.getBalance() >= amount) {
                            newBalance = senderBalance - amount;
                            description="Withdraw at ATM";
                        }
                        else {
                            Toast.makeText(getContext(), "Insufficient balance in your Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                    acc.setBalance(newBalance);
                    break;
                }
            }

            CallingAPI callingAPI=new CallingAPI();
            callingAPI.updateBalance(user.getSSN(),accountId, String.valueOf(newBalance));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
            Date date = new Date();
            Transaction transactionToSend=new Transaction(transactionType, amount,description,(formatter.format(date)));

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> callingAPI.addTransaction(user.getSSN(),accountId,transactionToSend),3000 );
            handler.postDelayed(() -> {
                UserMapping.user=callingAPI.login(user.getEmail(),user.getPassword());
                accAutoComplete.setText("");
                amountTxt.setText("");
                processingLayout.setVisibility(View.GONE);
                confirmationLayout.setVisibility(View.VISIBLE);
            },10000);


        }
        catch(Exception e) {
            System.out.println("problem with creating call (getUserAccountId)");
            transactionLayout.setVisibility(View.GONE);
            processingLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

}