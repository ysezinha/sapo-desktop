/*
 * LogError.java
 *
 * Created on 30 de Dezembro de 2004, 14:22
 */

/**
 *
 * @author Anderson Zanardi de Freitas
 */

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class LogError{
	String host, from, to, autor= "";
	SAPO sapo;
	
	/** Creates a new instance of LogError */
	public LogError(SAPO sapo) {
		this.sapo = sapo;
		// String ht, String fr, String us, String at
		host = sapo.user.dados.servidoremail;
		from = sapo.user.dados.useremail;
		autor = sapo.user.dados.nomecompleto; 
		to = "sapo2004fapesp@yahoo.com.br";
	}
	
	public void sendMail(javax.swing.JTextArea jtxtArea) {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		Authenticator auth = new PopupAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		// Define a mensagem
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Sapo error. Enviado por: "+autor);
			message.setText(jtxtArea.getText());
			Transport.send(message);
			javax.swing.JOptionPane.showMessageDialog(sapo, "Envio confirmado! \n Obrigado!", "Ok", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(sapo, "Falha no envio automático! \n Salve o arquivo no disco e envia para: \n" + to + "\n Obrigado! \n"+e, "Erro", javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public class PopupAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String result = javax.swing.JOptionPane.showInputDialog(
			"Entre: 'username,password'");
			
			java.util.StringTokenizer st = new java.util.StringTokenizer(result, ",");
			String username = st.nextToken();
			String password = st.nextToken();
			String ressssult = javax.swing.JOptionPane.showInputDialog(
			"Entre: 'username,password'");
			return new PasswordAuthentication(username, password);
		}
	}
	
}
