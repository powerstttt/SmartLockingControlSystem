package com.example.smartlockingcontrolsystem;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class PairDeviceFragment extends Fragment {

    BluetoothAdapter myBluetooth; //BluetoothAdapter bizm cihazımızı temsil ediyor.
    //BluetoothDevice etraftaki cihazları temsil ediyor
    private Set<BluetoothDevice> pairedDevices; //listelenecek aygıtlar dizisi
    Button toggle_button,pair_button;
    ListView pairedList;
    public static String EXTRA_ADDRESS = "device_address";
    ArrayAdapter<String> adapter;

    public PairDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_device, container, false);
        myBluetooth = BluetoothAdapter.getDefaultAdapter(); //Bluetooth cihazı var mı?

        toggle_button = (Button) view.findViewById(R.id.button_toggle); // toggle_button layouttaki buttona bağlandı
        pair_button = (Button) view.findViewById(R.id.button_pair);
        pairedList = view.findViewById(R.id.device_list);

        toggle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBluetooth();
            }
        });

        pair_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDevice();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    //Listeleme işlemi burada oluyor.
    private void listDevice() {
        pairedDevices = myBluetooth.getBondedDevices(); //eşleşmiş cihazları pairedDevices içine attı
        ArrayList list = new ArrayList();

        //eşleşmiş cihaz kontrol
        if(pairedDevices.size() > 0){
            for (BluetoothDevice bt: pairedDevices){ //bluetoothDevice olarak bt isimli bir tanımlama yapıldı ve
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else{
            MainActivity.prefConfig.displayToast("Eşleşmiş Cihaz Yok.");
        }

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list); //adaptör listelemk için gerekli
        //this yazan yer getActivity olarak değiştirildi.
        pairedList.setAdapter(adapter); //listeyi göster
        //aşağıdaki select device fonksiyonuna git
        pairedList.setOnItemClickListener(selectDevice);
    }

    //bluetooth var ise aç veya kapat
    private void toggleBluetooth() {
        if(myBluetooth == null) //bluetooth cihazı var mı?
            MainActivity.prefConfig.displayToast("Cihazın bluetooth bağlantısı bulunmamaktadır.");
        else if (!myBluetooth.isEnabled()){ //bluetooth aktif mi?
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
        }
        else{
            myBluetooth.disable();
        }
    }

    // Listeden cihaz seçmek için
    public AdapterView.OnItemClickListener selectDevice = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = (((TextView) view).getText().toString());
            String address = info.substring(info.length()-17);

            MainActivity.prefConfig.writeAddress(address); //send address to WelcomeFragment
            //Intent comintent = new Intent(getContext(), WelcomeFragment.class); /*** DEĞİŞTİRDİM ***/
            //MainActivity.this yazan yere getContext() yazdım.
            //comintent.putExtra(EXTRA_ADDRESS, address);
            //startActivity(comintent);
        }
    };

}
