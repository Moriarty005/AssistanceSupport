package com.example.tfclogin2;

public class AsistenciaVo {

    private String profesor;
    private String asig;
    private int asistido;

    private String fecha;

    public AsistenciaVo(){

    }

    public AsistenciaVo(String profesor, String asig, int asistido, String fecha) {
        this.profesor = profesor;
        this.asig = asig;
        this.asistido = asistido;
        this.fecha = fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getAsig() {
        return asig;
    }

    public int getAsistido() {
        return asistido;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public void setAsig(String asig) {
        this.asig = asig;
    }

    public void setAsistido(int asistido) {
        this.asistido = asistido;
    }
}
