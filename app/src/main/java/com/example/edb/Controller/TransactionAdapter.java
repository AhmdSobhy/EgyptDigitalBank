package com.example.edb.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edb.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

private final ArrayList <TransactionDataModel> dataSet;

public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView transactionDateText;
    TextView transactionTypeText;
    TextView transactionAmountText;
    TextView transactionIDText;
    TextView transactionNameText;

    public MyViewHolder(View itemView) {
        super(itemView);
        this.transactionDateText = itemView.findViewById(R.id.transaction_date_txt);
        this.transactionTypeText = itemView.findViewById(R.id.transaction_type_txt);
        this.transactionAmountText = itemView.findViewById(R.id.transaction_amount_txt);
        this.transactionIDText = itemView.findViewById(R.id.transaction_id_txt);
        this.transactionNameText = itemView.findViewById(R.id.transaction_name_txt);
    }
}

    public TransactionAdapter(ArrayList<TransactionDataModel> data) {
        this.dataSet = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        view.setOnClickListener(TransactionsFragment.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView transactionDateText = holder.transactionDateText;
        TextView transactionAmountText = holder.transactionAmountText;
        TextView transactionTypeText = holder.transactionTypeText;
        TextView transactionIDText = holder.transactionIDText;
        TextView transactionNameText = holder.transactionTypeText;

        transactionDateText.setText(dataSet.get(listPosition).getTransactionDate());
        transactionIDText.setText(dataSet.get(listPosition).getTransactionID());
        transactionNameText.setText(dataSet.get(listPosition).getTransactionName());
        transactionAmountText.setText(dataSet.get(listPosition).getAmount());
        transactionTypeText.setText(dataSet.get(listPosition).getTransactionType());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
