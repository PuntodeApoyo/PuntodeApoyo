package com.example.gabriel.puntodeapoyo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.Fragments.AlertFragment;
import com.example.gabriel.puntodeapoyo.Fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TabLayout tabs;
    private MapFragment mapFragment;
    private AlertFragment alertFragment;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout=findViewById(R.id.drawerLayout);
        alertFragment=new AlertFragment();
        mapFragment=new MapFragment();
        changeFragment(mapFragment);
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    public void changeFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.commit();
    }
    public void setToolbar(){

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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
