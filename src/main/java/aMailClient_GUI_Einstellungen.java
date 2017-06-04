
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class aMailClient_GUI_Einstellungen extends JDialog {

	

	private static final long serialVersionUID = -8439476419298094881L;

	public aMailClient_GUI_Einstellungen() {
		super();
		//super("adriano's MailClient - Einstellungen");
		// Set the default behaviour for the close button
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Erzeugung eines neuen Dialoges
		//JDialog meinJDialog = new JDialog();
		setTitle("Einstellungen");
		setSize(450, 300);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel panelGreen = new JPanel();

		panelGreen.setBackground(Color.GREEN);

		// Erzeugung eines JTabbedPane-Objektes
		JTabbedPane tabpane = new JTabbedPane(SwingConstants.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);

		// POP3
		// Hier erzeugen wir unsere JPanels
		JPanel panelRot = new JPanel();
		// Hier setzen wir die Hintergrundfarben für die JPanels
		panelRot.setBackground(Color.RED);

		// Raster erstellen
		JLabel pop3_server_label = new JLabel("POP3-Server: ");
		JLabel pop3_port_label = new JLabel("POP3-Server-Port: ");
		JLabel pop3_username_label = new JLabel("Username: ");
		JLabel pop3_password_label = new JLabel("Password: ");

		JTextField pop3_serverField = new JTextField(Configuration.getPop3Server(), 20);
		JTextField pop3_usernameField = new JTextField(Configuration.getPop3User(), 20);
		JPasswordField pop3_passwordField = new JPasswordField(Configuration.getPop3PW(), 20);
		JTextField pop3_portField = new JTextField(Configuration.getPop3Port(), 20);

		pop3_server_label.setDisplayedMnemonic(KeyEvent.VK_S);
		pop3_server_label.setLabelFor(pop3_serverField);
		pop3_port_label.setDisplayedMnemonic(KeyEvent.VK_O);
		pop3_port_label.setLabelFor(pop3_portField);
		pop3_username_label.setDisplayedMnemonic(KeyEvent.VK_U);
		pop3_username_label.setLabelFor(pop3_usernameField);
		pop3_password_label.setDisplayedMnemonic(KeyEvent.VK_P);
		pop3_password_label.setLabelFor(pop3_passwordField);

		panelRot.add(pop3_server_label);
		panelRot.add(pop3_serverField);
		panelRot.add(pop3_port_label);
		panelRot.add(pop3_portField);
		panelRot.add(pop3_username_label);
		panelRot.add(pop3_usernameField);
		panelRot.add(pop3_password_label);
		panelRot.add(pop3_passwordField);

		JButton pop3_save = new JButton("Speichern");
		pop3_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Configuration.setPop3Server(pop3_serverField.getText());
				Configuration.setPop3User(pop3_usernameField.getText());
				Configuration.setPop3PW(new String(pop3_passwordField.getPassword()));
				Configuration.setPop3Port(pop3_portField.getText());
			}
		});

		panelRot.add(pop3_save);

		// Hier werden die JPanels als Registerkarten hinzugefügt
		tabpane.addTab("POP3", panelRot);

		// smtp

		JPanel panelBlue = new JPanel();
		panelBlue.setBackground(Color.CYAN);

		// Raster erstellen
		JLabel smtp_server_label = new JLabel("smtp-Server: ");
		JLabel smtp_port_label = new JLabel("smtp-Server-Port: ");
		JLabel smtp_username_label = new JLabel("Username: ");
		JLabel smtp_password_label = new JLabel("Password: ");

		JTextField smtp_serverField = new JTextField(Configuration.getsmtpServer(), 20);
		JTextField smtp_usernameField = new JTextField(Configuration.getsmtpUser(), 20);
		JPasswordField smtp_passwordField = new JPasswordField(Configuration.getsmtpPW(), 20);
		JTextField smtp_portField = new JTextField(Configuration.getsmtpPort(), 20);

		smtp_server_label.setDisplayedMnemonic(KeyEvent.VK_S);
		smtp_server_label.setLabelFor(smtp_serverField);
		smtp_port_label.setDisplayedMnemonic(KeyEvent.VK_O);
		smtp_port_label.setLabelFor(smtp_portField);
		smtp_username_label.setDisplayedMnemonic(KeyEvent.VK_U);
		smtp_username_label.setLabelFor(smtp_usernameField);
		smtp_password_label.setDisplayedMnemonic(KeyEvent.VK_P);
		smtp_password_label.setLabelFor(smtp_passwordField);

		panelBlue.add(smtp_server_label);
		panelBlue.add(smtp_serverField);
		panelBlue.add(smtp_port_label);
		panelBlue.add(smtp_portField);
		panelBlue.add(smtp_username_label);
		panelBlue.add(smtp_usernameField);
		panelBlue.add(smtp_password_label);
		panelBlue.add(smtp_passwordField);

		JButton smtp_save = new JButton("Speichern");
		smtp_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Configuration.setsmtpServer(smtp_serverField.getText());
				Configuration.setsmtpUser(smtp_usernameField.getText());
				Configuration.setsmtpPW(new String(smtp_passwordField.getPassword()));
				Configuration.setsmtpPort(smtp_portField.getText());
			}
		});

		panelBlue.add(smtp_save);

		// Hier werden die JPanels als Registerkarten hinzugefügt
		tabpane.addTab("SMTP", panelBlue);

		tabpane.addTab("Diverses", panelGreen);

		// JTabbedPane wird unserem Dialog hinzugefügt
		add(tabpane);
		// Wir lassen unseren Dialog anzeigen
		setModal(true);
		setVisible(true);
		
	}
	
}
