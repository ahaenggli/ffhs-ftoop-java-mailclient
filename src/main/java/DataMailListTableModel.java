import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.table.AbstractTableModel;

/**
 * Model welches für das Nachrichten-Fenster gebraucht wird. Reihenfolge hier
 * definiert auch Reihenfolge und Sichtbarkeit von Feldern in Mail-Übersicht
 * 
 * @author ahaen
 *
 */
class DataMailListTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -6659022226302820746L;

	private String[] SpaltenNamen_Normal = { "Datum", "Absender", "Betreff" };
	private String[] SpaltenNamen_Postausgang = { "Datum", "Empfaenger", "Betreff" };

	private Preferences gewaehlterOrdner = null;
	private ArrayList<MailStruktur> tmpMail;

	public DataMailListTableModel() {
		super();
	}

	public void changeOrdner(Preferences Ordner) {
		this.gewaehlterOrdner = Ordner;
	}

	public DataMailListTableModel(Preferences Ordner) {
		super();
		this.gewaehlterOrdner = Ordner;
	}

	public DataMailListTableModel(ArrayList<MailStruktur> tmpMailList) {
		super();
		tmpMail = tmpMailList;
	}

	public void setNewData(ArrayList<MailStruktur> tmpMailList) {
		tmpMail = tmpMailList;
	}

	public int getColumnCount() {
		if (gewaehlterOrdner != null && gewaehlterOrdner == Configuration.getGesendet())
			return SpaltenNamen_Postausgang.length;
		else
			return SpaltenNamen_Normal.length;
	}

	public int getRowCount() {
		return tmpMail.size();
	}

	public void setValueAt(Object value, int row, int col) {
		// MailStruktur macData = (m_macDataVector.get(row));

	}

	public String getColumnName(int col) {
		if (gewaehlterOrdner != null && gewaehlterOrdner == Configuration.getGesendet())
			return SpaltenNamen_Postausgang[col];
		else
			return SpaltenNamen_Normal[col];
	}

	/*
	 * public Class getColumnClass(int col) { return m_colTypes[col]; }
	 */
	public Object getValueAt(int row, int col) {
		MailStruktur macData = (tmpMail.get(row));

		switch (col) {
		case 0:
			return macData.getDatum().toString();
		case 1:
			if (gewaehlterOrdner != null && gewaehlterOrdner == Configuration.getGesendet())
				return macData.getEmpfaenger();
			else
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
