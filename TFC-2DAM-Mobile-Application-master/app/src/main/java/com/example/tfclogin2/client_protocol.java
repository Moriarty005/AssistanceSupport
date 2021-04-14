package com.example.tfclogin2;

import java.util.ArrayList;

public class client_protocol {

    String palabra_reserva;

    public client_protocol(){

        this.palabra_reserva = "";
    }

    /**
        @brief Método que va a comprobar si la cadena que le llega es del protocolo
        @params String[] cosa, lista de cadenas con todas las palabras del protocolo ya divididas
        @author Alexinio
        @date finished 11/05/2020
     */
    public boolean checkIfStartIsCorrect(String[] cosa){

        boolean yes_or_no = false;

        if(cosa[0].equals("ASSISTANCESUPPORT")){
            yes_or_no = true;
        }

        return yes_or_no;
    }

    /**
        @brief Método que va a comprobar de dónde proviene el mensaje
        @params String[] cosa, lista de cadenas con todas las palabras del protocolo ya divididas
        @author Alexinio
        @date finished 11/05/2020
     */
    public String checkIfIsFromServer(String[] cosa){

        String what = "";

        if(cosa[1].equals("SERVER")){
            what = "SERVER";
        }else if(cosa[1].equals("RPI4")){
            what = "RPI4";
        }else {
            what = "ERROR";
        }

        return what;
    }

    /**
        @brief Método que va a comprobar si la cadena del protocolo que le llega tiene que ver con una acción de login de usuario
        @params String[] cosa, lista de cadenas con todas las palabras del protocolo ya divididas
        @author Alexinio
        @date finished 11/05/2020
     */
    public boolean checkIfIsToDoSomethingRelatedToTheLoginAction(String[] cosa){

        boolean yes_or_no = false;

        if(cosa[2].equals("LOGIN")){
            yes_or_no = true;
        }

        return yes_or_no;
    }

    /**
        @brief Método que va a comprobar si el usuario está logeado
        @params String cosa, cadena del protocolo donde está toda la info del usuario que se tiene que comporbar
        @author Alexinio
        @date finished 12/05/2020
     */
    public boolean checkIfLoggedCorrectly(String cosa){

        String[] lista = cosa.split("#");
        boolean yes_or_no = false;

        if(this.checkIfStartIsCorrect(lista)){
            if(this.checkIfIsFromServer(lista).equals("SERVER")){
                if(this.checkIfIsToDoSomethingRelatedToTheLoginAction(lista)){
                    if(lista[3].equals("LOGINCORRECT")){
                        yes_or_no = true;
                    }else if(lista[3].equals("LOGINERROR")){
                        yes_or_no = false;
                    }
                }
            }
        }

        return yes_or_no;
    }

    /**
     @brief Método que va a obtener la información del usuario del protocolo proveniente del servidor
     @params String inputline, cadena del protocolo donde está toda la info del usuario que se tiene que obtener
     @author Alexinio
     @date finished 12/05/2020
     */
    public String getUserInfoFromServerLoginProtocol(String inputline){

        String[] cosas = inputline.split("#");

        String devolver = cosas[5] + "%" + cosas[6] + "%" + cosas[7];

        return devolver;
    }

    /**
     @brief Método que va a retornar qué tipo de usuario está intentando ver su información
     @params String inputLine, cadena del protocolo donde está toda la info del rol
     @author Alexinio
     @date finished 12/05/2020
     */
    public String getUserRole(String inputLine){
        String[] cosas = inputLine.split("#");

        return cosas[4];

    }

    /**
     @brief Método que va a llenar la lñista de asistencias con los datos provenientes del servidor
     @params String cosa, cadena del protocolo donde está toda la info del usuario que se tiene que comporbar
     @params ArrayList<AsistenciaVo> lista_asistencias, lista que vamos a llenar
     @author Alexinio
     @date finished 12/05/2020
     */
    public void llenarListaAsistenciasConQueryProtocoloProfesor(String inputLine, ArrayList<AsistenciaVo> lista_asistencias){

        String cosas[] = inputLine.split("#");

        for(int index = 3; index < cosas.length; index++){
            String cosa[] = cosas[index].split("%");
            AsistenciaVo asis = new AsistenciaVo(cosa[3], cosa[2], Integer.parseInt(cosa[1]), cosa[0]);
            lista_asistencias.add(asis);
        }
    }

    public boolean comprobarSiModificacionDeAsistenciaCorrecta(String inputLine){

        String cosas[] = inputLine.split("#");
        boolean si_o_no = false;

        if(cosas[2].equals("TODOGUAY")){
            si_o_no = true;
        }

        return si_o_no;
    }

}
