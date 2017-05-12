
/*
Funktionen
Ihre Anwendung muss mindestens folgende Funktionen implementieren:

- Konfiguration von POP3 sowie SMTP Zugangsdaten 


- Öffnen einer Detailansicht von Mails in einem neuen Fenster mit Antwort/Weiterleitungsfunktion
- Beantworten und weiterleiten von Mails
- Erstellen von neuen Mails und Senden via SMTP

- Abholen von Mails von einem POP3 Server
- Einsortieren von abgeholten Mails in einen Standard Ordner „Neue Mails“

Für die Anbindung des Mailservers wird Ihnen im Moodle eine Bibliothek zur Verfügung gestellt.
Optional können noch folgende Funktionen implementiert werden:
- Verarbeitung von Attachments in empfangenen und gesendeten Mails
- Automatisches Abholen per Zeitintervall
- Anlegen, Bearbeiten und Löschen von lokalen Mail Ordnern
- Verschieben von Mails in einen existierenden Ordner

*/
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import javax.swing.*;

public class aMailClientGUI extends JFrame {

	private static final long serialVersionUID = 8508453892315236372L;
	private JLabel StatusBar = new JLabel("StatusBar");
	private JTable table_mailListe;
	private DataMailListTableModel m_simpleTableModel = new DataMailListTableModel();
	private DataHandler dh = new DataHandler();

	private DefaultMutableTreeNode baum_root = new DefaultMutableTreeNode("Ordner");

	private JTree baum_strukt = new JTree(new DefaultTreeModel(baum_root));

	public void addRunnable(Runnable run) {
		EventQueue.invokeLater(run);
	}

	public String getSelectedMailListRow(Point evt, int colS){
		String texti = "";
		
		int row = table_mailListe.rowAtPoint(evt);
		int col = table_mailListe.columnAtPoint(evt);
		if (row >= 0 && col >= 0) {
			
			texti = table_mailListe.getModel().getValueAt(table_mailListe.convertRowIndexToModel(row), colS)
					.toString();

			
		}
		
		return texti;
	}
	
	public void changeOrdner(String neu) {
		dh.setGewaehlterMailOrdner(neu);
	}

	public void setStatusBarText(String text){
		StatusBar.setText(text);
		super.update(this.getGraphics());
	}
	public JLabel guiGetStatusBar() {

		return StatusBar;
	}

