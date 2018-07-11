package com.example.gabriel.puntodeapoyo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gabii on 13/01/2018.
 */

public class ContactsDbHelper extends SQLiteOpenHelper {

    public ContactsDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(this.getClass().toString(), "Creando base de datos");
        db.execSQL("CREATE TABLE CONTACTS(" +
                " name TEXT PRIMARY KEY NOT NULL, " +
                " number TEXT NOT NULL)" );
        db.execSQL( "CREATE UNIQUE INDEX name ON CONTACTS(name ASC)" );
        Log.d(this.getClass().toString(),"Tabla creada");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
