package com.example.gabriel.puntodeapoyo.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gabriel.puntodeapoyo.Model.Contact;

import java.util.ArrayList;
//Una vez el programa funcione,eliminar el codigo comentado

public class ContactDAO {

    private Context context;
    private ContactsDbHelper helper;

    public ContactDAO(Context context){
        this.context=context;
        this.helper=new ContactsDbHelper(context,"Contacts",null,1);
    }

    public long anadirContacto(String name,String number){
        ContactsDbHelper contactsDbHelper=new ContactsDbHelper(context,"Contacts",null,1);
        SQLiteDatabase database=contactsDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("number", number);
            long insercion=database.insert("CONTACTS", null, values);
        database.close();
        return insercion;
    }

    public int eliminarContacto(String name){
        ContactsDbHelper contactsDbHelper=new ContactsDbHelper(context,"Contacts",null,1);
        SQLiteDatabase database=contactsDbHelper.getWritableDatabase();
        int eliminar=database.delete("CONTACTS","name='"+name+"'",null);
        database.close();
        return eliminar;
    }

    public ArrayList<Contact> leerTodos(){
        ArrayList<Contact> contactos=new ArrayList<>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select name,number from CONTACTS",null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            do {
                contactos.add(new Contact(cursor.getString(cursor.getColumnIndex("name")),
                                            cursor.getString(cursor.getColumnIndex("number"))));

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactos;
    }
    public void eliminarTabla(){
        context.deleteDatabase("CONTACTS");
    }
}
