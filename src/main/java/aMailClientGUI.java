/*
Funktionen
Ihre Anwendung muss mindestens folgende Funktionen implementieren:

- Konfiguration von POP3 sowie SMTP Zugangsdaten 

- Abholen von Mails von einem POP3 Server
- Einsortieren von abgeholten Mails in einen Standard Ordner „Neue Mails“
- Öffnen einer Detailansicht von Mails in einem neuen Fenster mit Antwort/Weiterleitungsfunktion
- Erstellen von neuen Mails und Senden via SMTP
- Beantworten und weiterleiten von Mails


Für die Anbindung des Mailservers wird Ihnen im Moodle eine Bibliothek zur Verfügung gestellt.
Optional können noch folgende Funktionen implementiert werden:
- Verarbeitung von Attachments in empfangenen und gesendeten Mails
- Automatisches Abholen per Zeitintervall
- Anlegen, Bearbeiten und Löschen von lokalen Mail Ordnern
- Verschieben von Mails in einen existierenden Ordner

*/
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import javax.swing.* ;



public class aMailClientGUI  extends JFrame {

  private JLabel StatusBar = new JLabel("StatusBar");
  private JTable mailListe;
  private SimpleTableModel m_simpleTableModel;
  
  private JFrame me = this;
   
	public void addRunnable(Runnable run){
	    	EventQueue.invokeLater(run);    			
	}
	
	  
	public JLabel guiGetStatusBar(){
		
		return StatusBar;
	}
	
	
	public void ShowSettingDialog() {
    // Erzeugung eines neuen Dialoges
    JDialog meinJDialog = new JDialog();
    meinJDialog.setTitle("Einstellungen");
    meinJDialog.setSize(450,300);
    meinJDialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE ) ;
    
    JPanel panelGreen = new JPanel();



 
    panelGreen.setBackground(Color.GREEN);


    // Erzeugung eines JTabbedPane-Objektes
    JTabbedPane tabpane = new JTabbedPane    (JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT );

    
    // POP3
    // Hier erzeugen wir unsere JPanels
    JPanel panelRot = new JPanel();
    // Hier setzen wir die Hintergrundfarben für die JPanels
    panelRot.setBackground(Color.RED);

    // Raster erstellen
    JLabel pop3_server_label   = new JLabel("POP3-Server: ");
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
    
