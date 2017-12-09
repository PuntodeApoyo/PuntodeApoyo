package com.example.gabriel.puntodeapoyo;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.fragments.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class ServiceLocalizacion extends Service {

    int tiempoEntreActualizaciones = 1500;//tiempo en milisegundos
    int cambioDistanciaParaActualizar = 1;//Distancia en metros
    public LocationManager locationManager;
    VariablesGlobales variables=new VariablesGlobales();


    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            variables.setIsProviderEnabled(false);
        }
    };

    public ServiceLocalizacion() {
    }

    @Override
    public void onCreate() {
        // Establecer punto de entrada para la API de ubicación
        miUbicacion();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        //Limpiar el servicio
        super.onDestroy();
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,15000,0,locListener);
    }
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            Toast.makeText(this, "Hola mundo", Toast.LENGTH_SHORT).show();
            variables.setLatitud(location.getLatitude());
            variables.setLongitud(location.getLongitude());
        }
    }
}
