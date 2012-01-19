package com.dati.image;
import java.awt.Point;
//import java.io.Serializable;

/*
 * Created on 27/01/2005 - Puig
 *
 * Esta classe contém as coordenadas, denominação,
 * designação genérica, valor do ângulo, tipo do ângulo 
 * e outras informações do ângulo medido.
 * 
 */

public class AnguloMedido /*implements Serializable*/ {
    public Point p[] = new Point[5];
    public String nome = new String();
    public String generico = new String(); // designação genérica
    public double angulo;
    public int tipo;
    public boolean apresenta;
}