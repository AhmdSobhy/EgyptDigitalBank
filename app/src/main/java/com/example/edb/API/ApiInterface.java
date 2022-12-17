package com.example.edb.API;

import com.example.edb.Model.Account;
import com.example.edb.Model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("login")
    Call<User> getUserInfo(@Body User user);
    
    @POST("login")
    Call<User> getUserInfo(@Body HashMap<String,String>map);

    @POST("add-user")
    Call<User> createUser(@Body User user);

    @POST("add-account")
    Call<User> createAccount(@Body User user);

    @POST("add-account/{userSSN}")
     Call<User> createAccount2(@Body Account account,@Path("userSSN") String userSSN) ;



}
