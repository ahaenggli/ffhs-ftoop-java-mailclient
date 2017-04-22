import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;

/**
 * This is a simple demo for the CardLayout
 *
 * @author  P. Tellenbach
 * @version 27-MAR-2014
 */
public class Card extends JFrame implements ActionListener
{
   private CardLayout layout ;

   /**
    * The constructor defines the layout and
    * adds some components to the content pane.
    */
   public Card()
   {
      super( "CardLayout-Beispiel" ) ;

      // Set the default behaviour for the close button
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;

      // Set the layout manager
      setLayout( (layout = new CardLayout()) ) ;

      // Add some components
      addCard( "Weiter" ) ;
      addCard( "Noch weiter" ) ;
      addCard( "Und noch weiter" ) ;

      // Set the size and show the window
      setSize( 400, 200 ) ;
      setVisible( true ) ;
   }

   public void addCard( String text )
   {
      JButton button = new JButton( text ) ;
      button.addActionListener( this ) ;
      add( button, text ) ;
   }

   public void actionPerformed( ActionEvent e )
   {
      layout.next( getContentPane() ) ;
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
      new Card() ;
   }
}
