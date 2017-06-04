import java.io.*;
import java.util.*;
import java.util.prefs.BackingStoreException;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;


import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;


import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * 
 * A simple program for receiving mail messages from gmail using POP3S.
 *
 * @version 02-APR-2015
 */
public class RevieceMail {

	// Fehlertext für StatusBar
	private String FehlerText = "";

	// Anzahl Empfangene Mails für Statusbar
	private int MailCounter = 0;

	// Neue Mails als Liste
	private ArrayList<MailStruktur> newMailList = new ArrayList<MailStruktur>();

	private boolean Erfolg = false;
	
	/**
	 * Konstruktur, setzt Werte zurück
	 */
	public RevieceMail() {
		this.FehlerText = "";
		MailCounter = 0;
	}
	

    private String leseMailInhalt(Part msg) throws Exception {

    	String returni = "";
    	
        // get the content type of the part
        String contentType = msg.getContentType();

        // handle the part coresponding to its type
        if (contentType.contains("multipart")) {
            Multipart multipart = (Multipart) msg.getContent();
            for (int j = 0; j < multipart.getCount(); j++) {
                BodyPart bodyPart = multipart.getBodyPart(j);
                	returni =  leseMailInhalt(bodyPart);                                 
            }
        }
        else if (contentType.contains("text/plain")) {
            returni =  (String)msg.getContent();
        }
        else if (contentType.contains("text/html")) {

            //System.out.println((String)msg.getContent());
            returni =  (String)msg.getContent();
            returni = returni.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
            returni = returni.replaceAll("&nbsp;", " ");
        }

        else {

        	returni = "Mail-Inhalt unlesbar";
        }
        return returni;
    }

    
	/**
	 * Versucht neue Mails zu empfangen
	 * Gibt ArrayListe mit neuen Mails zurück (ohne zu speichern!)
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
			folder.open(Folder.READ_ONLY);

			// Get the messages from the server
			Message[] messages = folder.getMessages();

			System.out.println(messages.length);
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
				
			
			     
			     
				MailStruktur mail = new MailStruktur(sent, from, subject, leseMailInhalt(msg), null, null);
				
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

	public boolean getErfolg(){
		return Erfolg;
	}
	public String getFehlerText() {
		return FehlerText;
	}

	public int getMailCounter() {
		return MailCounter;
	}

	public ArrayList<MailStruktur> getnewMailList(){
		return newMailList;
	}
	public void setFehlerText(String fehlerText) {
		FehlerText = fehlerText;
	}
}
