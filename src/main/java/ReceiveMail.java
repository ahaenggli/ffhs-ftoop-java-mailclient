import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * 
 * A simple program for receiving mail messages from gmail using POP3S.
 *
 * @version 02-APR-2015
 */
public class ReceiveMail {

	// Fehlertext fuer StatusBar
	private String FehlerText = "";

	// Anzahl Empfangene Mails fuer Statusbar
	private int MailCounter = 0;

	// Neue Mails als Liste
	private ArrayList<MailStruktur> newMailList = new ArrayList<MailStruktur>();

	private boolean Erfolg = false;

	/**
	 * Konstruktur, setzt Werte zur�ck
	 */
	public ReceiveMail() {
		this.FehlerText = "";
		MailCounter = 0;
	}

	/**
	 * Versucht neue Mails zu empfangen Gibt ArrayListe mit neuen Mails zurueck
	 * (ohne zu speichern!)
	 * 
	 * @return true | false
	 * 
	 */
	public boolean getMails() {
		Erfolg = false;

		setFehlerText("Empfang fehlgeschlagen");

		// The IP address of the POP3 server
		String host = Configuration.getPop3Server();

		// Username and password
		String user = Configuration.getPop3User();
		String password = Configuration.getPop3PW();

		// Get system properties
		Properties properties = System.getProperties();

		// Request POP3S
		properties.put("mail.store.protocol", "pop3s");

		// Get the default Session object
		Session session = Session.getDefaultInstance(properties);

		try {
			// Get a store for the POP3S protocol
			Store store = session.getStore();

			// Connect to the current host using the specified username and
			// password
			store.connect(host, user, password);

			// Create a Folder object corresponding to the given name
			Folder folder = store.getFolder("inbox");

			// Open the Folder
			folder.open(Folder.READ_WRITE);

			// Get the messages from the server
			Message[] messages = folder.getMessages();

			// Display message
			for (int i = 0; i < messages.length; i++) {

				Message msg = messages[i];

				String from = InternetAddress.toString(msg.getFrom());
				// if (from != null) System.out.println("From: " + from);

				String to = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
				// if (to != null) System.out.println("To: " + to);

				String subject = msg.getSubject();
				// if (subject != null) System.out.println("Subject: " +
				// subject);

				Date sent = msg.getSentDate();
				// if (sent != null) System.out.println("Sent: " + sent);

				// Empty line to separate header from body
				// System.out.println();
				// This could lead to troubles if anything but text was sent
				// System.out.println(msg.getContent());

				MailStruktur mail = new MailStruktur(sent, from, subject, msg.getContent().toString(), null, null);

				if (to != null && !to.isEmpty())
					mail.setEmpfaenger(to);

				newMailList.add(mail);

				MailCounter++;

				/*
				 * In der endgueltigen Version sollen die Mails geloest werden
				 */

				// Mark this message for deletion when the session is closed
				// msg.setFlag( Flags.Flag.DELETED, true ) ;
			}

			folder.close(true);
			store.close();
			Erfolg = true;
		} catch (MessagingException e) {
			setFehlerText("MessagingException: Stimmen die POP3-Benutzerdaten und haben Sie Internet?");
		} catch (IOException e) {
			setFehlerText("Fehler: IOException");
		} catch (Exception e) {
			setFehlerText("Fehler: unerwartete Exception");
		}

		return Erfolg;
	}

	/**
	 * Ermittelt den Erfolg vom Empfangen
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
	 * Anzahl empfangener Mails
	 * 
	 * @return Zahl
	 */
	public int getMailCounter() {
		return MailCounter;
	}

	/**
	 * Liste der empfangenen Mails
	 * 
	 * @return
	 */
	public ArrayList<MailStruktur> getnewMailList() {
		return newMailList;
	}

	/**
	 * setzt den Fehlertext
	 * 
	 * @param fehlerText
	 *            Neuer Wert fuer FehlerText
	 */
	public void setFehlerText(String fehlerText) {
		FehlerText = fehlerText;
	}
}
