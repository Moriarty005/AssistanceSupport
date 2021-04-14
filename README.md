# AssistanceSupport
Diseñé este sistema para mi TFC.

Está compuesto de tres partes principales:
 - Una aplicación en Android
 - Un servidor en Python
 - Una RaspberryPi

AssistanceSupport es un sistema diseñado para un entorno académico de estudios avanzados como bachillerato o la universidad.
El objetivo es eliminar el proceso que tiene que realizar el profesorado de comprobación de la presencialidad del alumnado, o lo que comunmente se conoce como "pasar lista".

El funcionamiento sería el siguiente; el alumno entraría al aula y mediante su cuenta de la aplicación de AssistanceSupport conectaría gracias al protocolo Bluetooth (IEEE 802.15) con la Raspberry Pi enviandole mediante este protocolo los datos del alumno. Acto seguido, la RaspberryPi, la cual siempre está conectada a un servidor escrito en Python y que conecta a su vez con una base de datos, registraría en esa base de datos mencionada con fecha y hora exactas el alumno que ha entrado a clase. Quednado así regtistrada la asistencia del alumno a un aula específica, una asignatura específica en una fecha y hora específica.
