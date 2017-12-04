package com.example.gabriel.puntodeapoyo.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.ServiceLocalizacion;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISOS = 0;
    GoogleMap nGoogleMap;
    MapView nMapView;
    View nView;
    private Marker marcador;
    AlertDialog alert =null;
    ArrayList nombres= new ArrayList();
    double latitud=0.0;
    double longitud=0.0;
    MyReceiver myReceiver;

    public MapFragment() {

    }

    @Override
    public void onStart() {
        //Registrar broadcast
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceLocalizacion.MY_ACTION);
        getActivity().registerReceiver(myReceiver,intentFilter);
        //Iniciar servicio
        Intent intent=new Intent(getActivity(),ServiceLocalizacion.class);
        getActivity().startService(intent);
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Intent intent=new Intent(getActivity(),ServiceLocalizacion.class);
        //getActivity().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nView = inflater.inflate(R.layout.fragment_map, container, false);
        FloatingActionButton button = (FloatingActionButton) nView.findViewById(R.id.alertFAB);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //enviarCoordenadas();
                //getActivity().startService(new Intent(getActivity(), LocationService.class));
                Toast.makeText(getActivity(), "latitud:"+latitud, Toast.LENGTH_SHORT).show();
            }
        });
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nMapView = (MapView) nView.findViewById(R.id.map);
        if (nMapView != null) {
            nMapView.onCreate(null);
            nMapView.onResume();
            nMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        nGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        leerGeoJson();
    }
    public void enviarCoordenadas(){
       // mService.miUbicacion();
        //SmsManager sms = SmsManager.getDefault();
       // String phoneNumber="2634402085";
        //String message="Latitud: "+lat+" longitud: "+lng;
        //sms.sendTextMessage(phoneNumber,null,message,null,null);
        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void agregarMarcador(double lat, double lng) {
            LatLng coordenadas = new LatLng(lat, lng);
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 18);
            if (marcador != null) marcador.remove();
            marcador = nGoogleMap.addMarker(new MarkerOptions().position(coordenadas).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black)));
            nGoogleMap.animateCamera(miUbicacion);
    }

    private void leerGeoJson(){
        try {
            GeoJsonLayer layer =  new GeoJsonLayer(nGoogleMap, R.raw.map, getContext());
            layer.addLayerToMap();
            marcadores(layer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return;
    }
    public void  marcadores (GeoJsonLayer layer){
        for (GeoJsonFeature feature : layer.getFeatures()) {//Itera sobre cada elemento del geojson
            if (feature.hasProperty("name")){
                String name=feature.getProperty("name");
                //Crea un nuevo estilo para el punto
                GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pda));
                pointStyle.setTitle("Nombre: "+name);
                feature.setPointStyle(pointStyle);
                nombres.add(name);
            }
        }
        String items="Algo";
    }

    @Override
    public void onPause() {
        nMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        nMapView.onResume();
        super.onResume();
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            latitud=arg1.getDoubleExtra("Latitud",0.0);
            longitud=arg1.getDoubleExtra("Longitud",0.0);
            Toast.makeText(getActivity(), "Latitud:"+latitud+"\nLongitud:"+longitud, Toast.LENGTH_SHORT).show();
            agregarMarcador(latitud,longitud);
        }
    }
}
