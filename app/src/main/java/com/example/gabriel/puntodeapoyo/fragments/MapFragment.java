package com.example.gabriel.puntodeapoyo.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.LocationUpdaterService;
import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.VariablesGlobales;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final String REQUESTING_LOCATION_UPDATES_KEY ="true" ;
    private GoogleMap nGoogleMap;
    private MapView nMapView;
    private View nView;
    private Marker marcador;
    private ArrayList nombres = new ArrayList();
    private VariablesGlobales variables = new VariablesGlobales();

    IntentFilter intentFilter;
    LatLng mCurrentLocation;

    public boolean isMapInitialized = false;
    public MapFragment() {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentLocation=intent.getParcelableExtra("LatLng");
            Toast.makeText(getActivity().getBaseContext(),"Broadcast iniciado", Toast.LENGTH_SHORT).show();
            agregarMarcador(mCurrentLocation);
        }
    };

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();
        intentFilter=new IntentFilter("SEND_LOCATION");
        getActivity().registerReceiver(mReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nView = inflater.inflate(R.layout.fragment_map, container, false);
        FloatingActionButton button = nView.findViewById(R.id.alertFAB);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enviarCoordenadas();
            }
        });
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nMapView = nView.findViewById(R.id.map);
        if (nMapView != null) {
            nMapView.onCreate(null);
            nMapView.getMapAsync(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        nMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        nMapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        nMapView.onDestroy();
        super.onDestroyView();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        nGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        leerGeoJson();
        //agregarMarcador(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
    }

    public void enviarCoordenadas() {
        //SmsManager sms = SmsManager.getDefault();
        // String phoneNumber="2634402085";
        //sms.sendTextMessage(phoneNumber,null,message,null,null);
        Toast.makeText(getActivity().getBaseContext(), "Latitud: " + variables.getLatitud() + "\nLongitud: " + variables.getLongitud(), Toast.LENGTH_SHORT).show();
    }

    public void agregarMarcador(LatLng coordenadas) {
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 18);
        if (marcador != null) marcador.remove();
        marcador = nGoogleMap.addMarker(new MarkerOptions().
                position(coordenadas).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black)));
        marcador.setTitle("Posición actual");
        nGoogleMap.animateCamera(miUbicacion);
        isMapInitialized = true;
    }

    private void leerGeoJson() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(nGoogleMap, R.raw.map, getContext());
            layer.addLayerToMap();
            marcadores(layer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return;
    }

    public void marcadores(GeoJsonLayer layer) {
        for (GeoJsonFeature feature : layer.getFeatures()) {//Itera sobre cada elemento del geojson
            if (feature.hasProperty("name")) {
                String name = feature.getProperty("name");
                //Crea un nuevo estilo para el punto
                GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pda));
                pointStyle.setTitle("Nombre: " + name);
                feature.setPointStyle(pointStyle);
                nombres.add(name);
                variables.setListaNombres(name);
            }
        }
    }


    public void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.activarUbicacion)
                .setMessage(R.string.mensajeUbicacion)
                .setPositiveButton(R.string.abrirAjustes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        AlertDialog alerta = dialog.create();
        alerta.show();
    }
    private void startService()
    {
        Intent service = new Intent(getActivity(), LocationUpdaterService.class);

        getActivity().startService(service);
    }

    private void stopService()
    {
        Intent service = new Intent(getActivity(), LocationUpdaterService.class);

        getActivity().stopService(service);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
