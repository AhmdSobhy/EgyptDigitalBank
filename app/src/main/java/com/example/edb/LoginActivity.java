package com.example.edb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.edb.API.ApiInterface;
import com.example.edb.Model.User;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import cz.msebera.android.httpclient.entity.StringEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailTxt = findViewById(R.id.email_txt);
        EditText passwordTxt = findViewById(R.id.password_txt);
        Button LoginBtn = findViewById(R.id.login_acc_btn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cloud = "https://bank-db-api.herokuapp.com/";
                Retrofit retrofit=new Retrofit.Builder().baseUrl(cloud).addConverterFactory(GsonConverterFactory.create()).build();

                ApiInterface apiInterface=retrofit.create(ApiInterface.class);
//                Call<User> call= apiInterface.getUserInfo(new User("mega2004@gmail.com","123456"));

                HashMap<String, String> map = new HashMap<>();
                map.put("Email","mega2004@gmail.com");
                map.put("Password", "123456");
                Call<User> call= apiInterface.getUserInfo(map);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, @NonNull Response<User> response) {
                        System.out.println(response.toString());
                        User res=response.body();
                        System.out.println(res.getFullName());
                        System.out.println(response.body());
                        String json = String.valueOf(response.body());
                        System.out.println(json);
                        try {
                            JSONObject jsonObj = new JSONObject(json);
                        } catch(Exception e) {
                            System.out.println("ERRRRRRRRRRRRRRRrrror");
                            System.out.println(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("Failure");
                    }
                });

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public static void post2() throws IOException, JSONException {
        URL url = new URL("https://bank-db-api.herokuapp.com/login"); //in the real code, there is an ip and a port
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("Email", "Yasser@gmail.com");
        jsonParam.put("Password", "123456");


        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));

        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
        System.out.println("Status: "+String.valueOf(conn.getResponseCode()));
        Log.i("MSG", conn.getResponseMessage());

        conn.disconnect();
    }
    public void post(String completeUrl, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(completeUrl);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity((HttpEntity) stringEntity);

            httpClient.execute(httpPost);
//            System.out.println(httpPost.r..);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void post3(){
        //                try {
//                    AsyncHttpClient client = new AsyncHttpClient();
//                    // Http Request Params Object
//                    String Email = "Yasser@gmail.com";
//                    String Password = "123456";
//                    RequestParams params = new RequestParams();
//                    params.add("Email", Email);
//                    params.add("Password", Password);
//                    String local = "http://localhost:3000/login";
//                    String cloud = "https://bank-db-api.herokuapp.com/login";
//
//                    JSONObject jsonParams = new JSONObject();
//                    jsonParams.put("Email", Email);
//                    jsonParams.put("Password", Password);
//                    RequestBody postBody = RequestBody.create(MediaType.parse("application/json"), "{\"Email\":\"Yasser@gmail.com\" \"Password\":\"123456\"}");
//
////                    MultipartBody.Builder postBody = new MultipartBody.Builder().addFormDataPart("test", "test.json", RequestBody.create(MediaType.parse("application/json"), "{\"test\":\"hello world\"}"));
//
//                    client.post(cloud, postBody, new JsonResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
//                            System.out.println("status code is " + statusCode);
//                            System.out.println("Response is " + responseBody.toString());
//                            try {
//                                JSONObject object = responseBody.getJSONObject(0);
//                                System.out.println("Id: " + object.getString("_id"));
//                                System.out.println("FullName: " + object.getString("FullName"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Log.i(getClass().getSimpleName(), "Response SP Status. ");
//
//                        }
//
//                        @Override
//                        public void onFailure(int x, Header[] h, String s, Throwable e) {
//                            System.out.println("status code is " + x);
//
//                        }
//
//
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    post2();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
    }
}