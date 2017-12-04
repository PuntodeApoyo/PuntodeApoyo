package com.example.gabriel.puntodeapoyo;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

public class ServiceLocalizacion extends Service {
    int tiempoEntreActualizaciones=1500;//tiempo en milisegundos
    int cambioDistanciaParaActualizar=1;//Distancia en metros
    double lat;//Variable para obtener latitud
    double lng ;//Variable para obtener longitud
    public LocationManager locationManager;
    public final static String MY_ACTION ="OBTENER_UBICACION";

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            miUbicacion();
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };
    public ServiceLocalizacion() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        miUbicacion();
        MyThread myThread = new MyThread();
        myThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                tiempoEntreActualizaciones,
                cambioDistanciaParaActualizar,
                locListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                tiempoEntreActualizaciones,
                cambioDistanciaParaActualizar,
                locListener);
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }
    public class MyThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                Intent intent = new Intent();
                intent.setAction(MY_ACTION);
                intent.putExtra("Latitud",lat);
                intent.putExtra("Longitud",lng);
                sendBroadcast(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
