import java.util.ArrayList;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.table.AbstractTableModel;

/**
 * Model welches für das Nachrichten-Fenster gebraucht wird.
 * Reihenfolge hier definiert auch Reihenfolge und Sichtbarkeit von Feldern in Mail-Übersicht
 * @author ahaen
 *
 */
class DataMailListTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6659022226302820746L;

	public String[] m_colNames = { "Datum", "Absender", "Betreff", "Nachricht", "ID"};
	public Class[] m_colTypes = { String.class, String.class, String.class, String.class, String.class};

	private ArrayList<MailStruktur> m_macDataVector;

	public DataMailListTableModel() {
		super();
	}

	public DataMailListTableModel(ArrayList<MailStruktur> tmpMailList) {
		super();
		m_macDataVector = tmpMailList;
	}

	public void setNewData(ArrayList<MailStruktur> tmpMailList) {
		m_macDataVector = tmpMailList;
	}

	public int getColumnCount() {
		return m_colNames.length;
	}

	public int getRowCount() {
		return m_macDataVector.size();
	}

	public void setValueAt(Object value, int row, int col) {
		MailStruktur macData = (m_macDataVector.get(row));

		switch (col) {
		/*
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
			break;*/
		default:
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
		MailStruktur macData = (m_macDataVector.get(row));

		switch (col) {
		case 0:
			return macData.getDatum().toLocaleString();
		case 1:
			return macData.getAbsender();
		case 2:
			return macData.getBetreff();
		case 3:
			return macData.getNachricht();
		case 4:
			return macData.getID();
		default:
			break;
		}

		return new String();
	}
}
