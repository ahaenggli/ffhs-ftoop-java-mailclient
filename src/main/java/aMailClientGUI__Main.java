
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

public class aMailClientGUI__Main extends JFrame {

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
		new aMailClientGUI_Einstellungen();
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
		JMenuItem menuItem, Einstellungen, Aktualisieren, Neuesmail;


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
		Aktualisieren.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			
				addRunnable(new Runnable(){

					@Override
					public void run() {
						(new Thread(){
							public void run(){
								new ReceiveMail();
								refreshMailListe();
								changeOrdner(baum_strukt.getSelectionPath().toString());
								refreshMailListe();
							}
					}).start();
					}
				
				});
				
		
				
			}
			
		});
		Extras.add(Aktualisieren);

		// Neues Mail machen
		Neuesmail = new JMenuItem("Neue Nachricht");
		Neuesmail.getAccessibleContext().setAccessibleDescription("Neue Nachricht schreiben");
		Neuesmail.setMnemonic(KeyEvent.VK_N);
		
		Neuesmail.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new aMailClientGUI_MailFenster("", "", "", 1);				
			}
			
		});
		Extras.add(Neuesmail);
		
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
					new aMailClientGUI_MailFenster(getSelectedMailListRow(evt.getPoint(), 2), getSelectedMailListRow(evt.getPoint(), 3), getSelectedMailListRow(evt.getPoint(), 1), 0);
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

	public aMailClientGUI__Main() {
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
		new aMailClientGUI__Main();

	}

}
