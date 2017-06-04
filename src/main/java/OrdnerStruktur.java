import java.util.*;

/**
 * Datenstruktir für einen Ornder
 * Wird meistens als als List bzw. Array gebraucht
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
	 * @param SortNr
	 * @param Name
	 * @param AbsolutePath
	 * @param Children
	 */
	public OrdnerStruktur(int SortNr, String Name, String AbsolutePath, ArrayList<OrdnerStruktur> Children) {
		this.SortNr = SortNr;
		this.Name = Name;
		this.Children = Children;
		this.AbsolutePath = AbsolutePath;
	}

	public int getSortNr() {
		return this.SortNr;
	}

	public String getName() {
		return this.Name;
	}

	public ArrayList<OrdnerStruktur> getChildren() {
		return this.Children;
	}


	@Override
	public int compareTo(OrdnerStruktur o) {
		Integer a = Integer.valueOf(this.getSortNr());
		Integer b = Integer.valueOf(o.getSortNr());

		return a.compareTo(b);
	}

	public String getAbsolutePath() {
		return AbsolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		AbsolutePath = absolutePath;
	}

}
