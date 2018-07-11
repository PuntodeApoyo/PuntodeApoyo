package com.example.gabriel.puntodeapoyo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonReaderService extends Service {
    ArrayList<String> lugares=new ArrayList<>();
    ArrayList<String> id=new ArrayList<>();
    ArrayList<String> lat=new ArrayList<>();
    ArrayList<String> lng=new ArrayList<>();


    public JsonReaderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
         sendArrays(id,lugares,lat,lng);
         return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadJSON();
    }
    public void loadJSON(){
        String json;
        try {
            InputStream is=getAssets().open("a.json");
            int size=is.available();
            byte[]buffer=new byte[size];
            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");
            JSONArray jsonArray=new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject obj=jsonArray.getJSONObject(i);
                lugares.add(obj.getString("Nombre"));
                id.add(obj.getString("Id"));
                lat.add(obj.getString("Latitud"));
                lng.add(obj.getString("Longitud"));
            }
            for (int i=0;i<lugares.size();i++) {
                Log.i("Id", id.get(i));
                Log.i("Nombre", lugares.get(i));
                Log.i("Latitud",lat.get(i));
                Log.i("Longitud",lng.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


    }
    public void sendArrays(ArrayList id,ArrayList nombre,ArrayList lat,ArrayList lng){
        Intent i = new Intent("Get places");
        i.putParcelableArrayListExtra("Nombres",nombre);
        i.putParcelableArrayListExtra("Ids",id);
        i.putParcelableArrayListExtra("Latitudes",lat);
        i.putParcelableArrayListExtra("Longitudes",lng);
        sendBroadcast(i);
        Log.i("Estado del broadcast","Enviado");
    }
}
