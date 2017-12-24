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
    public JsonReaderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
            }
            for (int i=0;i<lugares.size();i++) {
                Log.i("Id", id.get(i));
                Log.i("Nombre", lugares.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}
