import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Klasse mit allen wichtigen Einstellungen zum MailClient Da ueberall immer
 * dieselben Werte gebruacht werden (fix) sind saemtliche public Methoden
 * "static" Fuer die Datenspeicherung wird der Default-Typ "Preferences" aus Java
 * verwendet. So ist gewaehrleistet, dass der Client Plattformuebergreifend laeuft.
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

	// Debug Params fuer Sys-Outputs
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

	// Diverses-Einstellungen
	private final static String anzahlMinuten = "autoLadenMinuten";

	/*
	 * Private Methoden fuer internen Klassen-internen gebrauch
	 */

	/**
	 * Erstellt einen neuen Ordner fuer Einstellungen (oder Mails), falls noch
	 * nicht existent.
	 * 
	 * @param Name
	 *            Name des neuen Ordners
	 * @param SortNr
	 *            LaufNr des Ordners fuer Sortierung
	 * @param parent
	 *            Parent-Ordner
	 * @return Erfolg (=Ordner gibt es (nun)) true|false
	 */

	public static boolean createFolder(String Name, int SortNr, Preferences parent) {
		boolean returnWert = false;
		try {
			if (!parent.nodeExists(Name)) {
				parent.node(Name).putInt("SortNr", SortNr);
			}
			returnWert = true;
		} catch (BackingStoreException e) {
			returnWert = false;
		}
		return returnWert;
	}

	/**
	 * Gibt den gesuchten Wert zurueck, falls vorhanden. Existiert der Wert
	 * nicht, kommt leer zurueck.
	 * 
	 * @param Name
	 *            Name des gesuchten Wertes
	 * @return String Wert von Name
	 */
	private final static String getValue(String Name) {
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
		}

		return prefs.get(Name, "");
	}

	/**
	 * Setzt einen Wert zu einem Namen (nur fuer internen Gebrauch)
	 * 
	 * @param Name
	 *            Name des zu speichernen Wertes
	 * @param Value
	 *            Wert zum Speichern
	 * @return true|false: Gibt true bei Erfolg zurueck, sonst false
	 */
	private final static boolean setValue(String Name, String Value) {
		boolean result = false;
		try {

			prefs.sync();
			prefs.put(Name, Value);
			prefs.sync();
			result = true;
		} catch (BackingStoreException e) {

			result = false;
		}
		return result;
	}

	/*
	 * Public (static) Methoden fuer den Zugriff auf die Daten Alle anderen
	 * Classen und Methoden greifen nur auf diese zu Wenn hier etwas geaendert
	 * wird, gilt es somit fuers ganze MailClient-Projekt
	 * 
	 */

	/**
	 * Loescht alle Daten
	 */
	public final static void deleteConfig() {
		try {
			prefs.removeNode();
			prefs.flush();
			prefs = Preferences.userRoot().node("/ch/ahaenggli/MailClient");
		} catch (BackingStoreException e) {

		}

	}

	/**
	 * Gibt den MailClientName zurueck
	 * 
	 * @return MailClientName
	 */
	public final static String getMailClientName() {
		return MailClientName;
	}

	/**
	 * Gibt zurueck ob Debug Aufruf oder nicht (Div. System.out.* werden dadurch
	 * aktiviert/deaktiviert)
	 * 
	 * @return true|false
	 */
	public final static boolean getDebug() {
		return Debug;
	}

	/**
	 * Gibt das Root-Verzeichnis der Einstellungen zurueck
	 * 
	 * @return Preferences mit Einstellungs-Root
	 */
	public final static Preferences getPrefs() {
		return prefs;
	}

	/**
	 * Gibt das Mail-Ordner-Verzeichnis zurueck (hier liegen die Mails drin)
	 * 
	 * @return Preferences mit Mailordner
	 */
	public final static Preferences getFolders() {

		// Sicherstellen, dass Ein- und Ausgang existieren
		try {
			getEingang();
			getGesendet();
		} catch (BackingStoreException e) {

		}

		return folders;
	}

	/**
	 * Gibt den Namen des Postausgangs zurueck
	 * 
	 * @return Name von Postausgang
	 */
	public final static String getNamePostausgang() {
		return NamePostausgang;
	}

	/**
	 * Gibt den Namen des Posteingangs zurueck
	 * 
	 * @return Name von Posteingang
	 */
	public final static String getNamePosteingang() {
		return NamePosteingang;
	}

	/**
	 * Gibt den Namen des NameRootFolders zurueck (in dem alle anderen Ordner
	 * wie Posteingang/ausgang liegen)
	 * 
	 * @return Name von NameRootFolder
	 */
	public final static String getNameRootFolder() {
		return NameRootFolder;
	}

	/*
	 * POP 3 Einstellungen lesen/schreiben fuer andere Klassen/Methoden
	 */

	/**
	 * Gibt POP3 Server-Name zurueck
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
	 *            Neuer Wert fuer POP3Server
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setPop3Server(String newValue) {
		return setValue(POP3Server, newValue);
	}

	/**
	 * Gibt POP3User zurueck
	 * @return
	 * Text
	 */
	public final static String getPop3User() {
		return getValue(POP3User);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fuer den POP3User
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setPop3User(String newValue) {
		return setValue(POP3User, newValue);
	}

	/**
	 * Gibt POP3PW zurück
	 * @return
	 * text
	 */
	public final static String getPop3PW() {
		return getValue(POP3PW);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fuer POP3PW
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setPop3PW(String newValue) {
		return setValue(POP3PW, newValue);
	}

	/**
	 * Gibt POP3Port zurück
	 * @return
	 * Text
	 */
	public final static String getPop3Port() {
		return getValue(POP3Port);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fuer POP3Port
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setPop3Port(String newValue) {
		return setValue(POP3Port, newValue);
	}

	/*
	 * STMP Einstellungen lesen/schreiben fuer andere Klassen/Methoden
	 */
	/**
	 * Gibt smtpServer zurück
	 * @return
	 * Text
	 */
	public final static String getsmtpServer() {
		return getValue(smtpServer);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fuer smtpServer
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setsmtpServer(String newValue) {
		return setValue(smtpServer, newValue);
	}

	/**
	 * Gibt smtpUser zurück
	 * @return
	 * Text
	 */
	public final static String getsmtpUser() {
		return getValue(smtpUser);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fuer smtpUser
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setsmtpUser(String newValue) {
		return setValue(smtpUser, newValue);
	}

	/**
	 * Gibt smtpPW zurück
	 * 
	 * @return
	 * Text
	 */
	public final static String getsmtpPW() {
		return getValue(smtpPW);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fue smtpPW
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */

	public final static boolean setsmtpPW(String newValue) {
		return setValue(smtpPW, newValue);
	}

	/**
	 * Gibt smtpPort zurück
	 * @return
	 * Text
	 */
	public final static String getsmtpPort() {
		return getValue(smtpPort);
	}

	/**
	 * @param newValue
	 *            Neuer Wert fuer smtpPort
	 * @return Gibt bei Erfolg true zurueck, sonst false
	 */
	public final static boolean setsmtpPort(String newValue) {
		return setValue(smtpPort, newValue);
	}

	/**
	 * Ermittelt spezifischen Ordner und erstellt ihn falls noetig
	 * 
	 * @param x
	 *            Ordner zum suchen/erstellen
	 * @param parent
	 *            Wo soll Ordner gesucht/erstellt werden
	 * @return Gefundener Ordner
	 * @throws BackingStoreException
	 */
	public static Preferences getOrdner(String x, Preferences parent) throws BackingStoreException {

		if (parent == null)
			return folders;
		else
		{
		if (!parent.nodeExists(x))
			createFolder(x, 1, parent);

		return parent.node(x);
		}
	}

	/**
	 * Gibt Standard-Verzeichnis fuer Neue Mails
	 * 
	 * @return Preferences
	 * @throws BackingStoreException
	 */
	public static Preferences getEingang() {

		try {
			if (!folders.nodeExists(NamePosteingang))
				createFolder(NamePosteingang, 1, folders);
		} catch (BackingStoreException e) {

		}

		return folders.node(NamePosteingang);
	}

	/**
	 * Gibt sStandard-Verzeichnis fuer gesendete Mails
	 * 
	 * @return Preferences
	 * @throws BackingStoreException
	 */
	public static Preferences getGesendet() throws BackingStoreException {

		if (!folders.nodeExists(NamePostausgang))
			createFolder(NamePostausgang, 2, folders);
		return folders.node(NamePostausgang);
	}

	/**
	 * Gibt Anzahl Minuten fuer AutoRunner zurueck
	 * 
	 * @return Zahl
	 */
	public final static Long getAnzahlminuten() {
		Long min = (long) 0;

		try {
			min = Long.parseLong(getValue(anzahlMinuten));
		} catch (Exception e) {
			min = (long) 0;
		}
		return min;
	}

	/**
	 * Setze Wert fuer anzahlMinuten (AutoRunner)
	 * 
	 * @param newValue
	 *            Neuer Wert
	 * @return Gibt bei Erfolg true zuruech, sonst false
	 */
	public final static boolean setAnzahlminuten(String newValue) {
		return setValue(anzahlMinuten, newValue);
	}

	/**
	 * Ordner kopieren (fuer Umbenennung in Hauptfenster)
	 * 
	 * @param src
	 *            Von wo soll kopiert werden
	 * @param newNode
	 *            Zu war soll kopiert werden
	 * @throws BackingStoreException
	 *             Fehler
	 */
	public final static void copyNode(Preferences src, Preferences newNode) throws BackingStoreException {
		if (src.childrenNames().length > 0) {
			for (String child : src.childrenNames()) {
				copyNode(src.node(child), newNode.node(child));
			}
		}
		String[] keys = src.keys();
		for (String key : keys) {
			String value = src.get(key, "");
			newNode.put(key, value);
		}
	}

}