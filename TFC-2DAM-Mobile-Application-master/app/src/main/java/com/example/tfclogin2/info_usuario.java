package com.example.tfclogin2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class info_usuario extends AppCompatActivity {

    ArrayList<AsistenciaVo> lista_asistencias;
    RecyclerView lista_asistencias_vista;
    LinearLayoutManager manager;
    AdapterAsistenciaProfesor adapter_prof;
    AdapterAsistenciaAlumno adapter_alumno;

    Socket comunicacion;

    PrintWriter out;
    BufferedReader in;

    client_protocol protocol;

    ProgressBar progressBar;

    String dni = "";
    String nombre = "";
    String apellidos = "";
    String usuario = "";
    TextView dni_user, nombre_user, apellidos_user;
    ImageButton boton_cerrar;

    int currentItems, totalItems, scrollOutItems;
    boolean isScrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_alumno);

        try {

            this.comunicacion = new Socket("192.168.1.39", 39999);
            this.out = new PrintWriter(comunicacion.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(comunicacion.getInputStream()));

            Log.i("DEBUG", "Creamos los canales de comunicacion");

        } catch (IOException e) {

            Log.i("DEBUG", "(contructor de info alumno):" + e);
            e.printStackTrace();
        }

        lista_asistencias = new ArrayList<AsistenciaVo>();

        progressBar = (ProgressBar) findViewById(R.id.progress);
        dni_user = (TextView) findViewById(R.id.dni_usuario);
        nombre_user = (TextView) findViewById(R.id.nombre_user);
        apellidos_user = (TextView) findViewById(R.id.apellidos_usuario);

        boton_cerrar = (ImageButton) findViewById(R.id.boton_cerrar_info_usuario);
        boton_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    in.close();
                    out.close();
                    comunicacion.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        protocol = new client_protocol();

        dni = getIntent().getStringExtra("user_dni");
        nombre = getIntent().getStringExtra("user_name");
        apellidos = getIntent().getStringExtra("user_surnames");
        usuario = getIntent().getStringExtra("user_type");

        Log.i("DEBUG", "(Contructor de info_alumno: tipo ed usuario:" + usuario);

        dni_user.setText(this.dni);
        nombre_user.setText(this.nombre);
        apellidos_user.setText(this.apellidos);

        lista_asistencias_vista = (RecyclerView) findViewById(R.id.lista_asistencias_al);
        this.manager = new LinearLayoutManager(this);
        lista_asistencias_vista.setLayoutManager(manager);

        try {
            llenarListaAsistencias();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(this.usuario.equals("profesor")){
            adapter_prof = new AdapterAsistenciaProfesor(this.lista_asistencias, this.in, this.out, this.protocol);
            lista_asistencias_vista.setAdapter(adapter_prof);
        }else{
            progressBar.setVisibility(View.GONE);
            adapter_alumno = new AdapterAsistenciaAlumno(this.lista_asistencias);
            lista_asistencias_vista.setAdapter(adapter_alumno);
        }

        if(this.usuario.equals("profesor")){
            lista_asistencias_vista.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstVisibleItemPosition();

                    if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                        isScrolling = false;

                        obtenerNuevaInformacion();
                    }

                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                        isScrolling = true;
                    }
                }
            });
        }

    }

    public void obtenerNuevaInformacion(){
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{

                    String fecha = lista_asistencias.get(lista_asistencias.size() - 1).getFecha();
                    out.write("ASSISTANCESUPPORT#APP#GETANOTHER15ASSISTANCESFROMDATES#TEACHER#" + dni + "#" + fecha);
                    out.flush();

                    String inputLine;
                    inputLine = in.readLine();

                    if (inputLine != null){

                        protocol.llenarListaAsistenciasConQueryProtocoloProfesor(inputLine, lista_asistencias);
                    }

                }catch (Exception e){
                    out.close();
                    try {
                        in.close();
                        comunicacion.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Log.i("DEBUG", "Excepcion (llenarListaAsistencias): " + e);
                }

                adapter_prof.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void llenarListaAsistencias() throws IOException {

        try{

            if(this.usuario.equals("profesor")){
                this.out.write("ASSISTANCESUPPORT#APP#GETFIRST15ASSISTANCES#TEACHER#" + this.dni);

                this.out.flush();

                String inputLine;
                inputLine = this.in.readLine();

                Log.i("DEBUG", "(llenarListaAsistencias):Lo que nos llega: " + inputLine);

                if (inputLine != null){

                    this.protocol.llenarListaAsistenciasConQueryProtocoloProfesor(inputLine, this.lista_asistencias);
                }
            }else{
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                this.out.write("ASSISTANCESUPPORT#APP#GETTODAYASISTANCES#STUDENT#" + this.dni + "#" + dateFormat.format(date));

                this.out.flush();

                String inputLine;
                inputLine = this.in.readLine();

                Log.i("DEBUG", "(llenarListaAsistencias):Lo que nos llega: " + inputLine);

                if (inputLine != null){

                    this.protocol.llenarListaAsistenciasConQueryProtocoloProfesor(inputLine, this.lista_asistencias);
                }
            }



        }catch (Exception e){
            e.printStackTrace();
            Log.i("DEBUG", "Excepcion (llenarListaAsistencias): " + e);
            if(!this.comunicacion.isClosed()){
                this.out.close();
                this.in.close();
                this.comunicacion.close();
            }
        }
    }
}