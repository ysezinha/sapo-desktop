/*
 * SAPO.java 
 *
 * Desenvolvimento iniciado em 26 de Janeiro de 2004
 */

/**
 * <p>
 * SAPO - Software para Avaliação Postural, é um trabalho subvencionado pelo Governo do Brasil.
 * É de domínio público e código aberto. Não há copyright.
 * 
 */

/**
 * @author  Edison Puig Maldonado (puig@dati.com.br)
 * @author  Anderson Zanardi de Freitas (azanardi@dati.com.br)
 * 
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseListener;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;

import org.jdesktop.swingworker.SwingWorker;

import com.dati.data.DBConnection;
import com.dati.gui.EscapeDialog;
import com.dati.gui.FormUtilities;
import com.dati.net.BrowserControl;
import com.dati.util.FreeMemory;
import com.dati.util.MemoryLabel;
import com.sun.EFileFilter;
import com.sun.media.jai.codec.FileSeekableStream;
import com.thoughtworks.xstream.XStream;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import com.tomtessier.scrollabledesktop.EmptyDesktopIconUI;
import com.tomtessier.scrollabledesktop.JScrollableDesktopPane;

/**
 * Esta é a classe principal do SAPO - Software para Avaliação Postural.
 * Também consiste em sua janela principal.
 */
public class SAPO extends JFrame {
	
	/** ID de versão da classe serializável SAPO */
	private static final long serialVersionUID = -419848728153991439L;
	
	/** versão do software */
	public static final String VERSION = "0.68";
	
	/** data do software */
	public static final String RELEASEDATE = "Julho/2007";
	
	public static final String BASEWEB = "http://puig.pro.br";
	public static final String SAPOWEB = BASEWEB + "/SAPOWeb";
	
	public static final String AJUDAWEB    = "http://sapo.incubadora.fapesp.br/portal/ajuda";
	public static final String ATUALIZAWEB = "http://sapo.incubadora.fapesp.br/portal/software/builds/";
	public static final String SUPORTEWEB  = "http://sapo.incubadora.fapesp.br/portal/forum/";
	
	// indica se o SAPO pode abrir novo projeto
	public boolean disponivel = true;
	
	public static URL ICONPATH = SAPO.class.getResource("/res/frog.gif");
	
	public String dbDirPath  = "";
	public String figDirPath = "";
	public String dbURL      = "";
	public final String dbDRIVER   = "org.hsqldb.jdbcDriver";
	public final String dbUSERNAME = "sa";
	public final String dbPASSWORD = "po";
	
	/** número máximo de imagens por paciente */
	public static final int maxImg = 11;
	/** objeto que representa informações do paciente */
	Paciente paciente;
	/** objeto que representa informações do usuário */
	User user;
	UIUser uiu;
	UIProtocol uip;
	UIRelatorio uir;
	/** seleciona imagem corrente, de 1 até (maxImg-1). 0 é para imagem não selecionada. */
	int numImg = 0;
	/** desktop com scroll e menu window */
	protected JScrollableDesktopPane scrollableDesktop;
	/** janelas internas para exibição de imagens */
	
	public BaseInternalFrameSAPO[] jif = new BaseInternalFrameSAPO[maxImg];
	
	public BaseInternalFrame userProtocolFrame;
	
	public java.awt.Font fonteTextoGraph[];
	public java.awt.Font fonteAtual;
	
	LeJAIPanel genericoLeJAIPanel;
	CalibraVertical calVert;
	CalibraEscala  calEscX;
	CalibraEscala  calEscY;
	MarcaPontos     marca;
	Desenha         desenha;
	MedeAngulos medeAngulos;
	MedeDistancia medeDist;
	Zoom zoomClass = new Zoom(this);
	CalibraVerticalPanel cvPanel; 
	CalibraEscalaPanel jpCalEscala;
	MarcaPontosPanel jpMarca;
	MedeAngulosPanel jpAngulos;
	MedeDistanciaPanel jpMedeDist;
	ZoomPanel jpnlZoom;
	MarcaPontosProtocolPanel jpMarcaPontosProtocol;
	MarcaPontosProtocol marcaPontosProtocol;
	
	FerramentasDesenho frmDesenho = new FerramentasDesenho(this);
	FerramentasDesenhoPanel tbrDesenho = new FerramentasDesenhoPanel(this);
	FerramentasImagemPanel tbrImagem = new FerramentasImagemPanel(this);
	FerramentasImagem frmImagem = new FerramentasImagem(this);
	
	private static String osname;
	private static boolean isMac, isWin, isJava2;
	public final static String sep = File.separator;
	
	static Locale locale = new Locale("pt","BR");
	NumberFormat numFormat = NumberFormat.getInstance(locale);
	
	private int zoomValue = 100;
	protected StackTraceElement error[];
	EscapeDialog jDialogSAPO, jdlgErrorReport;
	
	DBConnection db = new DBConnection();
	
	ErrorReport errorReport = new ErrorReport(this);
	public java.awt.Color corDesenho = java.awt.Color.RED;
	public int desenhoTipo = 0;
	public int espLinha = 1;
	
	PrintRelatorioPanel jpPrintRelPanel;
	ProjectManager projMan;
	PublicaAnalise publicaAnalise;
	
	boolean isNewProject = false;
	
	Protocolo protoSapo, protoUser1, protoUser2, protoUser3;
	int pSapoAnt[] = {1,2,4,5,11,12,13,14,15,16,17,18,19,20,21,22,24,25};
	int pSapoPos[] = {6,7,16,31,32,34,36,38,40};
	int pSapoLDir[] = {1,4,7,20,21,22,23,29,30};
	int pSapoLEsq[] = {1,4,7,20,21,22,23,29,30};
	
	
	static {
		osname = System.getProperty("os.name");
		isWin = osname.startsWith("Windows");
		isMac = !isWin && osname.startsWith("Mac");
		String version = System.getProperty("java.version");
	}
	
	public static final Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
	public static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final int cursorDelay = 30; // in milliseconds
	
	public void release() {
		FreeMemory.runGarbageCollector(Runtime.getRuntime());
	}
	
	/** Cria novo form SAPO */
	public SAPO() {
		setLookAndFeel(this);
		initComponents();
		initDB();
		jlblStatus.setText(dbDirPath);
		initComponents2();
	}
	
	private void initDB() {
		try {
			SAPOIni ini = StartSAPO.leIni();
			if (ini != null) {
				if (	(ini.dbDirPath  != null) && (new File(ini.dbDirPath)).exists() &&
						(ini.figDirPath != null) && (new File(ini.figDirPath)).exists() ) {
					dbDirPath  = ini.dbDirPath;
					figDirPath = ini.figDirPath;
				}
			}
			String dbDir = dbDirPath.replace('\\','/');
			dbDir = "/" + dbDir + "/sapodb";
			dbURL = "jdbc:hsqldb:file:"+dbDir;
			if (db.open(dbDRIVER,dbURL,dbUSERNAME,dbPASSWORD) && AtualizaDB.atualizaDB(db)) 
				user = new User(db, this);
			else {
				erroAoAbrirDB();	
			}
		} catch (Exception e) {
			e.printStackTrace();
			erroAoAbrirDB();
		}
	}
	
