package com.example.gabriel.puntodeapoyo;

import android.app.Application;

public class VariablesGlobales extends Application {
    static double latitud;
    static double longitud;

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
