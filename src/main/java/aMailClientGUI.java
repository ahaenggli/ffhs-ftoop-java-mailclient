import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import javax.swing.* ;



public class aMailClientGUI  extends JFrame {

	
	public void addRunnable(Runnable run){
	    	EventQueue.invokeLater(run);    			
	}
	
	   /**
	    * The constructor defines the layout and
	    * adds some components to the content pane.
	    */
	JLabel StatusBar = new JLabel("StatusBar");
	
	public JMenuBar guiOben(){
		/*
		JPanel Nord = new JPanel(new GridLayout(1,5));
		Nord.setPreferredSize(new Dimension(10, 50));
		Nord.add( new JButton("Einstellungen") ) ;
		Nord.add( new JButton("Mails empfangen") ) ;
		return Nord;
		*/
		
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu Datei, Einstellungen, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		Datei = new JMenu("Datei");
		Datei.setMnemonic(KeyEvent.VK_A);
		Datei.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(Datei);

		//a group of JMenuItems
		menuItem = new JMenuItem("A text-only menu item",
		                         KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "This doesn't really do anything");
		Datei.add(menuItem);

		menuItem = new JMenuItem("Both text and icon",
		                         new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		Datei.add(menuItem);

		menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_D);
		Datei.add(menuItem);

		//a group of radio button menu items
		Datei.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		Datei.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		Datei.add(rbMenuItem);

		//a group of check box menu items
		Datei.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		Datei.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		Datei.add(cbMenuItem);

		//a submenu
		Datei.addSeparator();
		
		menuItem = new JMenuItem("Beenden");	
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK));
		
		
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

		//Build second menu in the menu bar.
		Einstellungen = new JMenu("Einstellungen");
		Einstellungen.setMnemonic(KeyEvent.VK_N);
		Einstellungen.getAccessibleContext().setAccessibleDescription(
		        "This menu does nothing");
	                      
		menuBar.add(Einstellungen);
		
		
		return menuBar;
	}
	
	
	public JScrollPane guiLinks(){
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
	
	public aMailClientGUI(){
		 super( "adriano's MailClient - GUI" ) ;
	      // Set the default behaviour for the close button
	      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
	      
	      // BorderLayout is the default for JFrame

	      // Add some components
	      add( guiOben(), BorderLayout.NORTH  ) ;
	      add( guiLinks(), BorderLayout.WEST   ) ;
	      add( new JButton("Mitte"),  BorderLayout.CENTER ) ;
	      //add( new JButton("Osten"),  BorderLayout.EAST   ) ;
	      add( StatusBar,  BorderLayout.SOUTH  ) ;
	      
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
