package com.example.gabriel.puntodeapoyo;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.gabriel.puntodeapoyo.Model.Comercio;

public class Sugerencias implements SearchSuggestion {
    String nombre;
    double latitud;
    double longitud;

    public Sugerencias(String nombre, double latitud, double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Sugerencias(Parcel source) {
        this.nombre=source.readString();
    }

    @Override
    public String getBody() {
        return nombre;
    }

    @Override
    public Double getLatitud() {
        return latitud;
    }

    @Override
    public Double getLongitud() {
        return longitud;
    }

    public static final Creator<Sugerencias> CREATOR= new Creator<Sugerencias>() {
        @Override
        public Sugerencias createFromParcel(Parcel parcel) {
            return new Sugerencias(parcel);
        }

        @Override
        public Sugerencias[] newArray(int size) {
            return new Sugerencias[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeDouble(latitud);
        parcel.writeDouble(longitud);
    }
}
