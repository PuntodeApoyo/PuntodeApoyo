package com.example.gabriel.puntodeapoyo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gabriel.puntodeapoyo.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    ListView listView;
    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_list, container, false);
        //String[]menuItems;
        //listView=view.findViewById(R.id.lista);
        //ArrayAdapter<String> listViewAdapter;
        //listView.setAdapter(listViewAdapter );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView=getActivity().findViewById(R.id.lista);
       // listView.setAdapter(listViewAdapter);
    }


    public void recibirTexto(ArrayList texto){
      //  ArrayAdapter<String> listViewAdapter=
        //        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, texto);
        //listView.setAdapter(listViewAdapter);
        System.out.println(texto);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
