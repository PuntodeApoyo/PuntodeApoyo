package com.example.gabriel.puntodeapoyo;

import android.app.Application;

import java.util.ArrayList;

public class VariablesGlobales extends Application {
    static double latitud;
    static double longitud;
    public  boolean isProviderEnabled;
    public static ArrayList listaNombres=new ArrayList();

//Getters and Setters
    public static double getLatitud() {
        return latitud;
    }
    public static void setLatitud(double latitud) {
        VariablesGlobales.latitud = latitud;
    }
    public static double getLongitud() {
        return longitud;
    }
    public static void setLongitud(double longitud) {
        VariablesGlobales.longitud = longitud;
    }

    public boolean isProviderEnabled() {
        return isProviderEnabled;
    }

    public void setProviderEnabled(boolean providerEnabled) {
        isProviderEnabled = providerEnabled;
    }

    public static void setListaNombres(String s) {
        VariablesGlobales.listaNombres.add(s);
    }

    public static ArrayList<String> getListaNombres() {
        return listaNombres;
    }
}
