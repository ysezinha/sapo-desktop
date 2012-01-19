/*
 * MedidasAnalise.java
 *
 * Created on 26 de Novembro de 2005, 16:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.awt.Point;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.dati.data.DBConnection;
import com.dati.image.PontoMedido;

/**
 *
 * @author Anderson Zanardi de Freitas
 */
public class MedidasAnalise {
    
	SAPO sapo;
	DBConnection db;
	
    double alfa1, deltaAlfa;
   
    public String[] medidas = {
                    "Alinhamento horizontal da cabeça", //Vista anterior
                    "Alinhamento horizontal dos acrômios",
                    "Alinhamento horizontal das espinhas ilíacas ântero-superiores",
                    "Ângulo entre os dois acrômios e as duas espinhas ilíacas ântero-superiores",
                    "Ângulo frontal do membro inferior direito",
                    "Ângulo frontal do membro inferior esquerdo",
                    "Diferença no comprimento dos membros inferiores (D-E)",
                    "Alinhamento horizontal das tuberosidades das tíbias",
                    "Ângulo Q direito",
                    "Ângulo Q esquerdo",
                    "Assimetria horizontal da escápula em relação à T3", //Vista Posterior
                    "Ângulo perna/retropé direito",
                    "Ângulo perna/retropé esquerdo",
                    "Alinhamento horizontal da cabeça (C7)", //Vista Lateral Direita
                    "Alinhamento vertical da cabeça (acrômio)",
                    "Alinhamento vertical do tronco",
                    "Ângulo do quadril (tronco e coxa)",
                    "Alinhamento vertical do corpo",
                    "Alinhamento horizontal da pélvis",
                    "Ângulo do joelho",
                    "Ângulo do tornozelo",
                    "Alinhamento horizontal da cabeça (C7)", //Vista Lateral Esquerda
                    "Alinhamento vertical da cabeça (acrômio)",
                    "Alinhamento vertical do tronco",
                    "Ângulo do quadril (tronco e coxa)",
                    "Alinhamento vertical do corpo",
                    "Alinhamento horizontal da pélvis",
                    "Ângulo do joelho",
                    "Ângulo do tornozelo",  //fim da vistas
                    "Centro de gravidade frontal X:",
                    "Centro de gravidade frontal Y:",
                    "Centro de gravidade Lateral Direita X:",
                    "Centro de gravidade Lateral Direita Y:",
                    "Centro de gravidade Lateral Esquerda X:",
                    "Centro de gravidade Lateral Esquerda Y:",
                    "Média do centro de gravidade lateral X:",
                    "Média do centro de gravidade lateral Y:",
                    "Assimetria no plano frontal:",
                    "Origem do centro de gravidade X:",
                    "Assimetria no plano sagital:",
                    "Origem do centro de gravidade Y:",
                    "Posição da projeção do CG relativo a posição média dos maléolos (plano frontal):",
                    "Posição da projeção do CG relativo a posição média dos maléolos (plano lateral):"};
                    
    public double valEsp[] = new double[medidas.length];             
    public double valEspTol[] = new double[medidas.length];
    public String unidade[] = new String[medidas.length];
    
    public ArrayList pontosCalculo[] = new ArrayList[medidas.length]; 
    
    double m[] = { 0.0,
                    0.0681,
                    0.5245,
                    0.1447,
                    0.0457,
                    0.0133};
    double r[] = { 0.0,
                    1.0,
                    0.4395,
                    0.3830,
                    0.4440,
                    0.4215};
                    
    double mediaCGX = 0;
    double mediaCGY = 0;
    double cgX = 0;
    double cgY = 0;    
    
    double cgld = 0;
    double cgle = 0;
    double mediaCGL = 0;
    public double distMaleolos = 1E300;
    public double tamPeMedio = 1E300;
    double tamPeDir = 0;
    double tamPeEsq = 0;
    
    /** Creates a new instance of MedidasAnalise 
     * @param sapo TODO*/
    public MedidasAnalise(SAPO sapo) {
    	this.sapo = sapo;
    	this.db = sapo.db;
    	lerValoresEsperados();
    }
  
