package com.example.edb;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.edb.Model.Account;
import com.example.edb.Model.User;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static RecyclerView.Adapter adapter;
    private static ArrayList<Account> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout transferMoneyBtn = view.findViewById(R.id.transfer_money_btn);
        LinearLayout atmBtn = view.findViewById(R.id.atm_btn);
        LinearLayout payBtn = view.findViewById(R.id.pay_btn);
        LinearLayout payCreditBtn = view.findViewById(R.id.pay_credit_btn);

        myOnClickListener = new MyOnClickListener();
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");

        RecyclerView recyclerView = view.findViewById(R.id.acc_cards_recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        System.out.println("==============================================================");
        System.out.println(user.getFullName());
        System.out.println(user.getAccounts().get(0).get_id());

        data = new ArrayList<>();
        for (int i = 0; i < user.getAccounts().size(); i++) {
            data.add(new Account(
                    user.getAccounts().get(i).get_id(),
                    user.getAccounts().get(i).getBalance()
            ));
        }

        removedItems = new ArrayList<>();

        adapter = new AccountCardAdapter(data);
        recyclerView.setAdapter(adapter);
        transferMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TransferActivity.class);
                startActivity(i);
            }
        });

        atmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new ATMFragment());
                fragmentTransaction.addToBackStack("HomeFragment");
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new TransactionsFragment());
            fragmentTransaction.addToBackStack("HomeFragment");
            fragmentTransaction.commit();
        }
    }
}