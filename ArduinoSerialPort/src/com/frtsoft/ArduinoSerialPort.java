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
    // Atributos
    private SerialPort puerto;          //Definimos el puerto serie
    private String nombrePuerto;        //Nombre del puerto serie (Como lo reconoce el sistema)
    private boolean estadoConexion;     //booleano para saber si esta conectado o no
    private char caracterRecibido;      //Datos recibidos del puerto serie
    private char caracterEnviado;       //Datos enviados al puerto serie
    private InputStream flujoEntrada;   //Permite leer del puerto serie
    private OutputStream flujoSalida;   //Permite escribir en el puerto serie

    //Configuracion de la conexion
    private int baudRate;  // Velocidad de transmision en bits por segundo
    private int dataBits;  // Tamaño de la trama de bits enviados
    private int parity;    //Sin paridad
    private int stopBits;  // Usamos 1 bit control

    //CONTRUCTORES
    // Contructor vacio
    public ArduinoSerialPort()
    {
        puerto  = null;
        nombrePuerto = "COM3";
        estadoConexion = false;
        baudRate = 9600;
        dataBits = 8;
        parity = SerialPort.PARITY_NONE;
        stopBits = SerialPort.STOPBITS_1;
        caracterRecibido = 0;
        caracterEnviado = 0;
        flujoEntrada = null;
        flujoSalida = null;
    }

    // Contructor con parametros
    public ArduinoSerialPort(SerialPort puerto,
                             String nombrePuerto,
                             boolean estadoConexion,
                             int baudRate,
                             int dataBits,
                             int parity,
                             int stopBits,
                             char caracterRecibido,
                             char caracterEnviado,
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
        this.caracterRecibido = caracterRecibido;
        this.caracterEnviado = caracterEnviado;
        this.flujoEntrada = flujoEntrada;
        this.flujoSalida = flujoSalida;
    }

    // Contructor de copia
    public ArduinoSerialPort(ArduinoSerialPort arduinoSerialPort)
    {
        this.puerto = arduinoSerialPort.puerto;
        this.nombrePuerto = arduinoSerialPort.nombrePuerto;
        this.estadoConexion = arduinoSerialPort.estadoConexion;
        this.baudRate = arduinoSerialPort.baudRate;
        this.dataBits = arduinoSerialPort.dataBits;
        this.parity = arduinoSerialPort.parity;
        this.stopBits = arduinoSerialPort.stopBits;
        this.caracterEnviado = arduinoSerialPort.caracterEnviado;
        this.caracterRecibido = arduinoSerialPort.caracterRecibido;
        this.flujoEntrada = arduinoSerialPort.flujoEntrada;
        this.flujoSalida = arduinoSerialPort.flujoSalida;
    }

    //Constructor solo con nombre del puerto y velocidad de transmision
    public ArduinoSerialPort(String nombrePuerto, int baudRate)
    {
        puerto  = null;
        this.nombrePuerto = nombrePuerto;
        estadoConexion = false;
        this.baudRate = baudRate;
        dataBits = 8;
        parity = SerialPort.PARITY_NONE;
        stopBits = SerialPort.STOPBITS_1;
        caracterEnviado = 0;
        caracterRecibido = 0;
        flujoEntrada = null;
        flujoSalida = null;
    }

    //Metodos consultores
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
    public char getCaracterRecibido()
    {
        return caracterRecibido;
    }
    public char getCaracterEnviado()
    {
        return caracterEnviado;
    }
    private InputStream getFlujoEntrada()
    {
        return flujoEntrada;
    }
    private OutputStream getFlujoSalida()
    {
        return flujoSalida;
    }

    //Metodos modificadores
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
    public void setCaracterEnviado(char caracterEnviado)
    {
        this.caracterEnviado = caracterEnviado;
    }
    public void setCaracterRecibido(char caracterRecibido)
    {
        this.caracterRecibido = caracterRecibido;
    }
    private void setFlujoEntrada(InputStream flujoEntrada)
    {
        this.flujoEntrada = flujoEntrada;
    }
    private void setFlujoSalida(OutputStream flujoSalida)
    {
        this.flujoSalida = flujoSalida;
    }

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
                puerto.removeEventListener(); //Eliminamos el escuchador de eventos
                puerto.close();        //Cerramos el puerto
                flujoEntrada.close();   //Cerramos el flujo de entrada
                flujoSalida.close();  //Cerramos el flujo de salida
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
                    caracterRecibido = (char) flujoEntrada.read();
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error: No se ha podido leer datos del puerto serie");
            e.printStackTrace();
        }
    }

    public void enviarCaracter(char caracter)
    {
        try
        {
            flujoSalida.write(caracter);
            caracterEnviado = caracter;
        }
        catch (IOException e)
        {
            System.out.println("Error: No se ha enviado '"+caracter+"' correctamente");
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

    // METODOS HEREDADOS
    @Override
    public String toString()
    {
        return (puerto.toString()+", "+nombrePuerto+", "+estadoConexion+", "+baudRate+", "+dataBits+", "+parity+", "+stopBits+", "+caracterEnviado+", "+caracterRecibido+", "+flujoEntrada.toString()+", "+flujoSalida.toString() );
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

    public int compareTo(ArduinoSerialPort p)
    {
        int comparacion;

        if (getNombrePuerto().equals(p.getNombrePuerto()))
        {
            comparacion = 0;
        }
        else
        {
            comparacion = 1;
        }
        return comparacion;
    }
}
