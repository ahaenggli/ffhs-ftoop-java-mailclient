import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * A simple program for sending a mail message to gmail using SMTP.
 *
 * @version 02-APR-2015
 */
public class SendMail {
	/**
	 * Constructs and sends a simple email.
	 *
	 * @param args
	 *            Not used
	 */
	public SendMail(String Empfaenger, String Betreff, String Nachricht) {
	

		// Sender mail address
		String from = aMailClient__Settings.getsmtpUser();

		String username = aMailClient__Settings.getsmtpUser();
		String password = aMailClient__Settings.getsmtpPW();

		// Get system properties
		Properties properties = System.getProperties();

		// Setup properties for the mail server
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", aMailClient__Settings.getsmtpServer());
		properties.setProperty("mail.smtp.port", aMailClient__Settings.getsmtpPort());

		// Get the default Session object
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(Empfaenger));

			// Set Subject: header field
			message.setSubject(Betreff);

			// Now set the actual message
			message.setText(Nachricht);

			Transport transport = session.getTransport("smtps");

			try {
				transport.connect(aMailClient__Settings.getsmtpServer(), username, password);
				transport.sendMessage(message, message.getAllRecipients());
			} finally {
				transport.close();
			}

			System.out.println("Message sent");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}