package com.example.gabriel.puntodeapoyo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.VariablesGlobales;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    VariablesGlobales variables=new VariablesGlobales();
    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_list, container, false);
        cargarLugares(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    void cargarLugares(View view){
        ArrayList listaNombres=new ArrayList();
        listaNombres.add("Elemento 1");
        ArrayList<String> arrays=variables.getListaNombres();

        ListView placesList=view.findViewById(R.id.lista);
        ArrayAdapter<String> itemsAdapter=
                new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_checked,listaNombres);
        placesList.setAdapter(itemsAdapter);
        placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity().getBaseContext(),"Solo soy un mensaje de prueba\n¿Qué esperabas que hiciera? ",Toast.LENGTH_LONG).show();
            }
        });
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
