from datetime import datetime, date, time, timedelta
import calendar

class Horarios:
    
    lunes = None
    martes = None
    miercoles = None
    jueves = None
    viernes = None
    
    grupos_lunes = None
    grupos_martes = None
    grupos_miercoles = None
    grupos_jueves = None
    grupos_viernes = None
    
    def __init__(self):
        self.lunes = []
        self.martes = []
        self.miercoles = []
        self.jueves = []
        self.viernes = []
        
        self.grupos_lunes = []
        self.grupos_martes = []
        self.grupos_miercoles = []
        self.grupos_jueves = []
        self.grupos_viernes = []
        
    #Este método va a asignar las asginaturas que se imparten el lunes en la clase en la que está esta Raspberry Pi  
    def asignarAsignaturas(self, asig_lunes, asig_martes, asig_miercoles, asig_jueves, asig_viernes):
           
        try:
            asigs_lunes = asig_lunes.split(".")
            print("asigs_lunes: ", asigs_lunes)
            for asig in asigs_lunes:
                cosa = asig.split("%")
                print("cosa: ", cosa)
                self.lunes.append(cosa[1])
                self.grupos_lunes.append(cosa[0])
            
            asigs_martes = asig_martes.split(".")
            for asig in asigs_martes:
                cosa = asig.split("%")
                self.martes.append(cosa[1])
                self.grupos_martes.append(cosa[0])
                
            asigs_miercoles = asig_miercoles.split(".")
            for asig in asigs_miercoles:
                cosa = asig.split("%")
                self.miercoles.append(cosa[1])
                self.grupos_miercoles.append(cosa[0])
                
            asigs_jueves = asig_jueves.split(".")
            for asig in asigs_jueves:
                cosa = asig.split("%")
                self.jueves.append(cosa[1])
                self.grupos_jueves.append(cosa[0])
                
            asigs_viernes = asig_viernes.split(".")
            for asig in asigs_viernes:
                cosa = asig.split("%")
                self.viernes.append(cosa[1])
                self.grupos_viernes.append(cosa[0])
    
            
        except Exception as e:
            print("Excepcion (asignarAsignaturas)", e )
        
        
    #Este método va a imprimir los dias del horario
    def printHorario(self):
        print("LUNES: ", self.lunes)
        print("MARTES: ", self.martes)
        print("MIERCOLES: ", self.miercoles)
        print("JUEVES: ", self.jueves)
        print("VIERNES: ", self.viernes)
        
        print("GRUPOS LUNES: ", self.grupos_lunes)
        print("GRUPOS MARTES: ", self.grupos_martes)
        print("GRUPOS MIERCOLES: ", self.grupos_miercoles)
        print("GRUPOS JUEVES: ", self.grupos_jueves)
        print("GRUPOS VIERNES: ", self.grupos_viernes)
        
    #Este es un método de testeo que simplmente va a asignar valores de prueba a las asignaturas    
    def asignarAsignaturasDePrueba(self):
        monday = "EIE.DI.AD.IG.SGE.PMDM"
        tuesday = "EIE.DI.PSP.DI.PMDM.DI"
        wednesday = "DI.PSP.EIE.SGE.DI.AD"
        thursday = "EIE.SGE.PSP.PMDM.AD.AD"
        friday = "PSP.PMDM.IG.SGE.DI.AD"
        
        self.asignarAsignaturas(monday, tuesday, wednesday, thursday, friday)
#         print("Hemos asignado las asignaturas a los dias")


    #Éste método se va a encargar de comprobar si el estudiante está intentando registrar su asistencia en horario escolar o si ha pulsado el botón fuera de él
    def comprobarSiEstudianteEstaIntentadoRegistrarseEnHorarioDeClase(self, fecha):
        
        comprobante = False
        
        formato = "%d/%m/%Y %H:%M:%S"
        fechia = datetime.strptime(fecha, formato)
        
        fecha_guay = fechia.ctime()
        
        if((fechia.hour > 8) and (fechia.hour < 14)):
#             print("El usuario se está intentando registrar en horario escolar")
            comprobante = True
        
        return comprobante
    
    
    #Método que va a comporbar la asignatura a la que está el usuario intentando registrar su asistencia
    def comprobarHoraALaQueSeIntentaRegistrarAsistencia(self, fecha):
        
        formato = "%d/%m/%Y %H:%M:%S"
        fechia = datetime.strptime(fecha, formato)
        
