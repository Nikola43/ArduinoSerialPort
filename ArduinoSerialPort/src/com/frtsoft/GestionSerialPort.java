package com.frtsoft;

import gnu.io.CommPortIdentifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class GestionSerialPort
{
    /*
    INTERFAZ
    Funcionamiento: Devuelve los puertos serie disponibles en el sistema
    Prototipo: public ArrayList<String> getPuertosDisponibles()
    Entrada:
    Precondiciones:
    Salida: Devuelve un arrayList de String con el nombre del los puertos disponibles en el sistema
    Postcondiciones: Los nombres del los puertos seran nombres de puerto existente en el sistema
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
    Funcionamiento: Muestra por pantalla los puertos serie disponibles en el sistema
    Prototipo: public void mostrarPuertosDisponibles ( ArrayList<String> puertosDisponibles )
    Entrada: ArrayList de string con los puertos disponibles en el sistema
    Precondiciones:
    Salida: Muestra por pantalla un arrayList de String con el nombre del los puertos disponibles en el sistema
    Postcondiciones: Los nombres del los puertos seran nombres de puerto existente en el sistema
    Entrada / Salida:
    */
    public void mostrarPuertosDisponibles ( ArrayList<String> puertosDisponibles ) {
        int contadorPuertosDisponibles = 0;

        if (puertosDisponibles.size() > 0) {
            System.out.println("\nPuertos disponibles en el sistema: ");
            while (contadorPuertosDisponibles < puertosDisponibles.size()) {
                System.out.println("\tPuerto " + (contadorPuertosDisponibles + 1) + ": " + puertosDisponibles.get(contadorPuertosDisponibles));
                contadorPuertosDisponibles++;
            }
        }
    }
    /*
    INTERFAZ
    Funcionamiento:
    Prototipo: public String mostrarElegirPuertoSerie()
    Entrada:
    Precondiciones:
    Salida: Devuelve el nombre del puerto serie seleccionado
    Postcondiciones: El nombre del puerto debe ser un puerto existente en el sistema
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

    public int seleccionarVelocidadTransmision()
    {
        int[] rangoBaudRate = { 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200 };
        int velocidadElegida = 0;
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
}
