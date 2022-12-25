package com.example.edb.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edb.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private Context mContext;
    private final ArrayList <TransactionDataModel> dataSet;
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

        public MyViewHolder(View itemView) {
            super(itemView);
            this.transactionDateText = itemView.findViewById(R.id.transaction_date_txt);
            this.transactionTypeText = itemView.findViewById(R.id.transaction_type_txt);
            this.transactionAmountText = itemView.findViewById(R.id.transaction_amount_txt);
            this.transactionIDText = itemView.findViewById(R.id.transaction_id_txt);
            this.transactionNameText = itemView.findViewById(R.id.transaction_name_txt);

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

    public TransactionAdapter(Context context,ArrayList<TransactionDataModel> data) {
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
        String date = dataSet.get(listPosition).getTransactionDate();

        holder.transactionDateText.setText(date.substring(5,10));
        holder.transactionIDText.setText(dataSet.get(listPosition).getTransactionID());
        holder.transactionNameText.setText(dataSet.get(listPosition).getTransactionName());
        holder.transactionAmountText.setText(dataSet.get(listPosition).getAmount());
        if (dataSet.get(listPosition).getTransactionType().equals("withdraw"))
            holder.transactionTypeText.setText("-");
        else
            holder.transactionTypeText.setText("+");

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
