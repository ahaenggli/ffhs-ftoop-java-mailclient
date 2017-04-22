import java.awt.* ;
import javax.swing.* ;

/**
 * This is a simple demo for the BorderLayout
 *
 * @author  P. Tellenbach
 * @version 25-FEB-2010
 */
public class Border extends JFrame
{
   /**
    * The constructor defines the layout and
    * adds some components to the content pane.
    */
   public Border()
   {
      super( "BorderLayout-Beispiel" ) ;

      // Set the default behaviour for the close button
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;

      // BorderLayout is the default for JFrame

      // Add some components
      add( new JButton("Norden"), BorderLayout.NORTH  ) ;
      add( new JButton("Westen"), BorderLayout.WEST   ) ;
      add( new JButton("Mitte"),  BorderLayout.CENTER ) ;
      add( new JButton("Osten"),  BorderLayout.EAST   ) ;
      add( new JButton("Süden"),  BorderLayout.SOUTH  ) ;

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
      new Border() ;
   }
}
