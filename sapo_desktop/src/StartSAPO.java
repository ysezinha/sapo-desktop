/*
 * StartSAPO.java
 *
 * Created on 19 de Dezembro de 2004, 16:39
 */

/**
 *
 * @author Edison Puig Maldonado
 */

import java.awt.Frame;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.jdesktop.swingworker.SwingWorker;

import com.thoughtworks.xstream.XStream;

public class StartSAPO {
	
	public final static String defaultDirPath = FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath();
	public final static String sep = File.separator;
	public final static String iniPath = defaultDirPath + sep + "SAPO.ini";
	private static Frame splashFrame = null;
	private final static int BUF_SIZE = 50000;
	
	public static boolean verificaDB(boolean force) {
		boolean sucesso = false;
		String dbDirPath  = defaultDirPath + sep + "SAPO-DB";
		String figDirPath = defaultDirPath + sep + "SAPO-FIG";
		try {
			SAPOIni ini = leIni();
			if ((ini == null) || force) {
				if (!criaIni(dbDirPath,figDirPath)) return false;
			}
			else {
				if ((ini.dbDirPath  != null) && (ini.figDirPath != null)) {
					File dbDir = new File(ini.dbDirPath);
					File imDir = new File(ini.figDirPath);
					if (!dbDir.getAbsolutePath().equals(ini.dbDirPath))  return false;
					if (!imDir.getAbsolutePath().equals(ini.figDirPath)) return false;
					if (!dbDir.exists() || !dbDir.isDirectory()) {
						if (!dbDir.mkdir()) return false;
					}
					if (!imDir.exists() || !imDir.isDirectory()) {
						if (!imDir.mkdir()) return false;
					}
					dbDirPath  = ini.dbDirPath;
					figDirPath = ini.figDirPath;
				}
				else {
					JOptionPane.showMessageDialog(null, 
							"Houve um erro ao ler as configurações de \n" +
							iniPath + "\n\n" +
							"Esta execução do programa será iniciada usando caminhos padrão.",
							"ERRO", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			File dbDir = new File(dbDirPath);
			if (dbDir.exists()) {
				if (!dbDir.isDirectory()) {
					dbDir.mkdir();
				}
			}
			else {
				dbDir.mkdir();
			}
			
			File figDir = new File(figDirPath);
			if (figDir.exists()) {
				if (!figDir.isDirectory()) {
					figDir.mkdir();
				}
			}
			else {
				figDir.mkdir();
			}
			
			String dbFilesNames[] = dbDir.list();
			boolean contemProperties = false;
			boolean contemScript = false;
			if (dbFilesNames != null) {
				for (int i=0; i<dbFilesNames.length; i++) {
					if (dbFilesNames[i].equalsIgnoreCase("sapodb.properties"))
						contemProperties = true;
					if (dbFilesNames[i].equalsIgnoreCase("sapodb.script"))
						contemScript = true;
					if (dbFilesNames[i].equalsIgnoreCase("sapodb.lck")) {
						File lck = new File(dbDirPath + sep + "sapodb.lck");
						try {
							lck.delete();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, 
									"Não foi possível abrir conexão com o banco de dados.\n" +
									"Isto pode ser porque alguma outra instância desse programa está rodando.",
									"ERRO", JOptionPane.INFORMATION_MESSAGE);
							System.exit(-1);
						}
					}
				}
			}
			if (!contemProperties || !contemScript) { 
				URL propertiesURL = StartSAPO.class.getResource("sapodb.properties");
				URL scriptURL = StartSAPO.class.getResource("sapodb.script");
				if ((propertiesURL != null) && (scriptURL != null) &&
						copy(propertiesURL,dbDirPath+sep+"sapodb.properties") &&
						copy(scriptURL,dbDirPath+sep+"sapodb.script"))
					sucesso = true;            			
			}
			else sucesso = true;
			return sucesso;
		} catch(Exception e) {
			e.printStackTrace();
			return sucesso;
		}
	}
	
	private static boolean criaIni(String dbDirPath, String figDirPath) {
		JFrame frame = new JFrame();
		SAPO.setLookAndFeel(frame);
		frame.setIconImage(new ImageIcon(SAPO.ICONPATH).getImage());
		Object[] options = {"Automática","  Manual  "};
		if (splashFrame != null) {
			try {
    			Thread.sleep (1500);
    			splashFrame.dispose();
    		}
    		catch (InterruptedException e) {
    			splashFrame.dispose();
    		}
		}
		int result = JOptionPane.showOptionDialog(frame,
				" SAPO - Escolha o tipo de instalação :  \n"," SAPO - Configuração",JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, 
				(new ImageIcon(SAPO.ICONPATH)), options, options[0]);
		if (result == JOptionPane.NO_OPTION) {
			Object[] options2 = {"Sim, usar padrão","Não, desejo definir"};
			int result2 = JOptionPane.showOptionDialog(frame,
					"Deseja utilizar as configurações padrão para o caminho de armazenagem dos dados?\n\n" +
					"OBS: se os projetos desenvolvidos com este programa devem ser compartilhados com\n" +
					"outros usuários deste computador, e se você souber o caminho de armazenamento dos\n" +
					"dados em comum (consulte o administrador do sistema), reponda NÃO a esta pergunta.     \n" +
					" \n",
					"Configurações",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
			if (result2 == JOptionPane.NO_OPTION) {
				File dbDir  = selecionaDir(frame,"Escolha a pasta para o banco de dados");
				File figDir = selecionaDir(frame,"Escolha a pasta para o armazenamento das imagens");
				if ((dbDir != null) && dbDir.exists() && (figDir != null) && figDir.exists()) {
					dbDirPath  = dbDir.getPath();
					figDirPath = figDir.getPath();
				}
			}
		}
		flashScreen();
		try {
			escreveIni(dbDirPath,figDirPath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static File selecionaDir(JFrame frame, String title){
		SAPO.setUILanguageJFileChooser();
		File arquivo = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle(title);
		if( fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION ) return null;
		arquivo = fc.getSelectedFile();
		if (!arquivo.exists()) 
			arquivo.mkdir();
		return(arquivo);
	}
	
	public static void escreveIni(String dbDirPath, String figDirPath) throws Exception {
		SAPOIni ini = new SAPOIni();
		ini.dbDirPath  = dbDirPath;
		ini.figDirPath = figDirPath;
		File arqIni = new File(iniPath);
		try{
			XStream xstream = new XStream();
			String xml = xstream.toXML(ini);
			PrintWriter pw = new PrintWriter(arqIni);
			pw.println(xml);
			pw.flush();
		} catch (Exception e) { throw new Exception(e); }
	}
	
	public static SAPOIni leIni() throws Exception {
		SAPOIni ini = null;
		File arqIni = new File(iniPath);
		if (arqIni.exists()) { 
			String xml = ""; 
			String linha;
			FileReader fr;
			try {
				fr = new FileReader(arqIni);
				BufferedReader br = new BufferedReader(fr);
				while ( (linha=br.readLine()) != null ) { 
					xml = xml + linha;
				}
			} catch (Exception e) {
				throw new Exception(e);
			}
			XStream xstream = new XStream();
			ini = (SAPOIni)xstream.fromXML(xml);
		}
		return ini;
	}
	
	public static boolean copy(URL inputURL, String output) {
		boolean copiou = false;
		FileOutputStream out = null;
		byte[] buf = new byte[BUF_SIZE];
		try {
			InputStream in = inputURL.openStream();
			File outputFile = new File(output);
			out = new FileOutputStream(outputFile);
			int read = -1;
			while ((read = in.read(buf, 0, buf.length)) != -1) 
				out.write(buf, 0, read);
			copiou = true;
		} catch (IOException e) {
			return copiou;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return copiou;
	}
	
	public static void copy(java.io.File src, java.io.File dst) throws IOException {
		java.io.InputStream in = new java.io.FileInputStream(src);
		java.io.OutputStream out = new java.io.FileOutputStream(dst);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
	
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	public static void deleteOnExit(String filename){
		File outdel = new File(filename);
		outdel.deleteOnExit();
	}
	
	public static void restart(JFrame sapo) {
		sapo.setVisible(false);
		sapo.dispose();
		flashScreen();
		SwingWorker aWorker = new SwingWorker() {
			public Object doInBackground() {
				new StartSapoDelay(2000);
				return null;
			}
		};
		aWorker.execute();
	}
	
	private static void flashScreen() {
		//	Read the image data and display the splash screen
		URL imageURL = StartSAPO.class.getResource("res/splash.jpg");
		if (imageURL != null) {
			splashFrame = SplashWindow.splash(
					Toolkit.getDefaultToolkit().createImage(imageURL)
			);
		} else {
			System.err.println("Splash image not found");
		}
	}
	
	private static void startSapo(String[] args) {	
		// Call the main method of the application using Reflection
		if (StartSAPO.verificaDB(false)) {
			try {
				Class.forName("SAPO")
				.getMethod("main", new Class[] {String[].class})
				.invoke(null, new Object[] {args});
			} catch (Throwable e) {
				e.printStackTrace();
				System.err.flush();
				System.exit(10);
			}
		}
		else {
			if (StartSAPO.verificaDB(true)) {
				try {
					Class.forName("SAPO")
					.getMethod("main", new Class[] {String[].class})
					.invoke(null, new Object[] {args});
				} catch (Throwable e) {
					e.printStackTrace();
					System.err.flush();
					System.exit(10);
				}
			}
			else {
				JOptionPane.showMessageDialog(null, 
						"Não foi possível iniciar o programa SAPO, pois" +
						"não foi possível encontrar ou criar o banco de dados.",
						"ERRO", JOptionPane.INFORMATION_MESSAGE);
				System.exit(-1);
			}
		}
		// Dispose the splash screen
		if (splashFrame != null) splashFrame.dispose();
	}
	
	public static void main(String[] args) {
		flashScreen();
		startSapo(args);
	}
	
	static class StartSapoDelay {
		StartSapoDelay(long msec) {
			final String[] args = {""};
			try {
				Thread.sleep(msec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				startSapo(args);
			}
		}
	}
}