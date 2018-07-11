package com.example.gabriel.puntodeapoyo.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.Contact;
import com.example.gabriel.puntodeapoyo.Data.ContactDAO;
import com.example.gabriel.puntodeapoyo.Data.ContactsDbHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SmsService extends Service {
    private LatLng mCurrentLocation;
    private IntentFilter intentFilter;
    private String latitude=null;
    private String longitude=null;

    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        //Broadcast para recibir las coordenadas del usuario desde LocationUpdaterService
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentLocation=intent.getParcelableExtra("LatLng");
            latitude=Double.toString(mCurrentLocation.latitude);
            longitude=Double.toString(mCurrentLocation.longitude);
            showToast(latitude,longitude);
            String message="https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude;
            sendAlert(message);
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startService();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
    public void showToast(String latitude,String longitude){
        Toast.makeText(this, "Latitud: "+latitude+"\nLongitud: "+longitude, Toast.LENGTH_SHORT).show();
    }

    public void sendAlert(String message){
        ContactDAO dao= new ContactDAO(getApplicationContext());
        ArrayList<Contact> contactos= dao.leerTodos();
        SmsManager sms = SmsManager.getDefault();
        for (int i=0;i > contactos.size();i++){
            sms.sendTextMessage(contactos.get(i).getNumber(),null,message,null,null);
        }//Verificar si sendTextMessage retorna algun valor para verificar
        // String phoneNumber="2634402085";
        //sms.sendTextMessage(phoneNumber,null,message,null,null);
        //Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();

    }
    private void startService(){
        Intent service = new Intent(this, LocationUpdaterService.class);
        startService(service);
        intentFilter=new IntentFilter("SEND_LOCATION");
        registerReceiver(mBroadcastReceiver,intentFilter);
    }
}
