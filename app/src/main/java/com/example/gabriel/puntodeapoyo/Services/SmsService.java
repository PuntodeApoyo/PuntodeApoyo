package com.example.gabriel.puntodeapoyo.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.Contact;
import com.example.gabriel.puntodeapoyo.ContactsDbHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class SmsService extends Service {
    private LatLng mCurrentLocation;
    private IntentFilter intentFilter;
    private String latitude=null;
    private String longitude=null;
    public SmsService() {
    }

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
        Cursor c=queryContacts();

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            //En este bucle se pasan todos los numeros del cursor hacia una lista
            String phoneNumber=c.getString(0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber,null,message,null,null);
        }
        // String phoneNumber="2634402085";
        //sms.sendTextMessage(phoneNumber,null,message,null,null);
        //Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();

    }
    public Cursor queryContacts (){
        //Este cursor obtiene todos los numeros guardados en la db creada en los ajustes
        ContactsDbHelper contactsDbHelper=new ContactsDbHelper(this,"Contacts",null,1);
        SQLiteDatabase database=contactsDbHelper.getWritableDatabase();
        Cursor data=database.rawQuery("select number from CONTACTS",null);
        return  data;
    }
    private void startService(){
        Intent service = new Intent(this, LocationUpdaterService.class);
        startService(service);
        intentFilter=new IntentFilter("SEND_LOCATION");
        registerReceiver(mBroadcastReceiver,intentFilter);
    }
}
