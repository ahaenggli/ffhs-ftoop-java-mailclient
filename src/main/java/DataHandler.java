import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DataHandler {

	private final static Preferences folders = aMailClientSettings.getPrefs().node("Folders");
	private ArrayList<DataFolderList> FolderList = new ArrayList<DataFolderList>();
	private ArrayList<DataMailList> MailList = new ArrayList<DataMailList>();

	private String gewaehlterMailOrdner = "x";

	
	
	public static boolean addMailToFolder(DataMailList mail, Preferences folder){
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
			newNachricht.put("Datum", mail.getDatum().toString());
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
		FolderList = new ArrayList<DataFolderList>();
		MailList = new ArrayList<DataMailList>();
		gewaehlterMailOrdner = "x";
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

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy H:mm", Locale.GERMAN);
		try {
			Datum = format.parse(mail.get("Datum", "27.07.1993 11:50"));
			// System.out.println(Datum);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Absender = mail.get("Absender", "<unbekannt>");
		Betreff = mail.get("Betreff", "<leer>");
		Nachricht = mail.get("Nachricht", "<nichts>");

		MailList.add(new DataMailList(Datum, Absender, Betreff, Nachricht, mail.name()));
		// System.out.println("added Mail");
	}

	public ArrayList<DataFolderList> getDataFolders(Preferences parent) throws BackingStoreException {

		ArrayList<DataFolderList> tmpList = new ArrayList<DataFolderList>();

		for (String childFolder : parent.childrenNames()) {
			Preferences child = parent.node(childFolder);
			String p = child.parent().absolutePath().replace(folders.absolutePath() + "/", "");
			p = "[Ordner, " + p.replace("/", ", ") + "]";

			System.out.println(p);
			System.out.println(gewaehlterMailOrdner);
			System.out.println("");

			if (!child.name().startsWith("msg_")) {
				int SortNr = parent.node(childFolder).getInt("SortNr", 0);
				tmpList.add(new DataFolderList(SortNr, childFolder, child.absolutePath(), getDataFolders(child)));
			} else if (gewaehlterMailOrdner.equals(p)) {
				addMail(child);

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

	public ArrayList<DataFolderList> getFolderList() {
		return FolderList;

	}

	public ArrayList<DataMailList> getMailList() {
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
