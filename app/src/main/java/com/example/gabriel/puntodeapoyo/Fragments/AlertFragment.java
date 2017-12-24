package com.example.gabriel.puntodeapoyo.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gabriel.puntodeapoyo.R;

public class AlertFragment extends Fragment {
    private ListView listViewSelected;


    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_alert, container, false);
        listViewSelected=view.findViewById(R.id.contacts_selected);

        String [] elementos={"un contacto","otro contacto","algun otro mas"};
        ArrayAdapter<String> itemsAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,elementos);
        listViewSelected.setAdapter(itemsAdapter);
        return view;
    }

                //String mensaje=mCursor.getString(2);
                //Toast.makeText(getActivity().getBaseContext(),"Seleccionaste el número:\n "+mensaje,Toast.LENGTH_LONG).show();

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
