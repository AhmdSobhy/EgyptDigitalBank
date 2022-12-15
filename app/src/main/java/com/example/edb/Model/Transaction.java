package com.example.edb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {
    @SerializedName("Type")
    @Expose
    private String Type;

    @SerializedName("Amount")
    @Expose
    private float Amount;

    @SerializedName("Date")
    @Expose
    private String Date;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("_id")
    @Expose
    private String _id;

    public String getType() {
        return Type;
    }

    public float getAmount() {
        return Amount;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return description;
    }

    public String get_id() {
        return _id;
    }

    // Setter Methods

    public void setType(String Type) {
        this.Type = Type;
    }

    public void setAmount(float Amount) {
        this.Amount = Amount;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