	private void erroAoAbrirDB() {
		Object[] options2 = {"Reconfigurar o programa","Sair sem reconfigurar"};
		String message = "Não foi possível abrir conexão com o banco de dados.\n" +
		"Isto pode ser porque alguma outra instância desse programa\n" +
		"está rodando, ou devido a erro no caminho da base de dados.\n" +
		"É recomendável não prosseguir com o uso do aplicativo.";
		int result = JOptionPane.showOptionDialog(SAPO.this, message,
				"ERRO",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options2, options2[0]);
		if (result == JOptionPane.YES_OPTION) {
			File ini = new File(StartSAPO.iniPath);
			try {
				try {
					StartSAPO.escreveIni("","");
				} catch (Exception e) {
					e.printStackTrace();
				}
				StartSAPO.restart(this);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		else {
			System.exit(-1);
		}
	}

	private void initComponents2() {
		scrollableDesktop = new JScrollableDesktopPane(menuBar);
		scrollableDesktop.setBackground(this.getBackground());
		scrollableDesktop.setBorder(new BevelBorder( BevelBorder.LOWERED));
		scrollableDesktop.setBorder(new BevelBorder( BevelBorder.LOWERED ));
		scrollableDesktop.setFont(getFont());
		jpMain3.add(scrollableDesktop, java.awt.BorderLayout.CENTER);
		criaJDialogErrorReport();
		genericoLeJAIPanel = new LeJAIPanel(this);
		cvPanel = new CalibraVerticalPanel(this);
		calVert = new CalibraVertical(this);
		calEscX = new CalibraEscala(this);
		calEscY = new CalibraEscala(this);
		marca   = new MarcaPontos(this);
		desenha = new Desenha(this);
		medeAngulos = new MedeAngulos(this);
		medeDist = new MedeDistancia(this);
		jpAngulos = new MedeAngulosPanel(this);
		jpMedeDist = new MedeDistanciaPanel(this);
		jpCalEscala = new CalibraEscalaPanel(this);
		jpMarca = new MarcaPontosPanel(this);
		jpnlZoom = new ZoomPanel(this);
		
		protoSapo = user.dados.protoSapo;
		Config config;
		numFormat.setMaximumFractionDigits(user.dados.decimalNumbersFormat);
		
		//define o protocolo SAPO como padrão!!
		protoSapo.nome = "Sapo";
		for(int i=0; i<pSapoAnt.length; i++)
			addPontoProto("Sapo", "anterior", pSapoAnt[i]);
		for(int i=0; i<pSapoPos.length; i++)
			addPontoProto("Sapo", "posterior", pSapoPos[i]);
		for(int i=0; i<pSapoLDir.length; i++)
			addPontoProto("Sapo", "latDir", pSapoLDir[i]);
		for(int i=0; i<pSapoLEsq.length; i++)
			addPontoProto("Sapo", "latEsq", pSapoLEsq[i]);
		
		protoUser1 = user.dados.protoUser1;
		protoUser1.nome = "User1";
		protoUser2 = user.dados.protoUser2;
		protoUser2.nome = "User2";
		protoUser3 = user.dados.protoUser3;
		protoUser3.nome = "User3";
		
		jpMarcaPontosProtocol = new MarcaPontosProtocolPanel(this);
		marcaPontosProtocol = new MarcaPontosProtocol(this);
		
		criaMemoryPanel();
		
		publicaAnalise = new PublicaAnalise(this);
		
		jmenuEdit.setVisible(false); // TODO implementar função
		jSeparator26.setVisible(false); // TODO implementar função
		jmenuitHelpContent.setVisible(false); // TODO implementar função
		jmenuitHelpIndex.setVisible(false); // TODO implementar função
		jmenuitHelpSearch.setVisible(false); // TODO implementar função
		jmenuitKeyboardMap.setVisible(false); //TODO implementar função
		jbUndo.setVisible(false); // TODO implementar função
		jbRedo.setVisible(false); // TODO implementar função
		jPanel2.setVisible(false); // TODO implementar função
		jSeparator12.setVisible(false); // TODO implementar função
		//jbMarcaPontos.setVisible(false); // TODO melhorar tutorial e wizard para não causar confusão
		
	}
	
	public void setDBDirPath(String path) {
		try {
			dbDirPath = path;
			StartSAPO.escreveIni(dbDirPath, figDirPath);
			solicitaReinicio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFigDirPath(String path) {
		try {
			figDirPath = path;
			StartSAPO.escreveIni(dbDirPath, figDirPath);
			solicitaReinicio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void solicitaReinicio() {
		Object[] options = {"Sim, reiniciar agora","Não, reiniciar depois"};
		int result = JOptionPane.showOptionDialog(null,
				"Você deve reiniciar o programa para que as alterações façam efeito.  \n" +
				" \n",
				"Aviso",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		if (result == JOptionPane.YES_OPTION) {
			commonJifDeactivation();
			user.escreveDB();
			if ((paciente != null) && (paciente.salvarAlteracoes)) paciente.escreveDB();
			db.close();
			StartSAPO.restart(this);
		}
	}
	
	public boolean publica() {
		boolean retorno = false;
		if (publicaAnalise.publica()) {
			paciente.dados.publicado = true;
			paciente.escreveDB();
			paciente.dadosInterface.mostraDados();
			retorno = true;
		}
		return retorno;
	}
	
	public void enableInternalFrames(boolean opt) {
		Component[] component = this.scrollableDesktop.getComponents();
		for (int i=0; i<component.length; i++)
			component[i].setEnabled(opt);
	}
	
	private void criaMemoryPanel() {
		javax.swing.JLabel jlblAbout = new MemoryLabel(2000);
		jpStatus.add(jlblAbout, java.awt.BorderLayout.EAST);
		jlblAbout.setVisible(true);
	}
	
	private void criaJDialogErrorReport() {
		jdlgErrorReport = new EscapeDialog(SAPO.this, "Relatório de Erros", true);
		jdlgErrorReport.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jdlgErrorReport.getContentPane().add(errorReport);
		jdlgErrorReport.pack();
	}
	
	public void clearGlassPane(RootPaneContainer rootPane) {
		rootPane.getGlassPane().setVisible(false);
		JPanel emptyGlass = new JPanel();
		emptyGlass.setOpaque(false);
		rootPane.setGlassPane(emptyGlass);
	}
	
	private void showUserProtocolFrame() {
		removePainelDeOutros(0);
		uip = new UIProtocol(this);
		incluirListenerInternalFrame(uip);
		scrollableDesktop.add(uip);
		try{
			uip.setMaximum(true);
		}catch(Exception e){};
	}
	
	private void showRelatorioFrame() {
		removePainelDeOutros(0);
		scrollableDesktop.add(new UIRelatorio(this));
	}
	
	/**
	 * Esse método é chamado pelo construtor para inicializar o form e seus componentes
	 * Não modifique.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpMain = new javax.swing.JPanel();
        jpMain2 = new javax.swing.JPanel();
        jpMain3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jbNew = new javax.swing.JButton();
        jbOpen = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jbImagem = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSeparator8 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jtbDesenho = new javax.swing.JToggleButton();
        jtbEdicao = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jSeparator12 = new javax.swing.JSeparator();
        jbUndo = new javax.swing.JButton();
        jbRedo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jSeparator13 = new javax.swing.JSeparator();
        jbCalibraVertical = new javax.swing.JButton();
        jbEscala = new javax.swing.JButton();
        jbtnNewProtocol = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        jbtnAnguloMenu = new javax.swing.JButton();
        jbtnMedeDist = new javax.swing.JButton();
        jbMarcaPontos = new javax.swing.JButton();
        jbtnProtocol = new javax.swing.JButton();
        jSeparator23 = new javax.swing.JSeparator();
        jbRelatorio = new javax.swing.JButton();
        jbPrintRel = new javax.swing.JButton();
        jbRelExcel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jSeparator14 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jpStatus = new javax.swing.JPanel();
        jlblStatus = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jmenuFile = new javax.swing.JMenu();
        jmenuitNewFile = new javax.swing.JMenuItem();
        jmenuitOpen = new javax.swing.JMenuItem();
        jmenuitClose = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JSeparator();
        jmenuitOpen1 = new javax.swing.JMenuItem();
        jmenuitSaveAs = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JSeparator();
        jmenuitImprRel = new javax.swing.JMenuItem();
        jmenuitExpExcel = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JSeparator();
        jmenuitExit = new javax.swing.JMenuItem();
        jmenuEdit = new javax.swing.JMenu();
        jmenuitUndo = new javax.swing.JMenuItem();
        jmenuitRedo = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JSeparator();
        jmenuitCut = new javax.swing.JMenuItem();
        jmenuitCopy = new javax.swing.JMenuItem();
        jmenuitPaste = new javax.swing.JMenuItem();
        jmenuitDelete = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JSeparator();
        jmenuitFind = new javax.swing.JMenuItem();
        jmenuitReplace = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JSeparator();
        jmenuitFindProject = new javax.swing.JMenuItem();
        jmenuView = new javax.swing.JMenu();
        jcbmenuitToolbar = new javax.swing.JCheckBoxMenuItem();
        jcbmenuitDraw = new javax.swing.JCheckBoxMenuItem();
        jcbmenuitEditImage = new javax.swing.JCheckBoxMenuItem();
        jmenuImage = new javax.swing.JMenu();
        jmenuitOpenImage = new javax.swing.JMenuItem();
        jmenuitSaveImage = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JSeparator();
        jmenuitZoom = new javax.swing.JMenuItem();
        jmenuAnalysis = new javax.swing.JMenu();
        jmenuitVertical = new javax.swing.JMenuItem();
        jmenuitScale = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JSeparator();
        jmenuitAngulos = new javax.swing.JMenuItem();
        jmenuItDist = new javax.swing.JMenuItem();
        jmenuItMarcaPontosProtocol = new javax.swing.JMenuItem();
        jmenuitMarca = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JSeparator();
        jmenuitAtualizaRef = new javax.swing.JMenuItem();
        jmenuitGeraRel = new javax.swing.JMenuItem();
        jmenuData = new javax.swing.JMenu();
        jmenuitPreferencias = new javax.swing.JMenuItem();
        jmenuitUsuario = new javax.swing.JMenuItem();
        jmenuitProtocolUser = new javax.swing.JMenuItem();
        jmenuitSujeito = new javax.swing.JMenuItem();
        jmenuitGC = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JSeparator();
        jmenuitSAPOWeb = new javax.swing.JMenuItem();
        jmenuHelp = new javax.swing.JMenu();
        jmenuitAjudaOnline = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JSeparator();
        jmenuitHelpContent = new javax.swing.JMenuItem();
        jmenuitHelpIndex = new javax.swing.JMenuItem();
        jmenuitHelpSearch = new javax.swing.JMenuItem();
        jmenuitKeyboardMap = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JSeparator();
        jmenuError = new javax.swing.JMenuItem();
        jmenuitFeedbackSuporte = new javax.swing.JMenuItem();
        jmenuitVerificaAtualizacoes = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JSeparator();
        jmenuitAbout = new javax.swing.JMenuItem();

        getContentPane().setLayout(new java.awt.BorderLayout(1, 1));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SAPO - Software para Avalia\u00E7\u00E3o Postural - v. " + VERSION);
        setBackground(java.awt.SystemColor.window);
        setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        setIconImage(getIconImage());
        setName("mainFrame");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jpMain.setLayout(new java.awt.BorderLayout());

        jpMain.setBackground(java.awt.SystemColor.window);
        jpMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jpMain2.setLayout(new java.awt.BorderLayout());

        jpMain2.setBackground(java.awt.SystemColor.window);
        jpMain3.setLayout(new java.awt.BorderLayout());

        jpMain3.setBackground(java.awt.SystemColor.window);
        jToolBar1.setRollover(true);
        jToolBar1.setToolTipText("");
        jToolBar1.setAlignmentX(0.0F);
        jToolBar1.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jToolBar1.setMinimumSize(new java.awt.Dimension(28, 28));
        jToolBar1.setName("SAPO - Ferramentas");
        jbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/new.gif")));
        jbNew.setToolTipText("Novo projeto");
        jbNew.setMaximumSize(new java.awt.Dimension(30, 30));
        jbNew.setMinimumSize(new java.awt.Dimension(24, 24));
        jbNew.setPreferredSize(new java.awt.Dimension(30, 30));
        jbNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNewActionPerformed(evt);
            }
        });

        jToolBar1.add(jbNew);

        jbOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/open.gif")));
        jbOpen.setToolTipText("Projetos criados");
        jbOpen.setMaximumSize(new java.awt.Dimension(30, 30));
        jbOpen.setMinimumSize(new java.awt.Dimension(24, 24));
        jbOpen.setPreferredSize(new java.awt.Dimension(30, 30));
        jbOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenActionPerformed(evt);
            }
        });

        jToolBar1.add(jbOpen);

        jbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/save.gif")));
        jbSave.setToolTipText("Salvar projeto em arquivo");
        jbSave.setEnabled(false);
        jbSave.setMaximumSize(new java.awt.Dimension(30, 30));
        jbSave.setMinimumSize(new java.awt.Dimension(24, 24));
        jbSave.setPreferredSize(new java.awt.Dimension(30, 30));
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jToolBar1.add(jbSave);

        jbImagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/foto.gif")));
        jbImagem.setToolTipText("Abrir imagem");
        jbImagem.setEnabled(false);
        jbImagem.setMaximumSize(new java.awt.Dimension(30, 30));
        jbImagem.setMinimumSize(new java.awt.Dimension(24, 24));
        jbImagem.setPreferredSize(new java.awt.Dimension(30, 30));
        jbImagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbImagemActionPerformed(evt);
            }
        });

        jToolBar1.add(jbImagem);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(14, 32767));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(14, 30));
        jSeparator8.setBackground(java.awt.SystemColor.control);
        jSeparator8.setForeground(java.awt.SystemColor.control);
        jSeparator8.setOrientation(jToolBar1.getOrientation());
        jSeparator8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(233, 231, 231), null));
        jSeparator8.setMaximumSize(new java.awt.Dimension(2, 26));
        jSeparator8.setPreferredSize(new java.awt.Dimension(2, 26));
        jSeparator8.setRequestFocusEnabled(false);
        jSeparator8.setVerifyInputWhenFocusTarget(false);
        jPanel1.add(jSeparator8, new java.awt.GridBagConstraints());

        jToolBar1.add(jPanel1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/lupa.gif")));
        jButton1.setToolTipText("Ajustar zoom da imagem atual");
        jButton1.setEnabled(false);
        jButton1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButton1.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton1.setPreferredSize(new java.awt.Dimension(30, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jToolBar1.add(jButton1);

        jtbDesenho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/desenha.gif")));
        jtbDesenho.setToolTipText("Mostrar barra de ferramentas de desenho");
        jtbDesenho.setEnabled(false);
        jtbDesenho.setMaximumSize(new java.awt.Dimension(30, 30));
        jtbDesenho.setMinimumSize(new java.awt.Dimension(24, 24));
        jtbDesenho.setPreferredSize(new java.awt.Dimension(30, 30));
        jtbDesenho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbDesenhoActionPerformed(evt);
            }
        });

        jToolBar1.add(jtbDesenho);

        jtbEdicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/edita.gif")));
        jtbEdicao.setToolTipText("Mostrar barra de ferramentas de edi\u00e7\u00e3o de imagem");
        jtbEdicao.setEnabled(false);
        jtbEdicao.setMaximumSize(new java.awt.Dimension(30, 30));
        jtbEdicao.setMinimumSize(new java.awt.Dimension(24, 24));
        jtbEdicao.setPreferredSize(new java.awt.Dimension(30, 30));
        jtbEdicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbEdicaoActionPerformed(evt);
            }
        });

        jToolBar1.add(jtbEdicao);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMaximumSize(new java.awt.Dimension(14, 32767));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(14, 30));
        jSeparator12.setBackground(java.awt.SystemColor.control);
        jSeparator12.setForeground(java.awt.SystemColor.control);
        jSeparator12.setOrientation(jToolBar1.getOrientation());
        jSeparator12.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(233, 231, 231), null));
        jSeparator12.setMaximumSize(new java.awt.Dimension(2, 26));
        jSeparator12.setPreferredSize(new java.awt.Dimension(2, 26));
        jSeparator12.setRequestFocusEnabled(false);
        jSeparator12.setVerifyInputWhenFocusTarget(false);
        jPanel2.add(jSeparator12, new java.awt.GridBagConstraints());

        jToolBar1.add(jPanel2);

        jbUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/undo.gif")));
        jbUndo.setToolTipText("Desfazer");
        jbUndo.setEnabled(false);
        jbUndo.setMaximumSize(new java.awt.Dimension(30, 30));
        jbUndo.setMinimumSize(new java.awt.Dimension(24, 24));
        jbUndo.setPreferredSize(new java.awt.Dimension(30, 30));
        jbUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbUndoActionPerformed(evt);
            }
        });

        jToolBar1.add(jbUndo);

        jbRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/redo.gif")));
        jbRedo.setToolTipText("Refazer");
        jbRedo.setEnabled(false);
        jbRedo.setMaximumSize(new java.awt.Dimension(30, 30));
        jbRedo.setMinimumSize(new java.awt.Dimension(24, 24));
        jbRedo.setPreferredSize(new java.awt.Dimension(30, 30));
        jbRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRedoActionPerformed(evt);
            }
        });

        jToolBar1.add(jbRedo);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setMaximumSize(new java.awt.Dimension(14, 32767));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(14, 30));
        jSeparator13.setBackground(java.awt.SystemColor.control);
        jSeparator13.setForeground(java.awt.SystemColor.control);
        jSeparator13.setOrientation(jToolBar1.getOrientation());
        jSeparator13.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(233, 231, 231), null));
        jSeparator13.setMaximumSize(new java.awt.Dimension(2, 26));
        jSeparator13.setPreferredSize(new java.awt.Dimension(2, 26));
        jSeparator13.setRequestFocusEnabled(false);
        jSeparator13.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(jSeparator13, new java.awt.GridBagConstraints());

        jToolBar1.add(jPanel3);

        jbCalibraVertical.setFont(getFont());
        jbCalibraVertical.setForeground(new java.awt.Color(255, 0, 0));
        jbCalibraVertical.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/calvertical.gif")));
        jbCalibraVertical.setToolTipText("Calibrar vertical e escala da imagem");
        jbCalibraVertical.setEnabled(false);
        jbCalibraVertical.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbCalibraVertical.setMaximumSize(new java.awt.Dimension(30, 30));
        jbCalibraVertical.setMinimumSize(new java.awt.Dimension(24, 24));
        jbCalibraVertical.setPreferredSize(new java.awt.Dimension(30, 30));
        jbCalibraVertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCalibraVerticalActionPerformed(evt);
            }
        });

        jToolBar1.add(jbCalibraVertical);

        jbEscala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/calib2.gif")));
        jbEscala.setToolTipText("Calibrar escala (detalhada) da imagem");
        jbEscala.setEnabled(false);
        jbEscala.setMaximumSize(new java.awt.Dimension(30, 30));
        jbEscala.setMinimumSize(new java.awt.Dimension(24, 24));
        jbEscala.setPreferredSize(new java.awt.Dimension(30, 30));
        jbEscala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEscalaActionPerformed(evt);
            }
        });

        jToolBar1.add(jbEscala);

        jbtnNewProtocol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/newproto.png")));
        jbtnNewProtocol.setToolTipText("Define um novo protocolo");
        jbtnNewProtocol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewProtocolActionPerformed(evt);
            }
        });

        jToolBar1.add(jbtnNewProtocol);

        jSeparator15.setBackground(java.awt.SystemColor.control);
        jSeparator15.setForeground(java.awt.SystemColor.control);
        jSeparator15.setOrientation(jToolBar1.getOrientation());
        jSeparator15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(233, 231, 231), null));
        jSeparator15.setMaximumSize(new java.awt.Dimension(2, 26));
        jSeparator15.setPreferredSize(new java.awt.Dimension(2, 26));
        jSeparator15.setRequestFocusEnabled(false);
        jSeparator15.setVerifyInputWhenFocusTarget(false);
        jToolBar1.add(jSeparator15);

        jbtnAnguloMenu.setFont(getFont());
        jbtnAnguloMenu.setForeground(new java.awt.Color(0, 0, 255));
        jbtnAnguloMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/angle.gif")));
        jbtnAnguloMenu.setToolTipText("Medir \u00e2ngulos livremente");
        jbtnAnguloMenu.setEnabled(false);
        jbtnAnguloMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnAnguloMenu.setMaximumSize(new java.awt.Dimension(30, 30));
        jbtnAnguloMenu.setMinimumSize(new java.awt.Dimension(24, 24));
        jbtnAnguloMenu.setPreferredSize(new java.awt.Dimension(30, 30));
        jbtnAnguloMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAnguloMenuActionPerformed(evt);
            }
        });

        jToolBar1.add(jbtnAnguloMenu);

        jbtnMedeDist.setFont(getFont());
        jbtnMedeDist.setForeground(new java.awt.Color(0, 153, 0));
        jbtnMedeDist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/mede_dist.gif")));
        jbtnMedeDist.setToolTipText("Medir dist\u00e2ncias livremente");
        jbtnMedeDist.setEnabled(false);
        jbtnMedeDist.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbtnMedeDist.setMaximumSize(new java.awt.Dimension(30, 30));
        jbtnMedeDist.setMinimumSize(new java.awt.Dimension(24, 24));
        jbtnMedeDist.setPreferredSize(new java.awt.Dimension(30, 30));
        jbtnMedeDist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMedeDistActionPerformed(evt);
            }
        });

        jToolBar1.add(jbtnMedeDist);

        jbMarcaPontos.setFont(getFont());
        jbMarcaPontos.setForeground(new java.awt.Color(0, 153, 0));
        jbMarcaPontos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/marca.gif")));
        jbMarcaPontos.setToolTipText("Marca\u00e7\u00e3o livre de pontos");
        jbMarcaPontos.setEnabled(false);
        jbMarcaPontos.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbMarcaPontos.setMaximumSize(new java.awt.Dimension(30, 30));
        jbMarcaPontos.setMinimumSize(new java.awt.Dimension(24, 24));
        jbMarcaPontos.setPreferredSize(new java.awt.Dimension(30, 30));
        jbMarcaPontos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbMarcaPontosActionPerformed(evt);
            }
        });

        jToolBar1.add(jbMarcaPontos);

        jbtnProtocol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/protocol.png")));
        jbtnProtocol.setToolTipText("Marca\u00e7\u00e3o de pontos seguindo protocolo");
        jbtnProtocol.setEnabled(false);
        jbtnProtocol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnProtocolActionPerformed(evt);
            }
        });

        jToolBar1.add(jbtnProtocol);

        jSeparator23.setBackground(java.awt.SystemColor.control);
        jSeparator23.setForeground(java.awt.SystemColor.control);
        jSeparator23.setOrientation(jToolBar1.getOrientation());
        jSeparator23.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(233, 231, 231), null));
        jSeparator23.setMaximumSize(new java.awt.Dimension(2, 26));
        jSeparator23.setPreferredSize(new java.awt.Dimension(2, 26));
        jSeparator23.setRequestFocusEnabled(false);
        jSeparator23.setVerifyInputWhenFocusTarget(false);
        jToolBar1.add(jSeparator23);

        jbRelatorio.setFont(getFont());
        jbRelatorio.setForeground(new java.awt.Color(153, 0, 153));
        jbRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/report.gif")));
        jbRelatorio.setToolTipText("Gerar relat\u00f3rio de an\u00e1lises");
        jbRelatorio.setEnabled(false);
        jbRelatorio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbRelatorio.setMaximumSize(new java.awt.Dimension(30, 30));
        jbRelatorio.setMinimumSize(new java.awt.Dimension(24, 24));
        jbRelatorio.setPreferredSize(new java.awt.Dimension(30, 30));
        jbRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRelatorioActionPerformed(evt);
            }
        });

        jToolBar1.add(jbRelatorio);

        jbPrintRel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/imprime.gif")));
        jbPrintRel.setToolTipText("Imprimir relat\u00f3rio de an\u00e1lises");
        jbPrintRel.setEnabled(false);
        jbPrintRel.setMaximumSize(new java.awt.Dimension(30, 30));
        jbPrintRel.setMinimumSize(new java.awt.Dimension(24, 24));
        jbPrintRel.setPreferredSize(new java.awt.Dimension(30, 30));
        jbPrintRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPrintRelActionPerformed(evt);
            }
        });

        jToolBar1.add(jbPrintRel);

        jbRelExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/xls.gif")));
        jbRelExcel.setToolTipText("Exportar relat\u00f3rio de an\u00e1lises para Excel");
        jbRelExcel.setEnabled(false);
        jbRelExcel.setMaximumSize(new java.awt.Dimension(30, 30));
        jbRelExcel.setMinimumSize(new java.awt.Dimension(24, 24));
        jbRelExcel.setPreferredSize(new java.awt.Dimension(30, 30));
        jbRelExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRelExcelActionPerformed(evt);
            }
        });

        jToolBar1.add(jbRelExcel);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMaximumSize(new java.awt.Dimension(14, 32767));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(14, 30));
        jSeparator14.setBackground(java.awt.SystemColor.control);
        jSeparator14.setForeground(java.awt.SystemColor.control);
        jSeparator14.setOrientation(jToolBar1.getOrientation());
        jSeparator14.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(233, 231, 231), null));
        jSeparator14.setMaximumSize(new java.awt.Dimension(2, 26));
        jSeparator14.setPreferredSize(new java.awt.Dimension(2, 26));
        jSeparator14.setRequestFocusEnabled(false);
        jSeparator14.setVerifyInputWhenFocusTarget(false);
        jPanel4.add(jSeparator14, new java.awt.GridBagConstraints());

        jToolBar1.add(jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setMaximumSize(new java.awt.Dimension(14, 32767));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(14, 30));
        jToolBar1.add(jPanel5);

        jpMain3.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jpMain2.add(jpMain3, java.awt.BorderLayout.CENTER);

        jpMain.add(jpMain2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpMain, java.awt.BorderLayout.CENTER);

        jpStatus.setLayout(new java.awt.BorderLayout());

        jpStatus.setFocusable(false);
        jpStatus.setFont(new java.awt.Font("Default", 0, 12));
        jpStatus.setMinimumSize(new java.awt.Dimension(14, 20));
        jpStatus.setPreferredSize(new java.awt.Dimension(14, 22));
        jlblStatus.setFont(new java.awt.Font("Default", 0, 11));
        jlblStatus.setText("    ");
        jlblStatus.setAlignmentX(1.0F);
        jlblStatus.setFocusable(false);
        jpStatus.add(jlblStatus, java.awt.BorderLayout.WEST);

        getContentPane().add(jpStatus, java.awt.BorderLayout.SOUTH);

        menuBar.setFont(getFont());
        menuBar.setMargin(new java.awt.Insets(0, 0, 0, 2));
        jmenuFile.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuFile.setMnemonic('A');
        jmenuFile.setText(" Arquivo ");
        jmenuFile.setFont(getFont());
        jmenuFile.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jmenuitNewFile.setFont(getFont());
        jmenuitNewFile.setMnemonic('N');
        jmenuitNewFile.setText("Novo Projeto");
        jmenuitNewFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitNewFileActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitNewFile);

        jmenuitOpen.setFont(getFont());
        jmenuitOpen.setMnemonic('A');
        jmenuitOpen.setText("Projetos Criados");
        jmenuitOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitOpenActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitOpen);

        jmenuitClose.setFont(getFont());
        jmenuitClose.setMnemonic('F');
        jmenuitClose.setText("Fechar Projeto");
        jmenuitClose.setEnabled(false);
        jmenuitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitCloseActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitClose);

        jmenuFile.add(jSeparator22);

        jmenuitOpen1.setFont(getFont());
        jmenuitOpen1.setMnemonic('b');
        jmenuitOpen1.setText("Abrir de Arquivo");
        jmenuitOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitOpen1ActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitOpen1);

        jmenuitSaveAs.setFont(getFont());
        jmenuitSaveAs.setMnemonic('v');
        jmenuitSaveAs.setText("Salvar em Arquivo");
        jmenuitSaveAs.setEnabled(false);
        jmenuitSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitSaveAsActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitSaveAs);

        jmenuFile.add(jSeparator25);

        jmenuitImprRel.setFont(getFont());
        jmenuitImprRel.setMnemonic('I');
        jmenuitImprRel.setText("Imprimir");
        jmenuitImprRel.setEnabled(false);
        jmenuitImprRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitImprRelActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitImprRel);

        jmenuitExpExcel.setFont(getFont());
        jmenuitExpExcel.setMnemonic('E');
        jmenuitExpExcel.setText("Exportar para Excel");
        jmenuitExpExcel.setEnabled(false);
        jmenuitExpExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitExpExcelActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitExpExcel);

        jmenuFile.add(jSeparator24);

        jmenuitExit.setFont(getFont());
        jmenuitExit.setMnemonic('S');
        jmenuitExit.setText("Sair");
        jmenuitExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitExitActionPerformed(evt);
            }
        });

        jmenuFile.add(jmenuitExit);

        menuBar.add(jmenuFile);

        jmenuEdit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuEdit.setMnemonic('E');
        jmenuEdit.setText(" Editar ");
        jmenuEdit.setFont(getFont());
        jmenuEdit.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jmenuitUndo.setFont(getFont());
        jmenuitUndo.setMnemonic('D');
        jmenuitUndo.setText("Desfazer");
        jmenuitUndo.setEnabled(false);
        jmenuitUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitUndoActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitUndo);

        jmenuitRedo.setFont(getFont());
        jmenuitRedo.setMnemonic('R');
        jmenuitRedo.setText("Refazer");
        jmenuitRedo.setEnabled(false);
        jmenuitRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitRedoActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitRedo);

        jmenuEdit.add(jSeparator16);

        jmenuitCut.setFont(getFont());
        jmenuitCut.setMnemonic('r');
        jmenuitCut.setText("Recortar");
        jmenuitCut.setEnabled(false);
        jmenuitCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitCutActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitCut);

        jmenuitCopy.setFont(getFont());
        jmenuitCopy.setMnemonic('C');
        jmenuitCopy.setText("Copiar");
        jmenuitCopy.setEnabled(false);
        jmenuitCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitCopyActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitCopy);

        jmenuitPaste.setFont(getFont());
        jmenuitPaste.setMnemonic('l');
        jmenuitPaste.setText("Colar");
        jmenuitPaste.setEnabled(false);
        jmenuitPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitPasteActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitPaste);

        jmenuitDelete.setFont(getFont());
        jmenuitDelete.setMnemonic('A');
        jmenuitDelete.setText("Apagar");
        jmenuitDelete.setEnabled(false);
        jmenuitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitDeleteActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitDelete);

        jmenuEdit.add(jSeparator18);

        jmenuitFind.setFont(getFont());
        jmenuitFind.setMnemonic('P');
        jmenuitFind.setText("Procurar");
        jmenuitFind.setEnabled(false);
        jmenuitFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitFindActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitFind);

        jmenuitReplace.setFont(getFont());
        jmenuitReplace.setMnemonic('S');
        jmenuitReplace.setText("Substituir");
        jmenuitReplace.setEnabled(false);
        jmenuitReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitReplaceActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitReplace);

        jmenuEdit.add(jSeparator19);

        jmenuitFindProject.setFont(getFont());
        jmenuitFindProject.setMnemonic('o');
        jmenuitFindProject.setText("Procurar em projetos");
        jmenuitFindProject.setEnabled(false);
        jmenuitFindProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitFindProjectActionPerformed(evt);
            }
        });

        jmenuEdit.add(jmenuitFindProject);

        menuBar.add(jmenuEdit);

        jmenuView.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuView.setMnemonic('x');
        jmenuView.setText(" Exibir ");
        jmenuView.setFont(getFont());
        jmenuView.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jcbmenuitToolbar.setFont(getFont());
        jcbmenuitToolbar.setMnemonic('P');
        jcbmenuitToolbar.setSelected(true);
        jcbmenuitToolbar.setText("Barra de Ferramentas Principal");
        jcbmenuitToolbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbmenuitToolbarActionPerformed(evt);
            }
        });

        jmenuView.add(jcbmenuitToolbar);

        jcbmenuitDraw.setFont(getFont());
        jcbmenuitDraw.setMnemonic('D');
        jcbmenuitDraw.setText("Barra de Ferramentas de Desenho");
        jcbmenuitDraw.setEnabled(false);
        jcbmenuitDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbmenuitDrawActionPerformed(evt);
            }
        });

        jmenuView.add(jcbmenuitDraw);

        jcbmenuitEditImage.setFont(getFont());
        jcbmenuitEditImage.setMnemonic('E');
        jcbmenuitEditImage.setText("Barra de Ferramentas de Edi\u00e7\u00e3o de Imagens");
        jcbmenuitEditImage.setEnabled(false);
        jcbmenuitEditImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbmenuitEditImageActionPerformed(evt);
            }
        });

        jmenuView.add(jcbmenuitEditImage);

        menuBar.add(jmenuView);

        jmenuImage.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuImage.setMnemonic('I');
        jmenuImage.setText(" Imagem ");
        jmenuImage.setFont(getFont());
        jmenuImage.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jmenuitOpenImage.setFont(getFont());
        jmenuitOpenImage.setMnemonic('A');
        jmenuitOpenImage.setText("Abrir imagem");
        jmenuitOpenImage.setEnabled(false);
        jmenuitOpenImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitOpenImageActionPerformed(evt);
            }
        });

        jmenuImage.add(jmenuitOpenImage);

        jmenuitSaveImage.setFont(getFont());
        jmenuitSaveImage.setMnemonic('S');
        jmenuitSaveImage.setText("Salvar imagem como ...");
        jmenuitSaveImage.setEnabled(false);
        jmenuitSaveImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitSaveImageActionPerformed(evt);
            }
        });

        jmenuImage.add(jmenuitSaveImage);

        jmenuImage.add(jSeparator17);

        jmenuitZoom.setFont(getFont());
        jmenuitZoom.setMnemonic('Z');
        jmenuitZoom.setText("Ajuste de Zoom");
        jmenuitZoom.setEnabled(false);
        jmenuitZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitZoomActionPerformed(evt);
            }
        });

        jmenuImage.add(jmenuitZoom);

        menuBar.add(jmenuImage);

        jmenuAnalysis.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuAnalysis.setMnemonic('n');
        jmenuAnalysis.setText(" An\u00e1lises ");
        jmenuAnalysis.setFont(getFont());
        jmenuAnalysis.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jmenuitVertical.setFont(getFont());
        jmenuitVertical.setMnemonic('V');
        jmenuitVertical.setText("Calibrar Vertical e Escala (2D)");
        jmenuitVertical.setEnabled(false);
        jmenuitVertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitVerticalActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuitVertical);

        jmenuitScale.setFont(getFont());
        jmenuitScale.setMnemonic('E');
        jmenuitScale.setText("Calibrar Escalas X e Y Separadas");
        jmenuitScale.setEnabled(false);
        jmenuitScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitScaleActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuitScale);

        jmenuAnalysis.add(jSeparator20);

        jmenuitAngulos.setFont(getFont());
        jmenuitAngulos.setMnemonic('n');
        jmenuitAngulos.setText("Medir \u00c2ngulos Livremente");
        jmenuitAngulos.setEnabled(false);
        jmenuitAngulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitAngulosActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuitAngulos);

        jmenuItDist.setFont(getFont());
        jmenuItDist.setMnemonic('D');
        jmenuItDist.setText("Medir Dist\u00e2ncias");
        jmenuItDist.setEnabled(false);
        jmenuItDist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuItDistActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuItDist);

        jmenuItMarcaPontosProtocol.setFont(getFont());
        jmenuItMarcaPontosProtocol.setMnemonic('P');
        jmenuItMarcaPontosProtocol.setText("Marca\u00e7\u00e3o de Pontos na Imagem pelo Protocolo");
        jmenuItMarcaPontosProtocol.setEnabled(false);
        jmenuItMarcaPontosProtocol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuItMarcaPontosProtocolActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuItMarcaPontosProtocol);

        jmenuitMarca.setFont(getFont());
        jmenuitMarca.setMnemonic('M');
        jmenuitMarca.setText("Marca\u00e7\u00e3o Livre de Pontos");
        jmenuitMarca.setEnabled(false);
        jmenuitMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitMarcaActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuitMarca);

        jmenuAnalysis.add(jSeparator21);

        jmenuitAtualizaRef.setFont(getFont());
        jmenuitAtualizaRef.setMnemonic('R');
        jmenuitAtualizaRef.setText("Atualizar Valores de Refer\u00eancia");
        jmenuitAtualizaRef.setEnabled(false);
        jmenuitAtualizaRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitAtualizaRefActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuitAtualizaRef);

        jmenuitGeraRel.setFont(getFont());
        jmenuitGeraRel.setMnemonic('R');
        jmenuitGeraRel.setText("Gerar Relat\u00f3rio de An\u00e1lises");
        jmenuitGeraRel.setEnabled(false);
        jmenuitGeraRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitGeraRelActionPerformed(evt);
            }
        });

        jmenuAnalysis.add(jmenuitGeraRel);

        menuBar.add(jmenuAnalysis);

        jmenuData.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuData.setMnemonic('F');
        jmenuData.setText(" Ferramentas ");
        jmenuData.setFont(getFont());
        jmenuData.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jmenuitPreferencias.setFont(getFont());
        jmenuitPreferencias.setMnemonic('P');
        jmenuitPreferencias.setText("Configura\u00e7\u00f5es do Programa");
        jmenuitPreferencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitPreferenciasActionPerformed(evt);
            }
        });

        jmenuData.add(jmenuitPreferencias);

        jmenuitUsuario.setFont(getFont());
        jmenuitUsuario.setMnemonic('U');
        jmenuitUsuario.setText("Configura\u00e7\u00e3o do Usu\u00e1rio");
        jmenuitUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitUsuarioActionPerformed(evt);
            }
        });

        jmenuData.add(jmenuitUsuario);

        jmenuitProtocolUser.setFont(getFont());
        jmenuitProtocolUser.setMnemonic('n');
        jmenuitProtocolUser.setText("Definir um Novo Protocolo");
        jmenuitProtocolUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitProtocolUserActionPerformed(evt);
            }
        });

        jmenuData.add(jmenuitProtocolUser);

        jmenuitSujeito.setFont(getFont());
        jmenuitSujeito.setMnemonic('A');
        jmenuitSujeito.setText("Dados da An\u00e1lise");
        jmenuitSujeito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitSujeitoActionPerformed(evt);
            }
        });

        jmenuData.add(jmenuitSujeito);

        jmenuitGC.setFont(getFont());
        jmenuitGC.setMnemonic('L');
        jmenuitGC.setText("Limpar Mem\u00f3ria");
        jmenuitGC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitGCActionPerformed(evt);
            }
        });

        jmenuData.add(jmenuitGC);

        jmenuData.add(jSeparator29);

        jmenuitSAPOWeb.setFont(getFont());
        jmenuitSAPOWeb.setMnemonic('A');
        jmenuitSAPOWeb.setText("SAPO - Web");
        jmenuitSAPOWeb.setToolTipText("SAPOWeb \u00e9 uma aplica\u00e7\u00e3o que permite fazer consultas \u00e0 base de dados de an\u00e1lises posturais realizadas por centros qualificados, relativa \u00e0 popula\u00e7\u00e3o brasileira.");
        jmenuitSAPOWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitSAPOWebActionPerformed(evt);
            }
        });

        jmenuData.add(jmenuitSAPOWeb);

        menuBar.add(jmenuData);

        jmenuHelp.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jmenuHelp.setMnemonic('j');
        jmenuHelp.setText(" Ajuda ");
        jmenuHelp.setFont(getFont());
        jmenuHelp.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jmenuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuHelpActionPerformed(evt);
            }
        });

        jmenuitAjudaOnline.setFont(getFont());
        jmenuitAjudaOnline.setMnemonic('A');
        jmenuitAjudaOnline.setText("Ajuda online...");
        jmenuitAjudaOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitAjudaOnlineActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitAjudaOnline);

        jmenuHelp.add(jSeparator26);

        jmenuitHelpContent.setFont(getFont());
        jmenuitHelpContent.setMnemonic('C');
        jmenuitHelpContent.setText("Conte\u00fado");
        jmenuitHelpContent.setEnabled(false);
        jmenuitHelpContent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitHelpContentActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitHelpContent);

        jmenuitHelpIndex.setFont(getFont());
        jmenuitHelpIndex.setMnemonic('I');
        jmenuitHelpIndex.setText("\u00cdndice");
        jmenuitHelpIndex.setEnabled(false);
        jmenuitHelpIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitHelpIndexActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitHelpIndex);

        jmenuitHelpSearch.setFont(getFont());
        jmenuitHelpSearch.setMnemonic('B');
        jmenuitHelpSearch.setText("Busca");
        jmenuitHelpSearch.setEnabled(false);
        jmenuitHelpSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitHelpSearchActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitHelpSearch);

        jmenuitKeyboardMap.setFont(getFont());
        jmenuitKeyboardMap.setMnemonic('T');
        jmenuitKeyboardMap.setText("Mapa do Teclado");
        jmenuitKeyboardMap.setEnabled(false);
        jmenuitKeyboardMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitKeyboardMapActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitKeyboardMap);

        jmenuHelp.add(jSeparator27);

        jmenuError.setFont(getFont());
        jmenuError.setMnemonic('E');
        jmenuError.setText("Relat\u00f3tios de erros");
        jmenuError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuErrorActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuError);

        jmenuitFeedbackSuporte.setFont(getFont());
        jmenuitFeedbackSuporte.setMnemonic('F');
        jmenuitFeedbackSuporte.setText("Feedback e Suporte");
        jmenuitFeedbackSuporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitFeedbackSuporteActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitFeedbackSuporte);

        jmenuitVerificaAtualizacoes.setFont(getFont());
        jmenuitVerificaAtualizacoes.setMnemonic('V');
        jmenuitVerificaAtualizacoes.setText("Verificar Atualiza\u00e7\u00f5es...");
        jmenuitVerificaAtualizacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitVerificaAtualizacoesActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitVerificaAtualizacoes);

        jmenuHelp.add(jSeparator28);

        jmenuitAbout.setFont(getFont());
        jmenuitAbout.setMnemonic('S');
        jmenuitAbout.setText("Sobre...");
        jmenuitAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuitAboutActionPerformed(evt);
            }
        });

        jmenuHelp.add(jmenuitAbout);

        menuBar.add(jmenuHelp);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmenuitSAPOWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitSAPOWebActionPerformed
        BrowserControl.displayURL(SAPOWEB);
    }//GEN-LAST:event_jmenuitSAPOWebActionPerformed
	
	private void jmenuitAtualizaRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitAtualizaRefActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_jmenuitAtualizaRefActionPerformed
	
	private void jbRelExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRelExcelActionPerformed
		new ExcelReport(this);
	}//GEN-LAST:event_jbRelExcelActionPerformed
	
	private void jbRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRedoActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jbRedoActionPerformed
	
	private void jbUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbUndoActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jbUndoActionPerformed
	
	private void jmenuitFindProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitFindProjectActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitFindProjectActionPerformed
	
	private void jmenuitReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitReplaceActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitReplaceActionPerformed
	
	private void jmenuitFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitFindActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitFindActionPerformed
	
	private void jmenuitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitDeleteActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitDeleteActionPerformed
	
	private void jmenuitPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitPasteActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitPasteActionPerformed
	
	private void jmenuitCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitCopyActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitCopyActionPerformed
	
	private void jmenuitCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitCutActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitCutActionPerformed
	
	private void jmenuitRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitRedoActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitRedoActionPerformed
	
	private void jmenuitUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitUndoActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitUndoActionPerformed
	
	private void jmenuitExpExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitExpExcelActionPerformed
		new ExcelReport(this);
	}//GEN-LAST:event_jmenuitExpExcelActionPerformed
	
	private void jmenuitVerificaAtualizacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitVerificaAtualizacoesActionPerformed
		BrowserControl.displayURL(ATUALIZAWEB);
	}//GEN-LAST:event_jmenuitVerificaAtualizacoesActionPerformed
	
	private void jmenuitFeedbackSuporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitFeedbackSuporteActionPerformed
		BrowserControl.displayURL(SUPORTEWEB);
	}//GEN-LAST:event_jmenuitFeedbackSuporteActionPerformed
	
	private void jmenuitKeyboardMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitKeyboardMapActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitKeyboardMapActionPerformed
	
	private void jmenuitHelpSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitHelpSearchActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitHelpSearchActionPerformed
	
	private void jmenuitHelpIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitHelpIndexActionPerformed
//		TODO add your handling code here:
	}//GEN-LAST:event_jmenuitHelpIndexActionPerformed
	
	private void jmenuitAjudaOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitAjudaOnlineActionPerformed
		BrowserControl.displayURL(AJUDAWEB);
	}//GEN-LAST:event_jmenuitAjudaOnlineActionPerformed
	
	private void jbtnNewProtocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewProtocolActionPerformed
		jmenuitProtocolUserActionPerformed(evt);
	}//GEN-LAST:event_jbtnNewProtocolActionPerformed
	
	private void jmenuitProtocolUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitProtocolUserActionPerformed
		showUserProtocolFrame();
	}//GEN-LAST:event_jmenuitProtocolUserActionPerformed
	
	private void jmenuitGCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitGCActionPerformed
		setExtendedState(JFrame.ICONIFIED);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		EscapeDialog freeMemory = new FreeMemory(this, scrollableDesktop, " Limpar Memória", true);
		this.repaint();
	}//GEN-LAST:event_jmenuitGCActionPerformed
	
	private void jbtnProtocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnProtocolActionPerformed
		jmenuItMarcaPontosProtocolActionPerformed(evt);
	}//GEN-LAST:event_jbtnProtocolActionPerformed
	
	private void jmenuItMarcaPontosProtocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuItMarcaPontosProtocolActionPerformed
		commonJifDeactivation();
		numImg = getIndiceImagemAtiva( );
		inserePainelEmInternalFrame(jpMarcaPontosProtocol, "WEST");
		marcaPontosProtocol.acoplar(numImg);
		jpMarcaPontosProtocol.mostraVista(paciente.dados.imgData[numImg].vista);
		jpMarca.atualizar();
		
	}//GEN-LAST:event_jmenuItMarcaPontosProtocolActionPerformed
	
	private void jmenuItDistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuItDistActionPerformed
		inserePainelEmInternalFrame(jpMedeDist, "WEST");
		medeDist.acoplar(numImg);
	}//GEN-LAST:event_jmenuItDistActionPerformed
	
	private void jbtnMedeDistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMedeDistActionPerformed
		jmenuItDistActionPerformed(evt);
	}//GEN-LAST:event_jbtnMedeDistActionPerformed
	
	private void JbImgSolDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbImgSolDownActionPerformed
		
	}//GEN-LAST:event_JbImgSolDownActionPerformed
	
	private void jbImgSolUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImgSolUpActionPerformed
		
	}//GEN-LAST:event_jbImgSolUpActionPerformed
	
	private boolean abreArquivoProjeto(String fileName) {
		File arq = null;
		if (fileName.equals("")) {
			setUILanguageJFileChooser();
			JFileChooser fc = new JFileChooser();
			String[] extensoes = new String[] {"sapo"};
			EFileFilter filter = new EFileFilter(extensoes, "Arquivos de Projetos");
			fc.addChoosableFileFilter(filter);
			fc.setDialogTitle("SAPO - Ler Projeto de Arquivo");
			if (user.dados.dirprojetos != "") {
				File prjDir = new File(user.dados.dirprojetos);
				if (prjDir.exists()) fc.setCurrentDirectory(prjDir);
			}
			if( fc.showOpenDialog(SAPO.this) != JFileChooser.APPROVE_OPTION ) return false;
			user.dados.dirprojetos = (fc.getSelectedFile()).getParent();
			arq = fc.getSelectedFile();
		}
		else
			arq = new File(fileName);
		
		try {
			return lePacienteDeArquivo(arq);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private boolean lePacienteDeArquivo(File arq) throws Exception {
		String xml = ""; 
		String linha;
		FileReader fr;
		try {
			fr = new FileReader(arq);
			BufferedReader br = new BufferedReader(fr);
			while ( (linha=br.readLine()) != null ) { 
				xml = xml + linha;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		XStream xstream = new XStream();
		paciente = (Paciente)xstream.fromXML(xml);
		if (paciente != null) {
			paciente.inicializa(this);
			return true;
		}
		else return false;
	}
	
	protected void openFile(String fileName) {
		cursorControl(SAPO.this, new PerformOp(){
			public void doAction() throws Exception {
				if (paciente != null) {
					paciente.escreveDB();
					paciente.salvarAlteracoes = false;
				}
				removerComponentesDoDesktop();
			}
		});
		if ((abreArquivoProjeto(fileName)) && (paciente.dadosInterface != null)) 
			cursorControl(SAPO.this, new PerformOp(){
				public void doAction() throws Exception {
					final MsgPane waitMsg = new MsgPane();
					SwingWorker aWorker = new SwingWorker() {
						public Object doInBackground() {
							restauraProjeto();
							clearGlassPane(SAPO.this);
							return null;
						}
					};
					setGlassPane(waitMsg);
					waitMsg.setText("Abrindo arquivo ...");
					getGlassPane().setVisible(true);
					aWorker.execute();
				}
			});
		else 
			JOptionPane.showMessageDialog(this,
					"Não foi possível abrir o arquivo",
					"Erro",JOptionPane.ERROR_MESSAGE);
	}
	
	private void jmenuitOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitOpen1ActionPerformed
		commonJifDeactivation();
		openFile("");
	}//GEN-LAST:event_jmenuitOpen1ActionPerformed
	
	private void jmenuitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitCloseActionPerformed
		closeProject();
	}//GEN-LAST:event_jmenuitCloseActionPerformed
	
	public void closeProject() {
		commonJifDeactivation();
		cursorControl(SAPO.this, new PerformOp(){
			public void doAction() throws Exception {
				if (paciente != null) {
					paciente.escreveDB();
					paciente.salvarAlteracoes = false;
				}
				removerComponentesDoDesktop();
				setEnabledAcessos(false);
				paciente = null;
			}
		});
	}
	
	private void jmenuitSujeitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitSujeitoActionPerformed
		try { 
			if (paciente != null) {
				paciente.dadosInterface.setIcon(false);
				paciente.dadosInterface.moveToFront();
			}
		} catch (PropertyVetoException e) { }
	}//GEN-LAST:event_jmenuitSujeitoActionPerformed
	
	
	private void jmenuitUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitUsuarioActionPerformed
		uiu = user.getUIUser();
		incluirListenerInternalFrame(uiu);
		scrollableDesktop.add(uiu);
	}//GEN-LAST:event_jmenuitUsuarioActionPerformed
	
	private void jcbmenuitEditImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbmenuitEditImageActionPerformed
		if ( jif[numImg].flagImg) jtbEdicao.doClick();
	}//GEN-LAST:event_jcbmenuitEditImageActionPerformed
	
	private void jcbmenuitDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbmenuitDrawActionPerformed
		if (jif[numImg].flagImg) jtbDesenho.doClick();        
	}//GEN-LAST:event_jcbmenuitDrawActionPerformed
	
	private void mostraToolbars(boolean opt) {
		jToolBar1.setVisible(false);
		jpMain3.remove(jToolBar1);
		if (opt) {
			jpMain3.add(jToolBar1, BorderLayout.NORTH);
			ToolBarUI tbUI = jToolBar1.getUI();
			if (tbUI instanceof BasicToolBarUI) {
				((BasicToolBarUI)tbUI).setOrientation(0);
			}
			jToolBar1.setVisible(true);
		}
		else {
			if (jToolBar1.getTopLevelAncestor() != null) {
				jToolBar1.getTopLevelAncestor().setVisible(false);
			}
		}
		this.repaint();
	}
	
	private void jcbmenuitToolbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbmenuitToolbarActionPerformed
		if (jcbmenuitToolbar.isSelected()) mostraToolbars(true);
		else mostraToolbars(false);
	}//GEN-LAST:event_jcbmenuitToolbarActionPerformed
	
	public void desejaSalvarImagem() {
		Object[] options = {"Sim","Não"};
		int result = JOptionPane.showOptionDialog(SAPO.this,
				"A imagem foi alterada !  \nDeseja salvar a imagem?  ",
				"Aviso",JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		if( result == JOptionPane.YES_OPTION) dialogoSalvarImagem();
	}
	
	private void jmenuHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuHelpActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_jmenuHelpActionPerformed
	
	private void jmenuErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuErrorActionPerformed
		jdlgErrorReport.pack();
		jdlgErrorReport.setVisible(true);
	}//GEN-LAST:event_jmenuErrorActionPerformed
	
	private void jmenuitHelpContentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitHelpContentActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_jmenuitHelpContentActionPerformed
	
	public boolean desejaProsseguir() { 
		boolean retorno = true;
		Object[] options = {"Sim","Não"};
		int result = JOptionPane.showOptionDialog(SAPO.this,
				"Deseja realmente sair ?", "Encerrando Aplicação",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		if( result == JOptionPane.NO_OPTION) retorno = false;
		return retorno;
	} 
	
	private void restauraProjeto() {
		setEnabledAcessos(true);
		incluirListenerProjeto(paciente.dadosInterface);
		scrollableDesktop.add(paciente.dadosInterface);
		for (int i=1; i < maxImg; i++){ 
			try{
				if ((paciente.dados.imgData[i] != null) &&
						(paciente.dados.imgData[i].getFileImage() != null)) 
					openImage(paciente.dados.imgData[i].getFileImage());
			}
			catch(Exception e){
				errorReport.jtxtErrorReport.append(e.toString()+"\n");
				e.printStackTrace();
				error = e.getStackTrace();
				for(int k=0; k<error.length; k++)
					errorReport.jtxtErrorReport.append(error[k].toString()+"\n");
				errorReport.jtxtErrorReport.append("==========================================="+"\n");
				jdlgErrorReport.pack();
				jdlgErrorReport.setVisible(true);      
			}
		}
		scrollableDesktop.setSelectedFrame(paciente.dadosInterface);
	}
	
	protected void openFromDBDialog() {
		if (paciente != null) {
			paciente.escreveDB();
			paciente.salvarAlteracoes = false;
		}
		jDialogSAPO = new EscapeDialog(SAPO.this, "Base de Dados de Análises (Local)", true);
		jDialogSAPO.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		projMan = new ProjectManager(this);
		jDialogSAPO.getContentPane().add(projMan);
		jDialogSAPO.pack();
		FormUtilities.centerInForm(this, jDialogSAPO);
		jDialogSAPO.setVisible(true);
		//}
	}
	
	protected void openFromDB(final long nID, boolean restaura) {
		disponivel = false;
		setCursor(busyCursor);
		removerComponentesDoDesktop();
		paciente = new Paciente(nID, SAPO.this);
		setCursor(defaultCursor);
		if (restaura)
			cursorControl(SAPO.this, new PerformOp(){
				public void doAction() throws Exception {
					final MsgPane waitMsg = new MsgPane();
					SwingWorker aWorker = new SwingWorker() {
						public synchronized Object doInBackground() {
							restauraProjeto();
							paciente.salvarAlteracoes = false;
							clearGlassPane(SAPO.this);
							disponivel = true;
							return null;
						}
					};
					setGlassPane(waitMsg);
					waitMsg.setText("Abrindo do banco de dados ...");
					getGlassPane().setVisible(true);
					aWorker.execute();
				}
			});
	}
	
	protected void deleteProjectInDB(long nID) {
		(new Paciente(this)).deleteProject(nID);
		if ((paciente != null) && (paciente.dados.nID == nID)) {
			removerComponentesDoDesktop();
			setEnabledAcessos(false);
			paciente = null;
		}
	}
	
	protected void cleanAllInDB() {
		(new Paciente(this)).cleanProject();
		if (paciente != null) {
			removerComponentesDoDesktop();
			setEnabledAcessos(false);
			paciente = null;
		}
	}
	
	private void jmenuitOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitOpenActionPerformed
		commonJifDeactivation();
		openFromDBDialog();
	}//GEN-LAST:event_jmenuitOpenActionPerformed
	
	private void jmenuitSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitSaveAsActionPerformed
		salvarEmArquivo();
	}//GEN-LAST:event_jmenuitSaveAsActionPerformed
	
	public void salvarEmArquivo() {
		commonJifDeactivation();
		paciente.salvarComo();
	}
	
	public void mostraZoom(JPanel jp, String obj) {
		inserePainelEmInternalFrame(jp, "WEST");
		//numImgCV = numImg;
		jif[numImg].jaiP.setPaint(false, false, false, false);
		if(obj.equals("MarcaPontosProtocol")){
			marcaPontosProtocol.acoplar(numImg);
			jpMarca.atualizar();
		}
		if(obj.equals("MarcaPontos")){
			marca.acoplar(numImg);
			jpMarca.atualizar();
		}
		if(obj.equals("MedeDistancia"))
			medeDist.acoplar(numImg);
		if(obj.equals("MedeAngulos"))
			medeAngulos.acoplar(numImg);
		
	}
	
	public void mostraZoom() {
		inserePainelEmInternalFrame(jpnlZoom, "WEST");
		jpnlZoom.jSlider1.setValue(jif[numImg].zoom);
		jpnlZoom.mostraOpcao();
		jpnlZoom.jTextField5.setText(String.valueOf(jif[numImg].zoom));
		jif[numImg].jaiP.setPaint(false, false, false, false);
		zoomClass.acoplar(numImg);
	}
	
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		mostraZoom();
	}//GEN-LAST:event_jButton1ActionPerformed
	
	private void dialogoSalvarImagem() {
		if (jif[numImg].flagImg) {
			setUILanguageJFileChooser();
			final JFileChooser fc = new JFileChooser();
			String[] extensoes = new String[] {"jpg", "png", "tif", "bmp"};
			EFileFilter filter = new EFileFilter(extensoes, "Arquivos de Imagens");
			fc.addChoosableFileFilter(filter);
			fc.setDialogTitle("SAPO - Salvar Imagem");
			
			fc.setSelectedFile(jif[numImg].file);
			
			int returnVal = fc.showSaveDialog(SAPO.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String fileName = fc.getSelectedFile().getPath();
				if ( (! fileName.toUpperCase().endsWith(".JPG")) &&
						(! fileName.toUpperCase().endsWith(".PNG")) &&
						(! fileName.toUpperCase().endsWith(".TIF")) &&
						(! fileName.toUpperCase().endsWith(".BMP")) ) { fileName += ".jpg"; }
				
				saveImage(fileName);
				File newFile = new File(fileName);
				paciente.dados.imgData[numImg].fileImage = newFile;
				jif[numImg].file = newFile;
				jlblStatus.setText(newFile.getPath());
				jif[numImg].setTitle((fc.getSelectedFile()).getName());
				user.escreveDB();
				paciente.escreveDB();
				paciente.salvarAlteracoes = false;
				frmImagem.inicializaParametros();
			}
		}
	}
	
	void saveImage(final String fileName) {
		final String encoder;
		int len = fileName.length();
		String ext = fileName.substring(len-3,len).toUpperCase();
		if (ext.contentEquals(new StringBuffer("JPG"))) encoder = "JPEG";
		else if (ext.contentEquals(new StringBuffer("TIF"))) encoder = "TIFF";
		else encoder = ext;
		// BMP, GIF, JPEG, PNG, PNM, TIFF, WBPM
		
		final MsgPane waitMsg = new MsgPane();
		SwingWorker aWorker = new SwingWorker() {
			public Object doInBackground() {
				JAI.create("filestore",jif[numImg].getRenderedImage(),fileName,encoder);
				clearGlassPane(SAPO.this);
				return null;
			}
		};
		setGlassPane(waitMsg);
		waitMsg.setText("Salvando imagem ...");
		getGlassPane().setVisible(true);
		aWorker.execute();
	}
	
	private void jmenuitSaveImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitSaveImageActionPerformed
		dialogoSalvarImagem();
	}//GEN-LAST:event_jmenuitSaveImageActionPerformed
	
	private void jbOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenActionPerformed
		jmenuitOpenActionPerformed(evt); 
	}//GEN-LAST:event_jbOpenActionPerformed
	
	private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
		paciente.salvar();
	}//GEN-LAST:event_jbSaveActionPerformed
	
	
	private void jbPrintRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPrintRelActionPerformed
		jmenuitImprRelActionPerformed(evt);
	}//GEN-LAST:event_jbPrintRelActionPerformed
	
	private void jbRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRelatorioActionPerformed
		jmenuitGeraRelActionPerformed(evt);
	}//GEN-LAST:event_jbRelatorioActionPerformed
	
	private void jbNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNewActionPerformed
		jmenuitNewFileActionPerformed(evt);
	}//GEN-LAST:event_jbNewActionPerformed
	
	private void jmenuitImprRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitImprRelActionPerformed
		printRel();
	}//GEN-LAST:event_jmenuitImprRelActionPerformed
	
	private void printRel() {
		CreatePDF pdf = new CreatePDF(this);
		pdf.setConfig(null,null);
	}
	
	private void jmenuitGeraRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitGeraRelActionPerformed
		showRelatorioFrame();
	}//GEN-LAST:event_jmenuitGeraRelActionPerformed
	
	private void setEnabledAcessos( boolean b){
		jmenuitSaveAs.setEnabled(b);
		jmenuitClose.setEnabled(b);
		jbImagem.setEnabled(b);
		jmenuitOpenImage.setEnabled(b);
		jmenuitGeraRel.setEnabled(b);
		jmenuitImprRel.setEnabled(b);
		jbPrintRel.setEnabled(b);
		jbRelatorio.setEnabled(b); 
		jbRelExcel.setEnabled(b);
		jmenuitExpExcel.setEnabled(b);
		jbSave.setEnabled(b);
		
		// jmenuEdit.setVisible(b);
		
	}
	
	private void incluirListenerProjeto(final BaseInternalFrame projectInterface) {
		
		projectInterface.addInternalFrameListener(
				new javax.swing.event.InternalFrameListener() {
					public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) { 
						numImg = 0; 
						jlblStatus.setText(dbDirPath);
					}
					public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
						removerComponentesDoDesktop();
						setEnabledAcessos(false);
					}
					public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
						projectInterface.dispose();
					}
					public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {}
					public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) { 
						numImg = 0; 
						jlblStatus.setText(dbDirPath);
					}
					public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {}
					public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) { 
						numImg = 0; 
						jlblStatus.setText(dbDirPath);
					}
				});
	}
	
	void incluirListenerInternalFrame(BaseInternalFrame bif) {
		
		bif.addInternalFrameListener(
				new javax.swing.event.InternalFrameListener() {
					private void all() { 
						numImg = 0;
						jlblStatus.setText(dbDirPath);
					}
					public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {all();} 
					public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {}
					public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {}
					public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {}
					public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {all();} 
					public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {}
					public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {all();} 
				});
	}
	
	protected void newProject() {
		commonJifDeactivation();
		isNewProject = true;
		cursorControl(SAPO.this, new PerformOp(){
			public void doAction() throws Exception {
				if (paciente != null) {
					paciente.escreveDB();
					paciente.salvarAlteracoes = false;
				}
				removerComponentesDoDesktop();
				setEnabledAcessos(true);
				paciente = new Paciente(SAPO.this);
				incluirListenerProjeto( paciente.dadosInterface );
				scrollableDesktop.add( paciente.dadosInterface );
				openImageManager();
			}
		});
	}
	
	private void doNewProject() {
		
	}
	
	private void jmenuitNewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitNewFileActionPerformed
		newProject();
	}//GEN-LAST:event_jmenuitNewFileActionPerformed
	
	
	void removerComponentesDoDesktop() {
		JInternalFrame jif = scrollableDesktop.getSelectedFrame();
		try {
			while( jif != null)	{
				jif.setClosed(true);
				jif = null;
				jif = scrollableDesktop.getSelectedFrame();
			}
		} catch (Exception e){
			errorReport.jtxtErrorReport.append(e.toString()+"\n");
			e.printStackTrace();
			error = e.getStackTrace();
			for(int k=0; k<error.length; k++)
				errorReport.jtxtErrorReport.append(error[k].toString()+"\n");
			errorReport.jtxtErrorReport.append("==========================================="+"\n");
			jdlgErrorReport.pack();
			jdlgErrorReport.setVisible(true);      
		}
		numImg = 0;
	}
	
	public double doubleLoc (String s) {
		double retorno = 0.0;
		s = s.replace(".", ",");
		try {
			retorno = ((Double) numFormat.parse(s)).doubleValue();
		} catch (Exception e1) {
			try {
				retorno = ((Long) numFormat.parse(s)).longValue();
			} catch (Exception e2) {
				e1.printStackTrace();
				e2.printStackTrace();
			}
		}
		return retorno;
	}
	
	public boolean isNumber(String str){
		try{
			str = str.replace(",", ".");
			double ttt = Double.parseDouble(str);
			return true;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(SAPO.this,"Número inválido!");
			return false;
		}
	}
	
	private void jbEscalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEscalaActionPerformed
		jmenuitScaleActionPerformed(evt);
	}//GEN-LAST:event_jbEscalaActionPerformed
	
	private void jtbEdicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbEdicaoActionPerformed
		if (jtbEdicao.isSelected()) {
			jpMain2.add(tbrImagem.toolBar, java.awt.BorderLayout.SOUTH);
			jcbmenuitEditImage.setSelected(true);
		}
		else {
			jpMain2.remove(tbrImagem.toolBar);
			jcbmenuitEditImage.setSelected(false);
		}
		jpMain2.setVisible(false);
		jpMain2.setVisible(true);
	}//GEN-LAST:event_jtbEdicaoActionPerformed
	
	private void jmenuitZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitZoomActionPerformed
		commonJifDeactivation();
		jButton1.doClick();
	}//GEN-LAST:event_jmenuitZoomActionPerformed
	
	private void jtbDesenhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbDesenhoActionPerformed
		if (jtbDesenho.isSelected()) {
			jpMain.add(tbrDesenho.toolBar, java.awt.BorderLayout.SOUTH); 
			jcbmenuitDraw.setSelected(true);
			tbrDesenho.resetButtons();
			tbrDesenho.resetColor();
		}
		else {
			jpMain.remove(tbrDesenho.toolBar); 
			jcbmenuitDraw.setSelected(false);
		}
		jpMain.setVisible(false);
		jpMain.setVisible(true);
		corDesenho = java.awt.Color.RED;
	}//GEN-LAST:event_jtbDesenhoActionPerformed
	
	private void jbImagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImagemActionPerformed
		jmenuitOpenImageActionPerformed(evt);
	}//GEN-LAST:event_jbImagemActionPerformed
	
	private void jbMarcaPontosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMarcaPontosActionPerformed
		jmenuitMarcaActionPerformed(evt);
	}//GEN-LAST:event_jbMarcaPontosActionPerformed
	
	private void jbtnAnguloMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAnguloMenuActionPerformed
		jmenuitAngulosActionPerformed(evt);
	}//GEN-LAST:event_jbtnAnguloMenuActionPerformed
	
	private void jbCalibraVerticalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCalibraVerticalActionPerformed
		jmenuitVerticalActionPerformed(evt);
	}//GEN-LAST:event_jbCalibraVerticalActionPerformed
	
	private void jmenuitAngulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitAngulosActionPerformed
		jpAngulos.limpaTabela();
		inserePainelEmInternalFrame(jpAngulos, "WEST");
		medeAngulos.acoplar(numImg);
		jpAngulos.atualizar();
	}//GEN-LAST:event_jmenuitAngulosActionPerformed
	
	private void jmenuitMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitMarcaActionPerformed
		commonJifDeactivation();
		numImg = getIndiceImagemAtiva( );
		inserePainelEmInternalFrame(jpMarca, "WEST");
		marca.acoplar(numImg);
		jpMarca.atualizar();
	}//GEN-LAST:event_jmenuitMarcaActionPerformed
	
	public void setZoom(int x){
		int largura = (int)((jif[numImg].getRenderedImage().getWidth())*(jif[numImg].zoom/100));
		try{
			zoomValue = (int)(100*largura/x);
			jpnlZoom.jSlider1.setValue(zoomValue);
		}
		catch(ArithmeticException e){
			errorReport.jtxtErrorReport.append(e.toString()+"\n");
			e.printStackTrace();
			error = e.getStackTrace();
			for(int k=0; k<error.length; k++)
				errorReport.jtxtErrorReport.append(error[k].toString()+"\n");
			errorReport.jtxtErrorReport.append("==========================================="+"\n");
			jdlgErrorReport.pack();
			jdlgErrorReport.setVisible(true);      
		}
	}
	
	private void jmenuitVerticalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitVerticalActionPerformed
		commonJifDeactivation();
		cvPanel.calibraVerticalClear();
	}//GEN-LAST:event_jmenuitVerticalActionPerformed
	
	void removePainelDeOutros(int num) {
		try{
			for (int i=1; i < SAPO.maxImg; i++)
				if (jif[i] != null) {
					jif[i].setMaximum(false);
					desacoplar(i);
					showInternalFrameWithImage(i);
				}
			if (num > 0) {
				numImg = num;
				showInternalFrameWithImage(numImg);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getIndiceImagemAtiva(){
		try {
			int i = 0;
			if (scrollableDesktop.getSelectedFrame() == null) i = -1;
			else if (scrollableDesktop.getSelectedFrame() instanceof BaseInternalFrameSAPO) 
				i = ((BaseInternalFrameSAPO)scrollableDesktop.getSelectedFrame()).numImg;
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	private void ajustarAnalise(){
		boolean habilitarAnalise = false;
		if( getIndiceImagemAtiva() >= 1) habilitarAnalise = true;	
		
		if (!habilitarAnalise && jtbDesenho.isSelected()) jtbDesenho.doClick();
		if (!habilitarAnalise && jtbEdicao.isSelected())  jtbEdicao.doClick();
		
		jtbDesenho.setEnabled( habilitarAnalise );
		jtbEdicao.setEnabled( habilitarAnalise );
		
		jButton1.setEnabled( habilitarAnalise );
		jbCalibraVertical.setEnabled( habilitarAnalise );
		jbEscala.setEnabled( habilitarAnalise );
		jbtnAnguloMenu.setEnabled( habilitarAnalise );
		jbMarcaPontos.setEnabled( habilitarAnalise );
		jmenuitZoom.setEnabled( habilitarAnalise );
		jcbmenuitEditImage.setEnabled( habilitarAnalise );
		jmenuitSaveImage.setEnabled( habilitarAnalise );
		jcbmenuitDraw.setEnabled( habilitarAnalise );
		jmenuitVertical.setEnabled( habilitarAnalise );
		jmenuitScale.setEnabled( habilitarAnalise );
		jmenuitMarca.setEnabled( habilitarAnalise );
		jmenuitAngulos.setEnabled( habilitarAnalise );
		jcbmenuitDraw.setEnabled( habilitarAnalise );
		jcbmenuitEditImage.setEnabled( habilitarAnalise );
		jbtnMedeDist.setEnabled(habilitarAnalise);
		jmenuItDist.setEnabled(habilitarAnalise);
		jbtnProtocol.setEnabled(habilitarAnalise);
		jmenuItMarcaPontosProtocol.setEnabled(habilitarAnalise);
		
	}    
	
	public void inserePainelEmInternalFrame(JPanel painel, String type) {
		
		/*if (jif[numImgCV] != null) removePainelDeOutros(numImgCV);
		 if (jif[numImgCE] != null) removePainelDeOutros(numImgCE);
		 if (jif[numImgA1] != null) removePainelDeOutros(numImgA1);
		 if (jif[numImgAng] != null) removePainelDeOutros(numImgAng);*/ 
		
		removePainelDeOutros(numImg);
		if(type.equals("WEST")) jif[numImg].getContentPane().add(painel, java.awt.BorderLayout.WEST);
		if(type.equals("EAST")) jif[numImg].getContentPane().add(painel, java.awt.BorderLayout.EAST);
		if(type.equals("NORTH")) jif[numImg].getContentPane().add(painel, java.awt.BorderLayout.NORTH);
		if(type.equals("SOUTH")) jif[numImg].getContentPane().add(painel, java.awt.BorderLayout.SOUTH);
		painel.setVisible(true);
		int jDP1W = scrollableDesktop.getWidth();
		int jDP1H = scrollableDesktop.getHeight();
		int ifX = jif[numImg].getX()+10;
		int ifY = jif[numImg].getY()+30;
		int w = jif[numImg].getWidth()+150;
		int h = jif[numImg].getHeight()+150;
		int jw = ((ifX+w+8) > jDP1W) ? jDP1W-ifX : w+12;
		int jh = ((ifY+h+32) > jDP1H) ? jDP1H-ifY : h+40;
		jif[numImg].reshape(jif[numImg].getX(), jif[numImg].getY(), jw, jh);
		jif[numImg].pack();
		try{
			jif[numImg].setMaximum(true);
		}catch(Exception e){};
	}
	
	public void restauraInternalFrameOriginal() {
		try{
			jif[numImg].setMaximum(false);
		}catch(Exception e){};
		showInternalFrameWithImage(numImg);
	}
	
	public void desacoplar(int n) {
		if ((jif[n] != null) && (jif[n].jaiP != null)) {
			MouseListener[] mls = (MouseListener[])(jif[n].jaiP.getListeners(MouseListener.class));
			for (int i=0; i<mls.length; i++) {
				String superclasse = mls[i].getClass().getSuperclass().getName();
				boolean isLeJAIPanel = superclasse.equalsIgnoreCase("LeJAIPanel");
				if ((mls[i] instanceof LeJAIPanel) || isLeJAIPanel) 
					((LeJAIPanel)mls[i]).desacoplar(n);
			}
		}
	}
	
	private void jmenuitScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitScaleActionPerformed
		commonJifDeactivation();
		inserePainelEmInternalFrame(jpCalEscala, "WEST");
	}//GEN-LAST:event_jmenuitScaleActionPerformed
	
	private void mostraConfig() {
		jDialogSAPO = new EscapeDialog(SAPO.this, "Preferências", true);
		jDialogSAPO.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Config pconfig = new Config(this);
		pconfig.mostraConfiguracoes();
		jDialogSAPO.getContentPane().add(pconfig);
		jDialogSAPO.setSize(600,480);
		FormUtilities.centerInForm(this, jDialogSAPO);
		jDialogSAPO.setVisible(true);
	}
	
	/** Mostra diálogo que configura unidades, diretório, etc. */
	private void jmenuitPreferenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitPreferenciasActionPerformed
		mostraConfig();
	}//GEN-LAST:event_jmenuitPreferenciasActionPerformed
	
	private void mostraSobre() {
		jDialogSAPO = new EscapeDialog(SAPO.this, "Sobre", true);
		jDialogSAPO.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jDialogSAPO.getContentPane().setLayout(new BorderLayout());
		jDialogSAPO.getContentPane().add(new Sobre(this), BorderLayout.CENTER);
		jDialogSAPO.pack();
		FormUtilities.centerInForm(this, jDialogSAPO);
		jDialogSAPO.setVisible(true);
	}
	
	/** Mostra diálogo "sobre" */
	private void jmenuitAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitAboutActionPerformed
		mostraSobre();
	}//GEN-LAST:event_jmenuitAboutActionPerformed
	
	/** Ação do Menu Imagens - > desenhar */    
	/** invoca chacoalha() */
	/** Arruma exibição de componentes ao mudar o tamanho da janela principal */    
	/** ação do slider que muda o zoom da imagem */
	/**
	 * este método recebe uma imagem e aplica um zoom por interpolação "NEAREST"
	 *             @return	RenderedImage com zoom
	 */
	public RenderedImage zoomImage(RenderedImage image, int x, int y, int zoom, int type) {
		float scX, scY;
		scX = scY = zoom / 100.0F;
		/*
		 * Create a standard Nearest interpolation object to be
		 * used with the "scale" operator.
		 */
		Interpolation interp = Interpolation.getInstance(type);
		//Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
		
		/**
		 * Stores the required input source and parameters in a
		 * ParameterBlock to be sent to the operation registry,
		 * and eventually to the "scale" operator.
		 */
		ParameterBlock params = new ParameterBlock();
		params.addSource(image);
		params.add(scX);         // x scale factor
		params.add(scY);         // y scale factor
		params.add(x*1.0F);      // x translate
		params.add(y*1.0F);      // y translate
		params.add(interp);      // interpolation method
		/* Create an operator to scale image1. */
		if (image != null) image = JAI.create("scale", params);
		return image;
	}
	
	/**
	 * este método cria um MDisplayJAI para uma imagem, adiciona-o a um JScrollPane
	 * corretamente dimensionado para exibição dentro do jDesktopPane1, e adiciona
	 * este JScrollPane ao BaseIntecnalFrame passado como parâmetro.
	 */
	protected void showInternalFrameWithImage(int num) { 
		RenderedImage rImage = jif[num].getRenderedImage();
		int xAnchor = 0;
		
		int yAnchor = 0;
		if (!paciente.dados.imgData[num].isImgRotate()) {
			xAnchor = paciente.dados.imgData[num].getXAnchor();
			yAnchor = paciente.dados.imgData[num].getYAnchor();
		}
		else {
			rImage = ImageRotate.rotateImage( rImage, 
					paciente.dados.imgData[num].getXAnchor(),
					paciente.dados.imgData[num].getYAnchor(),
					paciente.dados.imgData[num].getAnguloVertical());
			System.gc();
		}
		rImage = zoomImage(rImage, xAnchor, yAnchor, jif[num].zoom, Interpolation.INTERP_NEAREST);
		
		jif[num].jaiP = new JAIPanelSAPO(rImage);
		JScrollPane sPane = new JScrollPane(jif[num].jaiP);
		
		int imW = rImage.getWidth();
		int imH = rImage.getHeight();
		int jDP1W = scrollableDesktop.getWidth();
		int jDP1H = scrollableDesktop.getHeight();
		int w = imW > jDP1W-6 ? jDP1W : imW+6;
		int h = imH > jDP1H-2 ? jDP1H : imH+2;
		sPane.setSize(w, h);
		
		BaseInternalFrame bIFrame = jif[num];
		bIFrame.getContentPane().removeAll();
		bIFrame.getContentPane().add(sPane, java.awt.BorderLayout.CENTER);
		
		int ifX = bIFrame.getX()+10;
		int ifY = bIFrame.getY()+30;
		int jw = ((ifX+w+12) > jDP1W) ? jDP1W-ifX : w+12;
		int jh = ((ifY+h+40) > jDP1H) ? jDP1H-ifY : h+40;
		bIFrame.reshape(bIFrame.getX(), bIFrame.getY(), jw, jh) ;
		bIFrame.setVisible(true);
		rImage = null;
	}
	
	
	/**
	 * ação do openImageMenuItem. Abre um diálogo para escolha do arquivo de imagem,
	 * 
	 */
	protected void openImageManager() { 
		setUILanguageJFileChooser();
		String[] extensoes = new String[] {"gif", "jpg", "png", "tif", "bmp", "fpx", "pgm", "ppm"};
		EFileFilter filter = new EFileFilter(extensoes, "Arquivos de Imagens");
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("SAPO - Abrir Imagem");
		fc.addChoosableFileFilter(filter);
		if (user.dados.dirimagens != "") {
			File imgDir = new File(user.dados.dirimagens);
			if (imgDir.exists()) fc.setCurrentDirectory(imgDir);
		}
		int returnVal = fc.showOpenDialog(SAPO.this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			final File imgFile = fc.getSelectedFile();
			user.dados.dirimagens = (imgFile).getParent();
			
			cursorControl(SAPO.this, new PerformOp(){
				public void doAction() throws Exception {
					final MsgPane waitMsg = new MsgPane();
					SwingWorker aWorker = new SwingWorker() {
						public Object doInBackground() {
							String temp = optimizaTamanhoImagemSalva(imgFile);
							if (temp != null) {
								File tempf = new File(temp);
								openImage(tempf);
								if ((jif[numImg] != null) && (jif[numImg].jaiP != null)) {
									
									ArrayList arrOpcoes = new ArrayList();
									if (paciente.dados.getVista("anterior") == -1) arrOpcoes.add("anterior");
									if (paciente.dados.getVista("lateral direita") == -1) arrOpcoes.add("lateral direita");
									if (paciente.dados.getVista("lateral esquerda") == -1) arrOpcoes.add("lateral esquerda");
									if (paciente.dados.getVista("posterior") == -1) arrOpcoes.add("posterior");
									if (arrOpcoes.size() > 0) {
										arrOpcoes.add("não definido");
										String[] opcoes = new String[arrOpcoes.size()];
										for(int j=0; j<arrOpcoes.size(); j++)
											opcoes[j] = (String)arrOpcoes.get(j);
										
										String vista = (String)JOptionPane.showInputDialog(SAPO.this,"Escolha uma vista", "para esta imagem",JOptionPane.INFORMATION_MESSAGE,null,opcoes,opcoes[0]);
										paciente.dados.imgData[numImg].setVista(vista);
									}
									else paciente.dados.imgData[numImg].setVista("não definido");
									
									user.escreveDB();
									if ((numImg > 0) && (jif[numImg] != null) && jif[numImg].flagImg)
										paciente.escreveDB();
								}
							}
							paciente.salvarAlteracoes = false;
							clearGlassPane(SAPO.this);
							if (isNewProject) {
								scrollableDesktop.setSelectedFrame( paciente.dadosInterface );
								isNewProject = false;
							}
							return null;
						}
					};
					setGlassPane(waitMsg);
					waitMsg.setText("Abrindo imagem ...");
					getGlassPane().setVisible(true);
					aWorker.execute();
				}
			});
		}
	}
	
	private void jmenuitOpenImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitOpenImageActionPerformed
		commonJifDeactivation();
		final int nroImg = getIndiceImagemDisponivel();
		if( nroImg < 1 ) {
			JOptionPane.showMessageDialog(SAPO.this,
					"Nesta versão, este programa\nnão permite abrir mais que\n" +
					+(SAPO.maxImg-1)+" imagens simultaneamente.",
					"Limite Atingido", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		openImageManager();
	}//GEN-LAST:event_jmenuitOpenImageActionPerformed
	
	
	private void openImage(final File fileImg){
		
		if( fileImg == null ) return;    
		final int nroImg = getIndiceImagemDisponivel();
		if( nroImg < 1 ) return;
		numImg = nroImg;
		jif[numImg] = new BaseInternalFrameSAPO(this, fileImg);
		if ((jif[numImg] != null) && (jif[numImg].jaiP != null)) {
			jif[numImg].numImg = numImg;
			jif[numImg].flagImg = true;
			EmptyDesktopIconUI.createUI(jif[numImg]);
			
			jif[nroImg].setVisible(false);
			scrollableDesktop.add(jif[numImg]);
			
			/* Create an operator to decode the image file. */
			RenderedImage img = jif[numImg].getRenderedImage();
			
			int oZoom = calculaZoomOtimo(img.getWidth(), img.getHeight());
			jif[numImg].zoom = oZoom;
			
			addJifInternalFrameListener();
			showInternalFrameWithImage(numImg);
			paciente.dados.imgData[numImg].setFileImage(fileImg);
		}
		
		release();
	}// fim de openImage
	
	private void commonJifActivation() {
		ajustarAnalise();
		numImg = getIndiceImagemAtiva();
		frmImagem.inicializaParametros();
		jlblStatus.setText(jif[numImg].file.getPath());
	}
	
	private void commonJifDeactivation() {
		if (frmImagem.editou) {
			if ((jif[numImg] != null)) {
				this.tbrImagem.doOk();
			}
			frmImagem.editou = false;
		}
		ajustarAnalise();
	}
	
	private void closingJif() {
		cursorControl(jif[numImg], new PerformOp(){
			public void doAction() throws Exception {
				int nroImg = getIndiceImagemAtiva();
				if( nroImg < 1 ) return;
				jif[nroImg].flagImg = false;
				jif[nroImg] = null;
				numImg = paciente.destroiImagem( nroImg );
				ajustarAnalise();
				release();
			}
		});
	}
	
	private void addJifInternalFrameListener() {
		jif[numImg].addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
			public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
				commonJifActivation();
			}
			public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
			}
			public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
				commonJifDeactivation();
				closingJif();
				Thread.currentThread().interrupt();
			}
			public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
				commonJifDeactivation();
			}
			public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
				commonJifActivation();
			}
			public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
				commonJifDeactivation();
			}
			public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
				commonJifActivation();
			}
		});
		
	}
	
	public String optimizaTamanhoImagemSalva(final File fileImg){
		FileSeekableStream stream = null;
		int oZoom = 100;
		try {
			stream = new FileSeekableStream(fileImg.getPath());
		} catch (IOException e) {
			errorReport.jtxtErrorReport.append(e.toString()+"\n");
			e.printStackTrace();
			error = e.getStackTrace();
			for(int k=0; k<error.length; k++)
				errorReport.jtxtErrorReport.append(error[k].toString()+"\n");
			errorReport.jtxtErrorReport.append("==========================================="+"\n");
			jdlgErrorReport.pack();
			jdlgErrorReport.setVisible(true);
		}
		if (fileImg.exists() && (fileImg.length() > 0)) {
			RenderedImage img = JAI.create("stream", stream);
			oZoom = calculaZoomOtimo(img.getWidth(),img.getHeight());
			if (oZoom < 100) oZoom*=2;
			img = zoomImage(img, 0, 0, oZoom, Interpolation.INTERP_BICUBIC);
			String fileName = fileImg.getName();
			fileName = figDirPath + sep + fileName;
			String encoder;
			int len = fileName.length();
			encoder = "JPEG";
			fileName = fileName.substring(0,len-4) + ".jpg";
			while (fileName.indexOf("..") != -1) 
				fileName = fileName.substring(0,fileName.indexOf("..")) + fileName.substring(fileName.indexOf("..")+1);
			JAI.create("filestore",img,fileName,encoder);
			return fileName;
		}
		else return null;
	}
	
	/** Primeiro índice de imagem disponível (1..n)=?, com conteúdo null */
	public int getIndiceImagemDisponivel(){
		try {
			for (int i=1; i < SAPO.maxImg; i++)
				if ( jif[i] == null) return i;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}//getIndiceImagemDisponivel    
	
	
	public int calculaZoomOtimo(int width, int height/*, int nImg*/) {
		int oZoom = 100;
		int difW = 0;
		int difH = 0;
		int dktWidth = scrollableDesktop.getWidth();
		int dktHeight = scrollableDesktop.getHeight();
		if ((width > dktWidth) || (height > dktHeight)) {
			difW = width - dktWidth;
			difH = height - dktHeight;
			if (difW > difH) oZoom = Math.round(dktWidth/(width*0.012F));
			else oZoom = Math.round(dktHeight/(height*0.012F));
		}
		return oZoom;
	}
	
	private void sair() {
		commonJifDeactivation();
		if (user.dados.mostradicas) {
			if (desejaProsseguir()) { 
				fim();
			}
		}
		else fim();
	}
	
	private void fim() {
		user.escreveDB();
		if ((paciente != null) && (paciente.salvarAlteracoes)) paciente.escreveDB();
		db.close();
		System.exit(0);
	}

	/** Ação do exitMenuItem */
	private void jmenuitExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuitExitActionPerformed
		sair();
	}//GEN-LAST:event_jmenuitExitActionPerformed
	
	/** Ação de fechar o form */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		sair();
	}//GEN-LAST:event_exitForm
	
	/** Posiciona um form no centro da tela */
	public static void center(Window w) {
		Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dWindow = w.getSize();
		if (dWindow.width==0) return;
		int left = dScreen.width/2-dWindow.width/2;
		int top = dScreen.height/2-dWindow.height/2;
		if (top<0) top = 0;
		w.setLocation(left, top);
	}
	
	/** Ajusta exibição de texto antialiased em um Graphics */
	public void setAntialiasedText(Graphics g) {
		( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	
	/** Ajusta interpolação bilinear em um Graphics */
	public void setBilinearInterpolation(Graphics g) {
		( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//        ( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		//        ( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		//        ( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//        ( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		//        ( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		//        ( (Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	}
	
	public static void setUILanguageJFileChooser() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("JFileChooser", locale);
		
		UIManager.put("FileChooser.lookInLabelText", rb.getString("lookInLabelText"));
		UIManager.put("FileChooser.saveInLabelText", rb.getString("saveInLabelText"));
		UIManager.put("FileChooser.filesOfTypeLabelText", rb.getString("filesOfTypeLabelText"));
		UIManager.put("FileChooser.upFolderToolTipText", rb.getString("upFolderToolTipText"));
		UIManager.put("FileChooser.fileNameLabelText", rb.getString("fileNameLabelText"));
		UIManager.put("FileChooser.homeFolderToolTipText", rb.getString("homeFolderToolTipText"));
		UIManager.put("FileChooser.newFolderToolTipText", rb.getString("newFolderToolTipText"));
		UIManager.put("FileChooser.listViewButtonToolTipText", rb.getString("listViewButtonToolTipText"));
		UIManager.put("FileChooser.detailsViewButtonToolTipText", rb.getString("detailsViewButtonToolTipText"));
		UIManager.put("FileChooser.saveButtonText", rb.getString("saveButtonText"));
		UIManager.put("FileChooser.openButtonText", rb.getString("openButtonText"));
		UIManager.put("FileChooser.cancelButtonText", rb.getString("cancelButtonText"));
		UIManager.put("FileChooser.updateButtonText", rb.getString("updateButtonText"));
		UIManager.put("FileChooser.helpButtonText", rb.getString("helpButtonText"));
		UIManager.put("FileChooser.saveButtonToolTipText", rb.getString("saveButtonToolTipText"));
		UIManager.put("FileChooser.openButtonToolTipText", rb.getString("openButtonToolTipText"));
		UIManager.put("FileChooser.cancelButtonToolTipText", rb.getString("cancelButtonToolTipText"));
		UIManager.put("FileChooser.updateButtonToolTipText", rb.getString("updateButtonToolTipText"));
		UIManager.put("FileChooser.helpButtonToolTipText", rb.getString("helpButtonToolTipText"));
		UIManager.put("FileChooser.acceptAllFileFilterText", rb.getString("acceptAllFileFilterText"));
	}
	
	/** Ajusta as configurações de exibição para o "Look and Feel" do MSWindows */
	public static void setLookAndFeel(Component c) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(c);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void welcome(){
		jDialogSAPO = new EscapeDialog(SAPO.this, "Bem Vindo", true);
		jDialogSAPO.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jDialogSAPO.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				// algo que deva acontecer ao retornar de Welcome
			}
		});
		jDialogSAPO.getContentPane().setLayout(new BorderLayout());
		jDialogSAPO.getContentPane().add(new Welcome(this), BorderLayout.CENTER);
		jDialogSAPO.pack();
		FormUtilities.centerInForm(scrollableDesktop, jDialogSAPO);
		jDialogSAPO.setVisible(true);
	}
	
	private static void createAndShowGUI(String fileOpen, String filePrint) {
		final SAPO sapo = new SAPO();
		sapo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		sapo.setIconImage(new ImageIcon(SAPO.ICONPATH).getImage());
		SAPO.center(sapo);
		sapo.setAntialiasedText(sapo.getGraphics());
		sapo.setVisible(true);
		sapo.setExtendedState(Frame.MAXIMIZED_BOTH);
		if (fileOpen.equals("") && filePrint.equals("")) {
			if (sapo.user.dados.boasvindas) sapo.welcome();
		}
		else {
			if ((fileOpen.length() > 0) || (filePrint.length() > 0)) {
				if (fileOpen.length() > 0) sapo.openFile(fileOpen);
				else
					if (filePrint.length() > 0) sapo.openFile(filePrint);
			}
			if (filePrint.length() > 0) 
				sapo.printRel();
			
		}
	}
	
	/** Método principal */
	public static void main(String args[]) {
		String fileOpen  = "";
		String filePrint = "";
		for (int i=0; i<args.length; i++) {
			if (args[i].equalsIgnoreCase("-open"))  fileOpen  = args[i+1];
			if (args[i].equalsIgnoreCase("-print")) filePrint = args[i+1];
		}
		final String theFileOpen  = fileOpen;
		final String theFilePrint = filePrint;
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(theFileOpen,theFilePrint);
			}
		});
	}
	
	public void cursorControl(final Component comp, PerformOp pOp) {
		TimerTask timerTask = new TimerTask() {
			public void run() {
				comp.setCursor(busyCursor);
			}
		};
		Timer timer = new Timer(); 
		try {   
			timer.schedule(timerTask, cursorDelay);
			pOp.doAction();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			timer.cancel();
			comp.setCursor(defaultCursor);
		}
	}
	
	public void restauraZoom() {
		int zoom = calculaZoomOtimo(jif[numImg].getRenderedImage().getWidth(), 
				jif[numImg].getRenderedImage().getHeight());
		jif[numImg].zoom = zoom;
		if (jif[numImg].flagImg)
			showInternalFrameWithImage(numImg);
		mostraZoom();
	}
	
	public Protocolo getProtocol(String nome){
		Protocolo temp = new Protocolo();
		if(nome.equalsIgnoreCase("Sapo"))  temp = protoSapo;
		if(nome.equalsIgnoreCase("User1")) temp = protoUser1;
		if(nome.equalsIgnoreCase("User2")) temp = protoUser2;
		if(nome.equalsIgnoreCase("User3")) temp = protoUser3;
		return temp;
	}
	
	public void clearProtocol(String nomeProto) {
		Protocolo protocolo = getProtocol(nomeProto);
		protocolo.frente.clear();
		protocolo.poster.clear();
		protocolo.latDir.clear();
		protocolo.latEsq.clear();
	}
	
	public boolean hasPontoProto(String nomeProto, String vista, int pt) {
		boolean retorno = false;
		java.util.ArrayList valores = new java.util.ArrayList();
		Protocolo protocolo = getProtocol(nomeProto);
		if(vista.equals("anterior")) valores = protocolo.frente;
		if(vista.equals("posterior")) valores = protocolo.poster;
		if(vista.equals("latDir")) valores = protocolo.latDir;
		if(vista.equals("latEsq")) valores = protocolo.latEsq;       
		for (int i=0; i<valores.size(); i++)
			if (((Integer)valores.get(i)).intValue() == pt) retorno = true;
		return retorno;
	} // getPontoProto
	
	public void addPontoProto(String nomeProto, String vista, int pt){
		Integer pto = new Integer(pt);
		Protocolo protocolo = getProtocol(nomeProto);
		if(vista.equals("anterior")) protocolo.frente.add(pto);
		if(vista.equals("posterior")) protocolo.poster.add(pto);
		if(vista.equals("latDir")) protocolo.latDir.add(pto);
		if(vista.equals("latEsq")) protocolo.latEsq.add(pto);       
	} // addPontoProto
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbCalibraVertical;
    private javax.swing.JButton jbEscala;
    private javax.swing.JButton jbImagem;
    private javax.swing.JButton jbMarcaPontos;
    private javax.swing.JButton jbNew;
    private javax.swing.JButton jbOpen;
    protected javax.swing.JButton jbPrintRel;
    private javax.swing.JButton jbRedo;
    protected javax.swing.JButton jbRelExcel;
    protected javax.swing.JButton jbRelatorio;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbUndo;
    private javax.swing.JButton jbtnAnguloMenu;
    private javax.swing.JButton jbtnMedeDist;
    private javax.swing.JButton jbtnNewProtocol;
    private javax.swing.JButton jbtnProtocol;
    private javax.swing.JCheckBoxMenuItem jcbmenuitDraw;
    private javax.swing.JCheckBoxMenuItem jcbmenuitEditImage;
    private javax.swing.JCheckBoxMenuItem jcbmenuitToolbar;
    private javax.swing.JLabel jlblStatus;
    private javax.swing.JMenu jmenuAnalysis;
    private javax.swing.JMenu jmenuData;
    private javax.swing.JMenu jmenuEdit;
    private javax.swing.JMenuItem jmenuError;
    private javax.swing.JMenu jmenuFile;
    private javax.swing.JMenu jmenuHelp;
    private javax.swing.JMenu jmenuImage;
    private javax.swing.JMenuItem jmenuItDist;
    private javax.swing.JMenuItem jmenuItMarcaPontosProtocol;
    private javax.swing.JMenu jmenuView;
    private javax.swing.JMenuItem jmenuitAbout;
    private javax.swing.JMenuItem jmenuitAjudaOnline;
    private javax.swing.JMenuItem jmenuitAngulos;
    private javax.swing.JMenuItem jmenuitAtualizaRef;
    private javax.swing.JMenuItem jmenuitClose;
    private javax.swing.JMenuItem jmenuitCopy;
    private javax.swing.JMenuItem jmenuitCut;
    private javax.swing.JMenuItem jmenuitDelete;
    private javax.swing.JMenuItem jmenuitExit;
    private javax.swing.JMenuItem jmenuitExpExcel;
    private javax.swing.JMenuItem jmenuitFeedbackSuporte;
    private javax.swing.JMenuItem jmenuitFind;
    private javax.swing.JMenuItem jmenuitFindProject;
    private javax.swing.JMenuItem jmenuitGC;
    private javax.swing.JMenuItem jmenuitGeraRel;
    private javax.swing.JMenuItem jmenuitHelpContent;
    private javax.swing.JMenuItem jmenuitHelpIndex;
    private javax.swing.JMenuItem jmenuitHelpSearch;
    protected javax.swing.JMenuItem jmenuitImprRel;
    private javax.swing.JMenuItem jmenuitKeyboardMap;
    private javax.swing.JMenuItem jmenuitMarca;
    private javax.swing.JMenuItem jmenuitNewFile;
    private javax.swing.JMenuItem jmenuitOpen;
    private javax.swing.JMenuItem jmenuitOpen1;
    private javax.swing.JMenuItem jmenuitOpenImage;
    private javax.swing.JMenuItem jmenuitPaste;
    private javax.swing.JMenuItem jmenuitPreferencias;
    private javax.swing.JMenuItem jmenuitProtocolUser;
    private javax.swing.JMenuItem jmenuitRedo;
    private javax.swing.JMenuItem jmenuitReplace;
    private javax.swing.JMenuItem jmenuitSAPOWeb;
    private javax.swing.JMenuItem jmenuitSaveAs;
    private javax.swing.JMenuItem jmenuitSaveImage;
    private javax.swing.JMenuItem jmenuitScale;
    private javax.swing.JMenuItem jmenuitSujeito;
    private javax.swing.JMenuItem jmenuitUndo;
    private javax.swing.JMenuItem jmenuitUsuario;
    private javax.swing.JMenuItem jmenuitVerificaAtualizacoes;
    private javax.swing.JMenuItem jmenuitVertical;
    private javax.swing.JMenuItem jmenuitZoom;
    private javax.swing.JPanel jpMain;
    private javax.swing.JPanel jpMain2;
    private javax.swing.JPanel jpMain3;
    private javax.swing.JPanel jpStatus;
    private javax.swing.JToggleButton jtbDesenho;
    private javax.swing.JToggleButton jtbEdicao;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

} // fim do SAPO
