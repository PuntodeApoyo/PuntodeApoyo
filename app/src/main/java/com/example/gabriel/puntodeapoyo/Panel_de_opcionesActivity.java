package com.example.gabriel.puntodeapoyo;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.gabriel.puntodeapoyo.fragments.AlertFragment;
import com.example.gabriel.puntodeapoyo.fragments.ContenedorFragment;
import com.example.gabriel.puntodeapoyo.fragments.ListFragment;
import com.example.gabriel.puntodeapoyo.fragments.MapFragment;
import com.example.gabriel.puntodeapoyo.fragments.SettingsFragment;
import com.example.gabriel.puntodeapoyo.fragments.StartFragment;

public class Panel_de_opcionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,StartFragment.OnFragmentInteractionListener,AlertFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener,
        ContenedorFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener,
        ListFragment.OnFragmentInteractionListener{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_de_opciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Fragment fragment=new ContenedorFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment miFragment=null;
        boolean fragmentSeleccionado=false;

        if (id == R.id.nav_inicio) {
            // Handle the camera action
            miFragment=new ContenedorFragment();
            fragmentSeleccionado=true;
        } else if (id == R.id.nav_alertas) {
            miFragment=new AlertFragment();
            fragmentSeleccionado=true;
            Fragment mapFragment=getSupportFragmentManager().findFragmentById(R.id.map);

        } else if (id == R.id.nav_configuracion) {
            miFragment=new SettingsFragment();
            fragmentSeleccionado=true;

        }
        if (fragmentSeleccionado==true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        drawer.removeAllViews();
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
