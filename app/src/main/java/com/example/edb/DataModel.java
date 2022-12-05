package com.example.edb;

public class DataModel {

    String accountNumber;
    double balance;

    public DataModel(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
}