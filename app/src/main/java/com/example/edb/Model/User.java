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

    @SerializedName("PhoneNumber")
    @Expose
    private String PhoneNumber;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("Accounts")
    @Expose
     ArrayList< Account > Accounts = new ArrayList < Account> ();

    public ArrayList<Account> getAccounts() {
        return Accounts;
    }
    public User(String SSN, String FullName,String Email, String Password, String Gender,String PhoneNumber, String Address) {
        this.SSN = SSN;
        this.Email = Email;
        this.Password = Password;
        this.FullName = FullName;
        this.Gender = Gender;
        this.PhoneNumber = PhoneNumber;
        this.Address=Address;
    }

    public User(String ssn, Account account)
    {
        this.SSN = ssn;
        Accounts.add(account);
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

    public String getGender() {
        return Gender;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getAddress() {
        return Address;
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
