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
import android.widget.Toast;

import com.example.edb.Model.Account;
import com.example.edb.Model.User;
import com.example.edb.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AccountCardAdapter.OnItemClickListener {
    User user = UserMapping.user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout transferMoneyBtn = view.findViewById(R.id.transfer_money_btn);
        LinearLayout atmBtn = view.findViewById(R.id.atm_btn);
        LinearLayout requestsBtn = view.findViewById(R.id.requests_btn);
        LinearLayout payCreditBtn = view.findViewById(R.id.pay_credit_btn);

        Intent intent = requireActivity().getIntent(); // ......
        user = (User) intent.getSerializableExtra("user");

        RecyclerView recyclerView = view.findViewById(R.id.acc_cards_recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Account> accountsData = new ArrayList<>();
        for (int i = 0; i < UserMapping.user.getAccounts().size(); i++) {
            accountsData.add(new Account(
                    UserMapping.user.getAccounts().get(i).get_id(),
                    UserMapping.user.getAccounts().get(i).getBalance(),
                    UserMapping.user.getAccounts().get(i).getType()

            ));
        }

        AccountCardAdapter accountAdapter = new AccountCardAdapter(getContext(), accountsData);
        recyclerView.setAdapter(accountAdapter);
        accountAdapter.setOnItemClickListener(HomeFragment.this);

        transferMoneyBtn.setOnClickListener(transferView -> {
            Intent i = new Intent(getContext(), TransferActivity.class);
            i.putExtra("user",user);
            startActivity(i);
        });

        requestsBtn.setOnClickListener(requestsView -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new RequestsFragment());
            fragmentTransaction.addToBackStack("HomeFragment");
            fragmentTransaction.commit();
        });

        payCreditBtn.setOnClickListener(payCreditView -> Toast.makeText(getContext(),"No Credit Card on This Account", Toast.LENGTH_SHORT).show());

        atmBtn.setOnClickListener(atmView -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,new ATMFragment());
            fragmentTransaction.addToBackStack("HomeFragment");
            fragmentTransaction.commit();
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