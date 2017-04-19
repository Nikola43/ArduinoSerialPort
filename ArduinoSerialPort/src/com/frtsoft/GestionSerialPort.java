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
        Busca puertos serie en el equipo, devuelve los puertos disponibles en el sistema
    Prototipo:
        public ArrayList<String> getPuertosDisponibles()
    Entradas:
        -
    Precondiciones:
        -
    Salida:
        Devuelve un arrayList de String con el nombre de los puertos disponibles en el sistema
    Postcondiciones:
        Los nombres del los puertos seran nombres de puertos existentes en el sistema
    Entrada / Salida:
    */
    public ArrayList<String> getPuertosDisponibles()
    {
        //ArrayList donde almacenaremos los puertos del sistema
        ArrayList<String> puertosDisponibles = new ArrayList<>();

        //Identificador del puerto serie
        CommPortIdentifier idPuertoSerie;

        //Enum de puertos
        Enumeration puertosDelSistema;

        puertosDelSistema = CommPortIdentifier.getPortIdentifiers(); //Enum de los puertos del sistema

        //Si hay puertos...
        if ( puertosDelSistema.hasMoreElements() )
        {
            //Mientras haya mas puertos en el sistema
            while ( puertosDelSistema.hasMoreElements() )
            {
                idPuertoSerie = (CommPortIdentifier) puertosDelSistema.nextElement(); // Asignamos el id del puerto actual

                //Si el puerto es de tipo serie
                if (idPuertoSerie.getPortType() == CommPortIdentifier.PORT_SERIAL)
                {
                    //Insertamos el puerto actual en la lista de puertos
                    puertosDisponibles.add(idPuertoSerie.getName());
                }
            }
        }
        else
        {
            System.out.println("\nNo se detecta ningun puerto serie en el equipo");
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

        //Si hay algun puerto
        if (puertosDisponibles.size() > 0)
        {
            //Imprimimos los puertos por pantalla
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
        El nombre del puerto sera un puerto existente en el sistema
    Entrada / Salida:
    */
    public String seleccionarPuerto()
    {
        int puertoSeleccionado = 0;
        Scanner scanner = new Scanner(System.in);

        if ( getPuertosDisponibles().size() > 0 )
        {
            do
            {
                mostrarPuertosDisponibles(getPuertosDisponibles());

                System.out.print("\nIntroduce el puerto al que desea conectarse: ");
                puertoSeleccionado = scanner.nextInt();
            } while (puertoSeleccionado < 1 || puertoSeleccionado > getPuertosDisponibles().size());
        }
        return (getPuertosDisponibles().get(puertoSeleccionado - 1));
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

    /*
    INTERFAZ
    Descripcion:
        Inicia y configura la conexion con el puerto serie
    Prototipo:
        public void conectarsePuertoSerie( ArduinoSerialPort arduino )
    Entrada:
        Un objeto de tipo ArduinoSerialPort (puerto serie al que te quieres conectar)
    Precondiciones:
        -
    Salida:
        -
    Postcondiciones:
        La velocidad seleccionada debe ser una de las mostradas al usuario
    Entrada / Salida:
    */
    public void conectarsePuertoSerie( ArduinoSerialPort arduino )
    {
        if ( arduino != null )
        {
            arduino.setNombrePuerto(seleccionarPuerto()); // Asignamos nombre
            arduino.setBaudRate(seleccionarVelocidadTransmision()); // Asignamos velocidad
            arduino.setDataBits(8);
            arduino.setParity(SerialPort.PARITY_NONE);
            arduino.setStopBits(SerialPort.STOPBITS_1);
            arduino.abrirPuerto();
        }
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

            arduino.enviarByte(caracterEnviado);
            esperar(100);
        }
        else
        {
            System.out.println("\nDebe estar conectado a un puerto serie para enviar un caracter");
        }
    }

    public void enviarCadenaPuertoSerie( ArduinoSerialPort arduino )
    {
        String cadenaEnviada = " ";
        Scanner scanner = new Scanner(System.in);
        if ( arduino.getEstadoConexion() )
        {
            //do
            //{
                System.out.print("Introduce una cadena de caracteres: ");
                cadenaEnviada = scanner.nextLine();
            //} while ( cadenaEnviada.matches(".*([ \t]).*") == false );

            arduino.enviarString(cadenaEnviada);
            esperar(100);
            arduino.enviarByte(' ');
        }
        else
        {
            System.out.println("\nDebe estar conectado a un puerto serie para enviar una cadena de caracteres");
        }
    }

    public void imprimirCaracterRecibidoPuertoSerie( ArduinoSerialPort arduino )
    {
        if ( arduino.getEstadoConexion() )
        {
            System.out.println("Caracter Recibido: "+ (char) arduino.getDatosRecibidos());
        }
        else
        {
            System.out.println("\nError: Debe estar conectado a un puerto serie para leer un caracter");
        }
    }

    public void imprimirValorSensorLuz( ArduinoSerialPort arduino )
    {
        int luz;
        if ( arduino.getEstadoConexion() )
        {
            luz = arduino.getDatosRecibidos();
            System.out.println("Sensor Luz: "+luz);
            esperar(100);
        }
        else
        {
            System.out.println("\nError: Debe estar conectado a un puerto serie para leer un caracter");
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
}