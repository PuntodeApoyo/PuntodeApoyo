package com.example.gabriel.puntodeapoyo.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.Services.JsonReaderService;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    IntentFilter locFilter;
    private OnFragmentInteractionListener mListener;
    ArrayList<String> nombres=new ArrayList<>();
    ListView placesList;
    public ListFragment() {
    }
    private BroadcastReceiver lugares=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            nombres=intent.getStringArrayListExtra("Nombres");
           // id=intent.getStringArrayListExtra("Ids");
            //lat=intent.getStringArrayListExtra("Latitudes");
            //lng=intent.getStringArrayListExtra("Longitudes");
            cargarLugares();
        }
    };

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_list, container, false);
        placesList=view.findViewById(R.id.lista);
        startJsonReader();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void cargarLugares(){
        ArrayAdapter<String> itemsAdapter=
                new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,nombres);

        placesList.setAdapter(itemsAdapter);
        placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity().getBaseContext(),"Has seleccionado "+nombres.get(i),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startJsonReader(){
        Intent serviceIntent=new Intent(getActivity(),JsonReaderService.class);
        getActivity().startService(serviceIntent);
        locFilter=new IntentFilter("Get places");
        getActivity().registerReceiver(lugares,locFilter);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
