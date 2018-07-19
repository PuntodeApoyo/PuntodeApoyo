package com.example.gabriel.puntodeapoyo.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.AdapterCallback;
import com.example.gabriel.puntodeapoyo.ContactAdapter;
import com.example.gabriel.puntodeapoyo.Data.ContactDAO;
import com.example.gabriel.puntodeapoyo.Model.Contact;
import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.databinding.FragmentAlertBinding;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AlertFragment extends Fragment implements AdapterCallback{
    static final int PICK_CONTACT = 1;
    private List<Contact> contacts;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private TextView txtview;
    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =2 ;

    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAlertBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_alert,container,false);
        View view =binding.getRoot();
        AppCompatActivity activity= (AppCompatActivity) getActivity();
        Toolbar toolbar=binding.toolbar2;
        activity.setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout) activity.findViewById(R.id.drawerLayout);
        txtview=view.findViewById(R.id.textView2);
        recyclerView=binding.rvContacts;
        toggle= new ActionBarDrawerToggle(activity,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        activity.setSupportActionBar(binding.toolbar2);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestContactsPermission();
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        populateRecyclerView(recyclerView);
        contactAdapter.setOnClick(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));//Añade separador entre items
        if (!contacts.isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(), "Para eliminar un contacto,mantengalo presionado", Toast.LENGTH_LONG).show();
        }
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        return view;
    }


    public void populateRecyclerView(RecyclerView recycler){
        ContactDAO dao=new ContactDAO(getActivity());
        contacts=dao.leerTodos();
        textVisibility();
        contactAdapter=new ContactAdapter(contacts);
        recycler.setAdapter(contactAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(contactUri, null, null, null, null);
            if (cursor!=null && cursor.moveToFirst()){int nameIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            long insercion;
            String name=cursor.getString(nameIndex);
                String number=contactNumber(contactUri);
                ContactDAO dao=new ContactDAO(getActivity());
                insercion=dao.anadirContacto(name,number);
                if (insercion != -1){
                    Snackbar.make(getView(),"Contacto añadido exitosamente",Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(getView(),"No se pudo añadir el contacto",Snackbar.LENGTH_LONG).show();
                }
                populateRecyclerView(recyclerView);
                textVisibility();
            }
        }
    }
    @Override
    public void onItemClick(int position) {
        String name=contactAdapter.getmContactos().get(position).getName();
        deleteDialog(name);
    }

    private String contactNumber(Uri uriContact) {
        String contactID=null;
        String contactNumber = null;
        //Se utiliza el id del contacto para posteriormente obtener su numero
        Cursor cursorID = getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        //Se obtiene el numero del contacto
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.remove_contact_tittle)
                .setMessage(R.string.remove_contact_message)
                .setPositiveButton(R.string.remove_contact_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactDAO dao=new ContactDAO(getActivity().getApplicationContext());
                        //deleteContact(name);
                        int eliminar=dao.eliminarContacto(name);
                        if (eliminar != -1){
                            Snackbar.make(getView(),"Contacto removido exitosamente",Snackbar.LENGTH_LONG).show();
                        }
                        populateRecyclerView(recyclerView);
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
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS))!= PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_CONTACTS)) {

            }else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }

        }
    }



}