    JButton pop3_save = new JButton ("Speichern");
    pop3_save.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ev){
        	aMailClientSettings.setPop3Server(pop3_serverField.getText());       
        	aMailClientSettings.setPop3User(pop3_usernameField.getText());   
        	aMailClientSettings.setPop3PW(pop3_passwordField.getText());   
        	aMailClientSettings.setPop3Port(pop3_portField.getText());   
           }
       }) ;

    panelRot.add(pop3_save);
    
    // Hier werden die JPanels als Registerkarten hinzugefügt
    tabpane.addTab("POP3", panelRot);
    
    
    
 // smtp

    JPanel panelBlue = new JPanel();
    panelBlue.setBackground(Color.CYAN);


    // Raster erstellen
    JLabel smtp_server_label   = new JLabel("smtp-Server: ");
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

    JButton smtp_save = new JButton ("Speichern");
    smtp_save.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ev){
        	aMailClientSettings.setsmtpServer(smtp_serverField.getText());
        	aMailClientSettings.setsmtpUser(smtp_usernameField.getText());
        	aMailClientSettings.setsmtpPW(smtp_passwordField.getText());
        	aMailClientSettings.setsmtpPort(smtp_portField.getText());
           }
       }) ;

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
	public JMenuBar guiGetMenu(){
		/*
		JPanel Nord = new JPanel(new GridLayout(1,5));
		Nord.setPreferredSize(new Dimension(10, 50));
		Nord.add( new JButton("Einstellungen") ) ;
		Nord.add( new JButton("Mails empfangen") ) ;
		return Nord;
		*/
		
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu Datei, submenu, Extras;
		JMenuItem menuItem, Einstellungen, Aktualisieren;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Datei
		Datei = new JMenu("Datei");
		Datei.setMnemonic(KeyEvent.VK_D);
		Datei.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		
		
		menuBar.add(Datei);

		menuItem = new JMenuItem("Beenden");	
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		
		
		// Lasse die Applikation sterben
		ActionListener exitusOmne = new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        
		    	addRunnable(new Runnable() {
		            public void run() {
		                try {
		                    StatusBar.setText( StatusBar.getText() + "Neuer Text");
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
		Extras.getAccessibleContext().setAccessibleDescription(
		        "Extras");		
		menuBar.add(Extras);
		
		
		//Build second menu in the menu bar.
		Einstellungen = new JMenuItem("Einstellungen");
		Einstellungen.getAccessibleContext().setAccessibleDescription("Einstellungen bearbeiten");
		Einstellungen.setMnemonic(KeyEvent.VK_E);
		Einstellungen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		
		Einstellungen.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent ev){
	            	
	             //   JOptionPane.showMessageDialog(null, "Test" , "Test Titel", JOptionPane.OK_CANCEL_OPTION);
	                //hier Einstellungen aus dem Dialog auswerten
	            	ShowSettingDialog();  
	                
	                
	            }
	        });               
		Extras.add(Einstellungen);				
		
		//Mails empfangen
		Aktualisieren = new JMenuItem("Mails empfangen");
		Aktualisieren.getAccessibleContext().setAccessibleDescription("Mails empfangen");
		Aktualisieren.setMnemonic(KeyEvent.VK_M);
		Aktualisieren.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		Extras.add(Aktualisieren);
		
		
		return menuBar;
	}
	
	
	public JScrollPane guiGetFolders(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( "1" ) ;
	      addTreeChildren( root, 3, 10 ) ;

	      DefaultTreeModel model  = new DefaultTreeModel( root ) ;
	      JTree            tree   = new JTree( model ) ;
	      
	      TreeSelectionListener tsList = new TreeSelectionListener(){
	    	  
	   	   public void valueChanged( TreeSelectionEvent e )
		   {
		      DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent() ;
		      System.out.println( node.getUserObject() ) ; 
		   }
	      };
	      
	      tree.addTreeSelectionListener( tsList ) ;

	      JScrollPane      scroll = new JScrollPane( tree ) ;
	      //scroll.setBounds(0, 0, 100, 800);
	      //getContentPane().add( scroll ) ;
	      scroll.setPreferredSize(new Dimension(250, 500));

		return scroll;
	}
	
	
	public JScrollPane guiGetMailListe(){
		
		
		String[] columnNames = {
				"Erhalten",
                "Von",
                "Betreff",                
                "Auswahl"};
		
		Object[][] data = {
			    {"Kathy", "Smith",
			     "Snowboarding", new Integer(5), new Boolean(false)},
			    {"John", "Doe",
			     "Rowing", new Integer(3), new Boolean(true)},
			    {"Sue", "Black",
			     "Knitting", new Integer(2), new Boolean(false)},
			    {"Jane", "White",
			     "Speed reading", new Integer(20), new Boolean(true)},
			    {"Joe", "Brown",
			     "Pool", new Integer(10), new Boolean(false)}
			};
		
		
		
	    Vector dummyMacData = new Vector(10, 10);
	    dummyMacData.addElement(new Data( "A", "123","C", false, new Integer(100)));
	    dummyMacData.addElement(new Data( "B", "234","C", false, new Integer(101)));
	    dummyMacData.addElement(new Data( "C", "456","C", false, new Integer(102)));
	    
	    m_simpleTableModel = new SimpleTableModel(dummyMacData);
	    JTable table = new JTable(m_simpleTableModel);
	    
	    
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		table.setDefaultEditor(Object.class, null);
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (row >= 0 && col >= 0 &&  evt.getClickCount() == 2 ) {
		            JOptionPane.showMessageDialog(null, "Test" + row, "Test Titel", JOptionPane.OK_CANCEL_OPTION);

		        }
		    }
		});
  
	    
	    JScrollPane scroll = new JScrollPane(table ) ;
	    //JScrollPane scrollPane = new JScrollPane(m_simpleTable);
	    //getContentPane().add(scrollPane);
	    
	    
		
		 return scroll;
	}
	
	
	public aMailClientGUI(){
		 super( "adriano's MailClient - GUI" ) ;
	      // Set the default behaviour for the close button
	      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
	      
	      // BorderLayout is the default for JFrame

	      // Add some components
	      add( guiGetMenu(), BorderLayout.NORTH  ) ;
	      add( guiGetFolders(), BorderLayout.WEST   ) ;
	      add( guiGetMailListe(),  BorderLayout.CENTER ) ;
	      add( guiGetStatusBar(),  BorderLayout.SOUTH  ) ;
	      
	      // Set the size and show the window
	      setSize( 800, 600 ) ;
	      setVisible( true ) ;
	}
	
	
	   public void addTreeChildren( DefaultMutableTreeNode parent, int count, int level )
	   {
	      if( level > 0 )
	      {
	         for( int i = 0; i < count; i++ )
	         {
	            DefaultMutableTreeNode child = new DefaultMutableTreeNode( parent.getUserObject() + String.valueOf(i+1) ) ;
	            addTreeChildren( child, count, level - 1 ) ;
	            parent.add( child ) ;
	         }
	      }
	   }
   
	   
	   /**
	    * The program starts here.
	    * Note that it will not terminate when the main
	    * method terminates. This is because some background
	    * threads are started for the GUI.
	    *
	    * @param args Not used
	    */
	   public static void main( String args[] )
	   {
	      new aMailClientGUI() ;
	   }
	   
}
