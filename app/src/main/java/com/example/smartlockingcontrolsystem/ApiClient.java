package com.example.smartlockingcontrolsystem;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //public static final String BASE_URL = "http://192.168.137.1/LoginApp/";  //Wi-Fi IP address 10
    //public static final String BASE_URL = "http://192.168.1.53/LoginApp/";  //adonun ip'si
    public static final String BASE_URL = "http://192.168.1.33/LoginApp/";  //Wi-Fi IP address
    //public static final String BASE_URL = "http://192.168.43.218/LoginApp/";   //Android Hotspot Wi-Fi IP Address
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
