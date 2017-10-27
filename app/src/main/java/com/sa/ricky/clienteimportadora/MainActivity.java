package com.sa.ricky.clienteimportadora;

import android.Manifest;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import harmony.java.awt.Color;

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

    String NumeroFactura="";
    String NumeroSerie="";
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
                NumeroFactura=resul.getString("numero_Factura");
                NumeroSerie=resul.getString("serie");
                NOMBRE_DOCUMENTO=NumeroSerie+NOMBRE_DOCUMENTO;
                compra="Serie: "+resul.getString("serie")+"   Factura: "+resul.getString("numero_Factura");
                GenerarPDF();
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(f),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }
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

        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("cot");
        if(fragment2 != null)
            getSupportFragmentManager().beginTransaction().remove(fragment2).commit();

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
        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

        int permsRequestCode = 200;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }

        nuevo=new BlankFragment();
        fragmentTransaction1.add(R.id.fragment_container,nuevo,"LG");
        fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //fragmentTransaction1.add(R.id.fragment_container,cot,"cot");
        //fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction1.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public  int REQUEST_CODE ;

            @Override
            public void onClick(View view) {

                Snackbar.make(view, Credencial_username+" "+Credencial_tarjeta, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();








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


    public void setIp(String dire)
    {
        Servidor=dire;
        cat.ipservidor=Servidor;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Generacion de la factura en PDF

    private final static String NOMBRE_DIRECTORIO = "Facturas";
    private String NOMBRE_DOCUMENTO = "_Factura.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }
    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
    private boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                break;

        }

    }
    String RUTAPDF="";File f;
    public void GenerarPDF() {

        // Creamos el documento.
        Document documento = new Document();

        try {

            // Creamos el fichero con el nombre que deseemos.
            f = crearFichero(NOMBRE_DOCUMENTO);

            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.


            FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());
            //FileOutputStream ficheroPdf=openFileOutput(NOMBRE_DOCUMENTO,MODE_PRIVATE);
            Toast.makeText(getApplicationContext(),"Factura Guardada en: "+getFileStreamPath(NOMBRE_DOCUMENTO).getAbsolutePath(),Toast.LENGTH_LONG).show();
            // Asociamos el flujo que acabamos de crear al documento.
            RUTAPDF=getFileStreamPath(NOMBRE_DOCUMENTO).getAbsolutePath();
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el píe de página y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "FACTURA"), false);
            HeaderFooter pie = new HeaderFooter(new Phrase(
                    "FACTURA"), false);

            documento.setHeader(cabecera);
            documento.setFooter(pie);

            // Abrimos el documento.
            documento.open();

            // Añadimos un título con la fuente por defecto.

            // Añadimos un título con una fuente personalizada.
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 28,
                    Font.BOLD, Color.RED);
            documento.add(new Paragraph("Factura Importadora", font));
            documento.add(new Paragraph("Factura Numero: "+NumeroFactura));
            documento.add(new Paragraph("Numero de Serie: "+NumeroSerie));

            documento.add(new Paragraph("Usuario: "+Credencial_username));
            documento.add(new Paragraph("Tarjeta: "+Credencial_tarjeta));
            documento.add(new Paragraph("País Destino: "+cot.Destino));
            documento.add(new Paragraph("Precio Vehiculo: "+cot.val0));
            documento.add(new Paragraph("Precio Envio: "+cot.val1));
            documento.add(new Paragraph("Impuesto Sat: "+cot.val2));
            documento.add(new Paragraph("Impuesto Aduana: "+cot.val3));
            documento.add(new Paragraph("Taller: "+cot.val4));
            documento.add(new Paragraph("IVA: "+cot.val5));
            documento.add(new Paragraph("ISR: "+cot.val6));

            // Agregar marca de agua
            font = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD,
                    Color.GRAY);
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    Element.ALIGN_CENTER, new Paragraph(
                            "Cancelado", font), 297.5f, 421,
                    writer.getPageNumber() % 2 == 1 ? 45 : -45);

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        } finally {

            // Cerramos el documento.
            documento.close();
        }
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

