package com.example.gabriel.puntodeapoyo.Fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.Services.JsonReaderService;
import com.example.gabriel.puntodeapoyo.Services.LocationUpdaterService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap nGoogleMap;
    private MapView nMapView;
    private View nView;
    private Marker marcador;
    private IntentFilter intentFilter;
    private IntentFilter locFilter;
    private LatLng mCurrentLocation;
    private ArrayList<String> nombres=new ArrayList<>();
    private ArrayList<String> id=new ArrayList<>();
    private ArrayList<String> lat=new ArrayList<>();
    private ArrayList<String> lng=new ArrayList<>();


    public MapFragment() {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentLocation=intent.getParcelableExtra("LatLng");
            myLocationMarker(mCurrentLocation);
        }
    };

    private BroadcastReceiver lugares=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nombres=intent.getStringArrayListExtra("Nombres");
            id=intent.getStringArrayListExtra("Ids");
            lat=intent.getStringArrayListExtra("Latitudes");
            lng=intent.getStringArrayListExtra("Longitudes");
            pointsMarkers(lat,lng);
        }
    };

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();
        intentFilter=new IntentFilter("SEND_LOCATION");
        getActivity().registerReceiver(mReceiver,intentFilter);
        startJsonReader();
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
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        startJsonReader();
    }

    public void enviarCoordenadas() {
        //SmsManager sms = SmsManager.getDefault();
        // String phoneNumber="2634402085";
        //sms.sendTextMessage(phoneNumber,null,message,null,null);
        Toast.makeText(getActivity().getBaseContext(), "Latitud: " + mCurrentLocation.latitude + "\nLongitud: " + mCurrentLocation.longitude, Toast.LENGTH_SHORT).show();
    }

    public void myLocationMarker(LatLng coordenadas) {
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 18);
        if (marcador != null) marcador.remove();
        marcador = nGoogleMap.addMarker(new MarkerOptions().
                position(coordenadas).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black)));
        marcador.setTitle("Posición actual");
        nGoogleMap.animateCamera(miUbicacion);
    }
    public void pointsMarkers(ArrayList<String>lat,ArrayList<String>lng){
        for (int i=0;i<lat.size();i++){
            LatLng latLng=new LatLng(Double.parseDouble(lat.get(i)),Double.parseDouble(lng.get(i)));
            marcador=nGoogleMap.addMarker(new MarkerOptions().
                    position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pda)));
            marcador.setTitle(nombres.get(i));
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
    private void startJsonReader(){
        Intent serviceIntent=new Intent(getActivity(),JsonReaderService.class);
        getActivity().startService(serviceIntent);
        locFilter=new IntentFilter("Get places");
        getActivity().registerReceiver(lugares,locFilter);
    }
    private void stopJsonReader(){
        Intent serviceIntent=new Intent(getActivity(),JsonReaderService.class);
        getActivity().stopService(serviceIntent);
        getActivity().unregisterReceiver(lugares);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
