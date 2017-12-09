package com.example.gabriel.puntodeapoyo;

import android.app.Application;

import java.util.ArrayList;

public class VariablesGlobales extends Application {
    static double latitud;
    static double longitud;
    static boolean isProviderEnabled;
    static ArrayList listaNombres=new ArrayList();

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
    public static boolean isIsProviderEnabled() {
        return isProviderEnabled;
    }
    public static void setIsProviderEnabled(boolean isProviderEnabled) {
        VariablesGlobales.isProviderEnabled = isProviderEnabled;
    }
    public static ArrayList getListaNombres() {
        return listaNombres;
    }
    public static void setListaNombres(String s) {
        VariablesGlobales.listaNombres.add(s);
    }
}
