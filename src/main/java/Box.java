import java.awt.* ;
import javax.swing.* ;

/**
 * This is a simple demo for the BoxLayout
 *
 * @author  P. Tellenbach
 * @version 29-NOV-2011
 */
public class Box extends JFrame
{
   /**
    * The constructor defines the layout and
    * adds some components to the content pane.
    */
   public Box()
   {
      super( "BoxLayout-Beispiel" ) ;

      // Set the default behaviour for the close button
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;

      // Get a reference to the content pane
      Container c = getContentPane() ;

      // Set the layout manager
      c.setLayout( new BoxLayout(c,BoxLayout.Y_AXIS) ) ;

      // Add some components
      c.add( new JButton("Erster") ) ;
      c.add( new JButton("Zweiter") ) ;
      c.add( new JButton("Der dritte String ist lang") ) ;
      c.add( new JButton("Vierter") ) ;
      c.add( new JButton("Fünfter") ) ;

      // Optimize the layout and show the window
      pack() ;
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
      new Box() ;
   }
}