	public void ShowSettingDialog() {
		// Erzeugung eines neuen Dialoges
		JDialog meinJDialog = new JDialog();
		meinJDialog.setTitle("Einstellungen");
		meinJDialog.setSize(450, 300);
		meinJDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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

		JTextField pop3_serverField = new JTextField(aMailClientSettings.getPop3Server(), 20);
		JTextField pop3_usernameField = new JTextField(aMailClientSettings.getPop3User(), 20);
		JPasswordField pop3_passwordField = new JPasswordField(aMailClientSettings.getPop3PW(), 20);
		JTextField pop3_portField = new JTextField(aMailClientSettings.getPop3Port(), 20);

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
				aMailClientSettings.setPop3Server(pop3_serverField.getText());
				aMailClientSettings.setPop3User(pop3_usernameField.getText());
				aMailClientSettings.setPop3PW(new String(pop3_passwordField.getPassword()));
				aMailClientSettings.setPop3Port(pop3_portField.getText());
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

		JTextField smtp_serverField = new JTextField(aMailClientSettings.getsmtpServer(), 20);
		JTextField smtp_usernameField = new JTextField(aMailClientSettings.getsmtpUser(), 20);
		JPasswordField smtp_passwordField = new JPasswordField(aMailClientSettings.getsmtpPW(), 20);
		JTextField smtp_portField = new JTextField(aMailClientSettings.getsmtpPort(), 20);

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
				aMailClientSettings.setsmtpServer(smtp_serverField.getText());
				aMailClientSettings.setsmtpUser(smtp_usernameField.getText());
				aMailClientSettings.setsmtpPW(new String(smtp_passwordField.getPassword()));
				aMailClientSettings.setsmtpPort(smtp_portField.getText());
			}
		});

		panelBlue.add(smtp_save);

		// Hier werden die JPanels als Registerkarten hinzugefügt
		tabpane.addTab("SMTP", panelBlue);

		tabpane.addTab("Diverses", panelGreen);

		// JTabbedPane wird unserem Dialog hinzugefügt
		meinJDialog.add(tabpane);
		// Wir lassen unseren Dialog anzeigen
		meinJDialog.setModal(true);
		meinJDialog.setVisible(true);

	}

	public JMenuBar guiGetMenu() {
		/*
		 * JPanel Nord = new JPanel(new GridLayout(1,5));
		 * Nord.setPreferredSize(new Dimension(10, 50)); Nord.add( new
		 * JButton("Einstellungen") ) ; Nord.add( new JButton("Mails empfangen")
		 * ) ; return Nord;
		 */

		// Where the GUI is created:
		JMenuBar menuBar;
		JMenu Datei, Extras;
		JMenuItem menuItem, Einstellungen, Aktualisieren;


		// Create the menu bar.
		menuBar = new JMenuBar();

		// Datei
		Datei = new JMenu("Datei");
		Datei.setMnemonic(KeyEvent.VK_D);
		Datei.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");

		menuBar.add(Datei);

		menuItem = new JMenuItem("Beenden");
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

		// Lasse die Applikation sterben
		ActionListener exitusOmne = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				addRunnable(new Runnable() {
					public void run() {
						try {						
							setStatusBarText("am Beenden...");
						    
							Thread.sleep(5000);
							System.exit(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}
		};

		menuItem.addActionListener(exitusOmne);
		Datei.add(menuItem);

		Extras = new JMenu("Extras");
		Extras.setMnemonic(KeyEvent.VK_E);
		Extras.getAccessibleContext().setAccessibleDescription("Extras");
		menuBar.add(Extras);

		// Build second menu in the menu bar.
		Einstellungen = new JMenuItem("Einstellungen");
		Einstellungen.getAccessibleContext().setAccessibleDescription("Einstellungen bearbeiten");
		Einstellungen.setMnemonic(KeyEvent.VK_E);
		Einstellungen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));

		Einstellungen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				// JOptionPane.showMessageDialog(null, "Test" , "Test Titel",
				// JOptionPane.OK_CANCEL_OPTION);
				// hier Einstellungen aus dem Dialog auswerten
				ShowSettingDialog();

			}
		});
		Extras.add(Einstellungen);

		// Mails empfangen
		Aktualisieren = new JMenuItem("Mails empfangen");
		Aktualisieren.getAccessibleContext().setAccessibleDescription("Mails empfangen");
		Aktualisieren.setMnemonic(KeyEvent.VK_M);
		Aktualisieren.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		Extras.add(Aktualisieren);

		return menuBar;
	}


	public void refreshTreeStruct() {

		addTreeChildren(baum_root, dh.getFolderList());
		baum_strukt.expandPath(baum_strukt.getPathForRow(0));
		baum_strukt.setSelectionPath(baum_strukt.getPathForRow(1));

	}

	public JScrollPane guiGetFolders() {

		refreshTreeStruct();

		TreeSelectionListener tsList = new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath()
						.getLastPathComponent();
				System.out.println(node);

				addRunnable(new Runnable() {
					public void run() {
						changeOrdner(e.getNewLeadSelectionPath().toString());
						refreshMailListe();

						// refreshTreeStruct();
					}
				});

				// System.out.println( e.getNewLeadSelectionPath()) ;
			}
		};

		baum_strukt.addTreeSelectionListener(tsList);

		JScrollPane scroll = new JScrollPane(baum_strukt);

		scroll.setPreferredSize(new Dimension(250, 500));

		return scroll;
	}

	public void refreshMailListe() {

		m_simpleTableModel.setNewData(dh.getMailList());
		m_simpleTableModel.fireTableDataChanged();

	}

	public JScrollPane guiGetMailListe() {
		// m_simpleTableModel = new DataMailListTableModel(dh.getMailList());
		dh.setGewaehlterMailOrdner("[Ordner, Posteingang]");
		refreshMailListe();
		table_mailListe = new JTable(m_simpleTableModel);
		table_mailListe.setFillsViewportHeight(true);
		table_mailListe.setAutoCreateRowSorter(true);
		table_mailListe.setDefaultEditor(Object.class, null);
		
		table_mailListe.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				
		
				if (evt.getClickCount() == 2) {
					String texti;
					texti = getSelectedMailListRow(evt.getPoint(), 3);
					JOptionPane.showMessageDialog(null, texti, "Nachricht", JOptionPane.OK_CANCEL_OPTION);

				}
			}
		});

		TableColumnModel tcm = table_mailListe.getColumnModel();
		tcm.removeColumn(tcm.getColumn(4));
		tcm.removeColumn(tcm.getColumn(3));

		JScrollPane scroll = new JScrollPane(table_mailListe);
		// JScrollPane scrollPane = new JScrollPane(m_simpleTable);
		// getContentPane().add(scrollPane);

		return scroll;
	}

	public aMailClientGUI() {
		super("adriano's MailClient - GUI");
		// Set the default behaviour for the close button
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// BorderLayout is the default for JFrame

		// Add some components
		add(guiGetMenu(), BorderLayout.NORTH);
		add(guiGetFolders(), BorderLayout.WEST);
		add(guiGetMailListe(), BorderLayout.CENTER);
		add(guiGetStatusBar(), BorderLayout.SOUTH);

		// Set the size and show the window
		setSize(800, 600);
		setVisible(true);
	}

	public void addTreeChildren(DefaultMutableTreeNode parent, ArrayList<DataFolderList> Elements) {
		if (Elements.size() > 0) {

			for (DataFolderList Element : Elements) {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(Element.getName());

				addTreeChildren(child, Element.getChildren());

				parent.add(child);
			}

		}
	}

	/**
	 * The program starts here. Note that it will not terminate when the main
	 * method terminates. This is because some background threads are started
	 * for the GUI.
	 *
	 * @param args
	 *            Not used
	 */
	public static void main(String args[]) {
		new aMailClientGUI();

	}

}
