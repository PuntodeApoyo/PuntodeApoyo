package com.example.gabriel.puntodeapoyo.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.gabriel.puntodeapoyo.R;

public class AlertFragment extends Fragment {


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
        cargaListaContactos(view);
        return view;
    }


    public void cargaListaContactos(View view){
        ListView contactList = (ListView)view.findViewById(R.id.lista_contactos);

        String[] PROJECTION = new String[] { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String SELECTION = ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";


        Cursor mCursor = this.getActivity().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,PROJECTION
                ,SELECTION
                , null,
                ContactsContract.Data.DISPLAY_NAME + " ASC");


        ListAdapter adapter = new SimpleCursorAdapter(getActivity(), // context
                android.R.layout.simple_list_item_2, // Layout for the rows
                mCursor, // cursor
                new String[] { ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER }, // cursor
// fields
                new int[] { android.R.id.text1, android.R.id.text2 } // view
// fields
        );

        contactList.setAdapter(adapter);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
