package com.example.tfclogin2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterAsistenciaAlumno extends RecyclerView.Adapter<AdapterAsistenciaAlumno.AsistenciaViewHolder>{

    ArrayList<AsistenciaVo> lista_asistencias;

    public AdapterAsistenciaAlumno(ArrayList<AsistenciaVo> lista_asistencias) {
        this.lista_asistencias = lista_asistencias;
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asistencia_alumno, null, false);

        return new AsistenciaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAsistenciaAlumno.AsistenciaViewHolder holder, int position) {

        holder.asig.setText(lista_asistencias.get(position).getAsig());
        holder.prof.setText(lista_asistencias.get(position).getProfesor());
        if(lista_asistencias.get(position).getAsistido() == 1){
            holder.asistido.setText("Asistido");
        }else{
            holder.asistido.setText("No asistido");
        }
    }

    @Override
    public int getItemCount() {
        return lista_asistencias.size();
    }

    public class AsistenciaViewHolder extends RecyclerView.ViewHolder {

        TextView prof, asig, asistido;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            asig = (TextView) itemView.findViewById(R.id.asig_asistencia);
            prof = (TextView) itemView.findViewById(R.id.profesor_asistencia);
            asistido = (TextView) itemView.findViewById(R.id.asistido_asistencia);

        }
    }
}
