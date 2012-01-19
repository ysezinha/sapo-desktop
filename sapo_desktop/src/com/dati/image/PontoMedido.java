package com.dati.image;
import java.awt.Point;
//import java.io.Serializable;

/*
 * Created on 27/01/2005 - Puig
 *
 * Esta classe contém as coordenadas, denominação,
 * designação genérica e outras informações do ponto medido.
 * 
 */


public class PontoMedido /*implements Serializable*/ {
    public Point p = new Point();
    public String nome = new String();
    public String generico = new String(); // designação genérica
    public boolean apresenta;
}