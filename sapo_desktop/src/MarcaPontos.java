import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import com.dati.image.PontoMedido;

/*
 * Created on 02/11/2004
 *
 */

/**
 *
 * @author  Edison Puig Maldonado
 * @author  Anderson Zanardi de Freitas
 */

class MarcaPontos extends LeJAIPanel {
    
    /**
     * @param sapo
     */
    MarcaPontos(SAPO obj) {
            super(obj);
	}

    private int x, y, index;
   
    
    public void mousePressed(MouseEvent e) {
        tiraZoom(e.getPoint());
        formataIndex();
    } //mousePressed

    
    public void mouseDragged(MouseEvent e) {
        tiraZoom(e.getPoint());
        ImageData imgData = sapo.paciente.dados.imgData[sapo.numImg];
        if( index != -1 ) {
                imgData.setPoint(x, y, index);
                sapo.jpMarca.atualizar();
            sapo.jpMarca.selectRow(index);    
            sapo.paciente.salvarAlteracoes = true;
        }
    } //mouseDragged
 
    
    public void mouseReleased(MouseEvent e) {
        if(e.getButton()!=MouseEvent.BUTTON2){
            tiraZoom(e.getPoint());
            ImageData imgData = sapo.paciente.dados.imgData[sapo.numImg];  
            if(e.getButton()!=MouseEvent.BUTTON3){
                if( index == -1 ){// indica que é um novo ponto na imagem
                    javax.swing.table.DefaultTableModel a = (javax.swing.table.DefaultTableModel)sapo.jpMarca.jTableMarcaPontos.getModel();
                    a.addRow(new Object[]{null});   
                    imgData.addPoint(x, y, " " + a.getRowCount(), true);
                    }
                else
                    imgData.setPoint(x, y, index);
                sapo.jpMarca.atualizaTabela();
                sapo.paciente.salvarAlteracoes = true;
            }
            else{
                if(index != -1)
                    sapo.jpMarca.jmItemApagar.setEnabled(true);
                else
                    sapo.jpMarca.jmItemApagar.setEnabled(false);
                sapo.jpMarca.jpopupMenuMarca.show(e.getComponent(),e.getX(),e.getY());
            }
        }
    } //mouseReleased
    
    public int getIndex(){
        return index;
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
    
    
}