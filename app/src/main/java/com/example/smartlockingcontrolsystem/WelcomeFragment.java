package com.example.smartlockingcontrolsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {
    private TextView RegText,DelText;
    private TextView textView;
    private Button BnLogOut, btnPair;
    private ImageView btnOn, btnOff;

    OnLogOutListener logOutListener;
    OnRegisterFormActivityListener registerFormActivityListener;
    OnDeleteFormActivityListener deleteFormActivityListener;
    OnPairDeviceListener pairDeviceListener;

    public interface OnLogOutListener{
        public void logoutPerformed();
    }

    public interface OnRegisterFormActivityListener{
        public void performRegister();
    }

    public interface OnDeleteFormActivityListener{
        public void performDelete();
    }

    public interface OnPairDeviceListener{
        public void performPairDevice();
    }

    public WelcomeFragment() {
        // Required empty public constructor
    }

    /*** BUTON BAĞLANTILARI İÇİN EKLEMELER *** BAŞLANGIÇ ***/
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;

    BluetoothSocket btSocket = null;
    BluetoothDevice remoteDevice;
    BluetoothServerSocket mmServer; // kullandın mı?

    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //serial port için

    /*** BUTON BAĞLANTILARI İÇİN EKLEMELER ***   BİTİŞ   ***/


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        textView = view.findViewById(R.id.txt_name_info);
        String isim = MainActivity.prefConfig.readName();
        textView.setText("Welcome "+ isim.substring(0,1).toUpperCase() + isim.substring(1).toLowerCase());
        BnLogOut = view.findViewById(R.id.bn_logout);
        RegText = view.findViewById(R.id.register_txt);
        DelText = view.findViewById(R.id.remove_txt);

        /*** BUTON BAĞLANTILARI İÇİN EKLEMELER *** BAŞLANGIÇ ***/
        btnPair = view.findViewById(R.id.btn_pair);
        btnOn = view.findViewById(R.id.img_lock_on);
        btnOff = view.findViewById(R.id.img_lock_off);

        address = MainActivity.prefConfig.readAddress();

        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairDeviceListener.performPairDevice();
            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null){
                    try {
                        btSocket.getOutputStream().write("1".toString().getBytes());
                    } catch (IOException e) {
                        MainActivity.prefConfig.displayToast("Hata-1! Bluetooth veri gönderemedi.");
                        //e.printStackTrace();
                    }
                }
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null){
                    try {
                        btSocket.getOutputStream().write("2".toString().getBytes());
                    } catch (IOException e) {
                        MainActivity.prefConfig.displayToast("Hata-2! Bluetooth veri gönderemedi.");
                        //e.printStackTrace();
                    }
                }
            }
        });
        //new BTbaglan().execute(); /** BT CONNECTION **/

        /*** BUTON BAĞLANTILARI İÇİN EKLEMELER ***   BİTİŞ   ***/

        BnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutListener.logoutPerformed();
            }
        });
        // yeni kullanici butonu aktiflestirme
        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFormActivityListener.performRegister();
            }
        });

        DelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFormActivityListener.performDelete();
            }
        });
        //new BTbaglan().execute();
        return view;
    }

    /*** Functions that is after onCreate ***/
    private void Disconnect(){
        if(btSocket!=null){
            try {
                btSocket.close();
            } catch (IOException e){
                // msg("Error");
            }
        }
        getActivity().finish();
        //finish();
    }

    public void onBackPressed() {
        //super.getActivity().onBackPressed();
        Disconnect();
    }
    /** BT CONNECTION **//*
    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Connecting...", "Please wait...");
        }


        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //ConnectSuccess = true;
            if (!ConnectSuccess) {
                // msg("Baglantı Hatası, Lütfen Tekrar Deneyin");
                MainActivity.prefConfig.displayToast("Connection error! Try again...");
                getActivity().finish();
                //finish();
            } else {
                //   msg("Baglantı Basarılı");
                MainActivity.prefConfig.displayToast("Connection successful.");

                isBtConnected = true;
            }
            progress.dismiss();
        }

    }*/
    /** BT CONNECTION **/
    /*** Finish ***/


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        logOutListener = (OnLogOutListener) activity;
        registerFormActivityListener = (OnRegisterFormActivityListener) activity;
        deleteFormActivityListener = (OnDeleteFormActivityListener) activity;
        pairDeviceListener = (OnPairDeviceListener) activity;
    }
}
