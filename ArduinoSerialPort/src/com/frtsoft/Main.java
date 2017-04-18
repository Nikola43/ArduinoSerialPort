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
        int opcionConexion;

        int opcionMenuPrincipal;
        final int OPCION_SALIR = 5;

        Scanner scanner = new Scanner(System.in);

        GestionSerialPort gestionSerialPort = new GestionSerialPort();

        do
        {
            //MOSTRAR Y VALIDAR MENU PRINCIPAL
            do
            {
                mostrarMenuPrincipal();
                System.out.print("\nIntroduce la opcion que desea realizar: ");
                opcionMenuPrincipal = scanner.nextInt();
            } while (opcionMenuPrincipal < 1 || opcionMenuPrincipal > OPCION_SALIR);

            //Si la opcion no es salir
            if (opcionMenuPrincipal != OPCION_SALIR)
            {
                switch (opcionMenuPrincipal)
                {
                    //MODULO CONECTARSE A PUERTO SERIE
                    case 1: gestionSerialPort.conectarsePuertoSerie(arduino); break;

                    //MODULO ENVIAR CARACTERES
                    case 2: gestionSerialPort.enviarCadenaPuertoSerie(arduino); break;

                    //MODULO RECIBIR CARACTERES
                    case 3: gestionSerialPort.imprimirCaracterRecibidoPuertoSerie(arduino); break;

                    //MODULO LEER SENSOR TEMPERATURA
                    case 4: gestionSerialPort.imprimirValorSensorLuz(arduino); break;
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
        System.out.println("2. Enviar texto matriz de leds");
        System.out.println("3. Recibir caracter");
        System.out.println("4. Leer Sensor Temperatura");
        System.out.println("5. Salir");
    }
}

