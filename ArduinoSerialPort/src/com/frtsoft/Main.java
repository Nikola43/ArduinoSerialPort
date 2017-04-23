package com.frtsoft;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        //Variables para manejar el puerto serie
        ArduinoSerialPort arduino = new ArduinoSerialPort();
        boolean estadoConexion = false;
        int resultadoConexion;
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
                    case 1:
                        resultadoConexion = gestionSerialPort.conectarsePuertoSerie(arduino);
                        if (  resultadoConexion == 1)
                        {
                            System.out.println("Conexion del puerto " + arduino.getNombrePuerto() + " realizada correctamente");
                            estadoConexion = true;
                        }
                        else if ( resultadoConexion  == 2)
                        {
                            System.out.println("El puerto serie "+arduino.getNombrePuerto()+" ya esta en uso");
                        }
                        else if ( resultadoConexion  == 3)
                        {
                            System.out.println("No se encuentra el puerto "+arduino.getNombrePuerto());
                        }
                    break;

                    case 2:
                        if ( estadoConexion == true )
                        {
                            gestionSerialPort.enviarCaracterPuertoSerie(arduino); break;
                        }
                        else
                        {
                            System.out.println("\nDebe estar conectado a un puerto serie para enviar un caracter");
                        }
                        break;

                    //MODULO ENVIAR CARACTERES
                    case 3:
                        if ( estadoConexion == true )
                        {
                            gestionSerialPort.enviarCadenaPuertoSerie(arduino); break;
                        }
                        else
                        {
                            System.out.println("\nDebe estar conectado a un puerto serie para enviar un caracter");
                        }
                    break;

                    //Desconectarse
                    case 4:
                        if ( arduino.cerrarPuerto() == true )
                        {
                            System.out.println("Desconexion del puerto "+arduino.getNombrePuerto()+" realizada correctamente");
                            estadoConexion = false;
                        }
                        else
                        {
                            System.out.println("\nNo esta conectado a ningun puerto serie");
                        }
                    break;
                }
            }

        } while ( opcionMenuPrincipal != OPCION_SALIR );

        //Si el puerto esta abierto, cerramos la conexion antes de salir
        if ( estadoConexion == true )
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
        System.out.println("2. Enviar caracter a matriz de leds");
        System.out.println("3. Enviar cadena de caracteres a matriz de leds");
        System.out.println("4. Leer Sensor Temperatura");
        System.out.println("6. Salir");
    }
}

