package com.example.edb.Controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edb.Model.Transaction;
import com.example.edb.R;

import java.util.ArrayList;

public class TransactionsFragment extends Fragment implements TransactionAdapter.OnItemClickListener {

    private TransactionAdapter transactionAdapter;
    static int accountsIndex = 0;
    int listPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        TextView accType = view.findViewById(R.id.tr_acc_type_txt);
        TextView accId = view.findViewById(R.id.acc_num_txt);
        TextView accBalance = view.findViewById(R.id.acc_balance_txt);
        ImageButton nextAccountBtn = view.findViewById(R.id.next_acc_btn);
        ImageButton previousAccountBtn = view.findViewById(R.id.previous_acc_btn);

        Bundle args = getArguments();
        if (args != null) {
            listPosition = args.getInt("position");
            accountsIndex = listPosition;
            accType.setText(UserMapping.user.getAccounts().get(listPosition).getType());
            accId.setText(UserMapping.user.getAccounts().get(listPosition).getAccountNumber());
            accBalance.setText(String.valueOf(UserMapping.user.getAccounts().get(listPosition).getBalance()));
        }

        //Transactions RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.transaction_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        transactionAdapter = new TransactionAdapter(getContext(), getAccountTransactions(listPosition));
        recyclerView.setAdapter(transactionAdapter);
        transactionAdapter.setOnItemClickListener(TransactionsFragment.this);

        nextAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Transaction> transactionData = getAccountTransactions(accountsIndex+1);
                if (transactionData != null) {
                    transactionAdapter = new TransactionAdapter(getContext(), transactionData);
                    recyclerView.setAdapter(transactionAdapter);
                    accountsIndex++;
                    accType.setText(UserMapping.user.getAccounts().get(accountsIndex).getType());
                    accId.setText(UserMapping.user.getAccounts().get(accountsIndex).getAccountNumber());
                    accBalance.setText(String.valueOf(UserMapping.user.getAccounts().get(accountsIndex).getBalance()));
                }
            }
        });
        previousAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Transaction> transactionData = getAccountTransactions(accountsIndex-1);
                if (transactionData != null) {
                    transactionAdapter = new TransactionAdapter(getContext(), transactionData);
                    recyclerView.setAdapter(transactionAdapter);
                    accountsIndex--;
                    accType.setText(UserMapping.user.getAccounts().get(accountsIndex).getType());
                    accId.setText(UserMapping.user.getAccounts().get(accountsIndex).getAccountNumber());
                    accBalance.setText(String.valueOf(UserMapping.user.getAccounts().get(accountsIndex).getBalance()));
                }
            }
        });

        return view;
    }

    private ArrayList<Transaction> getAccountTransactions(int position){
        ArrayList<Transaction> transactionData = new ArrayList<>();
        try {
            ArrayList<Transaction> userTransaction = UserMapping.user.getAccounts().get(position).getTransactions();

            for(int i=0;i<userTransaction.size();i++) {
                String date = userTransaction.get(i).getDate();
                String id = userTransaction.get(i).get_id();
                String type = userTransaction.get(i).getType();
                float amount = userTransaction.get(i).getAmount();
                String name = userTransaction.get(i).getDescription();
                transactionData.add ( new Transaction(date, type, amount, id, name));
            }

            return transactionData;

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void onItemClick(int position) {
        //TODO
        //Navigate to Transaction Details Page
    }
}