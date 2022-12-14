package com.example.edb.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.edb.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        EditText fullNameTxt = view.findViewById(R.id.full_name_txt);
        EditText addressTxt = view.findViewById(R.id.address_txt);
        EditText mobileNumTxt = view.findViewById(R.id.mobile_num_txt);
        EditText emailTxt = view.findViewById(R.id.email_txt);
        EditText nationalIDTxt = view.findViewById(R.id.id_txt);
        Button logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        fullNameTxt.setText(UserMapping.user.getFullName());
        emailTxt.setText((UserMapping.user.getEmail()));
        nationalIDTxt.setText(UserMapping.user.getSSN());
        mobileNumTxt.setText(UserMapping.user.getPhoneNumber());
        addressTxt.setText(UserMapping.user.getAddress());

        return view;
    }
}