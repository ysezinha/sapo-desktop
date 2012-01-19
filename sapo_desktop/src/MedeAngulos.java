import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import java.awt.Point;
/*
 * Created on 02/11/2004
 * @author Anderson Zanardi de Freitas
 */


public class MedeAngulos extends LeJAIPanel{
    
    double anguloMedido;
    
    /**
     * @param sapo
     */
    MedeAngulos(SAPO obj) {
            super(obj);
            // TODO Auto-generated constructor stub
    }

    int index = -1;
    int x, y;
    int contPonto = 0;
    String message = "";
    
    private boolean wizard = false;
    
    public void mousePressed(MouseEvent e) {
        index = -1;
        if(e.getButton()!=MouseEvent.BUTTON3){
        x = e.getX();
        y = e.getY();
        Point[] ptAngulos = sapo.jif[sapo.numImg].jaiP.getptAngulos();
        
        switch(sapo.jif[sapo.numImg].jaiP.anguloTipo){
            case JAIPanelSAPO.ANGULO_HORIZONTAL :{
                for(int i=0; i<2; i++)
                    if(((x<=ptAngulos[i].x+6)&&(x>=ptAngulos[i].x-6))&&((y<=ptAngulos[i].y+6)&&(y>=ptAngulos[i].y-6)))
                        index = i;
            } //case 1
            break;
            case JAIPanelSAPO.ANGULO_VERTICAL :
                for(int i=0; i<2; i++)
                    if(((x<=ptAngulos[i].x+6)&&(x>=ptAngulos[i].x-6))&&((y<=ptAngulos[i].y+6)&&(y>=ptAngulos[i].y-6)))
                        index = i;
                break;
            case JAIPanelSAPO.ANGULO_TRES_PONTOS :{
                for(int i=0; i<4; i++)
                    if(((x<=ptAngulos[i].x+6)&&(x>=ptAngulos[i].x-6))&&((y<=ptAngulos[i].y+6)&&(y>=ptAngulos[i].y-6)))
                        index = i;
            } //case 3
            break;
            case JAIPanelSAPO.ANGULO_QUATRO_PONTOS :{
                for(int i=0; i<4; i++)
                    if(((x<=ptAngulos[i].x+6)&&(x>=ptAngulos[i].x-6))&&((y<=ptAngulos[i].y+6)&&(y>=ptAngulos[i].y-6)))
                        index = i;
            } //case 4
            break;
            default: sapo.jif[sapo.numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_INDEFINIDO;
        } //switch
        } //if
    }
    
    public void mouseReleased(MouseEvent e) { }
    
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        Point[] ptAngulos = sapo.jif[sapo.numImg].jaiP.getptAngulos();
        
        if(index!=-1){
            ptAngulos[index].x = x;
            ptAngulos[index].y = y;
            sapo.jif[sapo.numImg].jaiP.setptAngulos(ptAngulos);
            anguloMedido = Math.abs(sapo.jif[sapo.numImg].jaiP.getAngulo());
            sapo.jpAngulos.jTxtAngulo.setText(sapo.numFormat.format(anguloMedido));
        }
    } //mouseDragged
    
