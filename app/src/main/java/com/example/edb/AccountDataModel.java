package com.example.edb;

public class AccountDataModel {

    String accountNumber;
    double balance;

    public AccountDataModel(String accountNumber, double balance) {
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