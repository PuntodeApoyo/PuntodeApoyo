package com.example.gabriel.puntodeapoyo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.ServiceLocalizacion;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISOS = 0;
    GoogleMap nGoogleMap;
    MapView nMapView;
    View nView;
    private Marker marcador;
    AlertDialog alert =null;
    ArrayList nombres= new ArrayList();
    public ServiceLocalizacion mService;
    boolean isProviderEnabled=true;
    VariablesGlobales variables=new VariablesGlobales();

    public MapFragment() {

    }

    @Override
    public void onStart() {
        //Iniciar servicio
        Intent intent=new Intent(getActivity(),ServiceLocalizacion.class);
        getActivity().startService(intent);
        super.onStart();
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
                //Toast.makeText(getActivity(), "latitud:"+latitud, Toast.LENGTH_SHORT).show();
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
            nMapView.onResume();
            nMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        nGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ServiceLocalizacion mService=new ServiceLocalizacion();
        agregarMarcador(variables.getLatitud(),variables.getLongitud());
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

    private void agregarMarcador(double lat,double lng) {
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
    public void alertDialog(){
        if (isProviderEnabled==false){
            AlertDialog.Builder dialog= new AlertDialog.Builder(getActivity());
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
