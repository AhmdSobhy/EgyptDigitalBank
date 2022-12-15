package com.example.edb.API;

import com.example.edb.Model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("login")
    Call<User> getUserInfo(@Body User user);

    @POST("login")
    Call<User> getUserInfo(@Body HashMap<String, String> map);

    @POST("add-user")
    Call<User> addUser(@Body User user);

    @POST("add-transaction")
    Call<User> addTransaction(@Body User user);

    @POST("update-balance")
    Call<User> updateBalance(@Body User user);

}