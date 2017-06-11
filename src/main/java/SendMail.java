import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * A simple program for sending a mail message to gmail using SMTP.
 *
 * @version 02-APR-2015
 */
public class SendMail {

	private String FehlerText = "";
	private boolean Erfolg = false;
	private MailStruktur mail = null;

	/**
	 * Ermittelt den Erfolg vom Senden
	 * 
	 * @return Efolg true|false
	 */
	public boolean getErfolg() {
		return Erfolg;
	}

	/**
	 * Gibt den Fehlertext zurueck
	 * 
	 * @return Fehlertext
	 */
	public String getFehlerText() {
		return FehlerText;
	}

	/**
	 * Konstruktoren, initalisiert Variablen
	 */

	public SendMail() {

	}

	/**
	 * Wrapper fuer send() mit Angabe von Mail
	 *
	 * @param newMail
	 *            Mail zum senden
	 * @return Erfolg ja|nein
	 */
	public boolean send(MailStruktur newMail) {
		this.mail = newMail;
		return send();
	}

	/**
	 * Sendet eine Mail
	 *
	 * @return Erfolg ja|nein
	 */
	public boolean send() {
		Erfolg = false;

		// String Empfaenger = "";
		String Betreff = mail.getBetreff();
		String Nachricht = mail.getNachricht();

		// Sender mail address
		String from = Configuration.getsmtpUser();

		String username = Configuration.getsmtpUser();
		String password = Configuration.getsmtpPW();

		// Get system properties
		Properties properties = System.getProperties();

		// Setup properties for the mail server
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", Configuration.getsmtpServer());
		properties.setProperty("mail.smtp.port", Configuration.getsmtpPort());

		// Get the default Session object
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header
			if (mail.getEmpfaenger() != null && !mail.getEmpfaenger().isEmpty())
				for (String Empfaenger : mail.getEmpfaenger().split(";"))
					if (Empfaenger != null && !Empfaenger.isEmpty())
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(Empfaenger));

			// Set CC: header field of the header
			if (mail.getCC() != null && !mail.getCC().isEmpty())
				for (String cc : mail.getCC().split(";"))
					if (cc != null && !cc.isEmpty())
						message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));

			// Set BCC: header field of the header
			if (mail.getBCC() != null && !mail.getBCC().isEmpty())
				for (String bcc : mail.getBCC().split(";"))
					if (bcc != null && !bcc.isEmpty())
						message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));

			// Set Subject: header field
			message.setSubject(Betreff);

			// Now set the actual message
			message.setText(Nachricht);

			Transport transport = session.getTransport("smtps");

			try {
				transport.connect(Configuration.getsmtpServer(), username, password);
				transport.sendMessage(message, message.getAllRecipients());
				Erfolg = true;
			} finally {
				transport.close();
			}

			// System.out.println("Message sent");
		} catch (MessagingException e) {

			if (Configuration.isDebug())
				e.printStackTrace();

			FehlerText = "<html>Hoppla, da ist etwas schief gelaufen.<br> Stimmen die SMTP-Angaben?</html>";
			Erfolg = false;
		}
		return Erfolg;
	}
}