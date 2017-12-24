package com.example.gabriel.puntodeapoyo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gabriel.puntodeapoyo.Services.LocationUpdaterService;

/**
 * Esta clase fue creada con el objetivo de lanzar el servicio cuando
 * el dispositivo se enciende
 */

public class BootMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent=new Intent(context,LocationUpdaterService.class);
        context.startService(serviceIntent);
    }
}
