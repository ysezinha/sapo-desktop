package com.dati.image;
import java.awt.Point;
//import java.io.Serializable;

/*
 * DistanciaMedida.java
 *
 * Created on 13 de Julho de 2005, 17:34
 */

/**
 *
 * @author Anderson Zanardi de Freitas
 */

public class DistanciaMedida /*implements Serializable*/{
    public Point p[] = new Point[2];
    public String nome = new String();
    public double distanciaMedida;
    public String generico = new String(); // designação genérica
    public boolean apresentaDist;
}