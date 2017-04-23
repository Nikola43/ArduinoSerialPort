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
        Busca puertos serie en el sistema, devuelve los puertos disponibles en el sistema
    Prototipo:
        public ArrayList<String> getPuertosDisponibles()
    Entradas:
        -
    Precondiciones:
        -
    Salida:
        ArrayList de String
    Postcondiciones:
        Devolvera una lista con los nombres de los puertos detectados en el sistema
    Entrada / Salida:
    */
    public ArrayList<String> detectarPuertosDisponibles()
    {
        ArrayList<String> puertosDisponibles = new ArrayList<>();
        CommPortIdentifier idPuertoSerie;
        Enumeration puertosDelSistema;

        puertosDelSistema = CommPortIdentifier.getPortIdentifiers(); //Enum de los puertos del sistema

        //Si hay puertos...
        if ( puertosDelSistema.hasMoreElements() )
        {
            //Mientras haya mas puertos en el sistema
            while ( puertosDelSistema.hasMoreElements() )
            {
                idPuertoSerie = (CommPortIdentifier) puertosDelSistema.nextElement(); // Asignamos el id del puerto actual

                //Si el puerto es de tipo serial
                if (idPuertoSerie.getPortType() == CommPortIdentifier.PORT_SERIAL)
                {
                    puertosDisponibles.add(idPuertoSerie.getName());
                }
            }
        }
        return puertosDisponibles;
    }

    /*
    INTERFAZ
    Descripcion:
        Muestra por pantalla un arrayList de String con el nombre de los puertos disponibles en el sistema
    Prototipo:
        public void mostrarPuertosDisponibles ( ArrayList<String> puertosDisponibles )
    Entrada:
        ArrayList de string con los puertos del sistema
    Precondiciones:
        -
    Salida:
        -
    Postcondiciones:
        -
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
            System.out.println("\nNo se detecta ningun puerto serie en el equipo");
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
        ArrayList<String> puertosDisponibles;
        Scanner scanner = new Scanner(System.in);

        int puertoSeleccionado = 0;
        int numeroPuertosDisponibles = 0;

        //Detectamos los puertos del sistema
        puertosDisponibles = detectarPuertosDisponibles();
        numeroPuertosDisponibles = puertosDisponibles.size();

        //Si hay al menos un puerto
        if ( numeroPuertosDisponibles >= 1 )
        {
            //Mostramos los puertos al usuario y le pedimos que seleccione uno
            do
            {
                mostrarPuertosDisponibles(puertosDisponibles);

                System.out.print("\nIntroduce el puerto al que desea conectarse: ");
                puertoSeleccionado = scanner.nextInt();
            } while (puertoSeleccionado < 1 || puertoSeleccionado > numeroPuertosDisponibles );
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
        La velocidad seleccionada debe ser una de las mostradas al usuario
    Entrada / Salida:
    */
    public int seleccionarVelocidadTransmision()
    {
        Scanner scanner = new Scanner(System.in);

        //Array con las velocidades permitidas
        final int[] rangoBaudRate = { 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200 };
        int velocidadElegida;

        do
        {
            //Mostramos las velocidades permitidas
            System.out.print("\nVelocidades disponibles: \n");
            for (int indiceVelocidades = 0; indiceVelocidades < rangoBaudRate.length; indiceVelocidades++)
            {
                System.out.println((indiceVelocidades + 1) + ". " + rangoBaudRate[indiceVelocidades]);
            }

            //Pedimos al usuario que seleccione una de esas velocidades
            System.out.print("\nIntroduce la velocidad que deseas establecer para la conexion: ");
            velocidadElegida = scanner.nextInt();
        } while ( velocidadElegida < 0 || velocidadElegida > rangoBaudRate.length );

        return rangoBaudRate[velocidadElegida - 1];
    }

    public int conectarsePuertoSerie( ArduinoSerialPort arduino )
    {
        int resultadoConexion = 0;

        //Pedimos al usuario que seleccione el puerto al que se quiere conectar
        //y la velocidad de transmision con la que quiere enviar / recibir los datos
        String nombrePuerto = seleccionarPuerto();
        int velocidadTransmision = seleccionarVelocidadTransmision();

        //Configuramos el puerto y la conexion
        arduino.setNombrePuerto(nombrePuerto);
        arduino.setBaudRate(velocidadTransmision);
        arduino.setDataBits(8);
        arduino.setParity(SerialPort.PARITY_NONE);
        arduino.setStopBits(SerialPort.STOPBITS_1);

        //Abrimos el puerto serie
        resultadoConexion = arduino.abrirPuerto();

        return (resultadoConexion);
    }

    public void enviarCaracterPuertoSerie( ArduinoSerialPort arduino )
    {
        char caracterEnviado;
        Scanner scanner = new Scanner(System.in);

        //Pedimos al usuario que introduzca el caracter que quiere enviar
        do
        {
            System.out.print("Introduce un caracter de la a-z o 0-9: ");
            caracterEnviado = scanner.next().charAt(0);
        } while ( (caracterEnviado < 'a' || caracterEnviado > 'z') && (caracterEnviado < '0' || caracterEnviado > '9') );

        //Enviamos el caracter que introdujo el usuario
        arduino.enviarCaracter(caracterEnviado);
        esperar(100);
    }

    public void enviarCadenaPuertoSerie( ArduinoSerialPort arduino )
    {
        String cadenaEnviada;
        Scanner scanner = new Scanner(System.in);

        //Pedimos al usuario que introduzca la cadena que quiere enviar
        do
        {
            System.out.print("Introduce una cadena de caracteres: ");
            cadenaEnviada = scanner.nextLine();
        } while ( cadenaEnviada.matches(".*([ \t]).*") == false );

        //Enviamos la cadena que introdujo el usuario
        arduino.enviarString(cadenaEnviada+" ");
        esperar(100);
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
}