    public void mouseClicked(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON3) sapo.jif[sapo.numImg].jaiP.inverteAngulo();
        else{
            x = e.getX();
            y = e.getY();
            Point[] ptAngulos = sapo.jif[sapo.numImg].jaiP.getptAngulos();
            wizard = sapo.jpAngulos.jchkBoxWizardAngulo.isSelected();

            if(!wizard) setAngulosInicial();
            else{
                ptAngulos[contPonto].x = x;
                ptAngulos[contPonto].y = y;
                switch(sapo.jif[sapo.numImg].jaiP.anguloTipo){
                case JAIPanelSAPO.ANGULO_HORIZONTAL :{
                    contPonto = 0;
                    message = "Pontos selecionados. \n Ok!";
                    ptAngulos[1].x = x+40;
                    ptAngulos[1].y = y;
                } //case 1
                break;
                case JAIPanelSAPO.ANGULO_VERTICAL :{
                    contPonto = 0;
                    ptAngulos[1].x = x;
                    ptAngulos[1].y = y+40; 
                    message = "Pontos selecionados. \n Ok!";
                } //case 2
                break;
                case JAIPanelSAPO.ANGULO_TRES_PONTOS :{
                    if(contPonto==0){
                        ptAngulos[1].x = ptAngulos[2].x = x;
                        ptAngulos[1].y = ptAngulos[2].y = y;
                    }
                    if(contPonto<=2){
                        contPonto++;
                        message = "Ponto: "+contPonto+" selecionado. \n Click no próximo ponto!";
                    }
                    if (contPonto>2){
                        contPonto = 0;
                        message = "Pontos selecionados. \n Ok! Click nos pontos e arraste";
                    }
                } //case3
                break;
                case JAIPanelSAPO.ANGULO_QUATRO_PONTOS :{
                    if(contPonto==0){
                        ptAngulos[1].x = ptAngulos[2].x = ptAngulos[3].x = x;
                        ptAngulos[1].y = ptAngulos[2].y = ptAngulos[3].y = y;
                    }
                    if(contPonto<=3){
                        contPonto++;
                        message = "Ponto: "+contPonto+" selecionado. \n Click no próximo ponto!";
                    }    
                    if(contPonto>3){
                        contPonto = 0;
                        message = "Pontos selecionados. \n Ok! Click nos pontos e arraste";
                    }
                } //case4
                break;
                default: sapo.jif[sapo.numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_INDEFINIDO;
                }//switch    
                JOptionPane.showMessageDialog(sapo, message);
                //sapo.jif[sapo.numImg].jaiP.setptAngulos(ptAngulos);
            } //else
        }// else
        Point[] ptAngulos = sapo.jif[sapo.numImg].jaiP.getptAngulos();
        sapo.jif[sapo.numImg].jaiP.setptAngulos(ptAngulos);
        anguloMedido = Math.abs(sapo.jif[sapo.numImg].jaiP.getAngulo());
        sapo.jpAngulos.jTxtAngulo.setText(sapo.numFormat.format(anguloMedido));
    }
    
    public void setAngulosInicial() {
        Point[] ptAngulos = sapo.jif[sapo.numImg].jaiP.getptAngulos();
        switch(sapo.jif[sapo.numImg].jaiP.anguloTipo){
            case JAIPanelSAPO.ANGULO_HORIZONTAL :{
                ptAngulos[0].x = x;
                ptAngulos[0].y = y;
                ptAngulos[1].x = x+40;
                ptAngulos[1].y = y;
            } //case 1
            break;
            case JAIPanelSAPO.ANGULO_VERTICAL :{
                ptAngulos[0].x = x;
                ptAngulos[0].y = y;
                ptAngulos[1].x = x;
                ptAngulos[1].y = y+40;
            } //case 2
            break;
            case JAIPanelSAPO.ANGULO_TRES_PONTOS :{
                ptAngulos[0].x = x;
                ptAngulos[0].y = y;
                ptAngulos[1].x = x+40;
                ptAngulos[1].y = y;
                ptAngulos[2].x = x+40;
                ptAngulos[2].y = y-40;
            } //case3
            break;
            case JAIPanelSAPO.ANGULO_QUATRO_PONTOS :{
                ptAngulos[0].x = x;
                ptAngulos[0].y = y;
                ptAngulos[1].x = x+40;
                ptAngulos[1].y = y;
                ptAngulos[2].x = x;
                ptAngulos[2].y = y-40;
                ptAngulos[3].x = x+40;
                ptAngulos[3].y = y-40;
            } //case4
            break;
            default: {
                ptAngulos = new Point[5];
                for (int i=0; i< 5; i++){ ptAngulos[i] = new Point(0,0);}
                sapo.jif[sapo.numImg].jaiP.anguloTipo = JAIPanelSAPO.ANGULO_INDEFINIDO;}
        }    
        sapo.jif[sapo.numImg].jaiP.setptAngulos(ptAngulos);
    }
    
 //mouseClicked
    
}