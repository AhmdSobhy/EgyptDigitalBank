package com.example.edb.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edb.Model.Account;
import com.example.edb.R;

import java.util.ArrayList;

public class AccountCardAdapter extends RecyclerView.Adapter<AccountCardAdapter.MyViewHolder> {
    private Context mContext;
    private final ArrayList <Account> dataSet;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accountNumText;
        TextView currencyText;
        TextView balanceText;
        TextView accountType;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.accountNumText = itemView.findViewById(R.id.account_num_txt);
            this.currencyText = itemView.findViewById(R.id.currency_txt);
            this.balanceText = itemView.findViewById(R.id.balance_txt);
            this.accountType = itemView.findViewById(R.id.acc_type);

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

    public AccountCardAdapter(Context context,ArrayList<Account> data) {
        this.mContext = context;
        this.dataSet = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.accountNumText.setText(dataSet.get(listPosition).getAccountNumber());
        holder.balanceText.setText(String.valueOf(dataSet.get(listPosition).getBalance()));
        holder.accountType.setText(dataSet.get(listPosition).getType());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
