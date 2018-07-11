package com.example.gabriel.puntodeapoyo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.databinding.ActivityContacsBinding;
import com.example.gabriel.puntodeapoyo.databinding.ItemAlertListBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ContacsActivity extends AppCompatActivity implements AdapterCallback {
    static final int PICK_CONTACT = 1;
    private List<Contact> contacts;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private TextView txtview;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContacsBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_contacs);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestContactsPermission();
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });
       txtview=findViewById(R.id.textView2);
       recyclerView=binding.rvContacts;
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       populateRecyclerView(recyclerView);
       contactAdapter.setOnClick(this);
       recyclerView.addItemDecoration(new DividerItemDecoration(this,
               DividerItemDecoration.VERTICAL));//Añade separador entre items
        if (!contacts.isEmpty()){
            Toast.makeText(getApplicationContext(), "Para eliminar un contacto,mantengalo presionado", Toast.LENGTH_LONG).show();
        }
    }
    public void populateRecyclerView(RecyclerView recycler){
        ContactDAO dao=new ContactDAO(this);
        contacts=dao.leerTodos();
        textVisibility();
        contactAdapter=new ContactAdapter(contacts);
        recycler.setAdapter(contactAdapter);
    }
    @Override
    public void onItemClick(int position) {//Implementacion onClick personalizada para el recyclerview
        String name=contactAdapter.mContactos.get(position).name;
        deleteDialog(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            if (cursor!=null && cursor.moveToFirst()){int nameIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name=cursor.getString(nameIndex);
                String number=contactNumber(contactUri);
                ContactDAO dao=new ContactDAO(this);
                dao.anadirContacto(name,number);
                populateRecyclerView(recyclerView);
                textVisibility();
            }
        }
    }
    private String contactNumber(Uri uriContact) {
        String contactID=null;
        String contactNumber = null;
        //Se utiliza el id del contacto para posteriormente obtener su numero
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        //Se obtiene el numero del contacto
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        return contactNumber;
    }

    public void textVisibility(){
        if (contacts.isEmpty()){
            txtview.setVisibility(View.VISIBLE);
        }else {
            txtview.setVisibility(View.GONE);
        }
    }

    public void deleteDialog(final String name){ //Utilizado para invocar un alertDialog cuando se mantiene presionado un elemento
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.remove_contact_tittle)
                .setMessage(R.string.remove_contact_message)
                .setPositiveButton(R.string.remove_contact_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactDAO dao=new ContactDAO(getApplicationContext());
                        //deleteContact(name);
                        dao.eliminarContacto(name);
                        onBackPressed();
                    }
                }).setNegativeButton(R.string.remove_contact_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alerta = dialog.create();
        alerta.show();
    }
    public void requestContactsPermission(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS))!=PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }

}
