import java.awt.* ;
import javax.swing.* ;

/**
 * This is a simple demo for the FlowLayout
 *
 * @author  P. Tellenbach
 * @version 25-FEB-2010
 */
public class Flow extends JFrame
{
   /**
    * The constructor defines the layout and
    * adds some components to the content pane.
    */
   public Flow()
   {
      super( "FlowLayout-Beispiel" ) ;

      // Set the default behaviour for the close button
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;

      // Set the layout manager
      setLayout( new FlowLayout() ) ;

      // Add some components
      add( new JButton("Erster") ) ;
      add( new JButton("Zweiter") ) ;
      add( new JButton("Der dritte String ist lang") ) ;
      add( new JButton("Vierter") ) ;
      add( new JButton("Fünfter") ) ;

      // Set the size and show the window
      setSize( 400, 200 ) ;
      setVisible( true ) ;
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
      new Flow() ;
   }
}
