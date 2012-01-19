/*
 * Created on 09/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.dati.util;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableModel;

/**
 * @author EDISON PUIG MALDONADO
 *
 */

public class DataFormat {

    public static long parseLong(String str) {
    	long value = 0;
    	if (str.length()>0)
    		try {
    			value = Long.parseLong(str);
    		} catch (NumberFormatException e) {
    			e.printStackTrace();
    		}
    	return value;
    }
    
    public static void packColumn(JTable table, int vColIndex, int margin) {
        //TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;
    
        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;
    
        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }
    
        // Add margin
        width += 2*margin;
    
        // Set the width
        col.setPreferredWidth(width);
    }

	
}
