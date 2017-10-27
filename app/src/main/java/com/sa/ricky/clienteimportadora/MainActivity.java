package com.sa.ricky.clienteimportadora;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener, RegistroUsuario.OnFragmentInteractionListener,catalogo.OnFragmentInteractionListener,cotizacion.OnFragmentInteractionListener {

    public android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
    public FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
    public BlankFragment nuevo;
    public String Servidor="http://192.168.43.20:8093/Importadora/";
    public String Cargar_Vehiculos="cargar_Vehiculos";
    public String Calcular_Costo_Viaje="calcular_Costo_Viaje";
    public String Guardar_Transferencia="guardar_Transferencia";
    public String Obtener_Datos_Vehiculo="obtener_Datos_Vehiculo";
    public String Calcular_Impuesto_Sat="calcular_Impuesto_Sat";
    public String Registro_Id_Compra="registro_Id_Compra";
    public String Guardar_Manifiesto="guardar_Manifiesto";
    public String Guardar_Declaracion="guardar_Declaracion";
    public String calcular_Costo_Aduana="calcular_Costo_Aduana";
    public String guardar_Id_Transferencia="guardar_Id_Transferencia";
    public String transferencia_Cuenta="transferencia_Cuenta";


    public String Credencial_username;
    public String Credencial_tarjeta;

    public String id_vehiculo;

    public String getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(String credenciales) {

        this.credenciales = credenciales;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("LG");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public String credenciales;
    RegistroUsuario nuevoregistro;
    public void MostrarCrearUsuario()

    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("LG");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        FragmentTransaction fragmentTransaction2 = fragmentManager1.beginTransaction();
        nuevoregistro=new RegistroUsuario();
        fragmentTransaction2.replace(R.id.fragment_container,nuevoregistro,"RG");
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction2.commit();

    }
    cotizacion cot=new cotizacion();
    public void MostrarCotizacion(String id, String Destin)
    {


        Context context = getApplicationContext();
        HttpRequestTask nueva=new HttpRequestTask(context,"cotizar_Vehiculo");
        nueva.Agregar_Nuevo_Parametro("id_Vehiculo",id);
        nueva.Agregar_Nuevo_Parametro("pais_Destino",Destin);
        try {
            nueva.execute().get();
            JSONObject Resul = new JSONObject(nueva.Respuesta);
            Snackbar.make(findViewById(android.R.id.content), Resul.getString("descripcion"), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if(Resul.getString("status").equals("0"))
            {
                cot.val0=Resul.getString("precio_Vehiculo");
                cot.val1=Resul.getString("precio_Envio");
                cot.val2=Resul.getString("impuesto_Sat");
                cot.val3=Resul.getString("impuesto_Aduana");
                cot.val4=Resul.getString("taller");
                cot.val5=Resul.getString("iva");
                cot.val6=Resul.getString("isr");
                cot.id_vehiculo=id;
                cot.Destino=Destin;
                Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("cat");
                if(fragment1 != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment1).commit();

                FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,cot,"cot");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            final Toast toast1 = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            toast1.show();
        }



    }


    public void ComprarVehiculo()
    {
        Context context = getApplicationContext();
        HttpRequestTask nueva=new HttpRequestTask(context,"comprar_Vehiculo");
        nueva.Agregar_Nuevo_Parametro("username",Credencial_username);
        nueva.Agregar_Nuevo_Parametro("no_Tarjeta",Credencial_tarjeta);
        nueva.Agregar_Nuevo_Parametro("precio_Envio",cot.val1);
        nueva.Agregar_Nuevo_Parametro("impuesto_Sat",cot.val2);
        nueva.Agregar_Nuevo_Parametro("impuesto_Aduana",cot.val3);
        nueva.Agregar_Nuevo_Parametro("iva",cot.val5);
        nueva.Agregar_Nuevo_Parametro("isr",cot.val6);
        nueva.Agregar_Nuevo_Parametro("id_Vehiculo",cot.id_vehiculo);
        nueva.Agregar_Nuevo_Parametro("pais_Destino",cot.Destino);

        String compra="";
        try {
            nueva.execute().get();
            JSONObject resul = new JSONObject(nueva.Respuesta);
            Snackbar.make(findViewById(android.R.id.content), resul.getString("descripcion"), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if(resul.getString("status").equals("0"))
            {
                compra="Serie: "+resul.getString("serie")+"   Factura: "+resul.getString("numero_Factura");

            }
            else
                {
                    compra="Ha habido un error en la compra";
                }
            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.ic_launcher_auto;
            Intent i=new Intent(MainActivity.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);

            mBuilder =new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icono)
                    .setContentTitle("Resultado de la compra")
                    .setContentText(compra)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);



            mNotifyMgr.notify(1, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
            final Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }


    }
    public void HacerLogIn(String nombre, String password)
    {
        Context context = getApplicationContext();
        HttpRequestTask nueva=new HttpRequestTask(context,"validar_Sesion");
        nueva.Agregar_Nuevo_Parametro("username",nombre);
        nueva.Agregar_Nuevo_Parametro("password",password);

        try {
            nueva.execute().get();
            JSONObject Respuesta=new JSONObject(nueva.Respuesta);
            Snackbar.make(findViewById(android.R.id.content), Respuesta.getString("descripcion"), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            if(Respuesta.getString("status").equals("0"))
            {
                Credencial_tarjeta=Respuesta.getString("no_Tarjeta");
                Credencial_username=nombre;
                final Toast toast1 = Toast.makeText(context, "Numero de tarjeta: "+Credencial_tarjeta, Toast.LENGTH_SHORT);
                toast1.show();
                MostrarCatalogo();
            }
        } catch (Exception e) {
            final Toast toast1 = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            toast1.show();
        }
    }
    public void CancelarMostrarLogIn()

    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("RG");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        FragmentTransaction fragmentTransaction3 = fragmentManager1.beginTransaction();
        nuevo=new BlankFragment();
        fragmentTransaction3.replace(R.id.fragment_container,nuevo,"LG");
        fragmentTransaction3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction3.commit();

    }
    public void RegistarUsuario(String name,String nombre,String password, String no_tarjeta)
    {
        Context context = getApplicationContext();
        HttpRequestTask nueva=new HttpRequestTask(context,"crear_Cuenta");
        nueva.Agregar_Nuevo_Parametro("nombre",name);
        nueva.Agregar_Nuevo_Parametro("username",nombre);
        nueva.Agregar_Nuevo_Parametro("password",password);
        nueva.Agregar_Nuevo_Parametro("no_Tarjeta",no_tarjeta);

        try {
            nueva.execute().get();
            JSONObject Respuesta=new JSONObject(nueva.Respuesta);
            Snackbar.make(findViewById(android.R.id.content), Respuesta.getString("descripcion"), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            if(Respuesta.getString("status").equals("0"))
            {
                Credencial_tarjeta=no_tarjeta;
                Credencial_username=nombre;
                MostrarCatalogo();
            }
        } catch (Exception e) {
            final Toast toast1 = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            toast1.show();
        }



    }
    catalogo cat=new catalogo();
    public void MostrarCatalogo()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("RG");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("LG");
        if(fragment1 != null)
            getSupportFragmentManager().beginTransaction().remove(fragment1).commit();


        FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,cat,"cat");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nuevo=new BlankFragment();
        fragmentTransaction1.add(R.id.fragment_container,nuevo,"LG");
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction1.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, Credencial_username+" "+Credencial_tarjeta, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Context context = getApplicationContext();
                //HttpRequestTask nueva=new HttpRequestTask(context,"rest");
                //nueva.Agregar_Nuevo_Parametro("username","skrillfer");
                //nueva.Agregar_Nuevo_Parametro("password","abcd");
                //credenciales=nueva.Respuesta;
                //nueva.execute();







            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setIp(View view)
    {
        EditText txtip=(EditText)findViewById(R.id.editText15);
        Servidor=txtip.getText().toString();
        cat.ipservidor=Servidor;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //private class HttpRequestTask extends AsyncTask<Void, Void, Greeting>
    private class HttpRequestTask extends AsyncTask<Object,Integer,Object>
    {
        Context context;
        String Servicio="";
        String Respuesta="";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        String mensaje="Progress Update";
        public HttpRequestTask(Context c, String service){
            context=c;Servicio=service;
        }
        public void Agregar_Nuevo_Parametro(String Parametro, String Valor)
        {
            urlParameters.add(new BasicNameValuePair(Parametro, Valor));
        }
        @Override
        protected Object doInBackground(Object... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(Servidor+Servicio);
            post.setHeader("content-type", "application/text; charset=UTF-8");
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            mensaje="Trabajando Espere";

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
            toast.show();
        }
    }


}

