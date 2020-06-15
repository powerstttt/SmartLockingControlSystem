package com.example.smartlockingcontrolsystem;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private EditText Name,UserName,UserPassword;
    private Button BnRegister;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        Name = view.findViewById(R.id.txt_name);
        UserName = view.findViewById(R.id.txt_user_name);
        UserPassword = view.findViewById(R.id.txt_password);
        BnRegister = view.findViewById(R.id.bn_register);

        BnRegister.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) { //burası videoda view??? normalde v oluştu
                performRegistration();
            }
        });


        return view;
    }

    public void performRegistration(){
        String name = Name.getText().toString();
        String username = UserName.getText().toString();
        String password = UserPassword.getText().toString();

        Call<User> call = MainActivity.apiInterface.performRegistration(name,username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    MainActivity.prefConfig.displayToast("New user "+username+" is created successfully.");
                }
                else if (response.body().getResponse().equals("exist")){
                    MainActivity.prefConfig.displayToast("User already exist.");
                }
                else if (response.body().getResponse().equals("error")){
                    MainActivity.prefConfig.displayToast("Something went wrong. Registration error.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Server connection problem.");
            }
        });
        Name.setText("");
        UserPassword.setText("");
        UserName.setText("");
    }

}
