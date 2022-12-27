package com.example.edb.Controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.edb.API.ApiInterface;
import com.example.edb.API.ApiUrl;
import com.example.edb.Model.Account;
import com.example.edb.Model.Transaction;
import com.example.edb.Model.User;
import com.example.edb.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ATMFragment extends Fragment {

    LinearLayout trTypeLayout;
    LinearLayout transactionLayout;
    LinearLayout confirmationLayout;
    LinearLayout errorLayout;
    static String transactionType;
    User user = UserMapping.user;
    private ArrayList<String> accountsIDs;
    private ArrayList<String> accountsNumbers;
    private ArrayAdapter<String> accountArrayAdapter;
    int indexOfAccount=0;
    float newBalance =0;
    Call<Void> callUpdateAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atm, container, false);
        trTypeLayout = view.findViewById(R.id.transaction_type_layout);
        transactionLayout = view.findViewById(R.id.transaction_layout);
        confirmationLayout = view.findViewById(R.id.confirmation_layout);
        errorLayout = view.findViewById(R.id.error_layout);
        Button depositBtn = view.findViewById(R.id.deposit_btn);
        Button withdrawBtn = view.findViewById(R.id.withdraw_btn);
        AutoCompleteTextView accAutoComplete = view.findViewById(R.id.acc_txt);
        EditText amountTxt = view.findViewById(R.id.amount_txt);
        Button backBtn = view.findViewById(R.id.back_btn);
        Button confirmBtn = view.findViewById(R.id.confirm_btn);

        // Getting all the accounts for the user
        try {
            accountsIDs = new ArrayList<>();
            accountsNumbers = new ArrayList<>();
            for (int i = 0; i < user.getAccounts().size(); i++) {
                //accountsNumbers.add(user.getAccounts().get(i).getAccountNumber());
                accountsIDs.add(user.getAccounts().get(i).get_id());
            }
            // adding them to the Accounts (autocomplete)
            accountArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item, accountsIDs);
            accAutoComplete.setAdapter(accountArrayAdapter);
            //temp check
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  "+user.getAccounts().get(1).getBalance());
        }
        catch(Exception e)
        {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error Fetching Account IDs for AutoComplete!");
            e.printStackTrace();
        }

        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trTypeLayout.setVisibility(View.GONE);
                transactionLayout.setVisibility(View.VISIBLE);
                transactionType = depositBtn.getText().toString();
            }
        });

        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trTypeLayout.setVisibility(View.GONE);
                transactionLayout.setVisibility(View.VISIBLE);
                transactionType = withdrawBtn.getText().toString();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionLayout.setVisibility(View.GONE);
                trTypeLayout.setVisibility(View.VISIBLE);
                transactionType = withdrawBtn.getText().toString();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accountId = accAutoComplete.getText().toString();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ accountId);
                String amount = amountTxt.getText().toString();
                if (!accountId.isEmpty() && !amount.isEmpty()) {
                    confirmTransaction(accountId, Float.parseFloat(amount));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            confirmationLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                            trTypeLayout.setVisibility(View.VISIBLE);
                        }
                    }, 4000);

                }
                else
                    Toast.makeText(getContext(),"Invalid Account or Amount",Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void confirmTransaction(String account, float amount) {
        try {
            // for loop to get the index of the chosen account to get it's balance
            for (int i = 0; i < user.getAccounts().size(); i++) {
                if (account.equals(user.getAccounts().get(i).get_id())) {
                    float senderBalance = user.getAccounts().get(i).getBalance();
                    if (transactionType.equals("Deposit")) {
                        newBalance = senderBalance + amount;
                        updateBalance(i,"deposit",amount,"Deposit at Virtual ATM");
                    }
                    else if (transactionType.equals("Withdraw")) {
                        // checking if the Account's balance is less than the amount to be Withdrawn
                        if (user.getAccounts().get(i).getBalance() >= amount) {
                            newBalance = senderBalance - amount;
                            updateBalance(i,"withdraw",amount,"Cash Withdrawal at Virtual ATM");
                        }
                        else {
                            Toast.makeText(getContext(), "Insufficient balance in your Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println("problem with creating call (getUserAccountId)");
        }
    }

    private void updateBalance(int i, String type, float amount, String description){
        String cloudDbUrl = ApiUrl.serverUrl;
        Retrofit retrofitUpdateBalance = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiUpdateBalance = retrofitUpdateBalance.create(ApiInterface.class);
        HashMap<String, String> balanceMap = new HashMap<>();

        Account userAccount = user.getAccounts().get(i);
        userAccount.setBalance(newBalance);
        balanceMap.put("Balance", String.valueOf(newBalance));
        //we make a call for sender and update the balance
        indexOfAccount = i;
        callUpdateAccount = apiUpdateBalance.updateBalance(user.getSSN(), user.getAccounts().get(indexOfAccount).get_id(), balanceMap);

        callUpdateAccount.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 200) {
                    if(makeTransaction(userAccount, type, amount,description)) {
                        transactionLayout.setVisibility(View.GONE);
                        confirmationLayout.setVisibility(View.VISIBLE);
                    }
                    else{
                        transactionLayout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! error in makeTransaction");
                    }

                } else {
                    transactionLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    System.out.println("A33333333333333333333333333333 in update sender");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                transactionLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                System.out.println("Error in update balance");
            }
        });
    }
    private boolean makeTransaction(Account account, String type, float amount, String description){
        final boolean[] transactionIsMade = new boolean[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.serverUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Transaction transaction = new Transaction(LocalDate.now().toString(), type, amount, description);

        Call call = apiInterface.addTransaction(user.getSSN(), account.get_id(),transaction);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, @NonNull Response response) {
                if (response.code() == 200) {
                   transactionIsMade[0] = true;
                } else {
                    transactionIsMade[0] = false;
                    System.out.println(">>>>>>>>>>>>>>>>>>>>> Response code is "+response.code());
                    Toast.makeText(getContext(), "Response code is "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(), "Failed to make transaction", Toast.LENGTH_SHORT).show();
            }
        });
        return transactionIsMade[0];
    }

    public String getAccountId(String accountNumber){
        for (String accId:accountsIDs) {
            if(accId.replaceAll("[^0-9]","").equals(accountNumber))
                return accId;
            break;
        }
        return null;
    }
}