/**
 * Edison Puig Maldonado
 */

class DeslocaParaVerComponente extends java.awt.event.FocusAdapter {
    
	public void focusGained(java.awt.event.FocusEvent e) {
        java.awt.Component comp = e.getComponent();
        java.awt.Rectangle rect = comp.getBounds();
        rect.setBounds((int)rect.getMinX(),(int)rect.getMinY(),(int)rect.getWidth(),(int)rect.getHeight()+20);
        java.awt.Component parent = (javax.swing.JPanel)comp.getParent();
        javax.swing.JViewport jvp;
        if (parent.getParent() instanceof javax.swing.JScrollPane)
            jvp = (javax.swing.JViewport)parent;
        else if (parent.getParent().getParent() instanceof javax.swing.JScrollPane)
            jvp = (javax.swing.JViewport)parent.getParent();
        else if (parent.getParent().getParent().getParent() instanceof javax.swing.JScrollPane)
            jvp = (javax.swing.JViewport)parent.getParent().getParent(); 
        else jvp = new javax.swing.JViewport();
        java.awt.Rectangle rectSpn = jvp.getViewRect();
        if(!rectSpn.contains(rect)) {
            java.awt.Point p = new java.awt.Point(0,rect.y-5);
            jvp.setViewPosition(p);
        }
    }
}