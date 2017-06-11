import java.util.*;

/**
 * Datenstruktir für einen Ornder Wird meistens als als List bzw. Array
 * gebraucht
 * 
 * @author ahaen
 *
 */
public class OrdnerStruktur implements Comparable<OrdnerStruktur> {

	private int SortNr;
	private String Name;
	private String AbsolutePath;
	private ArrayList<OrdnerStruktur> Children;

	/**
	 * Konstruktor
	 * 
	 * @param SortNr
	 *            LaufNr fuer Sortierung innerhalb Ordner
	 * @param Name
	 *            Name des Ordners
	 * @param AbsolutePath
	 *            Absoluter Pfad des Ordners
	 * @param Children
	 *            Alle Unterordner des Ordners
	 */
	public OrdnerStruktur(int SortNr, String Name, String AbsolutePath, ArrayList<OrdnerStruktur> Children) {
		this.SortNr = SortNr;
		this.Name = Name;
		this.Children = Children;
		this.AbsolutePath = AbsolutePath;
	}

	/**
	 * SortNr ermitteln fuer Reihenfolge bei Sortierung und Baumstruktur (GUI)
	 * 
	 * @return Zahl
	 */
	public int getSortNr() {
		return this.SortNr;
	}

	/**
	 * Ordnername ermitteln
	 * 
	 * @return Text
	 */
	public String getName() {
		return this.Name;
	}

	/**
	 * Alle Unterordner ermitteln
	 * 
	 * @return Liste der Unterordner
	 */
	public ArrayList<OrdnerStruktur> getChildren() {
		return this.Children;
	}

	/*
	 * compareTo fuer Sortierung
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(OrdnerStruktur o) {
		Integer a = Integer.valueOf(this.getSortNr());
		Integer b = Integer.valueOf(o.getSortNr());

		return a.compareTo(b);
	}

	/**
	 * Pfad ermitteln
	 * 
	 * @return Text
	 */
	public String getAbsolutePath() {
		return AbsolutePath;
	}

	/**
	 * Pfad setzen
	 * 
	 * @param absolutePath
	 *            Wert fuer Pfad
	 */
	public void setAbsolutePath(String absolutePath) {
		AbsolutePath = absolutePath;
	}

}
