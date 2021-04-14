import threading
from horarios import *
import socket
from protocolo_rpi4 import *
import time
from datetime import datetime, date, timedelta
import calendar


class ComprobarFinalDeClase (threading.Thread):
    
    horarios = None
    protocolo = None
    
    client = None
    
    
    def __init__(self, horario, protocol):
        
        threading.Thread.__init__(self)
        self.protocolo = protocol
        self.horarios = horario
        try:
            
            self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.client.connect(('192.168.1.39', 39999))
            print("Creamos el socket de comunicacion")
        except Exception as e:
            print("Excepcion(constructor de fin_de_clase): ", e)
        
        
    def run(self):
        
        print("Empezamos a ejecutar(ComprobarFinalDeClase)")
        
        aux = "NOPE"
        try:
            while True:
                hoy = datetime.today()
                formato3 = "%d/%m/%Y %H:%M:%S"
                cadena = datetime.strftime(hoy, formato3)
                
                
                if(aux != "NOPE"):
                    if(aux != self.horarios.obtenerAsignaturaParaRegistrarAsistenciaEnBaseADiaYHora(cadena)):
                        print("DEBUG(bucle cambiar ed asignatura): Han cambiado las asigs")
                        grupo = self.horarios.obtenerGrupoEnBaseADiaYHora(cadena)
                        aux = self.horarios.obtenerAsignaturaParaRegistrarAsistenciaEnBaseADiaYHora(cadena)
                        
                        self.client.send(bytes(str("ASSISTANCESUPPORT#RPI4#CLASSENDING#{}#{}#{}".format(grupo, cadena, aux)), 'UTF-8'))
                        
                        print("DEBUG(bucle cambiar ed asignatura): actualizamos")
                        
                print("auxiliar antes de cambiar: ", aux)
                
                aux = self.horarios.obtenerAsignaturaParaRegistrarAsistenciaEnBaseADiaYHora(cadena)
                
                print("auxiliar de asignatura, para si han cambiado: ", aux)
                        
                time.sleep(60)
        except Exception as e:
            print("Excepcion (run de la hebra que comrprueba si es el final de la clase): ", e)
        
        
                    
                    
                    
                    
                    
                    