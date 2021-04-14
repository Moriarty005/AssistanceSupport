#Esta clase va a ser la encargada de administrar los mensajes que le llegen a la Raspberry tanto desde Android como del Servidor en Python

class protocolo_rpi4:
    
    palabra_auxiliar = None
    
    def __init__(self):
        self.palabra_auxiliar = ""
        
    #Método que va a comprobar si el mensaje que se le pasa contiene la palabra clave del protocolo y si está en la posición en al que debería estar        
    def checkIfMessageIsFromProtocol(self, cadena):

#         print("Vamos a ver en que falla esta vaina")
       
        cosas = cadena.split("#")
        
#         print("Jasemo el split")

        verdadero_o_false = False

        if cosas[0] == "ASSISTANCESUPPORT":
            print("Chacho po es verdad que es del protocolo")
            verdadero_o_false = True
            
        return verdadero_o_false
    
    #Método que va a comprobar de dónde viene el mensaje para realizar unas acciones u otras
    def checkWhereTheMessageIsFrom(self, cadena):
        
        cosas = cadena.split("#")
        
        de_donde_viene_el_mensaje = "";
        
        if cosas[1] == "SERVER":
            de_donde_viene_el_mensaje = "SERVER"
            
        elif cosas[1] == "APP":    
            de_donde_viene_el_mensaje = "APP"
            
        else:
            de_donde_viene_el_mensaje = "ERROR"
            
            
        return de_donde_viene_el_mensaje
    
    
    #Método que va a comrporbar qué acción se va a querer hacer
    def manageMessageFromApp(self, cadena):
        
        cosas = cadena.split("#")
        
        de_donde_viene_el_mensaje = "";
        
        if cosas[2] == "REGISTERASSISTANCE":
            de_donde_viene_el_mensaje = "ASSISTANCE"
            
        elif cosas[2] == "GETTIMETABLE":
            de_donde_viene_el_mensaje = "HORARIO"
            
        else:
            de_donde_viene_el_mensaje = "ERROR"
            
            
        return de_donde_viene_el_mensaje
    
    
    #Método que va a obtener la info del usuario de la query que viene de la aplicación tratando de registrar la asistencia del estudiante
    def getUserDniFromAppMessage(self, cadena):
        
        cosas = cadena.split("#")
        
        user_info = cosas[3] ;
         
        return user_info
    
    
    #Método que va a obtener la fecha a la que se envía el mensaje
    def getFechaFromAppMessage(self, cadena):
        
        cosas = cadena.split("#")
        
        user_info = cosas[4];
         
        return user_info
    
    
    #Método que va a obtener la fecha de la query que viene de la aplicación tratando de registrar la asistencia del estudiante
    def getUserInfoFromAppMessage(self, cadena):
        
        cosas = cadena.split("#")
        
        fecha = cosas[6];
         
        return user_info
    
    #Este método va a obtener las asignaturas del protocolo para poder asignarselas al horario
    def getSubjectsFromProtocol(self, cadena, horarios):
        
        asignaturas = ""
        
        if self.checkIfMessageIsFromProtocol(cadena):
            if(self.checkWhereTheMessageIsFrom(cadena) == "SERVER"):
                if(self.manageMessageFromApp(cadena) == "HORARIO"):
                    cosas = cadena.split("#")
                    horarios.asignarAsignaturas(cosas[4], cosas[6], cosas[8], cosas[10], cosas[12])
                    
                    
                    
                    
    
    
     
