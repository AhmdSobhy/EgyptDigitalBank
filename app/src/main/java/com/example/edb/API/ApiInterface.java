package com.example.edb.API;

import com.example.edb.Model.Account;
import com.example.edb.Model.Transaction;
import com.example.edb.Model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface  ApiInterface {

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

    @POST("add-user")
    Call<User> createUser(@Body User user);

    @POST("add-account/{userSSN}")
    Call<User> createAccount(@Path("userSSN") String userSSN, @Body Account account) ;

    @GET("/isValid/AccountNo/{accountID}")
    Call<Void> isExistAccountId(@Path("accountID") String accountID);
    @GET("add-transaction")
    Call<List<Transaction>> getTransactions(@Query("SSN") String SSN);
    @GET("/users/Accounts/{accountID}")
    Call<User> getUserByAccountId(@Path("accountID") String accountId);


}
