class Protocolo:

    palabra_auxiliar = None

    def __init__(self):
        self.palabra_auxiliar = ""


    """
        *@brief Este método divide la cadena introducida y comprueba que la primera parte de esta cadena es correcta
        *@author alexinio.xd
        *@params Cadena que debería contener el comienzo del protocolo
    """
    def comprobarEtiquetaInicio(self, cosa):
        cosas = cosa.split("#")

        verdadero_o_f = False

        #print("Cosas:", cosas)

        if cosas[0] == 'ASSISTANCESUPPORT':

            #print("Comienzo de cosa correcto")

            verdadero_o_f = True

        return verdadero_o_f

    """
        @brief Éste método va a comprobar de qué dispositivo vienen las peticiones al servidor
        @params protocolo, es lo que tenemos que comprobar de dónde nos llega
    """
    def comprobarDeDondeVienenLasPeticiones(self, cosa):

        cosas = cosa.split("#")

        verdadero_o_f = None

        #print("Cosas:", cosas)

        if cosas[1] == 'APP':

            verdadero_o_f = "APP"
        elif cosas[1] == 'RPI4':

            verdadero_o_f = "RPI4"

        return verdadero_o_f

    def checkActionToDoFromApp(self, inputLine):
        cosa = inputLine.split("#")

        return cosa[2]

    """
        @brief Éste método va a obtener el DNI del alumno para poder comprobar si ya está registrado
        @params protocolo, es lo que nos llega del servidor, que supuestamente ya debería de estar filtrado
    """
    def obtenerDniAlumnoDeProtocolo(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[3]

    """
        @brief Éste método va a obtener la constraseña del alumno para poder comprobar si ya está registrado
        @params protocolo, es lo que nos llega del servidor, que supuestamente ya debería de estar filtrado
    """
    def obtenerPasswdAlumnoDeProtocolo(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[4]

    """
        @brief Éste método va a comprobar qué hacer en base a lo que nos llegue del protocolo de la raspberry
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def checkWhatActionDoFromTheRaspberry(self, protocolo):

        cosa = protocolo.split("#")
        print("Cosa que hacer desde la raspberry: ", cosa[2])

        return cosa[2]

    """
        @brief Éste método va a obtener el dni del alumno del protocolo que viene de la raspberry para poder registrar la asistencia del alumno
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getStudentDniFromTheRaspberry(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[4]

    """
        @brief Éste método va a obtener la fecha del protocolo que viene de la raspberry para poder registrar la asistencia del alumno
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getRegisterDateFromTheRaspberry(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[3]

    """
        @brief Éste método va a obtener asignatura del protocolo que viene de la raspberry para poder registrar la asistencia del alumno
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getSubjectFromTheRaspberry(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[5]

    """
        @brief Éste método va a obtener la clase de la que tendremos que buscar su horario en la base de datos
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getClaseParaObtenerHorarios(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[3]

    """
        @brief Éste método va a obtener la el grupo del protocolo de la rapsberry para poder ver qué alumno NO han asisitido a esa clase
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getGrupoFromRpiProtocolQueryToCheckStudentsDidntAsisted(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[3]

    """
        @brief Éste método va a obtener la el fecha del protocolo de la rapsberry para poder registrar qué alumno NO han asisitido a esa clase
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getFechaFromRpiProtocolQueryToCheckStudentsDidntAsisted(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[4]

    """
        @brief Éste método va a obtener la asignatura del protocolo de la rapsberry para poder registrar qué alumno NO han asisitido a esa clase
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getAsigFromRpiProtocolQueryToCheckStudentsDidntAsisted(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[5]

    """
        @brief Éste método va a obtener el dni del profesor para poder obtener las asistencias referentes a ese profesor
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getTeacherDniToGetAssitances(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[4]

    """
        @brief Éste método va a obtener la fecha a partir de la cual buscar asistencias
        @params protocolo, es lo que nos llega de la raspberry, que supuestamente ya debería de estar filtrado
    """
    def getDateToGetAssitances(self, protocolo):

        cosa = protocolo.split("#")

        return cosa[5]
