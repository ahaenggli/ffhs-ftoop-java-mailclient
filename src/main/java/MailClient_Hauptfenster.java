
/*
Implementiert
Ihre Anwendung muss mindestens folgende Funktionen implementieren:

- Konfiguration von POP3 sowie SMTP Zugangsdaten 
- Oeffnen einer Detailansicht von Mails in einem neuen Fenster mit Antwort/Weiterleitungsfunktion
- Beantworten und weiterleiten von Mails
- Erstellen von neuen Mails und Senden via SMTP
- Abholen von Mails von einem POP3 Server
- Einsortieren von abgeholten Mails in einen Standard Ordner "Neue Mails"
- Automatisches Abholen per Zeitintervall
- Anlegen, Bearbeiten und Löschen von lokalen Mail Ordnern

ToDo:

=======
- Verschieben von Mails in einen existierenden Ordner
- Verarbeitung von Attachments in empfangenen und gesendeten Mails

*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
import javax.swing.tree.TreePath;

/**
 * Hauptfenster vom MailClient
 * 
 * @author ahaen
 *
 */
public class MailClient_Hauptfenster extends JFrame {

	private static final long serialVersionUID = 8508453892315236372L;

	// GUI-wichtige Komponenten (Methodenuebergreifend)
	private DefaultMutableTreeNode OrdnerListe = null;
	private JTree baum_strukt = null;
	private OrdnerHandler ordnerHandler = null;

	private DataMailListTableModel MailGrideModel = new DataMailListTableModel();
	private MailHandler mailHandler = null;

	private JLabel StatusBar = new JLabel("StatusBar");
	private JTable table_mailListe;

	// Auto-Runner
	ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	boolean isSesRunning = false;
	Runnable AutoRunner = new Runnable() {
		@Override
		public void run() {
			try {

				if (Configuration.getAnzahlminuten() <= 0) {
					if (Configuration.getDebug())
						System.out.println("Thread should die");
					ses.shutdownNow();
					isSesRunning = false;
				} else {
					if (Configuration.getDebug())
						System.out.println("Ich bin gelaufen");
					isSesRunning = true;
					empfangeMails();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	/**
	 * invokeLater mit Runnable, damit GUI nicht blockiert bei div. anderen
	 * Methoden
	 * 
	 * @param run
	 *            runnable
	 */
	public void addRunnable(Runnable run) {
		EventQueue.invokeLater(run);
	}

	/**
	 * Aktualisiere GUI und Ordner Pruefe ob background-Thread fuer Mail empfang
	 * aktiviert werden muss
	 */
	public void refreshGUI() {
		addRunnable(new Runnable() {

			@Override
			public void run() {
				changeOrdner(baum_strukt.getSelectionPath().toString());
				refreshMailListe();

				if (!isSesRunning && Configuration.getAnzahlminuten() > 0) {
					ses = Executors.newScheduledThreadPool(1);
					ses.scheduleAtFixedRate(new Thread(AutoRunner), 0, Configuration.getAnzahlminuten(),
							TimeUnit.SECONDS);
				}

				baum_strukt.repaint();
				baum_strukt.updateUI();

			}

		});
	}

	/**
	 * Ermittle ausgewaehlte Mail fuer Antworten/Weiterleiten
	 * 
	 * @param evt
	 *            Event
	 * @param colS
	 *            Gewaehlte Zeile
	 * @return Herkunft von Mail
	 */
	public Object getSelectedMailListRow(Point evt, int colS) {
		Object texti = "";

		int row = table_mailListe.rowAtPoint(evt);
		int col = table_mailListe.columnAtPoint(evt);
		if (row >= 0 && col >= 0) {

			texti = table_mailListe.getModel().getValueAt(table_mailListe.convertRowIndexToModel(row), colS).toString();
			if (colS == 5)
				texti = mailHandler.getMailList().get(row).getHerkunft();

		}

		return texti;
	}

	/**
	 * Wechsle Ordner und darum auch MailListe
	 * 
	 * @param neu
	 *            Neuer Ordner von Baum
	 */
	public void changeOrdner(String neu) {
		ordnerHandler.setGewaehlterMailOrdner(neu);
		refreshGUI();

	}

	/**
	 * Rufe die Mails im Hintergrund ab
	 */
	public void empfangeMails() {

		new SwingWorker<Object, Object>() {
			ReceiveMail RevieceMailer = new ReceiveMail();

			@Override
			protected Object doInBackground() throws Exception {
				return RevieceMailer.getMails();
			}

			@Override
			protected void done() {
				if (RevieceMailer.getErfolg()) {
					setStatusBarText(RevieceMailer.getMailCounter() + " neue Mails empfangen");

					MailHandler.addMailToFolder(RevieceMailer.getnewMailList(), Configuration.getEingang());

				} else
					setStatusBarText(RevieceMailer.getFehlerText());
			}

		}.execute();

	}

	/**
	 * Setze den StatusBar-Text
	 * 
	 * @param text
	 *            Wert fuer StatusBar
	 */
	public void setStatusBarText(String text) {

		addRunnable(new Runnable() {

			@Override
			public void run() {
				StatusBar.setText(text);
				StatusBar.updateUI();

				changeOrdner(baum_strukt.getSelectionPath().toString());
				refreshMailListe();

			}

		});

	}

	/**
	 * Zeige Dialog fuer Einstellungen
	 */
	public void ShowSettingDialog() {
		new MailClient_EinstellungenFenster();
	}

	/**
	 * Menu aufbauen, ganz oben in Client
	 * 
	 * @return Menubar
	 */
	private JMenuBar guiGetMenu() {

		JMenuBar menuBar = new JMenuBar();
		;
		JMenu Datei, Extras;
		JMenuItem menuItem, Einstellungen, Aktualisieren, Neuesmail;

		// Datei
		Datei = new JMenu("Datei");
		Datei.setMnemonic(KeyEvent.VK_D);

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

		// Einstellungen
		Einstellungen = new JMenuItem("Einstellungen");
		Einstellungen.getAccessibleContext().setAccessibleDescription("Einstellungen bearbeiten");
		Einstellungen.setMnemonic(KeyEvent.VK_E);
		Einstellungen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));

		Einstellungen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				// Stoppe Background thread, es koennte ja Einstellung geaendert
				// werden
				if (isSesRunning)
					ses.shutdownNow();

				isSesRunning = false;
				ShowSettingDialog();

				// GUI aktualisieren
				refreshGUI();
			}
		});
		Extras.add(Einstellungen);

