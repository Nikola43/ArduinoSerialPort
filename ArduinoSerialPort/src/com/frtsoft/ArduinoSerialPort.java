/*
    CLASE Jugador

    PROPIEDADES
        BASICAS
            NINGUNA

        DERIVADAS
            NINGUNA

        COMPARTIDAS
            NINGUNA

    RESTRICCIONES
        NINGUNA

    INTERFAZ
        METODOS CONSULTORES

        METODOS MODIFICADORES

        METODOS HEREDADOS
            public String toString()
            public int hashCode()
            public ArduinoSerialPort clone()
            public boolean equals(Object object)
            public int compareTo(ArduinoSerialPort arduinoSerialPort)

        METODOS AÑADIDOS
*/


package com.frtsoft;

//Importar clases para manejar el puerto serie
import gnu.io.CommPortIdentifier; //Identificador de puertos
import gnu.io.SerialPort; //Puerto serie
import gnu.io.SerialPortEvent; //Manejo de eventos
import gnu.io.SerialPortEventListener; //Escuchador de eventos

//Importar clases de java para manejar el programa
import java.io.IOException; //Manejo de excepciones
import java.io.InputStream; //Necesario para leer del puerto serie
import java.io.OutputStream; //Necesario para escribir del puerto serie
import java.util.Enumeration; //Enum de puertos

public class ArduinoSerialPort implements SerialPortEventListener
{
//------------------------------- PROPIEDADES -----------------------------------------------//
    //BASICAS
    private SerialPort puerto;          //Definimos el puerto serie
    private char datosRecibidos;
    //DERIVADAS

    //COMPARTIDAS
    //NINGUNA
//------------------------------- FIN PROPIEDADES --------------------------------------------//

//------------------------------- CONSTRUCTORES ----------------------------------------------//
    //CONSTRUCTOR POR DEFECTO
    public ArduinoSerialPort()
    {
        puerto  = null;
    }

    //CONSTRUCTOR SOBRECARGADO
    public ArduinoSerialPort(SerialPort puerto)
    {
        this.puerto = puerto;
    }

    //CONSTRUCTOR DE COPIA
    public ArduinoSerialPort(ArduinoSerialPort arduinoSerialPort)
    {
        this.puerto = arduinoSerialPort.getPuerto();
    }
//------------------------------- FIN CONSTRUCTORES ------------------------------------------//

//------------------------------- METODOS CONSULTORES ----------------------------------------//
    public SerialPort getPuerto()
{
    return puerto;
}
    public char getDatosRecibidos()
    {
        return datosRecibidos;
    }
//------------------------------- FIN METODOS CONSULTORES ------------------------------------//

//------------------------------- METODOS MODIFICADORES --------------------------------------//
    public void setPuerto(SerialPort puerto) {
        this.puerto = puerto;
    }
//------------------------------- FIN METODOS MODIFICADORES ----------------------------------//

//------------------------------- METODOS SOBRESCRITOS ---------------------------------------//
    @Override
    public String toString()
    {
        return (puerto.toString()+","+datosRecibidos);
    }

    @Override
    public ArduinoSerialPort clone()
    {
        ArduinoSerialPort copia = null;

        try
        {
            copia = (ArduinoSerialPort) super.clone();
        }
        catch (CloneNotSupportedException error)
        {
            System.out.println("Error: No se pudo clonar el puerto serie");
        }
        return copia;
    }

    @Override
    public int hashCode()
    {
        int codigo;
        codigo = puerto.getParity() * 13 + puerto.getStopBits() / 7 - puerto.getBaudRate() * 16 + puerto.getDataBits() * 3 - Integer.parseInt(puerto.getName());
        return codigo;
    }

    /*
    INTERFAZ
    Funcionamiento:
        Comprueba si dos puertos son iguales
    Prototipo:
        public boolean equals(Object o)
    Entrada:
        Un objeto
    Precondiciones:
        -
    Salida:
        Un booleano
    Postcondiciones:
        Devolvera el resultado de la comparacion
        * Si tienen el mismo valor en DataBits, BaudRate, StopBits y Parity para el sistema son puertos iguales
        * TRUE cuando tengan la misma configuracion
        * FALSE cuando no tengan la misma configuracion
    Entrada / Salida:
    */
    @Override
    public boolean equals(Object o)
    {
        boolean igual = false;

        // Seran iguales  cuando tengan el mismo nombre en el sistema
        if (o != null && o instanceof ArduinoSerialPort)
        {
            ArduinoSerialPort arduinoSerialPort = (ArduinoSerialPort) o;

            if ( puerto.getDataBits() == arduinoSerialPort.getPuerto().getDataBits() &&
                 puerto.getBaudRate() == arduinoSerialPort.getPuerto().getBaudRate() &&
                 puerto.getStopBits() == arduinoSerialPort.getPuerto().getStopBits() &&
                 puerto.getParity()   == arduinoSerialPort.getPuerto().getParity()
               )
            {
                igual = true;
            }
        }
        return igual;
    }

