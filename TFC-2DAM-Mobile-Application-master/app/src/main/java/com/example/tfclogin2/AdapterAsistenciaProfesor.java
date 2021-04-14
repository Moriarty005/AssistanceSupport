package com.example.tfclogin2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AdapterAsistenciaProfesor extends RecyclerView.Adapter<AdapterAsistenciaProfesor.AsistenciaViewHolder> {

    ArrayList<AsistenciaVo> lista_asistencias;
    Lock mutex;

    PrintWriter out;
    BufferedReader in;

    client_protocol protocol;

    public AdapterAsistenciaProfesor(ArrayList<AsistenciaVo> lista_asistencias, BufferedReader in, PrintWriter out, client_protocol protocol) {
        this.mutex = new ReentrantLock();
        this.lista_asistencias = lista_asistencias;
        this.in = in;
        this.out = out;
        this.protocol = protocol;

    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asistencia, null, false);

        return new AsistenciaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final AsistenciaViewHolder holder, int position) {

        holder.asig.setText(lista_asistencias.get(position).getAsig());
        holder.prof.setText(lista_asistencias.get(position).getProfesor());
        if(lista_asistencias.get(position).getAsistido() == 1){
            holder.asistido.setText("Asistido");
            holder.boton_registrar.setEnabled(false);
        }else{
            holder.asistido.setText("No asistido");
            final String dni = lista_asistencias.get(position).getProfesor();
            final String fecha = lista_asistencias.get(position).getFecha();

            final String[] fecha_dividida = fecha.split(" ");


            holder.boton_registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarAsistenciaAlumno(dni, fecha_dividida[0]);
                    holder.asistido.setText("Asistido");
                    holder.boton_registrar.setEnabled(false);
                }
            });
        }



    }

    /**
     * @brief Este método va a modificar las asitencia de un alumno si el profesor pulsa el boton
     * @param dni
     * @param fecha
     * @date 14/06/2020
     * @author alexinio
     */
    public void registrarAsistenciaAlumno(String dni, String fecha){

        try{

            this.mutex.lock();

            this.out.write("ASSISTANCESUPPORT#APP#MODIFYSTUDENTASSISTANCE#TEACHER#" + dni + "#" + fecha);
            this.out.flush();

            String inputLine;
            inputLine = in.readLine();


            if (this.protocol.comprobarSiModificacionDeAsistenciaCorrecta(inputLine)){

                Log.i("DEBUG", "(registrarAsistenciaAlumno): TODO GUAY");
            }else{
                Log.i("DEBUG", "(registrarAsistenciaAlumno): FALLO AL MODIFICAR ASISTENCIA");
            }

        }catch(Exception e){
            e.printStackTrace();
            Log.i("DEBUG", "(registrarAsistenciaAlumno): " + e);
            this.mutex.unlock();
        }finally{
            this.mutex.unlock();
        }
    }

    @Override
    public int getItemCount() {
        return lista_asistencias.size();
    }

    public class AsistenciaViewHolder extends RecyclerView.ViewHolder {

        TextView prof, asig, asistido;
        Button boton_registrar;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            asig = (TextView) itemView.findViewById(R.id.asig_impartida);
            prof = (TextView) itemView.findViewById(R.id.alumno);
            asistido = (TextView) itemView.findViewById(R.id.asisitido);

            boton_registrar = (Button) itemView.findViewById(R.id.button_cambiar_asistencia);
        }
    }
}
