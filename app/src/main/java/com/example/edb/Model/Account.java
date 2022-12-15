package com.example.edb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    @SerializedName("Type")
    @Expose
    private String Type;

    @SerializedName("Balance")
    @Expose
    private float Balance;

    @SerializedName("Currency")
    @Expose
    private String Currency;

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("DateOfCreation")
    @Expose
    private String DateOfCreation;

    @SerializedName("Transactions")
    @Expose
    ArrayList< Transaction > Transactions = new ArrayList < Transaction > ();

    @SerializedName("_id")
    @Expose
    private String _id;

    public Account(String id, float balance) {
        this._id = id;
        this.Balance = balance;
    }


    // Getter Methods

    public String getType() {
        return Type;
    }

    public float getBalance() {
        return Balance;
    }

    public String getCurrency() {
        return Currency;
    }

    public String getStatus() {
        return Status;
    }

    public String getDateOfCreation() {
        return DateOfCreation;
    }

    public String get_id() {
        return _id;
    }

    // Setter Methods

    public void setType(String Type) {
        this.Type = Type;
    }

    public void setBalance(float Balance) {
        this.Balance = Balance;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setDateOfCreation(String DateOfCreation) {
        this.DateOfCreation = DateOfCreation;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
