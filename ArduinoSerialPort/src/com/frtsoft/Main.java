package com.frtsoft;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        //Variables para manejar el puerto serie
        ArduinoSerialPort arduino = new ArduinoSerialPort();
        char caracterEnviado;
        char temperatura;
        char opcionConexion;

        int opcionMenuPrincipal;
        final int OPCION_SALIR = '6';

        Scanner scanner = new Scanner(System.in);

        GestionSerialPort gestionSerialPort = new GestionSerialPort();

        do
        {
            //MOSTRAR Y VALIDAR MENU PRINCIPAL
            do
            {
                mostrarMenuPrincipal();
                System.out.print("\nIntroduce la opcion que desea realizar: ");
                opcionMenuPrincipal = Character.toLowerCase(scanner.next().charAt(0));
            } while (opcionMenuPrincipal < '1' || opcionMenuPrincipal > OPCION_SALIR);

            //Si la opcion no es salir
            if (opcionMenuPrincipal != OPCION_SALIR)
            {
                switch (opcionMenuPrincipal)
                {
                    //MODULO CONECTARSE A PUERTO SERIE
                    case '1': gestionSerialPort.conectarsePuertoSerie(arduino); break;

                    //MODULO ENVIAR CARACTERES
                    case '2': gestionSerialPort.enviarCaracterPuertoSerie(arduino); break;

                    //MODULO RECIBIR CARACTERES
                    case '3': gestionSerialPort.imprimirCaracterRecibidoPuertoSerie(arduino); break;

                    //MODULO ENVIAR CADENA
                    case '4': gestionSerialPort.enviarCadenaPuertoSerie(arduino); break;

                    //MODULO LEER SENSOR TEMPERATURA
                    case '5': gestionSerialPort.imprimirValorSensorLuz(arduino); break;
                }
            }

        } while ( opcionMenuPrincipal != OPCION_SALIR );

        //Si el puerto esta abierto, cerramos la conexion antes de salir
        if ( arduino.getEstadoConexion() )
        {
            arduino.cerrarPuerto();
        }
    }

    //METODOS DE LA CLASE
    /*
    INTERFAZ
    Funcionamiento: Mostrar menu
    Prototipo: public void mostrarMenuPrincipal()
    Entrada:
    Precondiciones:
    Salida:
    Postcondiciones:
    Entrada / Salida:
    */
    public static void mostrarMenuPrincipal()
    {
        System.out.println("\n.:| Arduino Serial Port |:.\n");
        System.out.println("1. Conectarse a un puerto");
        System.out.println("2. Enviar caracter");
        System.out.println("3. Recibir caracter");
        System.out.println("4. Enviar texto matriz de leds");
        System.out.println("5. Leer Sensor Temperatura");
        System.out.println("6. Salir");
    }
}