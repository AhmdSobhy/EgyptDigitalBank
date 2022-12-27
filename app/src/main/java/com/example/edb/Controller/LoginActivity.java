package com.example.edb.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.edb.API.ApiInterface;
import com.example.edb.API.ApiUrl;
import com.example.edb.API.CallingAPI;
import com.example.edb.Model.User;
import com.example.edb.R;

import java.util.HashMap;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailTxt = findViewById(R.id.email_txt);
        EditText passwordTxt = findViewById(R.id.password_txt);
        Button loginBtn = findViewById(R.id.login_acc_btn);
        ImageButton fingerprintLoginBtn = findViewById(R.id.fingerprint_btn);
        Button createAccountBtn = findViewById(R.id.login_create_acc_btn);

        sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFromAPI(emailTxt.getText().toString(), passwordTxt.getText().toString());
            }
        });

        fingerprintLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithFingerprint();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });
    }

    void loginFromAPI(String email, String password) {
        try{
            CallingAPI callingAPI=new CallingAPI();
            callingAPI.login(email,password);

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                public void run() {
                    User user=UserMapping.user;
                    assert (user!=null);
                    sharedPreferences.edit().putString("email", user.getEmail()).commit();
                    sharedPreferences.edit().putString("password", user.getPassword()).commit();
                    System.out.println(user.getEmail());
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
            }, 10000);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void loginWithFingerprint() {
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device doesn't have fingerprint", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "fingerprint is not working", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No fingerprint registered", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                String email = sharedPreferences.getString("email", "null");
                String password = sharedPreferences.getString("password", "null");

                if (!email.equals("null") && !password.equals("null"))
                    loginFromAPI(email, password);
                else
                    Toast.makeText(getApplicationContext(), "Login to enable fingerprint", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use fingerprint to login")
                .setNegativeButtonText("Use Password")
                .setConfirmationRequired(false)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}