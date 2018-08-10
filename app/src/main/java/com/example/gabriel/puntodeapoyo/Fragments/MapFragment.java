package com.example.gabriel.puntodeapoyo.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.Services.LocationUpdaterService;
import com.example.gabriel.puntodeapoyo.Services.SmsService;
import com.example.gabriel.puntodeapoyo.Sugerencias;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback,Response.Listener<JSONObject>,Response.ErrorListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap nGoogleMap;
    private MapView nMapView;
    private View nView;
    private Marker marcador;
    private IntentFilter locFilter;
    private IntentFilter intentFilter;
    private LatLng mCurrentLocation;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private List<Sugerencias> suger=new ArrayList<>();


    //Broadcast proveniente de LocationService
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentLocation=intent.getParcelableExtra("LatLng");
            myLocationMarker(mCurrentLocation);
        }
    };
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();

        requestQueue= Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nView = inflater.inflate(R.layout.fragment_map, container, false);
        FloatingActionButton button = nView.findViewById(R.id.alertFAB);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SmsService.class);
                getActivity().startService(intent);
            }
        });
        final FloatingSearchView searchView= nView.findViewById(R.id.floating_search_view);
        searchView.attachNavigationDrawerToMenuButton((DrawerLayout) getActivity().findViewById(R.id.drawerLayout));
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (oldQuery!=newQuery) {
                    searchView.showProgress();
                  obtenerSugerencias(newQuery);
                    searchView.swapSuggestions(suger);
                    searchView.hideProgress();
                }
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(new LatLng(searchSuggestion.getLatitud(),
                        searchSuggestion.getLongitud()), 18);
                if (nGoogleMap!=null){
                    searchView.clearSearchFocus();
                    nGoogleMap.animateCamera(ubicacion);
                }
            }

            @Override
            public void onSearchAction(String currentQuery) {

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
        getActivity().unregisterReceiver(mReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        nMapView.onResume();
        intentFilter=new IntentFilter("SEND_LOCATION");
        getActivity().registerReceiver(mReceiver,intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopService();
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
        obtenerListaComercios();
        nGoogleMap.setOnMarkerClickListener(this);
    }

    public void myLocationMarker(LatLng coordenadas) {
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 18);
        if (marcador != null)
            marcador.remove();
        marcador = nGoogleMap.addMarker(new MarkerOptions().
                position(coordenadas).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black)));
        marcador.setTitle("Posición actual");
        nGoogleMap.animateCamera(miUbicacion);
    }
    public void agregarMarcadoresComercios(double lat,double lng,String nombre){
        Marker itemMarker;
        LatLng latLng=new LatLng(lat,lng);
        itemMarker=nGoogleMap.addMarker(new MarkerOptions().
                                        position(latLng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pda)));
        itemMarker.setTitle(nombre);
        itemMarker.setSnippet("Reputacion: Alta");
        
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
    public void obtenerListaComercios(){
        String url="https://gabiiascurra.000webhostapp.com/GetPlaces.php";
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity().getApplicationContext(), "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d("Servicios:","OnResponse iniciado");
        JSONArray jsonArray=response.optJSONArray("Comercios");
            try {
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String nombre=jsonObject.getString("nombre");
                    Double lat=jsonObject.getDouble("latitud");
                    Double lng=jsonObject.getDouble("longitud");
                    agregarMarcadoresComercios(lat,lng,nombre);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    //
    }
    public void parsearSugerencias(String json){
        if (json!=null) {
            Log.i("String recebido", json);
            try {

                JSONObject object = new JSONObject(json);
                if (object != null) {
                    JSONArray array = object.optJSONArray("Comercios");
                    suger.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        suger.add(new Sugerencias(jsonObject.getString("nombre"), jsonObject.getDouble("latitud"),
                                jsonObject.getDouble("longitud")));
                        Log.i("Nombre", jsonObject.getString("nombre"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void obtenerSugerencias(final String query){
        StringRequest stringRequest;
        String url="https://gabiiascurra.000webhostapp.com/GetPlacesSuggestions.php";
        stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Dao","query");
                Log.i("Respuesta",response);
                parsearSugerencias(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("query",query);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }
}
