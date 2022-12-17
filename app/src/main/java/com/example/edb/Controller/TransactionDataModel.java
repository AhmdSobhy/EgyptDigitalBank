package com.example.edb.Controller;

public class TransactionDataModel {

    String transactionDate;
    String transactionID;
    String amount;
    String transactionType;
    String transactionName;

    public TransactionDataModel(String transactionDate, String transactionType, String transactionAmount, String transactionID, String transactionName) {
        this.transactionID = transactionID;
        this.amount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.transactionName = transactionName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getAmount() {
        return amount;
    }

}