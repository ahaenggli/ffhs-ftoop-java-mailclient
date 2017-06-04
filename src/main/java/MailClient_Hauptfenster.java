
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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Hauptfenster vom MailClient
 * @author ahaen
 *
 */
public class MailClient_Hauptfenster extends JFrame {

	private static final long serialVersionUID = 8508453892315236372L;
	
	private DefaultMutableTreeNode OrdnerListe = null;
	private JTree baum_strukt = null;
	private OrdnerHandler ordnerHandler = null;
	
	private DataMailListTableModel MailGrideModel = new DataMailListTableModel();
	private MailHandler mailHandler = null;
	
	
	private JLabel StatusBar = new JLabel("StatusBar");
	private JTable table_mailListe;

	
	//private MailClient_Hauptfenster me = this;
	

	public void addRunnable(Runnable run) {
		EventQueue.invokeLater(run);
	}

	public void refreshGUI(){
		addRunnable(new Runnable(){

			@Override
			public void run() {				
				changeOrdner(baum_strukt.getSelectionPath().toString());
				refreshMailListe();				
				
			}
			
		});
	}
	
	
	public Object getSelectedMailListRow(Point evt, int colS){
		Object texti = "";
		
		int row = table_mailListe.rowAtPoint(evt);
		int col = table_mailListe.columnAtPoint(evt);
		if (row >= 0 && col >= 0) {
			
			texti = table_mailListe.getModel().getValueAt(table_mailListe.convertRowIndexToModel(row), colS)
					.toString();
			if(colS == 5)
			texti = mailHandler.getMailList().get(row).getHerkunft();
			
		}
		
		return texti;
	}
	
	public void changeOrdner(String neu) {
		ordnerHandler.setGewaehlterMailOrdner(neu);
		refreshMailListe();
		
		
	}

	public void empfangeMails(){
		
		new SwingWorker(){
			RevieceMail RevieceMailer = new RevieceMail();
			
			@Override
			protected Object doInBackground() throws Exception {
				return RevieceMailer.getMails() ;				
			}
			
			@Override
			protected void done(){
				if(	RevieceMailer.getErfolg() ){
					setStatusBarText(RevieceMailer.getMailCounter() + " neue Mails empfangen");
					
					MailHandler.addMailToFolder(RevieceMailer.getnewMailList(), Configuration.getEingang());
					
				}
				else setStatusBarText(RevieceMailer.getFehlerText());
			}
			
		}.execute();
		
		
	}
	
	public void setStatusBarText(String text){
		
		addRunnable(new Runnable(){

			@Override
			public void run() {		
				StatusBar.setText(text);
				StatusBar.updateUI();
				
				changeOrdner(baum_strukt.getSelectionPath().toString());
				refreshMailListe();	
				
			}
			
		});
		
		
		
		
	}
	
	
	public JLabel guiGetStatusBar() {

		return StatusBar;
	}

	public void ShowSettingDialog() {
		new MailClient_EinstellungenFenster();
	}

