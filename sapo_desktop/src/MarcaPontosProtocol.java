/*
 * MarcaPontosProtocol.java
 *
 * Created on 9 de Outubro de 2005, 13:08
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import com.dati.image.PontoMedido;

/**
 *
 * @author Anderson Zanardi de Freitas 
 */
public class MarcaPontosProtocol extends LeJAIPanel{
    
    /** Creates a new instance of MarcaPontosProtocol */
    public MarcaPontosProtocol(SAPO obj) {
        super(obj);
    }
    
    private int x, y;
    public int index;
    
    public void mousePressed(MouseEvent e) {
        if (e.getButton()!=MouseEvent.BUTTON2){
            tiraZoom(e.getPoint());
            formataIndex();
        }
    } //mousePressed

    
    public void mouseDragged(MouseEvent e) {
        if (e.getButton()!=MouseEvent.BUTTON2){
            tiraZoom(e.getPoint());
            ImageData imgData = sapo.paciente.dados.imgData[sapo.numImg];
            if( index != -1 ) {
                    imgData.setPoint(x, y, index);
                    sapo.jpMarca.atualizar();
                sapo.paciente.salvarAlteracoes = true;
            }
        }
    } //mouseDragged
    
    public void mouseReleased(MouseEvent e) {
    	if (e.getButton()!=MouseEvent.BUTTON2){
    		tiraZoom(e.getPoint());
    		ImageData imgData = sapo.paciente.dados.imgData[sapo.numImg];  
    		if(e.getButton()!=MouseEvent.BUTTON3){
    			if ((index == -1)) { // indica que é um novo ponto na imagem
    				if (adicionaPonto) {
    					javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)sapo.jpMarca.jTableMarcaPontos.getModel();
    					a.addRow(new Object[]{null});
    					String nomePonto = sapo.jpMarcaPontosProtocol.getSelectedLabel();
    					imgData.addPoint(x, y, nomePonto, true);
    					sapo.jpMarcaPontosProtocol.segueProtocol(true);
    				}
    			}
    			else
    				imgData.setPoint(x, y, index);
    			sapo.jpMarca.atualizaTabela();
    			sapo.paciente.salvarAlteracoes = true;
    		}
    		else{
    			if(index != -1) {
    				sapo.jpMarcaPontosProtocol.jmItemApagar.setEnabled(false); // confusa esta ação de apagar durante o protocolo!
    				sapo.jpMarcaPontosProtocol.jmItemPular.setEnabled(false); // confusa esta ação de pular durante o protocolo!
    			}
    			else {
    				sapo.jpMarcaPontosProtocol.jmItemApagar.setEnabled(false);
        			sapo.jpMarcaPontosProtocol.jmItemPular.setEnabled(false); // confusa esta ação de pular durante o protocolo!
    			}
    			sapo.jpMarcaPontosProtocol.jpopupMenuProtocol.show(e.getComponent(),e.getX(),e.getY());
    		}
    	}
    }
    
    private void tiraZoom(Point p){
        float zoom = sapo.jif[sapo.numImg].zoom / 100.0F;
        x = Math.round((int)p.getX()/zoom);
        y = Math.round((int)p.getY()/zoom);        
    }

    
    private void formataIndex(){
        ArrayList pontos = sapo.paciente.dados.imgData[sapo.numImg].getPontos(); // <PontoMedido>
        index = -1;
        for(int i=0; i<pontos.size(); i++){
            Point p = ((PontoMedido)pontos.get(i)).p;
            if(((x <= p.x+6 )&&( x >= p.x-6 ))&&(( y <= p.y+6 )&&( y >= p.y-6 )))
            index = i;
        } 
    }
    
    public void keyPressed(KeyEvent e){
        //System.out.println("XXX");
    }

}


