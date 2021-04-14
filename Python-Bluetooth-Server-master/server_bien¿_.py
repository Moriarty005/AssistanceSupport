import os
import glob
import time
import RPi.GPIO as GPIO
from bluetooth import *
from bluetooth_server_thread import *
import threading
from horarios import *
import socket
from protocolo_rpi4 import *
from final_de_clase import *


class BluetoothServer (threading.Thread):
    
    server_sock = None
    port = None
    server_thread_list = None
    excepcion = None
    
    horarios = None
    protocolo = None
    
    fin_de_clase = None
    
    def __init__(self):
        
        threading.Thread.__init__(self)
        self.server_thread_list = []
        self.excepcion = True
        
        self.protocolo = protocolo_rpi4()
        self.horarios = Horarios()
        
        self.obtenerHorarios()
        
        self.fin_de_clase = ComprobarFinalDeClase(self.horarios, self.protocolo)
        self.fin_de_clase.start()
        
        
    def run(self):
        server_sock=BluetoothSocket( RFCOMM )
        server_sock.bind(("",PORT_ANY))
        server_sock.listen(1)

        port = server_sock.getsockname()[1]

        uuid = "9318353d-e586-42e3-8477-f8a1d84252b2"

        advertise_service( server_sock, "PruebaServerAlexinio",
                           service_id = uuid,
                           service_classes = [ uuid, SERIAL_PORT_CLASS ],
                           profiles = [ SERIAL_PORT_PROFILE ] )


        try:

            while self.excepcion:          
                print ("Waiting for connection on RFCOMM channel {}".format(port))
                
                client_sock, client_info = server_sock.accept()
                print ("Accepted connection from ", client_info)
                
                hebra = BluetoothThread(self, client_sock, self.horarios, self.protocolo)
#                 print("Cosa1")
                self.server_thread_list.append(hebra)
#                 print("Cosa2")
                self.server_thread_list[-1].start()
                
#                 print("Cosa3")
                
        
        except Exception as e:

            print ("disconnected by exception: ", e)
            server_sock.close()
            
            excepcion = False
            print ("all done")
        
        finally:
            
            print ("disconnected")
            server_sock.close()
            
            excepcion = False
            print ("all done")
            
            
    def obtenerHorarios(self):
        print('Entramos en la parte en la que conectamos con el server')
        
        try:
            client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            client.connect(('192.168.1.39', 39999))
            print("DEBUG (obtenerHorarios): HEmos conectado con el server")

            client.send(b'ASSISTANCESUPPORT#RPI4#GETTIMETABLE#2D')
            print("DEBUG (obtenerHorarios): Enviamos la info")            
            cosa = client.recv(1024)
            print("DEBUG (obtenerHorarios): Recibimos")
            
            cosa = str(cosa)
            cosa = cosa[2:]
            cosa = cosa[:len(cosa) - 5]
            print("Lo que nos llega del server: ", cosa)
            self.protocolo.getSubjectsFromProtocol(cosa, self.horarios)
            print("Hemos asignado las asignaturas")
            self.horarios.printHorario()
            #self.horarios.asignarAsignaturas(cosas[1], cosas[3], cosas[5], cosas[7], cosas[9])        
            
            client.close()
        except Exception as e:
            print("Excepcion (obtenerHorarios): ", e)
            client.close()
            

cosa2 = BluetoothServer()
cosa2.run()
