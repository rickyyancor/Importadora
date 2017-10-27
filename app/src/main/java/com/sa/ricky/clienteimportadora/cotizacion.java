package com.sa.ricky.clienteimportadora;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cotizacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cotizacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cotizacion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public cotizacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cotizacion.
     */
    // TODO: Rename and change types and number of parameters
    public static cotizacion newInstance(String param1, String param2) {
        cotizacion fragment = new cotizacion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String val0="",val1="",val2="",val3="",val4="",val5="",val6="";
    String id_vehiculo="",Destino="";
    EditText txt1;
    EditText txt2;
    EditText txt3;
    EditText txt4;
    EditText txt5;
    EditText txt6;
    EditText txt7;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cotizacion, container, false);
        txt1=(EditText)view.findViewById(R.id.editText11);
        txt2=(EditText)view.findViewById(R.id.editText8);
        txt3=(EditText)view.findViewById(R.id.editText9);
        txt4=(EditText)view.findViewById(R.id.editText10);
        txt5=(EditText)view.findViewById(R.id.editText12);
        txt6=(EditText)view.findViewById(R.id.editText13);
        txt7=(EditText)view.findViewById(R.id.editText14);
        txt1.setText(val0);
        txt2.setText(val1);
        txt3.setText(val2);
        txt4.setText(val3);
        txt5.setText(val4);
        txt6.setText(val5);
        txt7.setText(val6);
        Button btn=(Button)view.findViewById(R.id.button8);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).ComprarVehiculo();

            }
        });
        Button btn1=(Button)view.findViewById(R.id.button9);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).MostrarCatalogo();

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
