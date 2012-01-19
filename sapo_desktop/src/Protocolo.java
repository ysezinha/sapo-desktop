/*
 * Protocolo.java
 *
 * Created on 13 de Novembro de 2005, 15:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Anderson Zanardi de Freitas
 */



public class Protocolo {
    public String frenteLabel[] = {"Glabela",
                            "Trago direito",
                            "Trago esquerdo",
                            "Mento",
                            "Acrômio direito",
                            "Acrômio esquerdo",
                            "Manúbrio do esterno",
                            "Epicôndilo lateral direito",
                            "Epicôndilo lateral esquerdo",
                            "Ponto médio entre o processo estilóide do rádio e a cabeça da ulna direita",
                            "Ponto médio entre o processo estilóide do rádio e a cabeça da ulna esquerda",
                            "Espinha ilíaca ântero-superior direita",
                            "Espinha ilíaca ântero-superior esquerda",
                            "Trocânter maior do fêmur direito ",
                            "Trocânter maior do fêmur esquerdo",
                            "Linha articular do joelho direito",
                            "Ponto medial da patela direita",
                            "Tuberosidade da tíbia direita",
                            "Linha articular do joelho esquerdo",
                            "Ponto medial da patela esquerda",
                            "Tuberosidade da tíbia esquerda",
                            "Maléolo lateral direito",
                            "Maléolo medial direito",
                            "Ponto entre a cabeça do 2º e 3º metatarso direito",
                            "Maléolo lateral esquerdo",
                            "Maléolo medial esquerdo",
                            "Ponto entre a cabeça do 2º e 3º metatarso esquerdo"};
       public int frenteX[] = {136,114,163,136,87,192,136,47,231,48,229,102,175,82,195,83,104,100,195,173,178,96,120,101,184,157,170};
       public int frenteY[] = {30,50,50,74,99,98,111,196,194,266,265,243,243,285,285,437,438,459,438,439,458,569,566,596,569,565,598};
                             
       public String latEsqLabel[] = {"Glabela",
                            "Trago esquerdo",
                            "Mento",
                            "Manúbrio do esterno",
                            "Acrômio esquerdo",
                            "Epicôndilo lateral esquerdo",
                            "Ponto médio entre o processo estilóide do rádio e a cabeça da ulna esquerda",
                            "Processo espinhoso C7",
                            "Processo espinhoso T1",
                            "Processo espinhoso T3",
                            "Processo espinhoso T5",
                            "Processo espinhoso T7",
                            "Processo espinhoso T9",
                            "Processo espinhoso T11",
                            "Processo espinhoso T12",
                            "Processo espinhoso L1",
                            "Processo espinhoso L3",
                            "Processo espinhoso L4",
                            "Processo espinhoso L5",
                            "Processo espinhoso S1",
                            "Espinha ilíaca ântero-superior esquerda",
                            "Espinha ilíaca póstero-superior esquerda",
                            "Trocânter maior do fêmur esquerdo",
                            "Linha articular do joelho esquerdo",
                            "Ponto medial da patela esquerda",
                            "Tuberosidade da tíbia",
                            "Ponto sobre a linha média da perna esquerda",
                            "Ponto sobre o tendão de aquiles esquerdo na altura média dos dois maléolos",
                            "Calcâneo esquerdo",
                            "Maléolo lateral esquerdo",
                            "Ponto entre a cabeça do 2º e 3º metatarso esquerdo"};        
        public int latEsqX[] = {94,131,98,120,140,124,52,161,173,186,191,191,188,185,184,182,177,176,176,190,110,181,146,136,110,115,165,170,174,150,95};
        public int latEsqY[] = {35,49,80,111,101,218,278,80,91,108,125,142,161,178,188,198,213,226,239,253,258,251,298,443,447,470,536,593,608,598,608};
                            
