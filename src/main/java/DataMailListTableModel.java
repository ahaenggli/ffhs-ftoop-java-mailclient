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

	/**
	 * Konstruktor
	 */
	public DataMailListTableModel() {
		super();
	}

	/**
	 * Wechselt Ordner, der gerade angezeigt wird
	 * 
	 * @param Ordner
	 *            Ordner, der gerade angezeigt wird
	 */
	public void changeOrdner(Preferences Ordner) {
		this.gewaehlterOrdner = Ordner;
	}

	/**
	 * setter Fuer die MailListe im Ordner
	 * 
	 * @param tmpMailList
	 *            Liste der Mail im aktuellen Ordner
	 */
	public void setNewData(ArrayList<MailStruktur> tmpMailList) {
		tmpMail = tmpMailList;
	}

	/*
	 * (non-Javadoc) Ermittelt Anzahl der Spalten
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (gewaehlterOrdner != null && gewaehlterOrdner == Configuration.getGesendet())
			return SpaltenNamen_Postausgang.length;
		else
			return SpaltenNamen_Normal.length;
	}

	/*
	 * (non-Javadoc) Ermittelt die Anzahl an Zeilen (= Anzahl Mails)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return tmpMail.size();
	}

	/*
	 * (non-Javadoc) Gibt die Spalten-Namen zurueck, je nach Ordner
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
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
