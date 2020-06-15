package com.example.smartlockingcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Bu proje https://www.youtube.com/watch?v=d5jfNSFu45U&list=WL&index=17&t=3871s
adresinde anlatılan biçimde (Prabeesh) login ve registration işlemleri yapar.
Wampserver'a 192.168.137.1 adresinden bağlanır ve MySQL tablosuna username, password ve name yazar.
Aynı zamanda login işlemlerini yapar ve welcome fragment ile karşılama ekranına gelir.

***************** 02April *****************
30 March çalışan projenin devamı.

*****************  10May  *****************
30march projesinin kopyasıdır. Yeni ismiyle proje oluşturulmuştur.
 */

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFormActivityListener,
        WelcomeFragment.OnLogOutListener, WelcomeFragment.OnRegisterFormActivityListener,
        WelcomeFragment.OnDeleteFormActivityListener, WelcomeFragment.OnPairDeviceListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }
            checkUser(MainActivity.prefConfig.readUsername());
            //daha önce giriş yapılmış mı?
            if (prefConfig.readLoginStatus()){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                        new WelcomeFragment()).commit();
            }
            else{
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                        new LoginFragment()).commit();
            }
        }
    }

    @Override
    public void performRegister() {
        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RegistrationFragment()).addToBackStack(null).commit();*/ //30 march
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RegistrationFragment()).commit();
    }

    @Override
    public void performLogin(String name) {
        prefConfig.writeName(name);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new WelcomeFragment()).commit();
    }

    @Override
    public void logoutPerformed() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeName("Kullanici");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new LoginFragment()).commit();
    }

    //uygulama açılışında hesabın hala geçerli olup olmadığını kontrol eden fonksiyon
    public void checkUser(String username){
        Call<User> call = MainActivity.apiInterface.checkUser(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    MainActivity.prefConfig.writeLoginStatus(true);
                }
                else if (response.body().getResponse().equals("failed")){
                    MainActivity.prefConfig.writeLoginStatus(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                MainActivity.prefConfig.writeLoginStatus(false);
            }
        });
    }

    @Override
    public void performDelete() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DeletionFragment()).commit();
    }

    @Override
    public void performPairDevice() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PairDeviceFragment()).commit();
    }
}
