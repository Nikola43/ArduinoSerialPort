package com.paulo;

public interface ArduinoSerialPort
{
    /*
    PROPIEDADES
        BASICAS
            String ruta | consultable y modificable
        DERIVADAS
            String nombre | consultable
        COMPARTIDAS
            -
*/

//------------------------------- METODOS CONSULTORES ----------------------------------------//

//------------------------------- FIN METODOS CONSULTORES ------------------------------------//

//------------------------------- METODOS MODIFICADORES --------------------------------------//

//------------------------------- FIN METODOS MODIFICADORES ----------------------------------//

//------------------------------- METODOS AÑADIDOS -------------------------------------------//
    String[] extraerMetadatos();
//------------------------------- FIN METODOS AÑADIDOS ---------------------------------------//
}