package com.example.edb.API;

import com.example.edb.Model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("login")
    Call<User> getUserInfo(@Body User user);

    @POST("login")
    Call<User> getUserInfo(@Body HashMap<String, String> map);

    @POST("add-user")
    Call<User> addUser(@Body User user);

    @POST("add-transaction")
    Call<User> addTransaction(@Body User user);

    @PATCH("update-balance/{userSSN}/{accountId}")
    Call<Void> updateBalance(@Path("userSSN") String userSSN, @Path("accountId")String accountId, @Body HashMap<String,String>map);

}