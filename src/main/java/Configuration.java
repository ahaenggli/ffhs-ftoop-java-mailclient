import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Klasse mit allen wichtigen Einstellungen zum MailClient Da überall immer
 * dieselben Werte gebruacht werden (fix) sind sämtliche public Methoden
 * "static" Für die Datenspeicherung wird der Default-Typ "Preferences" aus Java
 * verwendet. So ist gewährleistet, dass der Client Plattformübergreifend läuft.
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

	// Hauptzweig (Root) vom Gesamten MailClient-Datenspeicher.
	private final static Preferences prefs = Preferences.userRoot().node("/ch/ahaenggli/MailClient");

	// Hier liegen die Mailordner und Mails drin
	private final static Preferences folders = getPrefs().node("Folders");

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
	 * Private Methoden für internen Klassen-internen gebrauch
	 */

	/**
	 * Erstellt einen neuen Ordner für Einstellungen (oder Mails), falls nocht
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
	 * Gibt den gesuchten Wert zurück, falls vorhanden. Existiert der Wert
	 * nicht, kommt leer zurück.
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
	 * @return true|false: Gibt true bei Erfolg zurück, sonst false
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
	 * Public (static) Methoden für den Zugriff auf die Daten Alle anderen
	 * Classen und Methoden greifen nur auf diese zu Wenn hier etwas geändert
	 * wird, gilt es somit fürs ganze MailClient-Projekt
	 * 
	 */

	/**
	 * Gibt das Root-Verzeichnis der Einstellungen zurück
	 * 
	 * @return Preferences mit Einstellungs-Root
	 */
	public final static Preferences getPrefs() {
		return prefs;
	}

	/**
	 * Gibt das Mail-Ordner-Verzeichnis zurück (hier liegen die Mails drin)
	 * 
	 * @return Preferences mit Mailordner
	 */
	public final static Preferences getFolders() {
		return folders;
	}

	/*
	 * POP 3 Einstellungen lesen/schreiben für andere Klassen/Methoden
	 */

	/**
	 * Gibt POP3 Server-Name zurück
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
	 *            Neuer Wert für den POP3 Server Name
	 * @return Gibt bei Erfolg true zurück, sonst false
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
	 * STMP Einstellungen lesen/schreiben für andere Klassen/Methoden
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

	/**
	 * Gibt Standard-Verzeichnis für Neue Mails
	 * 
	 * @return Preferences
	 * @throws BackingStoreException 
	 */
	public static Preferences getEingang() throws BackingStoreException {
		
		if (!folders.nodeExists("Neue Mails"))
			createFolder("Neue Mails", 1, folders);

		
		return folders.node("Neue Mails");
	}

	/**
	 * Gibt sStandard-Verzeichnis für gesendete Mails
	 * 
	 * @return Preferences
	 * @throws BackingStoreException 
	 */
	public static Preferences getGesendet() throws BackingStoreException {
		
		if (!folders.nodeExists("Gesendet"))
			createFolder("Gesendet", 2, folders);
		return folders.node("Gesendet");
	}

}