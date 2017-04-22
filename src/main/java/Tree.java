import java.awt.* ;
import java.awt.event.* ;
import java.util.ArrayList ;
import javax.swing.* ;
import javax.swing.event.* ;
import javax.swing.tree.* ;

public class Tree extends JFrame implements TreeSelectionListener
{
   public Tree()
   {
      super( "TreeDemo" ) ;

      // Set the default behaviour for the close button
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;

      DefaultMutableTreeNode root = new DefaultMutableTreeNode( "1" ) ;
      addChildren( root, 3, 3 ) ;

      DefaultTreeModel model  = new DefaultTreeModel( root ) ;
      JTree            tree   = new JTree( model ) ;
      tree.addTreeSelectionListener( this ) ;

      JScrollPane      scroll = new JScrollPane( tree ) ;

      getContentPane().add( scroll ) ;

      setSize( new Dimension(400,200) ) ;
      setVisible( true ) ;
   }

   public void addChildren( DefaultMutableTreeNode parent, int count, int level )
   {
      if( level > 0 )
      {
         for( int i = 0; i < count; i++ )
         {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode( parent.getUserObject() + String.valueOf(i+1) ) ;
            addChildren( child, count, level - 1 ) ;
            parent.add( child ) ;
         }
      }
   }

   public void valueChanged( TreeSelectionEvent e )
   {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent() ;
      System.out.println( node.getUserObject() ) ; 
   }

   public static void main( String args[] )
   {
      new Tree() ;
   } 
}