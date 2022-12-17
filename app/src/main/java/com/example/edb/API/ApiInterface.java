package com.example.edb.API;

import com.example.edb.Model.Account;
import com.example.edb.Model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("login")
    Call<User> getUserInfo(@Body User user);
    
    @POST("login")
    Call<User> getUserInfo(@Body HashMap<String,String>map);

    @POST("add-user")
    Call<User> createUser(@Body User user);

    @POST("add-account/{userSSN}")
     Call<User> createAccount(@Path("userSSN") String userSSN, @Body Account account) ;

}
