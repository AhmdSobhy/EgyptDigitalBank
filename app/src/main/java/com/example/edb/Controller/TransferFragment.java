package com.example.edb.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edb.API.ApiInterface;
import com.example.edb.API.ApiUrl;
import com.example.edb.API.CallingAPI;
import com.example.edb.Model.Transaction;
import com.example.edb.Model.User;
import com.example.edb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferFragment extends Fragment {

    User sender;
    User Receiver;
    float newBalanceR=0;
    float newBalanceS=0;
    CallingAPI dbTransfer = new CallingAPI();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);

        BottomNavigationView bottomNavigationView;
        EditText receiverAccNum = view.findViewById(R.id.receiver_acc_txt);
        EditText amount = view.findViewById(R.id.amount_txt);
        Button transferBtn = view.findViewById(R.id.transfer_btn);
        sender = UserMapping.user;
        bottomNavigationView = view.findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.menu_transfer);
        AutoCompleteTextView senderAutoComplete = view.findViewById(R.id.sender_acc_txt);

        /*
         *  GET ALL USER'S ACCOUNTS FOR THE AUTOCOMPLETE EDIT TEXT
         * */
        try {

            ArrayAdapter<String> accountArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item, dbTransfer.fetchAccounts(sender));
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
                Retrofit retrofitUser = new Retrofit.Builder().baseUrl(ApiUrl.serverUrl).addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterfaceUser = retrofitUser.create(ApiInterface.class);
                Call<User> callReceiverGetUser = apiInterfaceUser.getUserByAccountId(recAccID);
                CallingAPI callingAPI=new CallingAPI();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
                Date date = new Date();

                Transaction transaction= new Transaction("Transfer",Float.parseFloat(amount.getText().toString()), "Transfer to account no: "+receiverAccNum.getText().toString(),(formatter.format(date)));
                Transaction transaction2= new Transaction("Transfer",Float.parseFloat(amount.getText().toString()), "from "+senderAccountID,(formatter.format(date)));



                callReceiverGetUser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, @NonNull Response<User> response) {
                        System.out.println("Response code: "+response.code());
                        if (response.code() == 200) {
                            Receiver=response.body();
                            for (int j = 0; j < Receiver.getAccounts().size(); j++) {
                                // update receiver's balance
                                if (RecevierID.equals(Receiver.getAccounts().get(j).get_id())) {

                                    final Handler handler = new Handler(Looper.getMainLooper());
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            addTransaction(sender.getSSN(),senderAccountID,transaction);
                                        }
                                    },3000);

                                    newBalanceR = Receiver.getAccounts().get(j).getBalance() + Float.parseFloat(amount.getText().toString());
                                    dbTransfer.updateBalance(Receiver.getSSN(),Receiver.getAccounts().get(j).get_id(),String.valueOf(newBalanceR));



                                    for (int i = 0; i < sender.getAccounts().size(); i++) {
                                        if (senderAccountID.equals(sender.getAccounts().get(i).get_id())) {
                                            if (sender.getAccounts().get(i).getBalance()>=Float.parseFloat(amount.getText().toString())) { //update sender's balance
                                                newBalanceS = sender.getAccounts().get(i).getBalance() - Float.parseFloat(amount.getText().toString());
                                                sender.getAccounts().get(i).setBalance(newBalanceS);
                                                dbTransfer.updateBalance(sender.getSSN(), sender.getAccounts().get(i).get_id(),String.valueOf(newBalanceS) );


                                                // transaction.setDescription("transaction from "+sender.getFullName());
                                                addTransaction(Receiver.getSSN(),RecevierID,transaction2);

                                                Toast.makeText(getContext(), "Transfer Complete", Toast.LENGTH_LONG).show();
                                               /* Intent ReOpenContext = new Intent(TransferActivity.this, TransferActivity.class);

                                                ReOpenContext.putExtra("user", sender);
                                                startActivity(ReOpenContext);*/

                                                // Reload current fragment
                                                Fragment fragment = null;
                                                fragment = getFragmentManager().findFragmentByTag("TransferFragment");
                                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                ft.detach(fragment);
                                                ft.attach(fragment);
                                                ft.commit();

                                                break;
                                            }
                                        }
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            UserMapping.user = dbTransfer.login(sender.getEmail(),sender.getPassword());
                                        }
                                    },10000);
                                    break;

                                }
                                else {
                                    Toast.makeText(getContext(),"Error in updating receivers data",Toast.LENGTH_LONG);
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


        return view;
    }

    private void addTransaction(String userSSN, String accountId, Transaction transaction){
        dbTransfer.addTransaction(userSSN,accountId,transaction);
    }
}