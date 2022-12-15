package com.example.edb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("SSN")
    @Expose
    private String SSN;

    @SerializedName("FullName")
    @Expose
    private String FullName;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("Password")
    @Expose
    private String Password;

    @SerializedName("Gender")
    @Expose
    private String Gender;

    @SerializedName("Accounts")
    @Expose
    ArrayList< Account > Accounts = new ArrayList < Account > ();

    public ArrayList<Account> getAccounts() {
        return Accounts;
    }

    public User(String ssn){
        this.SSN=ssn;
    }

    public User(String email, String password) {
        Email = email;
        Password = password;
    }
// Getter Methods

    public String get_id() {
        return _id;
    }

    public String getSSN() {
        return SSN;
    }

    public String getFullName() {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    // Setter Methods

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
