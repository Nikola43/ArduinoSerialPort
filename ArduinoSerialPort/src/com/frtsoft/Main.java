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
                    case 1:
                        System.out.println(".:| Conectarse a Puerto Serie |:.");

                        arduino.setNombrePuerto(gestionSerialPort.seleccionarPuerto()); // Asignamos nombre
                        arduino.setBaudRate(gestionSerialPort.seleccionarVelocidadTransmision()); // Asignamos velocidad
                        arduino.abrirPuerto();
                    break;

                    //MODULO RECIBIR ENVIAR CARACTERES
                    case 2:
                        if ( arduino.getEstadoConexion() )
                        {
                            do
                            {
                                System.out.print("Introduce un caracter de la a-z o 0-9: ");
                                caracterEnviado = scanner.next().charAt(0);
                            } while ( (caracterEnviado < 'a' || caracterEnviado > 'z') && (caracterEnviado < '0' || caracterEnviado > '9') );

                            arduino.enviarCaracter(caracterEnviado);
                            esperar(100);
                        }
                        else
                        {
                            System.out.println("\nError: Debe estar conectado a un puerto serie para enviar un caracter");

                        }
                    break;

                    //MODULO RECIBIR CARACTERES
                    case 3:
                        if ( arduino.getEstadoConexion() )
                        {
                            do
                            {
                                temperatura = arduino.getCaracterRecibido();
                                System.out.println("Caracter Recibido: "+ temperatura);
                                esperar(50);
                            } while (true);
                        }
                        else
                        {
                            System.out.println("\nError: Debe estar conectado a un puerto serie para leer un caracter");
                        }
                    break;

                    //MODULO LEER SENSOR TEMPERATURA
                    case 4:
                        if ( arduino.getEstadoConexion() )
                        {
                            do
                            {
                                temperatura = arduino.getCaracterRecibido();
                                System.out.println("Sensor Temperatura: "+temperatura);
                                esperar(1000);
                            } while (true);
                        }
                        else
                        {
                            System.out.println("\nError: Debe estar conectado a un puerto serie para leer un caracter");
                        }
                    break;
                }
            }

        } while ( opcionMenuPrincipal != OPCION_SALIR );

        //Si el puerto esta abierto, cerramos la conexion antes de salir
        if ( arduino.getEstadoConexion() )
        {
            arduino.cerrarPuerto();
        }
    }
    /*
     INTERFAZ
     Funcionamiento: Pausa el hilo de ejecucion un determinado tiempo
     Prototipo: public static void esperar(int tiempo)
     Entrada: Entero (tiempo)
     Precondiciones: El entero debe ser mayor que 0
     Salida:
     Postcondiciones:
     Entrada / Salida:
     */
    public static void esperar(int tiempo)
    {
        try
        {
            Thread.sleep(tiempo);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
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
        System.out.println("4. Leer Sensor Temperatura");
        System.out.println("5. Salir");
    }
}

