package com.example.smartlockingcontrolsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public  PrefConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file),
                Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status),status);
        editor.commit();
    }

    public boolean readLoginStatus(){
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status),false);
    }

    public void writeName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name),name);
        editor.commit();
    }

    protected void writeUsername(String Username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.example.preference_real_username",Username);
        editor.commit();
    }

    protected void writePass(String pass){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.example.preference_pass",pass);
        editor.commit();
    }
    protected String readUsername(){
        return sharedPreferences.getString("com.example.preference_real_username","invalidUsername");
    }

    protected String readPass(){
        return sharedPreferences.getString("com.example.preference_pass","invalidPassword");
    }

    public String readName(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_name),"User");
    }

    public void displayToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public void writeAddress(String address){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.example.preference_address",address);
        editor.commit();
    }

    protected String readAddress(){
        return sharedPreferences.getString("com.example.preference_address","device_address");
    }
}
