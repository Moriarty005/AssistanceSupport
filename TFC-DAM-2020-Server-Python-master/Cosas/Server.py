import threading

from Cosas.ServerThread import ServerThread
from Cosas.Protocolo import Protocolo
import socket


class Server (threading.Thread):

    listening = None
    server_thread_list = None
    portNumber = None
    ip = None
    protocol = None

    def __init__(self):
        #self.__init__()
        threading.Thread.__init__(self)
        self.listening = True
        self.server_thread_list = []
        self.protocol = Protocolo()

    def setPort(self, port):
        self.portNumber = port

    def setIPDB(self, ip_db):
        self.ip = ip_db

    def run(self):

        print("Empezamos el servicio")

        self.listening = True

        # Create a TCP/IP socket
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # Bind the socket to the port
        server_address = ("192.168.1.39", 39999)
        #print('starting up on {} port {}'.format(*server_address))
        sock.bind(server_address)

        # Listen for incoming connections
        sock.listen(5)
        try:
            while True:
                # Wait for a connection
                print('waiting for a connection')

                (connection, client_address) = sock.accept()

                print('connection from', client_address)
                #print("Connection: {}".format(connection))


                server_cliente = ServerThread(self, self.ip, connection, self.protocol)

                #print("creamos el server cliente")

                #print("VAlor de la lista pre-añadir:{}".format(self.server_thread_list.__len__()))

                self.server_thread_list.append(server_cliente)
                self.server_thread_list[-1].start()
                #print("Añadimos el socket-cliente a la lista de sockets-cliente, número actual:{}".format(self.server_thread_list.__len__()))

        except Exception as ex:
            print("EXCEPCION", ex)

            exit()

        finally:
            sock.close()
            for cosa in self.server_thread_list:
                cosa.join()
            exit()

cosa1 = Server()
cosa1.run()