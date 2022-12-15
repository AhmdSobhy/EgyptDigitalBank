package com.example.edb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TransactionsFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private static ArrayList<TransactionDataModel> data;
    static View.OnClickListener myOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.transaction_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        for (int i = 0; i < CardsData.transactionArray.length; i++) {
            data.add(new TransactionDataModel(
                    CardsData.transactionArray[i][0],
                    CardsData.transactionArray[i][1],
                    CardsData.transactionArray[i][2],
                    CardsData.transactionArray[i][3],
                    CardsData.transactionArray[i][4]
            ));
        }

        adapter = new TransactionAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
    private class MyOnClickListener implements View.OnClickListener {

        private MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
//            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container,new TransactionsFragment());
//            fragmentTransaction.addToBackStack("HomeFragment");
//            fragmentTransaction.commit();
        }
    }
}