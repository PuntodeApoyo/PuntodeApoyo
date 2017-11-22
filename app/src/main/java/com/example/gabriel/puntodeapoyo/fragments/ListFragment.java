package com.example.gabriel.puntodeapoyo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gabriel.puntodeapoyo.R;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_list, container, false);
        String[]menuItems={"Algo","Otro mas",
                            "Otro mas 2",
                            "Otro mas 3"};
        ListView listView=(ListView)view.findViewById(R.id.lista);

        ArrayAdapter<String> listViewAdapter=new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                menuItems
        );

        listView.setAdapter(listViewAdapter );
        return view;
    }
    public void obtenerArray(){

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
