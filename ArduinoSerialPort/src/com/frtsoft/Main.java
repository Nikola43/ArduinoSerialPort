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
        String cadenaEnviada;

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

                        //Pedimos al usuario que seleccione el puerto al que se quiere conectar
                        //y la velocidad de transmision con la que quiere enviar / recibir los datos
                        String nombrePuerto = gestionSerialPort.seleccionarPuerto();
                        int velocidadTransmision = gestionSerialPort.seleccionarVelocidadTransmision();

                        //Abrimos el puerto serie
                        resultadoConexion = arduino.abrirPuerto(nombrePuerto,velocidadTransmision);

                        if (  resultadoConexion == 1)
                        {
                            System.out.println("Conexion del puerto " + arduino.getPuerto().getName() + " realizada correctamente");
                            estadoConexion = true;
                        }
                        else if ( resultadoConexion  == 2)
                        {
                            System.out.println("El puerto serie "+arduino.getPuerto().getName()+" ya esta en uso");
                        }
                        else if ( resultadoConexion  == 3)
                        {
                            System.out.println("No se encuentra el puerto "+arduino.getPuerto().getName());
                        }
                    break;

                    case 2:
                        if ( estadoConexion == true )
                        {
                            //Pedimos al usuario que introduzca el caracter que quiere enviar
                            do
                            {
                                System.out.print("Introduce un caracter de la a-z o 0-9: ");
                                caracterEnviado = scanner.next().charAt(0);
                            } while ( (caracterEnviado < 'a' || caracterEnviado > 'z') && (caracterEnviado < '0' || caracterEnviado > '9') );

                            //Enviamos el caracter que introdujo el usuario
                            arduino.enviarCaracter(caracterEnviado);
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
                            //Pedimos al usuario que introduzca la cadena que quiere enviar
                            do
                            {
                                System.out.print("Introduce una cadena de caracteres: ");
                                cadenaEnviada = scanner.nextLine();
                            } while ( cadenaEnviada.matches(".*([ \t]).*") == false );

                            //Enviamos la cadena que introdujo el usuario
                            arduino.enviarString(cadenaEnviada+" ");
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
                            System.out.println("Desconexion del puerto "+arduino.getPuerto().getName()+" realizada correctamente");
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
        System.out.println("5. Salir");
    }
}

