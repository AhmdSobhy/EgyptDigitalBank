package com.example.edb.Controller;

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
import com.example.edb.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AccountCardAdapter.OnItemClickListener {
    private AccountCardAdapter accountAdapter;
    private static ArrayList<Account> accountsData;
    User user = UserMapping.user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout transferMoneyBtn = view.findViewById(R.id.transfer_money_btn);
        LinearLayout atmBtn = view.findViewById(R.id.atm_btn);
        LinearLayout payBtn = view.findViewById(R.id.pay_btn);
        LinearLayout payCreditBtn = view.findViewById(R.id.pay_credit_btn);

        //myOnClickListener = new MyOnClickListener();
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");

        RecyclerView recyclerView = view.findViewById(R.id.acc_cards_recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        accountsData = new ArrayList<>();
        for (int i = 0; i < UserMapping.user.getAccounts().size(); i++) {
            accountsData.add(new Account(
                    UserMapping.user.getAccounts().get(i).get_id(),
                    UserMapping.user.getAccounts().get(i).getBalance()
            ));
        }

        accountAdapter = new AccountCardAdapter(getContext(), accountsData);
        recyclerView.setAdapter(accountAdapter);
        accountAdapter.setOnItemClickListener(HomeFragment.this);

        transferMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TransferActivity.class);
                i.putExtra("user",user);
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

    @Override
    public void onItemClick(int position) {
        TransactionsFragment transactionsFragment = new TransactionsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        transactionsFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,transactionsFragment);
        fragmentTransaction.addToBackStack("HomeFragment");
        fragmentTransaction.commit();
    }
}