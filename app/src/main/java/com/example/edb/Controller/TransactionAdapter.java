package com.example.edb.Controller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edb.Model.Transaction;
import com.example.edb.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private Context mContext;
    private final ArrayList <Transaction> dataSet;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView transactionDateText;
        TextView transactionTypeText;
        TextView transactionAmountText;
        TextView transactionIDText;
        TextView transactionNameText;
        TextView transactionCurrency;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.transactionDateText = itemView.findViewById(R.id.transaction_date_txt);
            this.transactionTypeText = itemView.findViewById(R.id.transaction_type_txt);
            this.transactionAmountText = itemView.findViewById(R.id.transaction_amount_txt);
            this.transactionIDText = itemView.findViewById(R.id.transaction_id_txt);
            this.transactionNameText = itemView.findViewById(R.id.transaction_name_txt);
            this.transactionCurrency = itemView.findViewById(R.id.transaction_currency_txt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public TransactionAdapter(Context context,ArrayList<Transaction> data) {
        this.mContext = context;
        this.dataSet = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        String date = dataSet.get(listPosition).getDate();

        holder.transactionDateText.setText(date.substring(5,10).replace("-","/"));
        holder.transactionIDText.setText(dataSet.get(listPosition).get_id().substring(12));
        holder.transactionNameText.setText(dataSet.get(listPosition).getDescription());
        holder.transactionAmountText.setText(String.valueOf(dataSet.get(listPosition).getAmount()));
        if (dataSet.get(listPosition).getType().equals("withdraw")) {
            holder.transactionTypeText.setText("-");
            holder.transactionCurrency.setTextColor(Color.RED);
            holder.transactionTypeText.setTextColor(Color.RED);
            holder.transactionAmountText.setTextColor(Color.RED);
        }
        else {
            holder.transactionTypeText.setText("+");
            holder.transactionCurrency.setTextColor(Color.GREEN);
            holder.transactionTypeText.setTextColor(Color.GREEN);
            holder.transactionAmountText.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
