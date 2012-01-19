package com.dati.util;
import java.util.*;
import java.text.*;
import java.sql.*;

/*
 * DataHora.java
 *
 * Created on 4 de Fevereiro de 2005, 07:04
 */

/**
 *
 * @author Edison Puig Maldonado
 *
 */

public class DataHora {
    
	GregorianCalendar calendar = new GregorianCalendar(new Locale("pt","BR"));
        
	
	public DataHora() { this(System.currentTimeMillis()); }
	
	public DataHora(java.util.Calendar c) { calendar = (GregorianCalendar)c; }
	
	public DataHora(long nMilliSeconds) { calendar.setTime(new java.util.Date(nMilliSeconds)); }
	
	public DataHora(java.util.Date date) { this(date.getTime()); }
	
	public DataHora(java.sql.Date date) { this(date.getTime()); }
        
	
	public int getDay() { return calendar.get(Calendar.DAY_OF_MONTH); }
	
	public int getMonth() { return calendar.get(Calendar.MONTH)+1; }
	
	public int getYear() { 	return calendar.get(Calendar.YEAR); }
	
	public int getHours() { return calendar.get(Calendar.HOUR_OF_DAY); }
	
	public int getMinutes() { return calendar.get(Calendar.MINUTE); }
	
	public int getSeconds() { return calendar.get(Calendar.SECOND); }
	
	public int getWeekDay() { return calendar.get(Calendar.DAY_OF_WEEK); }
	
	public static String days[] = {"Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado"};
	public static String months[] = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
	
	public static String getWeekDay(int nDay) { return days[nDay-1]; }
	
	public static String getMonthName(int nMonth) { return months[nMonth-1]; }
	
	public String getLongDate() {
		String StrData = DateFormat.getDateInstance(DateFormat.LONG, 
                        new Locale("pt","BR")).format(calendar.getTime()) +
                        " - " + DateFormat.getTimeInstance(DateFormat.MEDIUM, 
                        new Locale("pt","BR")).format(calendar.getTime());
		return StrData;
	}
	
	public String getCurrentLongDate() {
		String date;
		calendar.setTime(new java.util.Date(System.currentTimeMillis()));
		date = String.valueOf(getMonth());
		date += "/";
		date += String.valueOf(getDay());
		date += "/";
		date += String.valueOf(getYear());
		date += " ";
		date += String.valueOf(getHours());
		date += ":";
		date += String.valueOf(getMinutes());
		date += ":";
		date += String.valueOf(getSeconds());
		date += "  ";
		return date;
	}
	
	
	public void set(int nYear,int nMonth,int nDay,int nHour,int nMin,int nSec) {
		calendar.clear();
		calendar.set(nYear,nMonth,nDay,nHour,nMin,nSec);
	}
	
	public String getShortDate() {
		String date;
		date = String.valueOf(getDay()) + "/";
		date += String.valueOf(getMonth()) + "/";
		date += String.valueOf(getYear());
		return date;
	}
	
	public String getWeekDayString() {
		return getWeekDay(getWeekDay());
	}
	
	public String getShortWeekDay() {
		return getWeekDayString().substring(0,3);
	}
		
	public long getMilliSeconds() {
		return calendar.getTime().getTime();
	}
	
	public static String getSQLDate(Timestamp ts) {
		DataHora d = new DataHora(ts.getTime());
		String text;
		text = d.getWeekDayString();
                text += ", " + String.valueOf(d.getDay());
		text += "/" + String.valueOf(d.getMonth());
		text += "/" + String.valueOf(d.getYear());
		text += " " + String.valueOf(d.getHours());
		text += ":" + String.valueOf(d.getMinutes());
		text += ":" + String.valueOf(d.getSeconds());
		return text;
	}
	
	public static Timestamp getDate() {
	    Timestamp nowTimestamp = new Timestamp((new java.util.Date()).getTime());
		return nowTimestamp;
	}
	
}
