package com.example.gabriel.puntodeapoyo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.gabriel.puntodeapoyo.Fragments.AlertFragment;
import com.example.gabriel.puntodeapoyo.Fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private MapFragment mapFragment;
    private AlertFragment alertFragment;
    private DrawerLayout drawerLayout;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawerLayout);
        alertFragment=new AlertFragment();
        mapFragment=new MapFragment();
        changeFragment(mapFragment);
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        View headerView=navigationView.getHeaderView(0);
        textView= headerView.findViewById(R.id.nav_correo);

        SharedPreferences preferences=getSharedPreferences("Sesion", Context.MODE_PRIVATE);
        String mail=preferences.getString("email","");

        textView.setText(mail);
    }


    public void changeFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_inicio:
                changeFragment(mapFragment);
                break;
            case R.id.nav_alertas:
                changeFragment(alertFragment);
                break;
            case R.id.nav_logout:
                SharedPreferences preferences=getSharedPreferences("Sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("email","");
                editor.commit();
                getApplicationContext().deleteDatabase("Contacts");
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
