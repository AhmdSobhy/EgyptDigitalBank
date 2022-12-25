package com.example.edb.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransferActivity extends AppCompatActivity {
<<<<<<< Updated upstream

=======
    User sender;
    User Receiver;
    private ArrayList<String> accountID;
    private ArrayAdapter<String> accountArrayAdapter;
    int indexOfAccount=0;
    float newBalanceR=0;
    float newBalanceS=0;
    String Sendersid;
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        BottomNavigationView bottomNavigationView;
        EditText senderAccNum = findViewById(R.id.sender_acc_txt);
        EditText receiverAccNum = findViewById(R.id.receiver_acc_txt);
        EditText amount = findViewById(R.id.amount_txt);
        Button transferBtn = findViewById(R.id.transfer_btn);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.menu_transfer);

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

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

<<<<<<< Updated upstream
=======
                //validation for receiver's account

                String cloudDbUrl = "http://10.0.2.2:3000";
                // call for checking if receiver account exists
                String recAccID= receiverAccNum.getText().toString();
                try {
                    //create a call to get receiver in a User object (for balance information and SSN)
                    Retrofit retrofitReciever = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                    ApiInterface apiInterfaceSender = retrofitReciever.create(ApiInterface.class);
                    Call<User> callReceiverGetUser = apiInterfaceSender.getUserByAccountId(recAccID);
                    callReceiverGetUser.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code()==200)  {
                                Receiver = response.body();
                                // validation for balance
                                //Call Update-balance
                                String senderAccountID = senderAutoComplete.getText().toString();
                                HashMap<String, String> balancemapS = new HashMap<>();
                                HashMap<String, String> balancemapR = new HashMap<>();
                                // for loop to get the index of the chosen account to get it's balance
                                Retrofit retrofitUpdateBalance = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                                ApiInterface apiUpdateBalance = retrofitUpdateBalance.create(ApiInterface.class);
                                for (int i = 0; i < sender.getAccounts().size(); i++) {
                                    // checking if the sender's balance is bigger than or equal to the amount to be transfered
                                    if (senderAccountID.equals(sender.getAccounts().get(i).get_id())) {
                                        //till we find the account we are looking for
                                        if (sender.getAccounts().get(i).getBalance() >= Float.parseFloat(amount.getText().toString())) {
                                            // if it's the selected account then prepare to add new balance
                                            float senderBalance = sender.getAccounts().get(i).getBalance();

                                            Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_LONG).show();
                                            // reduce senders balance
                                            newBalanceS = senderBalance - Float.parseFloat(amount.getText().toString());
                                            balancemapS.put("Balance", String.valueOf(newBalanceS));
                                            //we make a call for sender and update the balance
                                            indexOfAccount = i;
//                                            Call<Void> callUpdateSender = apiUpdateBalance.updateBalance(sender.getSSN(), sender.getAccounts().get(indexOfAccount).get_id(), balancemapS);
//
//                                            callUpdateSender.enqueue(new Callback<Void>() {
//                                                @Override
//                                                public void onResponse(Call<Void> call, Response<Void> response) {
//                                                    if (response.code() == 200) {
//                                                        System.out.println(sender.getAccounts().get(indexOfAccount).get_id());
//                                                        System.out.println("Sender's new balance " + balancemapS.get("Balance"));
//                                                        Toast.makeText(getApplicationContext(), "Reduced from account...", Toast.LENGTH_LONG).show();
//                                                    } else {
//                                                        System.out.println("A33333333333333333333333333333 in update sender");
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<Void> call, Throwable t) {
//                                                    System.out.println("Error in update balance sender's side");
//                                                }
//                                            });
                                        }

                                    }

                                }


                                //here we search for the index of account ID from receiver's side
                                Retrofit retrofitUpdateBalanceR = new Retrofit.Builder().baseUrl(cloudDbUrl).addConverterFactory(GsonConverterFactory.create()).build();
                                ApiInterface apiUpdateBalanceR = retrofitUpdateBalanceR.create(ApiInterface.class);
                                String RecevierID = receiverAccNum.getText().toString();

                                for (int j = 0; j < Receiver.getAccounts().size(); j++) {
                                    if (RecevierID.equals(Receiver.getAccounts().get(j).get_id())) {
                                        // add the amount to his balance
                                        float receiverBalance = Receiver.getAccounts().get(j).getBalance();
                                        newBalanceR = receiverBalance + Float.parseFloat(amount.getText().toString());
                                        balancemapS.put("Balance2", String.valueOf(newBalanceR));
                                        System.out.println("here here!!!!!!!!!" + RecevierID);
                                        // we make a new call for receiver's balance
                                        Call<Void> callUpdateReciever = apiUpdateBalanceR.updateBalance(Receiver.getSSN(), RecevierID, sender.getSSN(), sender.getAccounts().get(indexOfAccount).get_id(), balancemapS);
                                        callUpdateReciever.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (response.code() == 200) {
                                                    System.out.println("Receiver's new balance " + balancemapR.get("Balance"));
                                                    Toast.makeText(getApplicationContext(), "Transfer Complete", Toast.LENGTH_LONG).show();
                                                } else {
                                                    System.out.println("A33333333333333333333333333333 in update receiver");
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                System.out.println("Error in Update Balance Reciever Side");

                                            }
                                        });

                                    }
                                }


                            }
                            else
                            {
                                System.out.println("A33333333333333333333333333333 in update receiver");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            System.out.println("Connection problem with fetching sender!");

                        }
                    });
                }
                catch(Exception e)
                {
                    System.out.println("problem with creating call (getUserAccountId)");
                }

>>>>>>> Stashed changes
            }
        });

    }
}