    /*
    INTERFAZ
    Funcionamiento:
        Compara dos puertos segun su velocidad de transiomision
    Prototipo:
        public int compareTo(ArduinoSerialPort arduinoSerialPort)
    Entrada:
        Un objeto tipo ArduinoSerialPort
    Precondiciones:
        -
    Salida:
        Un entero
    Postcondiciones:
        Devolvera el resultado de la comparacion
        * 1  cuando tenga mayor velocidad
        * -1 cuado tenga menor velocidad
        * 0 cuando tengan la misma velodiad
    Entrada / Salida:
    */
    public int compareTo(ArduinoSerialPort arduinoSerialPort)
    {
        int comparacion;

        if (puerto.getBaudRate() > arduinoSerialPort.getPuerto().getBaudRate())
        {
            comparacion = 1;
        }
        else if (puerto.getBaudRate() < arduinoSerialPort.getPuerto().getBaudRate())
        {
            comparacion = -1;
        }
        else
        {
            comparacion = 0;
        }
        return comparacion;
    }
//------------------------------- FIN METODOS SOBRESCRITOS -----------------------------------//

//------------------------------- METODOS AÑADIDOS -------------------------------------------//
    /*
    INTERFAZ
    Funcionamiento:
        Inicia la comunicacion con el puerto serie
    Prototipo:
        public int abrirPuerto()
    Entrada:
        -
    Precondiciones:
        -
    Salida:
        Un entero
    Postcondiciones:
        Devolvera con el resultado de la conexion
        * 3 cuando no se encuentre el puerto
        * 2 cuando el puerto ya este en uso
        * 1 cuando la conexion se realice correctamente
    Entrada / Salida:
    */
    public int abrirPuerto(String nombrePuerto, int baudRate)
    {
        int resultadoConexion = 3;

        try
        {
            Enumeration puertosDelSistema = CommPortIdentifier.getPortIdentifiers(); //Enum de los puertos del sistema
            while (puertosDelSistema.hasMoreElements()) //Mientras haya mas puertos en el sistema
            {
                CommPortIdentifier idPuertoSerie = (CommPortIdentifier) puertosDelSistema.nextElement(); // Asignamos el id del puerto actual

                //Si el puerto es de tipo serial
                if (idPuertoSerie.getPortType() == CommPortIdentifier.PORT_SERIAL)
                {
                    //Si el nombre del puerto es igual al nombre del puerto al que nos queremos conectar
                    if (idPuertoSerie.getName().equals(nombrePuerto))
                    {
                        // Si el puerto no esta en uso
                        if ( idPuertoSerie.isCurrentlyOwned() == false )
                        {
                            //Abrimos el puerto
                            puerto = (SerialPort) idPuertoSerie.open("arduino serial", 2000);

                            //Configuramos la conexion
                            puerto.disableReceiveThreshold();
                            puerto.enableReceiveTimeout(3000);
                            puerto.setSerialPortParams(baudRate,8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                            //Creamos un listener para escuchar lo que manda el puerto serie al sistema
                            puerto.addEventListener(this);
                            puerto.notifyOnDataAvailable(true);

                            //Si el puerto fue abierto correctamente deberia ser distinto de null
                            if ( puerto != null )
                            {
                                resultadoConexion = 1; //Puerto abierto correctemente
                            }
                        }
                        else
                        {
                            resultadoConexion = 2; //Puerto seleccionado ya esta en uso
                        }
                    }
                    else
                    {
                        resultadoConexion = 3; //No se encuentra el puerto serie
                    }
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            //System.out.println("Error: Imposible abrir el puerto "+puerto.getName());
        }
        return resultadoConexion;
    }

    public void cerrarPuerto()
    {
        // Si el puerto esta abierto es que es distinto de null
        if ( puerto != null)
        {
            puerto.removeEventListener(); //Eliminamos el escuchador de eventos
            puerto.close();        //Cerramos el puerto
        }
    }

    synchronized public void serialEvent(SerialPortEvent serialEvent)
    {
        InputStream inputStream;
        try
        {   //Comprobamos el tipo de evento del puerto serie
            if ( serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE )
            {
                inputStream = puerto.getInputStream();
                // Comprobamos si hay datos disponibles antes de leerlos
                if ( inputStream.available() > 0 )
                {
                    datosRecibidos = (char) inputStream.read();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean enviarCaracter(char caracter)
    {
        boolean enviadoCorrectamente = false;
        OutputStream outputStream;
        try
        {
            outputStream = puerto.getOutputStream();
            outputStream.write(caracter);
            enviadoCorrectamente = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return enviadoCorrectamente;
    }

    public boolean enviarString(String cadena)
    {
        OutputStream outputStream;
        boolean enviadoCorrectamente = false;
        try
        {
            outputStream = puerto.getOutputStream();
            outputStream.write(cadena.getBytes());
            enviadoCorrectamente = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return enviadoCorrectamente;
    }
//------------------------------- FIN METODOS AÑADIDOS ---------------------------------------//
}
