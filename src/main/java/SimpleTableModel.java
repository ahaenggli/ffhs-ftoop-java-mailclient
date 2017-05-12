import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


class SimpleTableModel extends AbstractTableModel {
    public String[] m_colNames = { "Empfangen", "Von", "Betreff", "Auswahl", "ID" };
    public Class[] m_colTypes = { String.class, String.class, String.class, Boolean.class,  Integer.class };

    private Vector m_macDataVector; 	
	
    public SimpleTableModel(Vector macDataVector) {
      super();
      m_macDataVector = macDataVector;
    }
    
    public int getColumnCount() {
      return m_colNames.length-2;
    }
    
    public int getRowCount() {
      return m_macDataVector.size();
    }
    
    public void setValueAt(Object value, int row, int col) {
      Data macData = (Data) (m_macDataVector.elementAt(row));

      switch (col) {
      case 0:
        macData.setA((String) value);
        break;
      case 1:
        macData.setB((String) value);
        break;
      case 2:
        macData.setC((String) value);
        break;
      case 3:
        macData.setD((Boolean) value);
        break;
      case 4:
        macData.setE((Integer) value);
        break;
      }
    }

    public String getColumnName(int col) {
      return m_colNames[col];
    }

    public Class getColumnClass(int col) {
      return m_colTypes[col];
    }
    public Object getValueAt(int row, int col) {
      Data macData = (Data) (m_macDataVector.elementAt(row));

      switch (col) {
      case 0:
        return macData.getA();
      case 1:
        return macData.getB();
      case 2:
        return macData.getC();
      case 3:
        return macData.getD();
      case 4:
        return macData.getE();
      }

      return new String();
    }
  }