		// Mails empfangen
		Aktualisieren = new JMenuItem("Mails empfangen");
		Aktualisieren.getAccessibleContext().setAccessibleDescription("Mails empfangen");
		Aktualisieren.setMnemonic(KeyEvent.VK_M);
		Aktualisieren.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		Aktualisieren.addActionListener(new ActionListener() {
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

		Neuesmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MailClient_MailFenster(null, "", "", "", null, 1);
			}

		});
		Extras.add(Neuesmail);

		// Einstellungen löschen

		Neuesmail = new JMenuItem("Einstellungen löschen und beenden");
		Neuesmail.getAccessibleContext().setAccessibleDescription("Reset");
		// Neuesmail.setMnemonic(KeyEvent.VK_N);

		Neuesmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Configuration.deleteConfig();
				System.exit(0);
			}

		});
		Extras.add(Neuesmail);

		return menuBar;
	}

	/**
	 * Gibt Ordner-Bereich fuer linken Teil des GUI zurueck
	 * 
	 * @return
	 */
	private JScrollPane guiGetOrdner() {

		// Ordner laden/aktualisieren
		// Cache/Variable leeren
		OrdnerListe = new DefaultMutableTreeNode(Configuration.getNameRootFolder());
		ordnerHandler = new OrdnerHandler();

		System.out.println("Ordnerliste.ChildCount: " + OrdnerListe.getChildCount());

		// Aktuelle Ordner auslesen und anhï¿½ngen
		addTreeChildren(OrdnerListe, ordnerHandler.getFolderList());

		baum_strukt = new JTree(new DefaultTreeModel(OrdnerListe));

		System.out.println("Ordnerliste.ChildCount: " + OrdnerListe.getChildCount());

		// Baum ausklappen
		baum_strukt.expandPath(baum_strukt.getPathForRow(0));
		// Erstes Element auswaehlen
		baum_strukt.setSelectionPath(baum_strukt.getPathForRow(1));

		// Was bei Auswahl von Ordner passieren soll
		baum_strukt.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath()
						.getLastPathComponent();

				if (Configuration.getDebug())
					System.out.println(node);

				addRunnable(new Runnable() {
					public void run() {
						changeOrdner(e.getNewLeadSelectionPath().toString());
					}
				});

			}

		});

		baum_strukt.setEditable(true);
		baum_strukt.setComponentPopupMenu(getPopUpMenu());
		baum_strukt.addMouseListener(getMouseListener());

		JScrollPane scroll = new JScrollPane(baum_strukt);

		scroll.setPreferredSize(new Dimension(250, 500));

		return scroll;
	}

	/**
	 * MouseListener fuer Auswahl rechte Maustaste in Ordner-Baum
	 * 
	 * @return
	 */
	private MouseListener getMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent arg0) {
				// mit Rechtsklick auch richtiges Element auswählen
				if (arg0.getButton() == MouseEvent.BUTTON3) {
					TreePath pathForLocation = baum_strukt.getPathForLocation(arg0.getPoint().x, arg0.getPoint().y);
					if (pathForLocation != null)
						baum_strukt.setSelectionPath(pathForLocation);
				}
				super.mousePressed(arg0);
			}
		};
	}

	/**
	 * Context-Menu fuer Baum-Elemente
	 * 
	 * @return JPopupMenu
	 */
	private JPopupMenu getPopUpMenu() {
		JPopupMenu menu = new JPopupMenu();

		JMenuItem item2 = new JMenuItem("Neu");
		item2.addActionListener(getAddActionListener());
		menu.add(item2);

		JMenuItem item = new JMenuItem("Umbenennen");
		item.addActionListener(getEditActionListener());
		menu.add(item);

		JMenuItem item3 = new JMenuItem("Loeschen");
		item3.addActionListener(getDelActionListener());
		menu.add(item3);

		return menu;
	}

	/**
	 * Ordner hinzuefuegen in Baum
	 * 
	 * @return
	 */
	private ActionListener getAddActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (baum_strukt.getLastSelectedPathComponent() != null) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) baum_strukt.getLastSelectedPathComponent();
					if (node != null) {

						String neuerOrdner = "";

						neuerOrdner = JOptionPane.showInputDialog("Name des neuen Ordners:");

						if (neuerOrdner != null && !neuerOrdner.isEmpty()) {
							DefaultMutableTreeNode n = new DefaultMutableTreeNode(neuerOrdner);

							if (Configuration.createFolder(neuerOrdner, 0, ordnerHandler.getAktFolder()))
								node.add(n);

							refreshGUI();
						}
					}
				}
			}
		};
	}

	/**
	 * Ordnername bearbeiten in Baum
	 * 
	 * @return
	 */
	private ActionListener getEditActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (baum_strukt.getLastSelectedPathComponent() != null) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) baum_strukt.getLastSelectedPathComponent();
					if (node != null) {

						String editOrdner = "";

						editOrdner = JOptionPane.showInputDialog("Neuer Name:");

						if (editOrdner != null && !editOrdner.isEmpty()) {
							// DefaultMutableTreeNode n = new
							// DefaultMutableTreeNode(neuerOrdner);

							// ordnerHandler.getAktFolder().
							try {

								if (ordnerHandler.getAktFolder() != Configuration.getEingang()
										&& ordnerHandler.getAktFolder() != Configuration.getGesendet()
										&& ordnerHandler.getAktFolder() != Configuration.getFolders()) {

									Configuration.createFolder(editOrdner, 0, ordnerHandler.getAktFolder().parent());
									Configuration.copyNode(ordnerHandler.getAktFolder(),
											ordnerHandler.getAktFolder().parent().node(editOrdner));
									node.setUserObject(editOrdner);
									((DefaultTreeModel) baum_strukt.getModel()).nodeChanged(node);

									ordnerHandler.getAktFolder().removeNode();

								}

							} catch (BackingStoreException e) {

							}

							refreshGUI();
						}
					}
				}
			}
		};
	}

	/**
	 * Ordner loeschen in Baum
	 * 
	 * @return
	 */
	private ActionListener getDelActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (baum_strukt.getLastSelectedPathComponent() != null) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) baum_strukt.getLastSelectedPathComponent();
					if (node != null) {

						int reply = JOptionPane.showConfirmDialog(null, "Wirklich loeschen mit allen Mails?",
								"Loeschen", JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION) {

							try {

								if (ordnerHandler.getAktFolder() != Configuration.getEingang()
										&& ordnerHandler.getAktFolder() != Configuration.getGesendet()
										&& ordnerHandler.getAktFolder() != Configuration.getFolders()) {

									baum_strukt.setSelectionPath(
											new TreePath((((DefaultMutableTreeNode) node.getParent()).getPath())));
									baum_strukt.scrollPathToVisible(
											new TreePath((((DefaultMutableTreeNode) node.getParent()).getPath())));

									node.removeAllChildren();
									node.removeFromParent();

									((DefaultTreeModel) baum_strukt.getModel()).nodeChanged(node.getParent());

									ordnerHandler.getAktFolder().removeNode();

								}

							} catch (BackingStoreException e) {

							}

							refreshGUI();
						}
					}
				}
			}
		};
	}

	/**
	 * MailListe aktualisieren
	 */
	private void refreshMailListe() {
		mailHandler = new MailHandler(ordnerHandler.getAktFolder());
		MailGrideModel.setNewData(mailHandler.getMailList());
		MailGrideModel.fireTableDataChanged();
	}

	/**
	 * MailListe aufbauen fur GUI
	 * 
	 * @return JScrollPane
	 */
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
					new MailClient_MailFenster(null, (String) getSelectedMailListRow(evt.getPoint(), 2),
							(String) getSelectedMailListRow(evt.getPoint(), 3),
							(String) getSelectedMailListRow(evt.getPoint(), 1),
							(Preferences) getSelectedMailListRow(evt.getPoint(), 5), 0);

				}
			}
		});

		table_mailListe.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_DELETE) {

					// e.get
					if (table_mailListe.getSelectedRow() > -1) {
						int counter = 0;
						for (int idx : table_mailListe.getSelectedRows()) {
							mailHandler.removeMail(mailHandler.getMailList()
									.get(table_mailListe.convertRowIndexToModel(idx)).getHerkunft());
							counter++;
						}
						setStatusBarText(counter + " Mails geloescht");
						refreshGUI();
					}

				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			// nicht verwendet
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				//nicht verwendet
				
			}

		});

		TableColumnModel tcm = table_mailListe.getColumnModel();
		tcm.removeColumn(tcm.getColumn(4));
		tcm.removeColumn(tcm.getColumn(3));

		JScrollPane scroll = new JScrollPane(table_mailListe);

		return scroll;
	}

	/**
	 * Konstruktor
	 */
	public MailClient_Hauptfenster() {

		// JFrame aufbauen und Titel setzen
		super(Configuration.getMailClientName());

		// Was passiert beim Schliessen?
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// BorderLayout is the default for JFrame

		/*
		 * Fuege Komponenten zum Hauptschirm dazu Kompenenten werden in eigenen
		 * Methoden aufgebaut fuer bessere uebersicht
		 */
		add(guiGetMenu(), BorderLayout.NORTH);
		add(guiGetOrdner(), BorderLayout.WEST);
		add(guiGetMailListe(), BorderLayout.CENTER);
		add(StatusBar, BorderLayout.SOUTH);

		// Fenstergroesse setzen
		setSize(800, 600);
		// Sichtbar machen
		setVisible(true);
		refreshGUI();
	}

	/**
	 * Haengt einem Ordner andere Ordner unter
	 * 
	 * @param parent
	 *            Diesem Element werden die anderen angehaengt
	 * @param Elements
	 *            Elemente zum anhaengen
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
	 * main-Methode fuer Programmstart
	 *
	 * @param args
	 *            nicht verwendet
	 */
	public static void main(String args[]) {
		new MailClient_Hauptfenster();

	}

}
