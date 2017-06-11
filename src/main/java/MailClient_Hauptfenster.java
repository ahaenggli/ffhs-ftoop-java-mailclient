
/*
Implementiert:
- Konfiguration von POP3 sowie SMTP Zugangsdaten 
- Oeffnen einer Detailansicht von Mails in einem neuen Fenster mit Antwort/Weiterleitungsfunktion
- Beantworten und weiterleiten von Mails
- Erstellen von neuen Mails und Senden via SMTP
- Abholen von Mails von einem POP3 Server
- Einsortieren von abgeholten Mails in einen Standard Ordner "Neue Mails"
- Automatisches Abholen per Zeitintervall
- Anlegen, Bearbeiten und Löschen von lokalen Mail Ordnern

TODO:
- Verschieben von Mails in einen existierenden Ordner
- Verarbeitung von Attachments in empfangenen und gesendeten Mails

*/

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import javax.swing.JDialog;
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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
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
					if (Configuration.isDebug())
						System.out.println("Thread should die");
					ses.shutdownNow();
					isSesRunning = false;
				} else {
					if (Configuration.isDebug())
						System.out.println("Ich bin gelaufen");
					isSesRunning = true;
					empfangeMails();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	public TreePath getSelectedPath() {
		return baum_strukt.getSelectionPath();
	}

	/**
	 * invokeLater mit Runnable, damit GUI nicht blockiert bei div. anderen
	 * Methoden
	 * 
	 * @param run
	 *            runnable zum Ausfuehren in Hintergrund
	 */
	public void addRunnable(Runnable run) {
		// EventQueue.invokeLater(run);
		SwingUtilities.invokeLater(run);
	}

	/**
	 * Oeffne neues MailFenster in neuem Thread
	 * 
	 * @param msX
	 *            MailDaten falls bestehendes Mail geoeffnet wird
	 * @param ParamX
	 *            Ansichtsparam fuer Beschriftung von labels
	 *            (Absender/Empfaenger)
	 */
	public void oeffneMailFenster(MailStruktur msX, int ParamX) {
		MailStruktur ms = null;

		if (msX != null)
			ms = msX;
		else
			ms = new MailStruktur(); // Leeres Mail...

		final MailStruktur fms = ms;
		final int fParam = ParamX;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				JDialog jd = new MailClient_MailFenster(fms, fParam);
				jd.setVisible(true);
			}

		};

		SwingUtilities.invokeLater(r);

	}

	/**
	 * Ermittle TreePahth an Koordinate
	 * 
	 * @param x
	 *            X-Koorinate aus GUI
	 * @param y
	 *            Y-Koordinate aus GUI
	 * @return TreePath von Auswahl
	 */
	public TreePath getPathInBaumStrukt(int x, int y) {
		return baum_strukt.getPathForLocation(x, y);
	}

	/**
	 * Scrolle/Oeffne Baum an gewuenschten Ort
	 * 
	 * @param to
	 *            Tree Path zum Anzeigen in BaumStruktur
	 */
	public void scrollBaumStrukt(TreePath to) {

		baum_strukt.scrollPathToVisible(to);
	}

	/**
	 * Ermittelt das aktuelle getLastSelectedPathComponent von baum_strukt wird
	 * in anonymen inneren Klassen verwendet
	 * 
	 * @return Object fuer baum_strukt.getLastSelectedPathComponent()
	 */
	public Object getBaumStruktPath() {
		return baum_strukt.getLastSelectedPathComponent();
	}

	/**
	 * Ermittelt das aktuelle Model von baum_strukt; wird in anonymen inneren
	 * Klassen verwendet
	 * 
	 * @return TreeModel von baum_strukt
	 */
	public TreeModel getBaumStrukturModel() {
		return baum_strukt.getModel();
	}

	/**
	 * Zeichne die Baumstruktur neu bei anonymen inneren Klassen
	 */
	public void repaint_baum_strukt() {
		baum_strukt.updateUI();
		baum_strukt.repaint();
	}

	/**
	 * Zeichne StatusBar neu bei anonymen inneren Klassen
	 */
	public void repaint_StatusBar() {
		StatusBar.updateUI();
		StatusBar.repaint();
	}

	/**
	 * Aktualisiere GUI und Ordner Pruefe ob background-Thread fuer Mail empfang
	 * aktiviert werden muss
	 */
	public void refreshGUI() {
		addRunnable(new Runnable() {

			@Override
			public void run() {
				changeOrdner(getSelectedPath().toString());
				refreshMailListe();

				if (!isSesRunning && Configuration.getAnzahlminuten() > 0) {
					ses = Executors.newScheduledThreadPool(1);
					ses.scheduleAtFixedRate(new Thread(AutoRunner), 0, Configuration.getAnzahlminuten(),
							TimeUnit.MINUTES);
				}

				repaint_baum_strukt();
				repaint_StatusBar();
			}

		});
	}

	/**
	 * Ermittelt orignalen Array-Index zu Row
	 * 
	 * @param row
	 *            Zeile, deren original-index gewuenscht wird
	 * @return Index innerhalb ArrayList von gewueschter Sichtbaren Zeile (row)
	 */
	public int MailListRowToIndex(int row) {
		int result = -1;
		if (row > -1)
			result = table_mailListe.convertRowIndexToModel(row);
		return result;
	}

	/**
	 * Ist mind. 1 E-Mail gewaehlt?
	 * 
	 * @return true || false
	 */
	public boolean MailListHatAuswahl() {
		boolean result = false;

		if (table_mailListe.getSelectedRow() > -1)
			result = true;

		return result;
	}

	/**
	 * Alle ausgewaehlten Zeilen
	 * 
	 * @return Array (int) der gewaehlten Zeilen
	 */
	public int[] gewaehlteMails() {
		return table_mailListe.getSelectedRows();
	}

	/**
	 * Ermittle ausgewaehlte Mail fuer Antworten/Weiterleiten
	 * 
	 * @param evt
	 *            Event mit Point von Aufruf drin
	 * @return Index (row) von gewähltem Mail in original Array Falls Klick
	 *         ausserhalb Bereich ist Rückgabe = -1
	 */
	public int getSelectedMailListRow(Point evt) {
		int idx_view = table_mailListe.rowAtPoint(evt);
		int result = -1;

		if (idx_view > -1)
			result = MailListRowToIndex(idx_view);

		return result;
	}

	/**
	 * Ermittle den aktuellen Ordner fuer anonyme innere Klassen.
	 * 
	 * @return ordnerHandler.getAktFolder()
	 */
	public Preferences getGewaehlterOrdner() {
		return ordnerHandler.getAktFolder();
	}

	/**
	 * Wechsle Ordner und darum auch MailListe
	 * 
	 * @param neu
	 *            Neuer Ordner von Baum
	 */
	public void changeOrdner(String neu) {
		ordnerHandler.setGewaehlterMailOrdner(neu);
		// refreshGUI();
		refreshMailListe();

	}

	/**
	 * Rufe die Mails im Hintergrund ab
	 */
	public void empfangeMails() {

		new SwingWorker<Boolean, Object>() {
			ReceiveMail RevieceMailer = new ReceiveMail();

			@Override
			protected Boolean doInBackground() throws Exception {
				return Boolean.valueOf(RevieceMailer.getMails());
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

	public void setPublicStatusBarText(String txt) {
		StatusBar.setText(txt);
	}

	/**
	 * Setze den StatusBar-Text in inneren anonymen Klassen
	 * 
	 * @param text
	 *            Wert fuer StatusBar
	 */
	public void setStatusBarText(final String text) {

		addRunnable(new Runnable() {

			@Override
			public void run() {
				setPublicStatusBarText(text);
				refreshGUI();
			}

		});

	}

	/**
	 * Zeige Dialog fuer Einstellungen
	 */
	public void ShowSettingDialog() {
		JDialog jd = new MailClient_EinstellungenFenster();
		jd.setVisible(true);
	}

	/**
	 * Menu aufbauen, ganz oben in Client
	 * 
	 * @return Menubar
	 */
	private JMenuBar guiGetMenu() {

		JMenuBar menuBar = new JMenuBar();

		JMenu Datei, Extras;
		JMenuItem menuItem, Einstellungen, Aktualisieren, Neuesmail;

		// Datei
		Datei = new JMenu("Datei");
		Datei.setMnemonic(KeyEvent.VK_D);

		menuBar.add(Datei);

		menuItem = new JMenuItem("Beenden");
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});
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

				oeffneMailFenster(null, 1);

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

		if (Configuration.isDebug())
			System.out.println("Ordnerliste.ChildCount: " + OrdnerListe.getChildCount());

		// Aktuelle Ordner auslesen und anhï¿½ngen
		addTreeChildren(OrdnerListe, ordnerHandler.getFolderList());

		baum_strukt = new JTree(new DefaultTreeModel(OrdnerListe));

		if (Configuration.isDebug())
			System.out.println("Ordnerliste.ChildCount: " + OrdnerListe.getChildCount());

		// Baum ausklappen
		baum_strukt.expandPath(baum_strukt.getPathForRow(0));
		// Erstes Element auswaehlen
		setBaumStruktSelectionPath(baum_strukt.getPathForRow(1));

		// Was bei Auswahl von Ordner passieren soll
		baum_strukt.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(final TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath()
						.getLastPathComponent();

				if (Configuration.isDebug())
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
	 * Wrapper Methoden für baum_strukt.setSelectionPath bei anonymen inneren
	 * Klassen
	 * 
	 * @param pathForLocation
	 *            TreePath: Neuer Pfad zum Setzen
	 */
	public void setBaumStruktSelectionPath(TreePath pathForLocation) {
		baum_strukt.setSelectionPath(pathForLocation);
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
					TreePath pathForLocation = getPathInBaumStrukt(arg0.getPoint().x, arg0.getPoint().y);
					if (pathForLocation != null)
						setBaumStruktSelectionPath(pathForLocation);
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
				if (getBaumStruktPath() != null) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getBaumStruktPath();
					if (node != null) {

						String neuerOrdner = "";

						neuerOrdner = JOptionPane.showInputDialog("Name des neuen Ordners:");

						if (neuerOrdner != null && !neuerOrdner.isEmpty()) {
							DefaultMutableTreeNode n = new DefaultMutableTreeNode(neuerOrdner);

							if (Configuration.createFolder(neuerOrdner, 0, getGewaehlterOrdner()))
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
				if (getBaumStruktPath() != null) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getBaumStruktPath();
					if (node != null) {

						String editOrdner = "";

						editOrdner = JOptionPane.showInputDialog("Neuer Name:");

						if (editOrdner != null && !editOrdner.isEmpty()) {

							try {

								if (getGewaehlterOrdner() != Configuration.getEingang()
										&& getGewaehlterOrdner() != Configuration.getGesendet()
										&& getGewaehlterOrdner() != Configuration.getFolders()) {

									Configuration.createFolder(editOrdner, 0, getGewaehlterOrdner().parent());
									Configuration.copyNode(getGewaehlterOrdner(),
											getGewaehlterOrdner().parent().node(editOrdner));
									node.setUserObject(editOrdner);
									((DefaultTreeModel) getBaumStrukturModel()).nodeChanged(node);

									getGewaehlterOrdner().removeNode();

								}

							} catch (BackingStoreException e) {
								if (Configuration.isDebug())
									e.printStackTrace();

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
				if (getBaumStruktPath() != null) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getBaumStruktPath();
					if (node != null) {

						int reply = JOptionPane.showConfirmDialog(null, "Wirklich loeschen mit allen Mails?",
								"Loeschen", JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION) {

							try {

								if (getGewaehlterOrdner() != Configuration.getEingang()
										&& getGewaehlterOrdner() != Configuration.getGesendet()
										&& getGewaehlterOrdner() != Configuration.getFolders()) {

									setBaumStruktSelectionPath(
											new TreePath((((DefaultMutableTreeNode) node.getParent()).getPath())));
									scrollBaumStrukt(
											new TreePath((((DefaultMutableTreeNode) node.getParent()).getPath())));

									node.removeAllChildren();
									node.removeFromParent();

									((DefaultTreeModel) getBaumStrukturModel()).nodeChanged(node.getParent());

									getGewaehlterOrdner().removeNode();

								}

							} catch (BackingStoreException e) {
								if (Configuration.isDebug())
									e.printStackTrace();

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
	public void refreshMailListe() {
		mailHandler = new MailHandler(getGewaehlterOrdner());

		// MailGrideModel = new DataMailListTableModel();
		MailGrideModel.changeOrdner(getGewaehlterOrdner());
		MailGrideModel.setNewData(mailHandler.getMailList());
		MailGrideModel.fireTableDataChanged();
		MailGrideModel.fireTableStructureChanged();
	}

	/**
	 * Wrapper: Gibt den MailHandler fuer die anonymen inneren Klassen zurueck
	 * 
	 * @return MailHandler
	 */
	public MailHandler getMailHandler() {
		return mailHandler;
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
					int idx = getSelectedMailListRow(evt.getPoint());
					if (idx > -1 && idx <= getMailHandler().getMailList().size())
						oeffneMailFenster(getMailHandler().getMailList().get(idx), 0);

				}
			}
		});

		table_mailListe.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_DELETE) {

					// e.get
					if (MailListHatAuswahl()) {
						int counter = 0;
						for (int idx : gewaehlteMails()) {
							int row = MailListRowToIndex(idx);
							if (row > -1)
								getMailHandler().removeMail(getMailHandler().getMailList().get(row).getHerkunft());
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
				// nicht verwendet

			}

		});

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

			if (Configuration.isDebug())
				System.out.println("Anzahl Elemente: " + Elements.size());

			for (OrdnerStruktur Element : Elements) {

				if (Configuration.isDebug())
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
		JFrame main = new MailClient_Hauptfenster();
		main.setVisible(true);

	}

}
