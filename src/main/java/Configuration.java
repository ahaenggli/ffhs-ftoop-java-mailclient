import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Klasse mit allen wichtigen Einstellungen zum MailClient Da �berall immer
 * dieselben Werte gebruacht werden (fix) sind s�mtliche public Methoden
 * "static" F�r die Datenspeicherung wird der Default-Typ "Preferences" aus Java
 * verwendet. So ist gew�hrleistet, dass der Client Plattform�bergreifend l�uft.
 * 
 * @author ahaen
 *
 */

/**
 * @author ahaen
 *
 */
public final class Configuration {
	/*
	 * Declare der private Variablen und fixen Wert (initial) setzen
	 */

	// Name des MailClients
	private static final String MailClientName = "Bananaa MailClient";
	
	// Hauptzweig (Root) vom Gesamten MailClient-Datenspeicher.
	private static Preferences prefs = Preferences.userRoot().node("/ch/ahaenggli/MailClient");

	// Hier liegen die Mailordner und Mails drin
	private static final String NameRootFolder = "Folders";
	private static Preferences folders = getPrefs().node(NameRootFolder);	
	private static final String NamePosteingang = "Neue Mails";
	private static final String NamePostausgang = "Gesendet";
	
	// Debug Params f�r Sys-Outputs
	private static boolean Debug = true;
	
	// pop3-Einstellungen (Mail senden)
	private final static String POP3Server = "POP3Server";
	private final static String POP3User = "POP3User";
	private final static String POP3PW = "POP3PW";
	private final static String POP3Port = "POP3Port";

	// smtp-Einstellungen (Mail empfang)
	private final static String smtpServer = "smtpServer";
	private final static String smtpUser = "smtpUser";
	private final static String smtpPW = "smtpPW";
	private final static String smtpPort = "smtpPort";

	/*
	 * Private Methoden f�r internen Klassen-internen gebrauch
	 */

	/**
	 * Erstellt einen neuen Ordner f�r Einstellungen (oder Mails), falls nocht
	 * nicht existent
	 * 
	 * @param Name
	 * @param SortNr
	 * @param parent
	 * @throws BackingStoreException
	 */
	public static void createFolder(String Name, int SortNr, Preferences parent) throws BackingStoreException {
		if (!parent.nodeExists(Name)) {
			parent.node(Name).putInt("SortNr", SortNr);
		}
	}

	/**
	 * Gibt den gesuchten Wert zur�ck, falls vorhanden. Existiert der Wert
	 * nicht, kommt leer zur�ck.
	 * 
	 * @param Name
	 *            Name des gesuchten Wertes
	 * @return String Wert von Name
	 */
	private final static String getValue(String Name) {
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return prefs.get(Name, "");
	}