    private void lerValoresEsperados() {
    	String sqlPreparado = "SELECT * FROM referencias";
		DBConnection.PreparaInsereEnvia pie = db.getPreparaInsereEnvia(sqlPreparado);
		ResultSet rs = pie.executaQuery();
		if ((rs != null))
			try {
				for (int i=0; i<41; i++) {
					rs.next();
					valEsp[i]    = rs.getDouble(2);
					valEspTol[i] = rs.getDouble(3);
					unidade[i]   = rs.getString(4);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

    public double calculaAngulos(Point2D.Double p1, Point2D.Double p2, int anguloTipo){
        alfa1 = 0;
        deltaAlfa = 0;
        if ( anguloTipo == JAIPanelSAPO.ANGULO_HORIZONTAL ) {
            double deltaX = p2.x - p1.x;
            double deltaY = p2.y - p1.y;
            deltaAlfa = Math.toDegrees(Math.atan2(-deltaY,deltaX)); //inversão da tela
        }
        else
            if ( anguloTipo == JAIPanelSAPO.ANGULO_VERTICAL ) {
                alfa1 = -90;
                double deltaX = p2.x - p1.x;
                double deltaY = p2.y - p1.y;
                deltaAlfa = Math.toDegrees(Math.atan2(deltaX,deltaY)); //inversão da tela
            }
        return deltaAlfa;        
    }
    
    public double calculaAngulos(Point2D.Double p2, Point2D.Double p1, Point2D.Double p3, boolean inverteAngulo){
        double deltaX = p2.x - p1.x;
        double deltaY = p2.y - p1.y;
        alfa1 = Math.toDegrees(Math.atan2(-deltaY,deltaX)); //inversão da tela
        deltaX = p3.x - p1.x;
        deltaY = p3.y - p1.y;
        double alfa2 = Math.toDegrees(Math.atan2(-deltaY,deltaX));
        deltaAlfa = alfa2 - alfa1;
        deltaAlfa = (alfa1<alfa2) ? deltaAlfa : (deltaAlfa+360);
        if(inverteAngulo) deltaAlfa = -(360-deltaAlfa);
        return deltaAlfa;
    }
    
    public double calculaAngulos(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double p4, boolean inverteAngulo){
        double deltaX1 = p2.x - p1.x;
        double deltaY1 = p2.y - p1.y;
        double alfa1 = Math.atan2(deltaY1,deltaX1);
        double deltaX2 = p4.x - p3.x;
        double deltaY2 = p4.y - p3.y;
        double alfa2 = Math.atan2(deltaY2,deltaX2);
        double deltaAlfa = - Math.toDegrees((alfa2 - alfa1));
        return deltaAlfa;
    }
    
    public double indiceAssimetria(double m, double n){
        return (m-n)/((m+n)/2)*100;
    }
    
    public double distancia(Point2D.Double p1, Point2D.Double p2){
        double dx = Math.abs(p2.x-p1.x);
        double dy = Math.abs(p2.y-p1.y);
        return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
    }
    
    public double moduloDif(double p1, double p2){
        return Math.abs(p2-p1);
    }
    
    public Point2D.Double cgFrontal(Point2D.Double p1,Point2D.Double p2,Point2D.Double p3,Point2D.Double p4,Point2D.Double p5,Point2D.Double p6,Point2D.Double p7,Point2D.Double p8,Point2D.Double p9,Point2D.Double p10){
        double cgx = (p1.x+p2.x)/2*m[1]*r[1]+(p3.x+p4.x)/2*m[2]*r[2]+(p5.x+p6.x)/2*m[2]*(1-r[2])+(p5.x+p6.x)*m[3]*(1-r[3])+(p7.x+p8.x)*m[3]*r[3]+(p7.x+p8.x)*m[4]*(1-r[4])+(p9.x+p10.x)*m[4]*r[4]+(p9.x+p10.x)*m[5];
        double cgy = (p1.y+p2.y)/2*m[1]*r[1]+(p3.y+p4.y)/2*m[2]*r[2]+(p5.y+p6.y)/2*m[2]*(1-r[2])+(p5.y+p6.y)*m[3]*(1-r[3])+(p7.y+p8.y)*m[3]*r[3]+(p7.y+p8.y)*m[4]*(1-r[4])+(p9.y+p10.y)*m[4]*r[4]+(p9.y+p10.y)*m[5];
        Point2D.Double pt = new Point2D.Double(cgx,cgy);
        return pt;
    }
    
    public Point2D.Double cgLateral(Point2D.Double p1,Point2D.Double p2,Point2D.Double p3,Point2D.Double p4,Point2D.Double p5,Point2D.Double p6){
        double cgx = p1.x*m[1]*r[1]+p2.x*m[2]*r[2]+p3.x*m[2]*(1-r[2])+2*p3.x*m[3]*(1-r[3])+2*p4.x*m[3]*r[3]+2*p4.x*m[4]*(1-r[4])+2*p5.x*m[4]*r[4]+2*p5.x*m[5]*(1-r[5])+2*p6.x*m[5]*r[5];
        double cgy = p1.y*m[1]*r[1]+p2.y*m[2]*r[2]+p3.y*m[2]*(1-r[2])+2*p3.y*m[3]*(1-r[3])+2*p4.y*m[3]*r[3]+2*p4.y*m[4]*(1-r[4])+2*p5.y*m[4]*r[4]+2*p5.y*m[5]*(1-r[5])+2*p6.y*m[5]*r[5];
        Point2D.Double pt = new Point2D.Double(cgx,cgy);
        return pt;
    }
    
    public double[] calculaAnalise(SAPO sapo){
        Point2D.Double pt2d1 = new Point2D.Double();
        Point2D.Double pt2d2 = new Point2D.Double();
        Point2D.Double pt2d3 = new Point2D.Double();
        pt2d1= pt2d2 = pt2d3 = null;
        Point2D.Double pt1ldir = new Point2D.Double();
        Point2D.Double pt2ldir = new Point2D.Double();
        Point2D.Double pt3lesq = new Point2D.Double();
        Point2D.Double pt4lesq = new Point2D.Double();
        
        double[] calculos = new double[medidas.length];
        for (int i=0; i<medidas.length; i++) calculos[i] = 1E300;

        for(int i=0; i<medidas.length; i++) pontosCalculo[i] = new ArrayList();
        
        for (int i=1; i<SAPO.maxImg; i++) {
            if ( sapo.jif[i] == null ) continue;   
                String pict = "";
                java.awt.Image ttt = null;

         //implementa os resultados da análise
         Protocolo proto = new Protocolo();
         String str = sapo.paciente.dados.imgData[i].getVista();
         ArrayList valores2 = sapo.paciente.dados.imgData[i].getPontos();
         
         double escalaX = sapo.paciente.dados.imgData[i].getEscalaX();
         double escalaY = sapo.paciente.dados.imgData[i].getEscalaY();
         ArrayList valores = new ArrayList();
         for (int m=0; m<valores2.size(); m++) {
            Point pt = new Point(((PontoMedido)valores2.get(m)).p);
            Point2D.Double pp = new Point2D.Double(pt.x*escalaX, pt.y*escalaY);
            valores.add(pp);
         }
        
         Point2D.Double pt1 = new Point2D.Double();
         Point2D.Double pt2 = new Point2D.Double();
         Point2D.Double pt3 = new Point2D.Double();
         Point2D.Double pt4 = new Point2D.Double();
         Point2D.Double pt5 = new Point2D.Double();
         Point2D.Double pt6 = new Point2D.Double();
         Point2D.Double pt7 = new Point2D.Double();
         Point2D.Double pt8 = new Point2D.Double();
         Point2D.Double pt9 = new Point2D.Double();
         Point2D.Double pt10 = new Point2D.Double();
         
         pt1 = null; pt2 = null; pt3 = null; pt4 = null; pt5 = null;
         pt6 = null; pt7 = null; pt8 = null; pt9 = null; pt10 = null; 
         
         String medida = "";
         double result = 1E300;
         String resultStr = "";
         if(str.equalsIgnoreCase("Anterior")){
                //cabeça
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[1]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[2]))
                        pt2 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)){
                    result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                    calculos[0] = result;
                    pontosCalculo[0].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[0].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                }
                //fim cabeça
                //tronco
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                //tronco
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[4]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[5]))
                        pt2 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)){
                    result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                    calculos[1] = result;
                    pontosCalculo[1].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[1].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
               }
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[11]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[12]))
                        pt2 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)){
                    result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                    calculos[2] = result;
                    pontosCalculo[2].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[2].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
               }
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[4]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[5]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[11]))
                        pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[12]))
                        pt4 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                    result = calculaAngulos(pt1, pt2, pt3, pt4, true);
                    calculos[3] = result;
                    pontosCalculo[3].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[3].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[3].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                    pontosCalculo[3].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
                }
              //fim tronco
              //Membros inferiores  
              pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[13]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[15]))
                        pt2 = pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[21]))
                        pt4 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                    result = calculaAngulos(pt1, pt2, pt3, pt4, false);
                    calculos[4] = result;
                    pontosCalculo[4].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[4].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[4].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                    pontosCalculo[4].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
              }
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[14]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[18]))
                        pt2 = pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[24]))
                        pt4 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                    result = -calculaAngulos(pt1, pt2, pt3, pt4, false);
                    calculos[5] = result;
                    pontosCalculo[5].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[5].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[5].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                    pontosCalculo[5].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
                }
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[11]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[22]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[12]))
                        pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[25]))
                        pt4 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                    double mm = distancia(pt1,pt2);
                    double nn = distancia(pt3,pt4);
                    //result = indiceAssimetria(mm,nn);
                    result = mm - nn; // vamos utilizar diferença do comprimento dos membros
                    calculos[6] = result;
                    pontosCalculo[6].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[6].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[6].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                    pontosCalculo[6].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
               }    
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[17]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[20]))
                        pt2 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)){
                    result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                    calculos[7] = result;
                    pontosCalculo[7].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[7].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
               }
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[11]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[16]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[17]))
                        pt3 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)){
                    result = -calculaAngulos(pt1, pt2, pt2, pt3, true);
                    calculos[8] = result;
                    pontosCalculo[8].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[8].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[8].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[8].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                } //Ângulo Q Direito
                pt1 = null; pt2 = null; pt3 = null; pt4 = null;
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[12]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[19]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[20]))
                        pt3 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)){
                    result = calculaAngulos(pt1, pt2, pt2, pt3, true);
                    calculos[9] = result;
                    pontosCalculo[9].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                    pontosCalculo[9].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[9].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                    pontosCalculo[9].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                } //Ângulo Q Esquerdo
 
                //fim Membros Inferiores
         }//if anterior
          
         if(str.equalsIgnoreCase("Posterior")){
           //Tronco
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.posteriorLabel[6]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[16]))
                    pt2 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[7]))
                    pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[16]))
                    pt4 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                double mm = moduloDif(pt1.x, pt2.x);
                double nn = moduloDif(pt3.x, pt4.x);
                result = indiceAssimetria(mm,nn);
                calculos[10] = result;
                pontosCalculo[10].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[10].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[10].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[10].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
            }    
            //fim Tronco
            //Membros Inferiores
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.posteriorLabel[31]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[34]))
                    pt2 = pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[36]))
                    pt4 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                result = calculaAngulos(pt1,pt2,pt3,pt4,true);
                calculos[11] = result;
                pontosCalculo[11].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[11].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[11].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[11].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
           }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.posteriorLabel[32]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[38]))
                    pt2 = pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.posteriorLabel[40]))
                    pt4 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                result = -calculaAngulos(pt1,pt2,pt3,pt4,false);
                calculos[12] = result;
                pontosCalculo[12].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[12].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[12].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[12].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
            }
            //fim Membros Inferiores
         }//if Posterior
     
         //Vista Lateral Direita
         if(str.equalsIgnoreCase("Lateral Direita")){
            //Cabeça
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[7]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[1]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                calculos[13] = result;
                pontosCalculo[13].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[13].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
            }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
           for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[1]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[4]))
                    pt2 = (Point2D.Double)valores.get(m);
            }
            if((pt1!=null)&&(pt2!=null)){
                result = -calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_VERTICAL);
                calculos[14] = result;
                pontosCalculo[14].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[14].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
            }
            //fim Cabeça
            //Tronco
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[4]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[22]))
                    pt2 = (Point2D.Double)valores.get(m);
            }
            if((pt1!=null)&&(pt2!=null)){
                result = -calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_VERTICAL);
                calculos[15] = result;
                pontosCalculo[15].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[15].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
            }
            //Ângulo do quadril:            
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[4]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[22]))
                    pt2 = pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[23]))
                    pt4 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                result = calculaAngulos(pt1,pt2,pt3,pt4,false);
                calculos[16] = result;
                pontosCalculo[16].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[16].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[16].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[16].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
           }
            //Ângulo do corpo (acrômio/maléolo) com a vertical:
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[4]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[29]))
                    pt2 = (Point2D.Double)valores.get(m);
            }
            if((pt1!=null)&&(pt2!=null)){
                result = -calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_VERTICAL);
                calculos[17] = result;
                pontosCalculo[17].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[17].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
            }
            //Ângulo das espinhas ilíacas com a horizontal:
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[21]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[20]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                calculos[18] = result;
                pontosCalculo[18].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[18].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
          }
            //fim Tronco
            //Membros Inferiores
            //Ângulo do joelho:
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[22]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[23]))
                    pt2 = pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[29]))
                    pt4 = (Point2D.Double)valores.get(m);
            }
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                result = -calculaAngulos(pt1,pt2,pt3,pt4,false);
                calculos[19] = result;
                pontosCalculo[19].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[19].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[19].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[19].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
         }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latDirLabel[29]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latDirLabel[23]))
                    pt2 = (Point2D.Double)valores.get(m);
            }
            if((pt1!=null)&&(pt2!=null)){
                result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                calculos[20] = result;
                pontosCalculo[20].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[20].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                
            }
            //fim Membros Inferiores
         }//fim Lateral Direita
     
         //Vista Lateral Esquerda
         if(str.equalsIgnoreCase("Lateral Esquerda")){
            //Cabeça
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
           for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[1]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[7]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = -calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                calculos[21] = result;
                pontosCalculo[21].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[21].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                
          }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[1]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[4]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_VERTICAL);
                calculos[22] = result;
                pontosCalculo[22].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[22].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
           }
            //fim Cabeça
            //Tronco
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[4]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[22]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_VERTICAL);
                calculos[23] = result;
                pontosCalculo[23].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[23].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
          }
            //Ângulo do quadril:
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[4]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[22]))
                    pt2 = pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[23]))
                    pt4 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                result = -calculaAngulos(pt1,pt2,pt3,pt4,false);
                calculos[24] = result;
                pontosCalculo[24].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[24].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[24].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[24].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
          }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[4]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[29]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_VERTICAL);
                calculos[25] = result;
                pontosCalculo[25].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[25].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
          }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
           for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[20]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[21]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
               result = -calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                calculos[26] = result;
                pontosCalculo[26].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[26].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
           }
            //fim Tronco
            //Membros Inferiores
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[22]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[23]))
                    pt2 = pt3 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[29]))
                    pt4 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)){
                result = calculaAngulos(pt1,pt2,pt3,pt4,false);
                calculos[27] = result;
                pontosCalculo[27].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[27].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
                pontosCalculo[27].add(new Point2D.Double(pt3.x/escalaX,pt3.y/escalaY));
                pontosCalculo[27].add(new Point2D.Double(pt4.x/escalaX,pt4.y/escalaY));
           }
            pt1 = null; pt2 = null; pt3 = null; pt4 = null;
            for (int m=0; m<valores.size(); m++) {
                String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                if(nomeTemp.equals(proto.latEsqLabel[23]))
                    pt1 = (Point2D.Double)valores.get(m);
                if(nomeTemp.equals(proto.latEsqLabel[29]))
                    pt2 = (Point2D.Double)valores.get(m);
            } //for m
            if((pt1!=null)&&(pt2!=null)){
                result = -calculaAngulos(pt1,pt2,JAIPanelSAPO.ANGULO_HORIZONTAL);
                calculos[28] = result;
                pontosCalculo[28].add(new Point2D.Double(pt1.x/escalaX,pt1.y/escalaY));
                pontosCalculo[28].add(new Point2D.Double(pt2.x/escalaX,pt2.y/escalaY));
           }
            //fim Membros Inferiores
         }//fim Lateral Esquerda
         
         //Estimativa do Centro de Gravidade
         pt1 = null; pt2 = null; pt3 = null; pt4 =null; pt5 = null;
         pt6 = null; pt7 = null; pt8 = null; pt9 =null; pt10 = null;
         //pt2d1 = pt2d2 = pt2d3 = null;
         if(str.equalsIgnoreCase("Anterior")){
             for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[1]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[2]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[4]))
                        pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[5]))
                        pt4 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[13]))
                        pt5 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[14]))
                        pt6 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[15]))
                        pt7 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[18]))
                        pt8 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[21]))
                        pt9 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[24]))
                        pt10 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)&&(pt5!=null)&&(pt6!=null)&&(pt7!=null)&&(pt8!=null)&&(pt9!=null)&&(pt10!=null)){
                    pt2d1 = cgFrontal(pt1,pt2,pt3,pt4,pt5,pt6,pt7,pt8,pt9,pt10);
                    calculos[29] = pt2d1.getX();
                    calculos[30] = pt2d1.getY();
                    calculos[41] = - (pt2d1.getX()- (pt9.x+pt10.x)/2);
               }
            }//if Anterior
            pt1 = null; pt2 = null; pt3 = null; pt4 =null; pt5 = null;
            pt6 = null; pt7 = null; pt8 = null; pt9 =null; pt10 = null;
            if(str.equalsIgnoreCase("Lateral Direita")){
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.latDirLabel[1]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latDirLabel[4]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latDirLabel[22]))
                        pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latDirLabel[23]))
                        pt4 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latDirLabel[29]))
                        pt5 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latDirLabel[30]))
                        pt6 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)&&(pt5!=null)&&(pt6!=null)){
                    pt2d2 = cgLateral(pt1,pt2,pt3,pt4,pt5,pt6);
                    calculos[31] = pt2d2.getX();
                    calculos[32] = pt2d2.getY();
                    cgld = pt2d2.getX() - pt5.x;
                    tamPeDir = Math.abs(pt5.x-pt6.x);
                }
            }// Lateral Direita
            pt1 = null; pt2 = null; pt3 = null; pt4 =null; pt5 = null;
            pt6 = null; pt7 = null; pt8 = null; pt9 =null; pt10 = null;
            if(str.equalsIgnoreCase("Lateral Esquerda")){
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.latEsqLabel[1]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[4]))
                        pt2 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[22]))
                        pt3 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[23]))
                        pt4 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[29]))
                        pt5 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[30]))
                        pt6 = (Point2D.Double)valores.get(m);    
               } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt3!=null)&&(pt4!=null)&&(pt5!=null)&&(pt6!=null)){
                        pt2d3 = cgLateral(pt1,pt2,pt3,pt4,pt5,pt6);
                        calculos[33] = pt2d3.getX();
                        calculos[34] = pt2d3.getY();
                        cgle = -(pt2d3.getX() - pt5.x);
                        tamPeEsq = Math.abs(pt5.x-pt6.x);
               }
            }// Lateral Esquerda            
            //Media lateral do CG
            if((pt2d2!=null)&&(pt2d3!=null)){
                mediaCGX = (pt2d2.getX()+pt2d3.getX())/2; 
                calculos[35] = mediaCGX;
                mediaCGY = (pt2d2.getY()+pt2d3.getY())/2;
                calculos[36] = mediaCGY;
                mediaCGL = (cgld+cgle)/2;
                calculos[42] = mediaCGL;
            } else if((pt2d2!=null)){
                mediaCGX = pt2d2.getX(); 
                calculos[35] = mediaCGX;
                mediaCGY = pt2d2.getY();
                calculos[36] = mediaCGY;
                mediaCGL = cgld;
                calculos[42] = mediaCGL;
            } else if((pt2d3!=null)){
                mediaCGX = pt2d3.getX(); 
                calculos[35] = mediaCGX;
                mediaCGY = pt2d3.getY();
                calculos[36] = mediaCGY;
                mediaCGL = cgle;
                calculos[42] = mediaCGL;
            }
            if(str.equalsIgnoreCase("Anterior")){
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.frenteLabel[21]))
                        pt1 = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.frenteLabel[24]))
                        pt2 = (Point2D.Double)valores.get(m);
                } //for m
                if((pt1!=null)&&(pt2!=null)&&(pt2d1!=null)){
                    result = indiceAssimetria((pt1.x+pt2.x)/2,pt2d1.getX());
                    //calculos[37] = result;
                    cgX = (pt1.x+pt2.x)/2;
                    distMaleolos = Math.abs(pt1.x-pt2.x);
                    calculos[38] = cgX;
                    double lb = Math.abs(pt1.x-pt2.x);
                    calculos[37] = -(pt2d1.getX()-cgX)/(lb/2)*100;//- (cga(1)-pma)/(lb/2)*100
                }
            } //if Anterior
            pt1 = pt2 = pt3 = pt4 = null;
            if(str.equalsIgnoreCase("Lateral Direita")||str.equalsIgnoreCase("Lateral Esquerda")){
                for (int m=0; m<valores.size(); m++) {
                    String nomeTemp = ((PontoMedido)valores2.get(m)).nome;
                    if(nomeTemp.equals(proto.latDirLabel[29]))
                        pt1ldir = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latDirLabel[30]))
                        pt2ldir = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[29]))
                        pt3lesq = (Point2D.Double)valores.get(m);
                    if(nomeTemp.equals(proto.latEsqLabel[30]))
                        pt4lesq = (Point2D.Double)valores.get(m);
                } //for m
                //Este if abaixo não funciona (é sempre 1) para o caso de haver apenas uma vista lateral
                //if((pt1ldir!=null)&&(pt2ldir!=null)&&(pt3lesq!=null)&&(pt4lesq!=null)){
                if((pt2d2!=null)&&(pt2d3!=null)){
                    //Origem do centro de gravidade
                    cgY = (pt1ldir.x+pt3lesq.x)/2;
                    calculos[40] = cgY;
                    double cp = ((pt2ldir.x - pt1ldir.x) - (pt4lesq.x - pt3lesq.x))/2;
                    calculos[39] = (mediaCGL/cp)*100;
                    tamPeMedio = (tamPeDir+tamPeEsq)/2;
                } else if(pt2d2!=null){
                    cgY = pt1ldir.x;
                    calculos[40] = cgY;                
                    double cp = (pt2ldir.x - pt1ldir.x);
                    calculos[39] = (mediaCGL/cp)*100;
                    tamPeMedio = tamPeDir;
                } else if(pt2d3!=null){
                    cgY = pt3lesq.x;
                    calculos[40] = cgY;              
                    double cp = - (pt4lesq.x - pt3lesq.x);
                    calculos[39] = (mediaCGL/cp)*100;
                    tamPeMedio = tamPeEsq;
                }
            } //if Lateral        
        //fim Estimativa do Centro de Gravidade
        } //i #image
        
        return calculos;
    } //fim calculos

}//fim MedidaAnálise

