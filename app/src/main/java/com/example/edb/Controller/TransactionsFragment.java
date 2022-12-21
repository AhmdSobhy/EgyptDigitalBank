package com.example.edb.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.edb.API.ApiInterface;
import com.example.edb.CardsData;
import com.example.edb.Model.Transaction;
import com.example.edb.Model.User;
import com.example.edb.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransactionsFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private static ArrayList<TransactionDataModel> data;
    static View.OnClickListener myOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.transaction_view);
        recyclerView.setHasFixedSize(true);

        //user transaction
        ArrayList<Transaction> userTransaction = UserMapping.user.getAccounts().get(0).getTransactions();
        String date = userTransaction.get(0).getDate();
        String id = userTransaction.get(0).get_id();
        String type = userTransaction.get(0).getType();
        Float amount = userTransaction.get(0).getAmount();
        String name = userTransaction.get(0).getDescription();
        TransactionDataModel transactionDataModel = new TransactionDataModel(date,type,amount.toString(),id,name);

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