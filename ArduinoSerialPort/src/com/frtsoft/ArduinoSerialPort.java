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
    private String nombrePuerto;        //Nombre del puerto serie (Como lo reconoce el sistema)
    private boolean estadoConexion;     //booleano para saber si esta conectado o no
    private InputStream flujoEntrada;   //Permite leer del puerto serie
    private OutputStream flujoSalida;   //Permite escribir en el puerto serie
    private char datosRecibidos;

    //Configuracion de la conexion
    private int baudRate;  // Velocidad de transmision en bits por segundo
    private int dataBits;  // Tamaño de la trama de bits enviados
    private int parity;    //Sin paridad
    private int stopBits;  // Usamos 1 bit control

    //DERIVADAS
    //NINGUNA

    //COMPARTIDAS
    //NINGUNA
//------------------------------- FIN PROPIEDADES --------------------------------------------//

    //------------------------------- CONSTRUCTORES ----------------------------------------------//
    //CONSTRUCTOR POR DEFECTO
    public ArduinoSerialPort()
    {
        puerto  = null;
                nombrePuerto = "COM3";
               estadoConexion = false;
                baudRate = 9600;
               dataBits = 8;
                parity = SerialPort.PARITY_NONE;
                stopBits = SerialPort.STOPBITS_1;
                flujoEntrada = null;
                flujoSalida = null;
    }

    //CONSTRUCTOR SOBRECARGADO
    public ArduinoSerialPort(SerialPort puerto,
                             String nombrePuerto,
                             boolean estadoConexion,
                             int baudRate,
                             int dataBits,
                             int parity,
                             int stopBits,
                             InputStream flujoEntrada,
                             OutputStream flujoSalida)
    {
        this.puerto = puerto;
        this.nombrePuerto = nombrePuerto;
        this.estadoConexion = estadoConexion;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.parity = parity;
        this.stopBits = stopBits;
        this.flujoEntrada = flujoEntrada;
        this.flujoSalida = flujoSalida;
    }

    //CONSTRUCTOR DE COPIA
    public ArduinoSerialPort(ArduinoSerialPort arduinoSerialPort)
    {
        this.puerto = arduinoSerialPort.getPuerto();
        this.nombrePuerto = arduinoSerialPort.getNombrePuerto();
        this.estadoConexion = arduinoSerialPort.getEstadoConexion();
        this.baudRate = arduinoSerialPort.getBaudRate();
        this.dataBits = arduinoSerialPort.getDataBits();
        this.parity = arduinoSerialPort.getParity();
        this.stopBits = arduinoSerialPort.getStopBits();
        this.flujoEntrada = arduinoSerialPort.flujoEntrada;
        this.flujoSalida = arduinoSerialPort.flujoSalida;
    }
//------------------------------- FIN CONSTRUCTORES ------------------------------------------//

    //------------------------------- METODOS CONSULTORES ----------------------------------------//
    public SerialPort getPuerto()
    {
        return puerto;
    }
    public String getNombrePuerto()
    {
        return nombrePuerto;
    }
    public boolean getEstadoConexion()
    {
        return estadoConexion;
    }
    public int getBaudRate()
    {
        return baudRate;
    }
    public int getDataBits()
    {
        return dataBits;
    }
    public int getParity()
    {
        return parity;
    }
    public int getStopBits()
    {
        return stopBits;
    }
    public int getDatosRecibidos()
    {
        return datosRecibidos;
    }
    public InputStream getFlujoEntrada()
    {
        return flujoEntrada;
    }
    public OutputStream getFlujoSalida()
    {
        return flujoSalida;
    }
//------------------------------- FIN METODOS CONSULTORES ------------------------------------//

    //------------------------------- METODOS MODIFICADORES --------------------------------------//
    public void setPuerto(SerialPort puerto) {
        this.puerto = puerto;
    }
    public void setNombrePuerto(String nombrePuerto)
    {
        this.nombrePuerto = nombrePuerto;
    }
    public void setEstadoConexion(boolean estadoConexion)
    {
        this.estadoConexion = estadoConexion;
    }
    public void setBaudRate(int baudRate)
    {
        this.baudRate = baudRate;
    }
    public void setDataBits(int dataBits)
    {
        this.dataBits = dataBits;
    }
    public void setParity(int parity)
    {
        this.parity = parity;
    }
    public void setStopBits(int stopBits)
    {
        this.stopBits = stopBits;
    }
    private void setFlujoEntrada(InputStream flujoEntrada)
    {
        this.flujoEntrada = flujoEntrada;
    }
    private void setFlujoSalida(OutputStream flujoSalida)
    {
        this.flujoSalida = flujoSalida;
    }
