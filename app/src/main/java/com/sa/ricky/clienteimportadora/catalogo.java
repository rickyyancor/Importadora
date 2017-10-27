package com.sa.ricky.clienteimportadora;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.id.list;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link catalogo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link catalogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class catalogo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public catalogo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment catalogo.
     */
    // TODO: Rename and change types and number of parameters
    public static catalogo newInstance(String param1, String param2) {
        catalogo fragment = new catalogo();
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
    ArrayList<String> listItems;



    ArrayAdapter<String> adaptador;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_catalogo,container,false);
        final EditText Destino=(EditText) view.findViewById(R.id.editText7);
        ListView lista;
        listItems = new ArrayList<String>();
        lista = (ListView)view.findViewById(R.id.listView);
        adaptador = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,listItems);
        lista.setAdapter(adaptador);
        Button b=(Button)view.findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptador.clear();
                obtenervehiculos();
                adaptador.notifyDataSetChanged();

            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(view.getContext(), "" + position, Toast.LENGTH_SHORT).show();
                try {
                    ((MainActivity)getActivity()).MostrarCotizacion(array.getJSONObject(position).getString("id_Vehiculo"),Destino.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        return view;
    }
    JSONArray array;
    public void obtenervehiculos()
    {
        HttpRequestTask nueva=new HttpRequestTask(view.getContext());
        try {
            nueva.execute().get();
            JSONObject objetos=new JSONObject(nueva.Respuesta);
            //Snackbar.make(view.findViewById(android.R.id.content), objetos.getString("descripcion"), Snackbar.LENGTH_LONG)
                  //  .setAction("Action", null).show();
            //
            if(objetos.getString("status").equals("0")){
                array=objetos.getJSONArray("vehiculos");
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = array.getJSONObject(n);
                    object.getString("marca");
                    listItems.add("Marca: "+object.getString("marca")+"   Linea: "+object.getString("linea")+"   Modelo: "+object.getString("modelo")+"   Pais Origen: "+object.getString("pais_Origen")+"   Precio: "+object.getString("precio_Vehiculo"));

                }

            }
            else
                {

                }
            Toast.makeText(view.getContext(),objetos.getString("descripcion"),Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

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

public String ipservidor="http://192.168.43.20:8093/Importadora/";


    public class HttpRequestTask extends AsyncTask<Object,Integer,Object>
    {
        Context context;
        String Respuesta="";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        String mensaje="Progress Update";
        public HttpRequestTask(Context c){
            context=c;
        }
        public void Agregar_Nuevo_Parametro(String Parametro, String Valor)
        {
            urlParameters.add(new BasicNameValuePair(Parametro, Valor));
        }
        @Override
        protected Object doInBackground(Object... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(ipservidor+"solicitar_Catalogo_Vehiculos");
            post.setHeader("content-type", "application/text; charset=UTF-8");
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            //mensaje="Trabajando Espere";

            publishProgress();
            try {
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                HttpResponse response =httpClient.execute(post);
                String json = EntityUtils.toString(response.getEntity());
                Respuesta=json;

            } catch (IOException e) {
                e.printStackTrace();
                mensaje=e.getMessage();
                publishProgress();
            }

            //Respuesta=mensaje;
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            final Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT);
            //toast.show();
        }
    }
}