#         print("fecha.hour: ", fechia.hour)
#         print("fecha.minute: ", fechia.minute)
        
        hora = 0
        
        if((fechia.hour == 8 and fechia.minute >= 0) and (fechia.hour < 9)):
            print("Primera hora")
            hora = 0
            
            
        elif((fechia.hour == 9 and fechia.minute >= 0) and (fechia.hour < 10)):
            print("Segunda hora")
            hora = 1
            
        elif((fechia.hour == 10 and fechia.minute >= 0) and (fechia.hour < 11)):
            print("Tercera hora")
            hora = 2
            
        elif((fechia.hour == 11 and fechia.minute >= 30) or (fechia.hour == 12 and fechia.minute <= 30)):
            print("Cuarta hora")
            hora = 3
            
        elif((fechia.hour == 12 and fechia.minute >= 30) or (fechia.hour == 13 and fechia.minute <= 30)):
            print("Quinta hora")
            hora = 4
            
        elif((fechia.hour == 13 and fechia.minute >= 30) or (fechia.hour == 14 and fechia.minute <= 30)):
            print("Sexta hora")
            hora = 5
            
        else:
            print("No es horario escolar")
            hora = 99
            
#         print("zalimo")
        return hora
    

    def obtenerElDiaEnBaseALaFecha(self, fecha):
        
        formato = "%d/%m/%Y %H:%M:%S"
        fechia = datetime.strptime(fecha, formato)
        
        fecha_guay = fechia.ctime()
        
        dia = ""
        
        partes_fecha = fecha_guay.split(" ")
        
        dia = partes_fecha[0]
#         print("Dia de la semana: ", dia)
        
        return dia
        
        
    def obtenerAsignaturaParaRegistrarAsistenciaEnBaseADiaYHora(self, fecha):
        
        dia = self.obtenerElDiaEnBaseALaFecha(fecha)
        hora = self.comprobarHoraALaQueSeIntentaRegistrarAsistencia(fecha)
        
        print("Fecha que nos llega: ", fecha)
        
        asignatura = ""
        
        if(dia is not "Sat" and dia is not "Sun"):
        
            if(hora >= 1 and hora <= 6):
            
                if(dia == "Mon"):
                    asignatura = self.lunes[hora]
                
                elif(dia == "Tue"):
                    asignatura = self.martes[hora]
                
                elif(dia == "Wed"):
                    asignatura = self.miercoles[hora]
                
                elif(dia == "Thu"):
                    asignatura = self.jueves[hora]
                
                elif(dia == "Fri"):
                    asignatura = self.viernes[hora]
                    
                else:
                    asignatura =  "NOPE"
            else:
                asignatura =  "NOPE"
        else:
            asignatura =  "NOPE"
        
        print("Asignatura que obtenemos con la fecha: ", asignatura)
        return asignatura
    
    def obtenerGrupoEnBaseADiaYHora(self, fecha):
        
        dia = self.obtenerElDiaEnBaseALaFecha(fecha)
        hora = self.comprobarHoraALaQueSeIntentaRegistrarAsistencia(fecha)
        
        print("Fecha que nos llega: ", fecha)
        
        grupo = ""
        
        if(dia is not "Sat" and dia is not "Sun"):
        
            if(hora >= 1 and hora <= 6):
            
                if(dia == "Mon"):
                    grupo = self.grupos_lunes[hora]
                
                elif(dia == "Tue"):
                    grupo = self.grupos_martes[hora]
                
                elif(dia == "Wed"):
                    grupo = self.grupos_miercoles[hora]
                
                elif(dia == "Thu"):
                    grupo = self.grupos_jueves[hora]
                
                elif(dia == "Fri"):
                    grupo = self.grupos_viernes[hora]
                    
                else:
                    grupo =  "NOPE"
            else:
                grupo =  "NOPE"
        else:
            grupo =  "NOPE"
        
        print("Grupo que obtenemos con la fecha: ", grupo)
        return grupo
    
   
   
"""horario = Horarios()
horario.asignarAsignaturasDePrueba()
fecha = datetime.today()
x = fecha.replace(microsecond=0)
x = x - timedelta(hours=9)
print("Asig: ", horario.obtenerAsignaturaParaRegistrarAsistenciaEnBaseADiaYHora(x))"""
"""horario = Horarios()

fecha = datetime.today()
x = fecha.replace(microsecond=0)
print("Ahora: ", x)

morning = x - timedelta(hours=5)
print("this morning: ", morning)

horario.comprobarHoraALaQueSeIntentaRegistrarAsistencia(x)

print("Y ahora con la hora de esta mañana")

horario.comprobarHoraALaQueSeIntentaRegistrarAsistencia(morning)
horario.obtenerElDiaEnBaseALaFecha(morning)"""