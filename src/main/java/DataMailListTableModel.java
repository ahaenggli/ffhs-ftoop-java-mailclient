import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


class DataMailListTableModel extends AbstractTableModel  {
	
	private Date Datum;
	private String Sender;
	private String Betreff;
	private String Nachricht;
	
	
    public String[] m_colNames = { "Datum", "Absender", "Betreff", "Nachricht", "ID" };
    public Class[] m_colTypes = { Date.class, String.class, String.class, String.class,  String.class };

    private ArrayList<DataMailList> m_macDataVector; 	
	
    public DataMailListTableModel(){
    	super();
    }
    public DataMailListTableModel(ArrayList<DataMailList> tmpMailList) {
      super();
      m_macDataVector = tmpMailList;
    }
    
    public void setNewData(ArrayList<DataMailList> tmpMailList){
    	m_macDataVector = tmpMailList;    	
    }
    
    public int getColumnCount() {
      return m_colNames.length;
    }
    
    public int getRowCount() {
      return m_macDataVector.size();
    }
    
    public void setValueAt(Object value, int row, int col) {
    	DataMailList macData = (DataMailList) (m_macDataVector.get(row));

      switch (col) {
      case 0:
        macData.setDatum((Date) value);
        break;
      case 1:
        macData.setAbsender((String) value);
        break;
      case 2:
        macData.setBetreff((String) value);
        break;
      case 3:
        macData.setNachricht((String) value);
        break;
      case 4:
        macData.setID((String) value);
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
    	DataMailList macData = (DataMailList) (m_macDataVector.get(row));

      switch (col) {
      case 0:
        return macData.getDatum();
      case 1:
        return macData.getAbsender();
      case 2:
        return macData.getBetreff();
      case 3:
        return macData.getNachricht();
      case 4:
        return macData.getID();
      }

      return new String();
    }
  }

