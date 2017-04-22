import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

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
	public static void main(String[] args) {
		// Receiver mail address
		String to = "------@------.ch";

		// Sender mail address
		String from = "------@gmail.com";

		String username = "-----@gmail.com";
		String password = "-----";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup properties for the mail server
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "587");

		// Get the default Session object
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("Testmail");

			// Now set the actual message
			message.setText("Na Bravo !");

			Transport transport = session.getTransport("smtps");

			try {
				transport.connect("smtp.gmail.com", username, password);
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