	/**
	 * Setzt einen Wert zu einem Namen
	 * 
	 * @param Name
	 *            Name des zu speichernen Wertes
	 * @param Value
	 *            Wert zum Speichern
	 * @return true|false: Gibt true bei Erfolg zur�ck, sonst false
	 */
	private final static boolean setValue(String Name, String Value) {
		boolean result = false;
		try {

			prefs.sync();
			prefs.put(Name, Value);
			prefs.sync();
			result = true;
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/*
	 * Public (static) Methoden f�r den Zugriff auf die Daten Alle anderen
	 * Classen und Methoden greifen nur auf diese zu Wenn hier etwas ge�ndert
	 * wird, gilt es somit f�rs ganze MailClient-Projekt
	 * 
	 */

	/**
	 * L�scht alle Daten
	 */
	public final static void deleteConfig()
	{
		try {
			prefs.removeNode();
			prefs.flush();
			 prefs = Preferences.userRoot().node("/ch/ahaenggli/MailClient");
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	/** 
	 * Gibt den MailClientName zur�ck
	 * @return MailClientName
	 */
	public final static String getMailClientName()
	{
		return MailClientName;
	}
	
	/**
	 * Gibt zur�ck ob Debug Aufruf oder nicht
	 * @return true|false
	 */
	public final static boolean getDebug(){
		return Debug;
	}
	
	/**
	 * Gibt das Root-Verzeichnis der Einstellungen zur�ck
	 * 
	 * @return Preferences mit Einstellungs-Root
	 */
	public final static Preferences getPrefs() {
		return prefs;
	}

	/**
	 * Gibt das Mail-Ordner-Verzeichnis zur�ck (hier liegen die Mails drin)
	 * 
	 * @return Preferences mit Mailordner
	 */
	public final static Preferences getFolders() {
		
		
		// Sicherstellen, dass Ein- und Ausgang existieren
		try {
			getEingang();
			getGesendet();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return folders;
	}

	
	/**
	 * Gibt den Namen des Postausgangs zur�ck
	 * @return Name von Postausgang
	 */
	public final static String getNamePostausgang(){
		return  NamePostausgang;
	}
	
	/**
	 * Gibt den Namen des Posteingang zur�ck
	 * @return Name von Posteingang
	 */
	public final static String getNamePosteingang(){
		return NamePosteingang;
	}
	
	/**
	 * Gibt den Namen des NameRootFolder zur�ck
	 * @return Name von NameRootFolder
	 */
	public final static String getNameRootFolder(){
		return NameRootFolder;
	}
	
	/*
	 * POP 3 Einstellungen lesen/schreiben f�r andere Klassen/Methoden
	 */

	/**
	 * Gibt POP3 Server-Name zur�ck
	 * 
	 * @return String mit POP3 Server Name
	 */

	public final static String getPop3Server() {
		return getValue(POP3Server);
	}

	/**
	 * Setzt den POP3 Server-Name
	 * 
	 * @param newValue
	 *            Neuer Wert f�r den POP3 Server Name
	 * @return Gibt bei Erfolg true zur�ck, sonst false
	 */
	public final static boolean setPop3Server(String newValue) {
		return setValue(POP3Server, newValue);
	}

	/**
	 * @return
	 */
	public final static String getPop3User() {
		return getValue(POP3User);
	}

	/**
	 * @param newValue
	 * @return
	 */
	public final static boolean setPop3User(String newValue) {
		return setValue(POP3User, newValue);
	}

	/**
	 * @return
	 */
	public final static String getPop3PW() {
		return getValue(POP3PW);
	}

	/**
	 * @param newValue
	 * @return
	 */
	public final static boolean setPop3PW(String newValue) {
		return setValue(POP3PW, newValue);
	}

	/**
	 * @return
	 */
	public final static String getPop3Port() {
		return getValue(POP3Port);
	}

	/**
	 * @param newValue
	 * @return
	 */
	public final static boolean setPop3Port(String newValue) {
		return setValue(POP3Port, newValue);
	}

	/*
	 * STMP Einstellungen lesen/schreiben f�r andere Klassen/Methoden
	 */
	/**
	 * @return
	 */
	public final static String getsmtpServer() {
		return getValue(smtpServer);
	}

	/**
	 * @param newValue
	 * @return
	 */
	public final static boolean setsmtpServer(String newValue) {
		return setValue(smtpServer, newValue);
	}

	/**
	 * @return
	 */
	public final static String getsmtpUser() {
		return getValue(smtpUser);
	}

	/**
	 * @param newValue
	 * @return
	 */
	public final static boolean setsmtpUser(String newValue) {
		return setValue(smtpUser, newValue);
	}

	/**
	 * @return
	 */
	public final static String getsmtpPW() {
		return getValue(smtpPW);
	}

	/**
	 * @param newValue
	 * @return
	 */

	public final static boolean setsmtpPW(String newValue) {
		return setValue(smtpPW, newValue);
	}

	/**
	 * @return
	 */
	public final static String getsmtpPort() {
		return getValue(smtpPort);
	}

	/**
	 * @param newValue
	 * @return
	 */
	public final static boolean setsmtpPort(String newValue) {
		return setValue(smtpPort, newValue);
	}

	
	public static Preferences getOrdner(String x, Preferences parent) throws BackingStoreException{
		
		if(parent == null) parent = folders;
		
		if (!parent.nodeExists(x))
			createFolder(x, 1, parent);

		
		return parent.node(x);
	}
	
	/**
	 * Gibt Standard-Verzeichnis f�r Neue Mails
	 * 
	 * @return Preferences
	 * @throws BackingStoreException 
	 */
	public static Preferences getEingang() {
		
		try {
			if (!folders.nodeExists(NamePosteingang))
				createFolder(NamePosteingang, 1, folders);
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return folders.node(NamePosteingang);
	}

	/**
	 * Gibt sStandard-Verzeichnis f�r gesendete Mails
	 * 
	 * @return Preferences
	 * @throws BackingStoreException 
	 */
	public static Preferences getGesendet() throws BackingStoreException {
		
		if (!folders.nodeExists(NamePostausgang))
			createFolder(NamePostausgang, 2, folders);
		return folders.node(NamePostausgang);
	}

}