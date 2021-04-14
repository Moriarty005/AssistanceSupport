package com.example.tfclogin2;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;



public class pantalla_boton_conectar_bt extends AppCompatActivity {

    Button boton_resgistrar_asistencia_emdiante_bt;
    ImageButton boton_ir_para_atras, boton_info_usuario;
    TextView info;

    BluetoothAdapter bluetoothAdapter;

    Set<BluetoothDevice> pairedDevices;
    ArrayList<BluetoothDevice> undiscoveredDevices = new ArrayList<>();

    UUID uuid = UUID.fromString("9318353d-e586-42e3-8477-f8a1d84252b2"); //Código único que también utilizamos en la raspberry pi para poder conectar

    BluetoothSocket btsocket;

    String dni = "";
    String nombre = "";
    String apellidos = "";



    boolean encontrado = false;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice extra_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(extra_device.getName().equals("raspberrypi")){
                        Log.i("DEBUG", "Hemos encontrado la raspberry ");
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){

                            conectarConDispositivo(extra_device);
                            encontrado = true;
                        }
                }

                Log.i("DEBUG", "Dispositivo encontrado: " + extra_device.getName() + "; MAC " + extra_device.getAddress());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_boton_conectar_bt);

        dni = getIntent().getStringExtra("user_dni");
        nombre = getIntent().getStringExtra("user_name");
        apellidos = getIntent().getStringExtra("user_surnames");

        Log.i("DEBUGEXTERNO", "Valores que nos traemos de la otra actividad: " + this.dni + ", " + this.nombre + ", " + this.apellidos);

        this.boton_ir_para_atras = (ImageButton) findViewById(R.id.boton_atras);
        this.boton_info_usuario = (ImageButton) findViewById(R.id.boton_ir_a_info_usuario);
        this.boton_resgistrar_asistencia_emdiante_bt = (Button) findViewById(R.id.boton_bluetooth);



        this.info = (TextView) findViewById(R.id.advertencias_info);

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            this.info.setText(R.string.conexion_bluetooth_imposible);
            /*Toast toast = Toast.makeText(getApplicationContext(), "Este dipositivo no puede utilizar Bluetooth", Toast.LENGTH_LONG);
            toast.show();*/
        }else{
            if(!bluetoothAdapter.isEnabled()){
                this.info.setText(R.string.conectar_bluetooth);
            }else{
                this.info.setText(R.string.texto_explicacion);
            }
        }

        this.boton_resgistrar_asistencia_emdiante_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscarYConectarConRPi4();
            }
        });

        this.boton_ir_para_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.boton_info_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarIntentInfoAlumno();
            }
        });

    }

    /**
     * @brief Este método va a iniciar la actividad en la que se motrará la información del usuario
     * @author alexinio
     * @date  15/05/2020
     */
    public void iniciarIntentInfoAlumno(){
        Intent intent = new Intent(this, info_usuario.class);

        intent.putExtra("user_dni", dni);
        intent.putExtra("user_name", nombre);
        intent.putExtra("user_surnames", apellidos);
        intent.putExtra("user_type", "estudiante");
        startActivity(intent);
    }

    /**
     * @brief Este método va a comprobar si el Bluetooth está activo y si no lo está preguntará al usuario si quiere activarlo
     * @author alexinio
     * @date  15/05/2020
     */
    public void comprobarEstadoBT(){

        //Instrucciones que, en caso de que el usuario no tenga el BT activado, el sistema le pregunte si quiere activarlo
        if(!this.bluetoothAdapter.isEnabled()){
            Log.i("DEBUG", "El dispositivo no estaba activo asique loa citvamos par que puesda hbauscar");
            // El Bluetooth está apagado, solicitamos permiso al usuario para iniciarlo
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // REQUEST_ENABLE_BT es un valor entero que vale 1
            startActivityForResult(enableBtIntent, 1);
        }
    }

    /**
     * @brief Este método va a hacer visible nuestro dispositivo para que la rpi4 pueda detectarnos
     * @author alexinio
     * @date  15/05/2020
     */
    public void makeDiscoverable(){

        //Intent que va a preguntarle al usuario si quiere que su dispositivo sea visible
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
        startActivity(discoverableIntent);
    }

    /**
     * @brief Este método va a hacer visible nuestro dispositivo para que la rpi4 pueda detectarnos
     * @author alexinio
     * @date  15/05/2020
     */
    public void buscarDispositivos(){

        Log.i("DEBUG", "Entramos al método uqe busca dispositivos");


        //Comprobamos el estado del bluetooth para poder buscar
        this.comprobarEstadoBT();

        //Hacemos el dispositivo visible para poder concetar con la rpi4
        this.makeDiscoverable();


        if (this.bluetoothAdapter.isDiscovering()) {
            Log.i("DEBUG", "El bluetooth estaba buscando asique reiniciamos");
            // El Bluetooth ya está en modo discover, lo cancelamos para iniciarlo de nuevo
            this.bluetoothAdapter.cancelDiscovery();

            this.bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);

            Log.i("DEBUG", "Lanzamos de nuevo el buscar");
        }else{

            this.bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);

            Log.i("DEBUG", "Lanzamos el buscar");
        }
    }

    /**
     * @brief Este método va a buscar los dispositivos que hay en la lista de dispositivos la Rpi4 y va a conectar con ella
     * @author alexinio
     * @date 29/05/2020
     */
    public void buscarYConectarConRPi4(){

        this.pairedDevices = this.bluetoothAdapter.getBondedDevices();

        for(BluetoothDevice device : this.pairedDevices){

            Log.i("DEBUG:", "(buscarYConectarConRPi4) Nombre del dispositivo de la lista de emparejados: " + device.getName());

            if(device.getName().equals("raspberrypi")){

                conectarConDispositivo(device);
                encontrado = true;
            }
        }

        if(!encontrado){
            this.buscarDispositivos();
        }

    }

    /**
     * @brief Este método va a conectar con el dispositivo que se le pase como arguento
     * @param dispositivo (BluetoothDevice) es el dispositivo desde el que crearemos el socket y nos comunicaremos con él
     * @author alexinio
     * @date 29/05/2020
     */
    public void conectarConDispositivo(BluetoothDevice dispositivo){

        try {

            this.btsocket = dispositivo.createRfcommSocketToServiceRecord(this.uuid);
            if (!this.btsocket.isConnected()){
                this.btsocket.connect();
            }

            Log.i("DEBUG:", "(buscarYConectarConRPi4) Hemos conectado con las raspberry suspuestamente");


            Timestamp ahora = new Timestamp(System.currentTimeMillis());

            Date date = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Log.i("DEBUG", "LA hora y la fehca de hoy es: " + hourdateFormat.format(date));

            String msg = "ASSISTANCESUPPORT#APP#REGISTERASSISTANCE#" + this.dni + "#" + hourdateFormat.format(date);
            msg += "\n";
            OutputStream out = this.btsocket.getOutputStream();
            out.write(msg.getBytes());

            Log.i("DEBUG:", "(buscarYConectarConRPi4) Supuestamente hemos enviado el mensaje");

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            Log.i("DEBUG", "Vamos a desconectar el socket");
            this.btsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.bluetoothAdapter.cancelDiscovery();
    }
}
