import java.util.*;

public class DataFolderList implements Comparable<DataFolderList> {
	private int SortNr;
	private String Name;
	private String AbsolutePath;
	private ArrayList<DataFolderList> Children;

	public DataFolderList(int SortNr, String Name, String AbsolutePath, ArrayList<DataFolderList> Children) {
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

	public ArrayList<DataFolderList> getChildren() {
		return this.Children;
	}

	/*
	 * @Override public int compareTo(DataFolderList o1, DataFolderList o2) {
	 * Integer a = ((DataFolderList) o1).getSortNr(); Integer b =
	 * ((DataFolderList) o2).getSortNr();
	 * 
	 * return a.compareTo(b); }
	 * 
	 */

	@Override
	public int compareTo(DataFolderList o) {
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