	/**
	 * Menu aufbauen, ganz oben in Client
	 * @return Menubar
	 */
	private JMenuBar guiGetMenu() {

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

		
		menuItem.addActionListener(actionEvent -> System.exit(0));
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
			
				empfangeMails();
				
		
				
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
				new MailClient_MailFenster(null, "", "", "", null, 1);				
			}
			
		});
		Extras.add(Neuesmail);
		
		
		
		
		// Neues Mail machen
				Neuesmail = new JMenuItem("Einstellungen löschen");
				Neuesmail.getAccessibleContext().setAccessibleDescription("Reset");
				//Neuesmail.setMnemonic(KeyEvent.VK_N);
				
				Neuesmail.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
					 Configuration.deleteConfig();
					 refreshGUI();
					 
					}
					
				});
				Extras.add(Neuesmail);
				
				
		return menuBar;
	}


	
	/**
	 * Gibt Ordner-Bereich für linken Teil des GUI zurück
	 * @return
	 */
	private JScrollPane guiGetOrdner() {

		// Ordner laden/aktualisieren
		// Cache/Variable leeren
		OrdnerListe = new DefaultMutableTreeNode(Configuration.getNameRootFolder()); 		
		ordnerHandler = new OrdnerHandler();
		
		System.out.println("Ordnerliste.ChildCount: " + OrdnerListe.getChildCount());
		
		// Aktuelle Ordner auslesen und anhängen
		addTreeChildren(OrdnerListe, ordnerHandler.getFolderList());		
		
		baum_strukt = new JTree(new DefaultTreeModel(OrdnerListe));
		
		System.out.println("Ordnerliste.ChildCount: " + OrdnerListe.getChildCount());
		
		
		// Baum ausklappen
		baum_strukt.expandPath(baum_strukt.getPathForRow(0));
		//Erstes Element auswählen
		baum_strukt.setSelectionPath(baum_strukt.getPathForRow(1));
		
		// Was bei Auswahl von Ordner passieren soll


		//baum_strukt.addTreeSelectionListener(tsList);
		baum_strukt.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath()
						.getLastPathComponent();
				
				if(Configuration.getDebug())
				System.out.println(node);

				addRunnable(new Runnable() {
					public void run() {
						changeOrdner(e.getNewLeadSelectionPath().toString());
						//refreshTreeStruct();
						// refreshGUI();

						// refreshTreeStruct();
					}
				});

				// System.out.println( e.getNewLeadSelectionPath()) ;
			}
						
		});
		
		baum_strukt.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
				if(Configuration.getDebug())
					System.out.println(	e.getKeyCode() );
					
					if(e.getKeyCode() == KeyEvent.VK_DELETE)
					System.out.println("todo: Delete");
					
				
			}

			@Override
			public void keyTyped(KeyEvent k) {
				// TODO Auto-generated method stub
		
			}
			
		});
		
		
		JScrollPane scroll = new JScrollPane(baum_strukt);

		scroll.setPreferredSize(new Dimension(250, 500));

		return scroll;
	}

	
	public void refreshMailListe() {	
		mailHandler = new MailHandler(ordnerHandler.getAktFolder());
		MailGrideModel.setNewData(mailHandler.getMailList());
		MailGrideModel.fireTableDataChanged();
	}
	

	public JScrollPane guiGetMailListe() {

		refreshMailListe();
		table_mailListe = new JTable(MailGrideModel);
		table_mailListe.setFillsViewportHeight(true);
		table_mailListe.setAutoCreateRowSorter(true);
		table_mailListe.setDefaultEditor(Object.class, null);
		
		table_mailListe.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
		
				if (evt.getClickCount() == 2) {
					new MailClient_MailFenster( null,
							(String) getSelectedMailListRow(evt.getPoint(), 2), 
							(String) getSelectedMailListRow(evt.getPoint(), 3), 
							(String) getSelectedMailListRow(evt.getPoint(), 1),
							(Preferences) getSelectedMailListRow(evt.getPoint(), 5), 
							0);
					
				}
			}
		});
	
		table_mailListe.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
		
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_DELETE){
					
					//e.get
		if(table_mailListe.getSelectedRow() > -1)
		{			
			int counter = 0;
			for(int idx :	table_mailListe.getSelectedRows()){
				mailHandler.removeMail(mailHandler.getMailList().get(table_mailListe.convertRowIndexToModel(idx)).getHerkunft());
				counter++;
			}	
			setStatusBarText(counter + " Mails gelöscht");
			refreshMailListe();
		}
		
		//			refreshGUI();
				} //else System.out.println(e.getKeyCode());
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

	public MailClient_Hauptfenster() {

		// JFrame aufbauen und Titel setzen
		super(Configuration.getMailClientName());
		
		// Was passiert beim Schliessen?
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// BorderLayout is the default for JFrame

		/*
		 *  Füge Komponenten zum Hauptschirm dazu
		 *  Kompenenten werden in eigenen Methoden aufgebaut für 
		 *  bessere Übersicht
		 */
		add(guiGetMenu(), BorderLayout.NORTH);
		add(guiGetOrdner(), BorderLayout.WEST);
		add(guiGetMailListe(), BorderLayout.CENTER);
		add(guiGetStatusBar(), BorderLayout.SOUTH);

		// Fenstergrösse setzen
		setSize(800, 600);
		// Sichtbar machen
		setVisible(true);
	}

	/**
	 * Hängt einem Ordner andere Ordner unter
	 * @param parent
	 * 			Diesem Element werden die anderen angehängt
	 * @param Elements
	 * 			Elemente zum anhängen
	 */
	public void addTreeChildren(DefaultMutableTreeNode parent, ArrayList<OrdnerStruktur> Elements) {
		if (Elements.size() > 0) {
			
			System.out.println("Anzahl Elemente: " + Elements.size());
			
			for (OrdnerStruktur Element : Elements) {
			
				System.out.println("Element: " + Element.getName());
				
				
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
		new MailClient_Hauptfenster();

	}

}