//------------------------------- FIN METODOS MODIFICADORES ----------------------------------//

    //------------------------------- METODOS HEREDADOS ------------------------------------------//
    @Override
    public String toString()
    {
        return (puerto.toString()+", "+nombrePuerto+", "+estadoConexion+", "+baudRate+", "+dataBits+", "+parity+", "+stopBits+", "+datosRecibidos+", "+flujoEntrada.toString()+", "+flujoSalida.toString() );
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

        codigo = parity * 13 + stopBits / 7 - baudRate * 16 + dataBits * 3; // Preguntar a Asun si puede usarse math.random

        return codigo;
    }

    @Override
    public boolean equals(Object o)
    {
        boolean igual = false;

        // Seran iguales  cuando tengan el mismo nombre en el sistema
        if (o != null && o instanceof ArduinoSerialPort)
        {
            ArduinoSerialPort a = (ArduinoSerialPort) o;

            if ( nombrePuerto.equals(a.getNombrePuerto()) )
            {
                igual = true;
            }
        }
        return igual;
    }

    public int compareTo(ArduinoSerialPort arduinoSerialPort)
    {
        int comparacion;

        if (getNombrePuerto().equals(arduinoSerialPort.getNombrePuerto()))
        {
            comparacion = 0;
        }
        else
        {
            comparacion = 1;
        }
        return comparacion;
    }
    //------------------------------- FIN METODOS HEREDADOS --------------------------------------//
    //METODOS DE LA CLASE
    /*
    INTERFAZ
    Funcionamiento: Inicia la comunicacion con el puerto serie
    Prototipo: public boolean abrirPuerto()
    Entrada:
    Precondiciones:
    Salida: Devuelve el estado de la conexion despues de la llamada al metodo, si se ha conectado correctamente o no
    Postcondiciones: La salida sera verdadero si se ha conectado y falso y no se ha podido realizar la conexion
    Entrada / Salida:
    */
    public void abrirPuerto()
    {
        estadoConexion = false; // false = no conectado // true = conectado

        try
        {
            Enumeration puertosDelSistema = CommPortIdentifier.getPortIdentifiers(); //Enum de los puertos del sistema
            while (puertosDelSistema.hasMoreElements()) //Mientras haya mas puertos en el sistema
            {
                CommPortIdentifier portId = (CommPortIdentifier) puertosDelSistema.nextElement(); // Asignamos el id del puerto actual

                //Si el puerto es de tipo serial
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
                {
                    //Si el nombre del puerto es igual al nombre del puerto al que nos queremos conectar
                    if (portId.getName().equals(nombrePuerto))
                    {
                        if ( portId.isCurrentlyOwned() == false )
                        {
                            puerto = (SerialPort) portId.open("arduino serial", 2000); //Abrimos el puerto

                            puerto.disableReceiveThreshold();
                            puerto.enableReceiveTimeout(3000);
                            puerto.setSerialPortParams(baudRate, dataBits, stopBits, parity);

                            setFlujoEntrada(getPuerto().getInputStream());
                            setFlujoSalida(getPuerto().getOutputStream());

                            puerto.addEventListener(this);
                            puerto.notifyOnDataAvailable(true);

                            System.out.println("Conexion del puerto " + getNombrePuerto() + " realizada correctamente");
                            estadoConexion = true;
                        }
                        else
                        {
                            System.out.println("Error: El puerto serie "+portId.getName()+" ya esta en uso");
                        }
                    }
                    else
                    {
                        System.out.println("Error: No se encuentra el puerto "+getNombrePuerto());
                    }
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println("Error: Imposible abrir el puerto "+getNombrePuerto());
            e.printStackTrace();
            setPuerto(null);
            setFlujoEntrada(null);
            setFlujoSalida(null);
        }
    }

    public void cerrarPuerto()
    {
        // Si el puerto esta abierto es que es distinto de null
        if ( puerto != null)
        {
            try
            {
                flujoEntrada.close();   //Cerramos el flujo de entrada
                flujoSalida.close();  //Cerramos el flujo de salida
                puerto.removeEventListener(); //Eliminamos el escuchador de eventos
                puerto.close();        //Cerramos el puerto
                System.out.println("Desconexion del puerto "+getNombrePuerto()+" realizada correctamente");
            }
            catch (IOException e)
            {
                System.out.println("Error: No se ha cerrado la conexion correctamente");
                e.printStackTrace();
            }
        }
    }

    synchronized public void serialEvent(SerialPortEvent serialEvent)
    {
        try
        {   //Comprobamos el tipo de evento del puerto serie
            if ( serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE )
            {
                // Comprobamos si hay datos disponibles antes de leerlos
                if ( flujoEntrada.available() > 0 )
                {
                    datosRecibidos = (char) flujoEntrada.read();
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error: No se ha podido leer datos del puerto serie");
            e.printStackTrace();
        }
    }

    public void enviarByte(char caracter)
    {
        try
        {
            flujoSalida.write(caracter);
        }
        catch (IOException e)
        {
            System.out.println("Error: No se ha enviado '"+caracter+"' correctamente");
            e.printStackTrace();
        }
    }

    public void enviarByte(int numero)
    {
        try
        {
            flujoSalida.write(numero);
        }
        catch (IOException e)
        {
            System.out.println("Error: No se ha enviado '"+numero+"' correctamente");
            e.printStackTrace();
        }
    }



    public void enviarString(String cadena)
    {
        try
        {
            flujoSalida.write(cadena.getBytes());
        }
        catch (IOException e)
        {
            System.out.println("Error: No se ha enviado '"+cadena+"' correctamente");
            e.printStackTrace();
        }
    }
}