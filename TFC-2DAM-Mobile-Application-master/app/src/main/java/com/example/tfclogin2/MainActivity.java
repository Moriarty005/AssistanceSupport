package com.example.tfclogin2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView dni, contrasenia;
    Button boton_login;
    Socket comunicacion;

    PrintWriter out;
    BufferedReader in;

    client_protocol protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        contrasenia = (TextView) findViewById(R.id.passwd_field);
        dni = (TextView) findViewById(R.id.username_field);

        this.boton_login = (Button) this.findViewById(R.id.login_button);

        protocol = new client_protocol();

        try {

            this.comunicacion = new Socket("192.168.1.39", 39999);
            this.out = new PrintWriter(comunicacion.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(comunicacion.getInputStream()));

            Log.i("DEBUG", "Creamos los canales de comunicacion");

        } catch (IOException e) {
            e.printStackTrace();
        }

        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    abrirPatanllaBotonRegistrarAsistencia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
        @brief Método que va a ejecutar las instrucciones para comprobar si el usuario está registrado o no, y en caso de que lo esté lanzará la siguiente pantalla
        @params None
        @author Alexinio
        @date finished 12/05/2020
     */
    public void abrirPatanllaBotonRegistrarAsistencia() throws IOException {

        Log.i("DEBUG", "Entramos en el metodo que va a enviar el mensaje");

        String inputLine = "";

        this.out.write("ASSISTANCESUPPORT#APP#LOGIN#" + this.dni.getText() + "#" + this.contrasenia.getText());

        this.out.flush();

        try {

            inputLine = this.in.readLine();
            Log.i("DEBUG", "Lo que nos llega del socket: " + inputLine);

            if(this.protocol.checkIfLoggedCorrectly(inputLine)){

                if(this.protocol.getUserRole(inputLine).equals("STUDENT")){
                    Intent intent = new Intent(this, pantalla_boton_conectar_bt.class);

                    String info_user = this.protocol.getUserInfoFromServerLoginProtocol(inputLine);

                    String[] info_separada = info_user.split("%");

                    intent.putExtra("user_dni", info_separada[0]);
                    intent.putExtra("user_name", info_separada[1]);
                    intent.putExtra("user_surnames", info_separada[2]);

                    startActivity(intent);

                }else if(this.protocol.getUserRole(inputLine).equals("TEACHER")){
                    Intent intent = new Intent(this, info_usuario.class);

                    String info_user = this.protocol.getUserInfoFromServerLoginProtocol(inputLine);

                    String[] info_separada = info_user.split("%");

                    intent.putExtra("user_dni", info_separada[0]);
                    intent.putExtra("user_name", info_separada[1]);
                    intent.putExtra("user_surnames", info_separada[2]);
                    intent.putExtra("user_type", "profesor");
                    startActivity(intent);
                }


            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(), "Ha habido un error durante el proceso de login", Toast.LENGTH_LONG);

                toast1.show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Log.i("DEBUG: ", "Cerramos los canales de comunicacion");
            this.in.close();
            this.out.close();
            this.comunicacion.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if(!this.comunicacion.isClosed()){
                this.in.close();
                this.out.close();
                this.comunicacion.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
