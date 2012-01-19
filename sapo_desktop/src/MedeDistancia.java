import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import com.dati.image.DistanciaMedida;
import javax.swing.JOptionPane;

/*
 * MedeDistancia.java
 *
 * Created on 13 de Julho de 2005, 17:28
 */

/**
 *
 * @author Anderson Zanardi de Freitas
 */

public class MedeDistancia extends LeJAIPanel{
   
    /**
     * @param sapo
     */
    public MedeDistancia(SAPO obj) {
        super(obj);
    }
    
    double distanciaMedida;
    int indexI, indexJ;
    Point[] ptMedidas = new Point[2];
    
    public void mousePressed(MouseEvent e) {
        if(e.getButton()!=MouseEvent.BUTTON3){
            formataIndex(tiraZoom(e.getPoint()));
            ptMedidas[0] = tiraZoom(e.getPoint());
        }
    }
    
    public void mouseReleased(MouseEvent e) { }
    
    public void mouseDragged(MouseEvent e) {
        sapo.jif[sapo.numImg].jaiP.setEspLinhaCorLinha(sapo.corDesenho, sapo.espLinha);
        if((indexI!=-1)&&(indexJ!=-1)){
            sapo.paciente.dados.imgData[sapo.numImg].atualizaMedidaPosicao(tiraZoom(e.getPoint()), indexI, indexJ);
            sapo.jif[sapo.numImg].jaiP.atualizaApresentaDistancia(sapo.paciente.dados.imgData[sapo.numImg].distanciaList);
            Point pt[] = ((DistanciaMedida)sapo.paciente.dados.imgData[sapo.numImg].distanciaList.get(indexI)).p;
            double dx = (Math.abs(pt[1].x-pt[0].x))*sapo.paciente.dados.imgData[sapo.numImg].getEscalaX();
            double dy = (Math.abs(pt[1].y-pt[0].y))*sapo.paciente.dados.imgData[sapo.numImg].getEscalaX();
            distanciaMedida = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
            ((DistanciaMedida)sapo.paciente.dados.imgData[sapo.numImg].distanciaList.get(indexI)).distanciaMedida = distanciaMedida;
            sapo.jpMedeDist.atualizar();
        }
        else{
            ptMedidas[1] = tiraZoom(e.getPoint());
            //sapo.jif[sapo.numImg].jaiP.setEspLinhaCorLinha(sapo.corDesenho, sapo.espLinha);
            sapo.jif[sapo.numImg].jaiP.setptDistancia(ptMedidas, true);
            double dx = (Math.abs(ptMedidas[1].x-ptMedidas[0].x))*sapo.paciente.dados.imgData[sapo.numImg].getEscalaX();
            double dy = (Math.abs(ptMedidas[1].y-ptMedidas[0].y))*sapo.paciente.dados.imgData[sapo.numImg].getEscalaX();
            distanciaMedida = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
            sapo.jpMedeDist.jtxtMedeDist.setText(sapo.numFormat.format(distanciaMedida));
        }
        
    } //mouseDragged
    
    public void mouseClicked(MouseEvent e) {
        if(e.getButton()!=MouseEvent.BUTTON3)
            JOptionPane.showMessageDialog(sapo, "Para medir distâncias, clique com o botão esquerdo do mouse e arraste!");
        else
            sapo.jpMedeDist.jpopupMenuDist.show(e.getComponent(),e.getX(),e.getY());
    }//mouseClicked
    
    private void formataIndex(Point pt){
        int x = pt.x;
        int y = pt.y;
        ArrayList medidas = sapo.paciente.dados.imgData[sapo.numImg].getDistanciaMedida();
        indexI = -1;
        indexJ = -1;
        for(int i=0; i<medidas.size(); i++){
            Point p[] = ((DistanciaMedida)medidas.get(i)).p;
            for(int j=0; j<2; j++){
                if(((x <= p[j].x+6 )&&( x >= p[j].x-6 ))&&(( y <= p[j].y+6 )&&( y >= p[j].y-6 ))){
                    indexI = i;
                    indexJ = j;
                }
            }
        }
    } //formataIndex
    
     private Point tiraZoom(Point p){
        float zoom = sapo.jif[sapo.numImg].zoom / 100.0F;
        Point p1 = new Point();
        p1.x = (int)Math.round(p.getX()/zoom);
        p1.y = Math.round((int)p.getY()/zoom);
        sapo.jif[sapo.numImg].jaiP.setZoom(zoom);
        return p1; 
     }
    
}