        public String latDirLabel[] = {"Glabela",
                            "Trago direito",
                            "Mento",
                            "Manúbrio do esterno",
                            "Acrômio direito",
                            "Epicôndilo lateral direito",
                            "Ponto médio entre o processo estilóide do rádio e a cabeça da ulna direita",
                            "Processo espinhoso C7",
                            "Processo espinhoso T1",
                            "Processo espinhoso T3",
                            "Processo espinhoso T5",
                            "Processo espinhoso T7",
                            "Processo espinhoso T9",
                            "Processo espinhoso T11",
                            "Processo espinhoso T12",
                            "Processo espinhoso L1",
                            "Processo espinhoso L3",
                            "Processo espinhoso L4",
                            "Processo espinhoso L5",
                            "Processo espinhoso S1",
                            "Espinha ilíaca ântero-superior direita",
                            "Espinha ilíaca póstero-superior direita",
                            "Trocânter maior do fêmur direito",
                            "Linha articular do joelho direito",
                            "Ponto medial da patela direita",
                            "Tuberosidade da tíbia",
                            "Ponto sobre a linha média da perna direita",
                            "Ponto sobre o tendão de aquiles direito na altura média dos dois maléolos",
                            "Calcâneo direito",
                            "Maléolo lateral direito",
                            "Ponto entre a cabeça do 2º e 3º metatarso direito"};
        public int latDirX[] = {172,136,169,147,127,143,215,106,94,81,76,76,79,81,83,85,89,91,91,76,157,86,121,131,156,151,101,97,93,116,171};
        public int latDirY[] = {35,49,80,111,101,219,278,80,91,107,125,142,161,178,188,198,213,226,239,253,258,252,299,443,447,470,536,593,608,598,609};
                           
        public String posteriorLabel[] =  {"Trago direito",
                                "Trago esquerdo",
                                "Acrômio direito",
                                "Acrômio esquerdo",
                                "Intersecção entre a margem medial e a espinha da escápula direita",
                                "Intersecção entre a margem medial e a espinha da escápula esquerda",
                                "Ângulo inferior da escápula direito",
                                "Ângulo inferior da escápula esquerdo",
                                "Espinha ilíaca póstero-superior direita",
                                "Espinha ilíaca póstero-superior esquerda",
                                "Epicôndilo lateral direito",
                                "Epicôndilo lateral esquerdo",
                                "Ponto médio entre o processo estilóide do rádio e a cabeça da ulna direita",
                                "Ponto médio entre o processo estilóide do rádio e a cabeça da ulna esquerda",
                                "Processo espinhoso C7",
                                "Processo espinhoso T1",
                                "Processo espinhoso T3",
                                "Processo espinhoso T5",
                                "Processo espinhoso T7",
                                "Processo espinhoso T9",
                                "Processo espinhoso T11",
                                "Processo espinhoso T12",
                                "Processo espinhoso L1",
                                "Processo espinhoso L3",
                                "Processo espinhoso L4",
                                "Processo espinhoso L5",
                                "Processo espinhoso S1",
                                "Trocânter maior do fêmur direito",
                                "Trocânter maior do fêmur esquerdo",
                                "Linha articular do joelho direito",
                                "Linha articular do joelho esquerdo",
                                "Ponto sobre a linha média da perna direita",
                                "Ponto sobre a linha média da perna esquerda",
                                "Maléolo lateral direito",
                                "Ponto sobre o tendão de aquiles direito na altura média dos dois maléolos",
                                "Maléolo medial direito",
                                "Calcâneo direito",
                                "Maléolo lateral esquerdo",
                                "Ponto sobre o tendão de aquiles esquerdo na altura média dos dois maléolos",
                                "Maléolo medial esquerdo",
                                "Calcâneo esquerdo"};                    
        public int posX[] = {169,114,199,86,169,109,164,113,161,119,237,49,225,62,139,139,139,139,139,138,139,138,139,139,138,139,139,200,83,199,84,178,101,186,172,157,175,96,109,125,104};
        public int posY[] = {55,55,99,99,119,119,150,150,250,249,219,219,278,277,79,91,107,124,141,160,178,186,196,212,226,239,253,309,311,435,434,525,525,586,585,583,612,585,583,581,609};
                            
        public String nome = new String();
        public ArrayList frente = new ArrayList();
        public ArrayList latEsq = new ArrayList();
        public ArrayList latDir = new ArrayList();
        public ArrayList poster = new ArrayList();       
        
        public String[] getLabels(String vista) {
        	String[] retorno = {""}; 
        	if (vista.equalsIgnoreCase("anterior")) return frenteLabel;
    		if (vista.equalsIgnoreCase("posterior")) return posteriorLabel;
    		if (vista.equalsIgnoreCase("lateral direita")) return latDirLabel;
    		if (vista.equalsIgnoreCase("lateral esquerda")) return latEsqLabel;
    		else return retorno;
        }
        
}
