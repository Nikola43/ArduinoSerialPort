package com.frtsoft;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class GestionSerialPort
{
    /*
    INTERFAZ
    Descripcion:
        Devuelve los puertos serie disponibles en el sistema
    Prototipo:
        public ArrayList<String> getPuertosDisponibles()
    Entradas:
        -
    Precondiciones:
        -
    Salida:
        Devuelve un arrayList de String con el nombre del los puertos disponibles en el sistema
    Postcondiciones:
        Los nombres del los puertos seran nombres de puerto existente en el sistema
    Entrada / Salida:
    */
    public ArrayList<String> getPuertosDisponibles()
    {
        ArrayList<String> puertosDisponibles = new ArrayList<>();
        int contadorPuertos = 0;

        Enumeration puertosDelSistema = CommPortIdentifier.getPortIdentifiers(); //Enum de los puertos del sistema

        //Si hay puertos...
        if ( puertosDelSistema.hasMoreElements() )
        {
            while ( puertosDelSistema.hasMoreElements() ) //Mientras haya mas puertos en el sistema
            {
                CommPortIdentifier portId = (CommPortIdentifier) puertosDelSistema.nextElement(); // Asignamos el id del puerto actual

                //Si el puerto es de tipo serial
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
                {
                    puertosDisponibles.add(contadorPuertos, portId.getName());
                }
            }
        }
        else
        {
            System.out.println("\nError: No se detecta ningun puerto serie en el equipo");
        }
        return puertosDisponibles;
    }

    /*
    INTERFAZ
    Descripcion:
        Muestra por pantalla los puertos serie disponibles en el sistema
    Prototipo:
        public void mostrarPuertosDisponibles ( ArrayList<String> puertosDisponibles )
    Entrada:
        ArrayList de string con los puertos disponibles en el sistema
    Precondiciones:
        El array debe tener al menos un elemento
    Salida:
        Muestra por pantalla un arrayList de String con el nombre del los puertos disponibles en el sistema
    Postcondiciones:
        Los nombres del los puertos seran nombres de puerto existente en el sistema
    Entrada / Salida:
    */
    public void mostrarPuertosDisponibles ( ArrayList<String> puertosDisponibles )
    {
        int contadorPuertosDisponibles = 0;

        if (puertosDisponibles.size() > 0)
        {
            System.out.println("\nPuertos disponibles: ");
            while (contadorPuertosDisponibles < puertosDisponibles.size())
            {
                System.out.println("\tPuerto " + (contadorPuertosDisponibles + 1) + ": " + puertosDisponibles.get(contadorPuertosDisponibles));
                contadorPuertosDisponibles++;
            }
        }
        else
        {
            System.out.println("No hay ningun puerto serie en el sistema.");
        }
    }
    /*
    INTERFAZ
    Descripcion:
        Pide al usuario que seleccione un puerto del sistema
    Prototipo:
        public String seleccionarPuerto()
    Entrada:
        -
    Precondiciones:
        -
    Salida:
        Devuelve el nombre del puerto serie seleccionado
    Postcondiciones:
        El nombre del puerto debe ser un puerto existente en el sistema
    Entrada / Salida:
    */
    public String seleccionarPuerto()
    {
        int puertoSeleccionado = 0;
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> puertosDisponibles;

        puertosDisponibles = getPuertosDisponibles();

        if ( puertosDisponibles.size() > 0 )
        {
            do
            {
                mostrarPuertosDisponibles(puertosDisponibles);

                System.out.print("\nIntroduce el puerto al que desea conectarse: ");
                puertoSeleccionado = scanner.nextInt();
            } while (puertoSeleccionado < 1 || puertoSeleccionado > puertosDisponibles.size());
        }
        return (puertosDisponibles.get(puertoSeleccionado - 1));
    }

    /*
    INTERFAZ
    Descripcion:
        Muestra las diferentes velocidades de transmision validas y solicita al usuario que seleccione una de ellas
    Prototipo:
        public int seleccionarVelocidadTransmision()
    Entrada:
        -
    Precondiciones:
        -
    Salida:
        Devuelve un entero con la velocidad seleccionada
    Postcondiciones:
        La velocidad debe ser una de las mostradas al usuario
    Entrada / Salida:
    */
    public int seleccionarVelocidadTransmision()
    {
        final int[] rangoBaudRate = { 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200 };
        int velocidadElegida;
        Scanner scanner = new Scanner(System.in);

        do
        {
            System.out.print("\nVelocidades disponibles: \n");

            for (int indiceVelocidades = 0; indiceVelocidades < rangoBaudRate.length; indiceVelocidades++)
            {
                System.out.println((indiceVelocidades + 1) + ". " + rangoBaudRate[indiceVelocidades]);
            }
            System.out.print("\nIntroduce la velocidad que deseas establecer para la conexion: ");
            velocidadElegida = scanner.nextInt();
        } while ( velocidadElegida < 0 || velocidadElegida > rangoBaudRate.length );

        return rangoBaudRate[velocidadElegida - 1];
    }

    public void conectarsePuertoSerie( ArduinoSerialPort arduino )
    {
        System.out.println(".:| Conectarse a Puerto Serie |:.");

        arduino.setNombrePuerto(seleccionarPuerto()); // Asignamos nombre
        arduino.setBaudRate(seleccionarVelocidadTransmision()); // Asignamos velocidad
        arduino.setDataBits(8);
        arduino.setParity(SerialPort.PARITY_NONE);
        arduino.setStopBits(SerialPort.STOPBITS_1);
        arduino.abrirPuerto();
    }

    public void enviarCaracterPuertoSerie( ArduinoSerialPort arduino )
    {
        char caracterEnviado;
        Scanner scanner = new Scanner(System.in);
        if ( arduino.getEstadoConexion() )
        {
            do
            {
                System.out.print("Introduce un caracter de la a-z o 0-9: ");
                caracterEnviado = scanner.next().charAt(0);
            } while ( (caracterEnviado < 'a' || caracterEnviado > 'z') && (caracterEnviado < '0' || caracterEnviado > '9') );

            arduino.enviarCaracter(caracterEnviado);
            Main.esperar(100);
        }
        else
        {
            System.out.println("\nError: Debe estar conectado a un puerto serie para enviar un caracter");
        }
    }

    public void enviarCadenaPuertoSerie( ArduinoSerialPort arduino )
    {
        String cadenaEnviada;
        Scanner scanner = new Scanner(System.in);
        if ( arduino.getEstadoConexion() )
        {
            do
            {
                System.out.print("Introduce una cadena de caracteres: ");
                cadenaEnviada = scanner.nextLine();
            } while ( cadenaEnviada.matches(".*([ \t]).*")== false );

            arduino.enviarString(cadenaEnviada);
            Main.esperar(100);
            arduino.enviarString(" ");
        }
        else
        {
            System.out.println("\nError: Debe estar conectado a un puerto serie para enviar un caracter");
        }
    }

    public void imprimirCaracterRecibidoPuertoSerie( ArduinoSerialPort arduino )
    {
        int caracterRecibido;
        if ( arduino.getEstadoConexion() )
        {
            do
            {
                caracterRecibido = arduino.getCaracterRecibido();
                System.out.println("Caracter Recibido: "+ caracterRecibido);
                Main.esperar(50);
            } while (true);
        }
        else
        {
            System.out.println("\nError: Debe estar conectado a un puerto serie para leer un caracter");
        }
    }

    public void imprimirValorSensorLuz( ArduinoSerialPort arduino )
    {
        char luz;
        if ( arduino.getEstadoConexion() )
        {
            do
            {
                luz = arduino.getCaracterRecibido();
                System.out.println("Sensor Luz: "+luz);
                Main.esperar(1000);
            } while (true);
        }
        else
        {
            System.out.println("\nError: Debe estar conectado a un puerto serie para leer un caracter");
        }
    }
}
