import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DataHandler {

	private final static Preferences folders = aMailClient__Settings.getPrefs().node("Folders");
	private Preferences aktFolder = null;
	
	private ArrayList<DataMailFolderStruktur> FolderList = new ArrayList<DataMailFolderStruktur>();
	private ArrayList<DataMailStrukur> MailList = new ArrayList<DataMailStrukur>();

	private String gewaehlterMailOrdner = "x";

	public Preferences getAktFolder(){
		return aktFolder;
	}
	public static boolean removeMail(Preferences mail){
		boolean result = false;
		
		if(mail != null){
		try {
			mail.clear();
			mail.removeNode();
			folders.flush();
			folders.sync();
			result = true;
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		}
		return result;
	}
	public static boolean addMailToFolder(DataMailStrukur mail, Preferences folder){
		boolean result = false;
			
		try {
			String msg_name = "";
			
			if(mail.getID() == null) msg_name =  mail.newID();
			else msg_name = mail.getID();
			
			if(!msg_name.startsWith("msg_")) msg_name = "msg_" + msg_name;
		
			createFolder(msg_name, 0, folder);

			Preferences newNachricht = folder.node(msg_name);
			newNachricht.put("Absender", mail.getAbsender());
			newNachricht.put("Betreff", mail.getBetreff());
			newNachricht.putLong("Datum", mail.getDatum().getTime());
			newNachricht.put("Nachricht", mail.getNachricht());

			
			result = true;
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		finally{
		return result;
		}
	}
	
	public static Preferences getEingang(){
		return folders.node("Posteingang");
	}
	public static Preferences getGesendet(){
		return folders.node("Gesendet");
	}
	
	private static void createFolder(String Name, int SortNr, Preferences parent) throws BackingStoreException {
		if (!parent.nodeExists(Name)) {
			parent.node(Name).putInt("SortNr", SortNr);
		}
	}

	public void resetData() {
		FolderList = new ArrayList<DataMailFolderStruktur>();
		MailList = new ArrayList<DataMailStrukur>();
		gewaehlterMailOrdner = "x";
		try {
			folders.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataHandler() {
		resetData();
		try {
			if (!folders.nodeExists("Posteingang"))
				createFolder("Posteingang", 1, folders);
			if (!folders.nodeExists("Gesendet"))
				createFolder("Gesendet", 2, folders);
			FolderList = getDataFolders(folders);
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addMail(Preferences mail) {
		Date Datum = new Date();
		String Absender;
		String Betreff;
		String Nachricht;

		System.out.println(mail.absolutePath());

		//DateFormat format = new SimpleDateFormat("dd.MM.yyyy H:mm", Locale.GERMAN);
		try {
			Datum.setTime(mail.getLong("Datum", 0));
			// System.out.println(Datum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Absender = mail.get("Absender", "<unbekannt>");
		Betreff = mail.get("Betreff", "<leer>");
		Nachricht = mail.get("Nachricht", "<nichts>");

		MailList.add(new DataMailStrukur(Datum, Absender, Betreff, Nachricht, mail.name(), mail));
		// System.out.println("added Mail");
	}

	public ArrayList<DataMailFolderStruktur> getDataFolders(Preferences parent) throws BackingStoreException {

		ArrayList<DataMailFolderStruktur> tmpList = new ArrayList<DataMailFolderStruktur>();

		for (String childFolder : parent.childrenNames()) {
			Preferences child = parent.node(childFolder);
			String p = child.parent().absolutePath().replace(folders.absolutePath() + "/", "");
			p = "[Ordner, " + p.replace("/", ", ") + "]";

			System.out.println(p);
			System.out.println(gewaehlterMailOrdner);
			System.out.println("");

			if (!child.name().startsWith("msg_")) {
				int SortNr = parent.node(childFolder).getInt("SortNr", 0);
				tmpList.add(new DataMailFolderStruktur(SortNr, childFolder, child.absolutePath(), getDataFolders(child)));
			} else if (gewaehlterMailOrdner.equals(p)) {
				addMail(child);
				aktFolder = child;
			}
			
			
			
			
		}

		tmpList.sort(null);

		/*
		 * for(DataFolderList myList: tmpList){
		 * 
		 * System.out.print(myList.getSortNr());
		 * System.out.print(myList.getName()); System.out.println(""); }
		 */

		return tmpList;
	}

	public ArrayList<DataMailFolderStruktur> getFolderList() {
		return FolderList;

	}

	public ArrayList<DataMailStrukur> getMailList() {
		return MailList;
	}

	public String getGewaehlterMailOrdner() {
		return gewaehlterMailOrdner;
	}

	public void setGewaehlterMailOrdner(String gewaehlterMailOrdner) {
		resetData();
		this.gewaehlterMailOrdner = gewaehlterMailOrdner;
		try {
			FolderList = getDataFolders(folders);
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ordner: " + this.gewaehlterMailOrdner + "; Anzahl: " + MailList.size());
	}

}
