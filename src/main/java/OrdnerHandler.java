import java.util.ArrayList;
import java.util.Collections;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Handlerklasse für alle Ordner-Operationen - Liste aller Ordner - aktuell
 * gewählter Ordner - Ordner-Wechseln
 * 
 * @author ahaen
 *
 */
public class OrdnerHandler {

	// Liste aller Ordner
	private ArrayList<OrdnerStruktur> FolderList = new ArrayList<OrdnerStruktur>();

	// Default = Posteingang
	private String gewaehlterMailOrdner = "[" + Configuration.getNameRootFolder() + ", "
			+ Configuration.getNamePosteingang() + "]";
	private Preferences aktFolder = null;

	/**
	 * Konstruktor
	 */
	public OrdnerHandler() {

	}

	/**
	 * Gibt den aktuell gewählten Ordner zurück
	 * 
	 * @return Selektierter Ordner
	 */
	public Preferences getAktFolder() {
		return aktFolder;
	}

	/**
	 * Ermittelt alle Ordner (interne Methode)
	 * 
	 * @param parent
	 *            Root-Order um alle Unterordner zu ermitteln Bei "null" wird
	 *            komplette Struktur zurückgegeben
	 * @return Liste aller Ordner
	 */
	private ArrayList<OrdnerStruktur> makeFolderList(Preferences parentX) {
		Preferences SucheIn = null;

		// Ist kein Start-Ordner definiert, nimm Hauptordner mit allem drin
		if (parentX == null) {
			SucheIn = Configuration.getFolders();
		} else
			SucheIn = parentX;

		if (Configuration.isDebug())
			System.out.println("--> Absoluter Pfad: " + SucheIn.absolutePath());

		ArrayList<OrdnerStruktur> tmpList = new ArrayList<OrdnerStruktur>();

		try {
			for (String childFolder : SucheIn.childrenNames()) {
				Preferences child = Configuration.getOrdner(childFolder, SucheIn); // SucheIn.node(childFolder);
				String p = child.absolutePath().replace(Configuration.getPrefs().absolutePath() + "/", "");

				p = "[" + p.replace("/", ", ") + "]";

				if (Configuration.isDebug()) {
					System.out.println("--> Vergleichspfad: " + p);
					System.out.println("--> Suchenderpfad : " + gewaehlterMailOrdner);
				}

				// startet nicht mit "msg_", somit Ordner und weitermachen
				if (!child.name().startsWith("msg_")) {

					// ist unser gesuchter Ordner, daher aktFolder setzen
					if (gewaehlterMailOrdner.equals(p)) {

						aktFolder = child;

						if (Configuration.isDebug())
							System.out.println("---> Ordner gefunden" + aktFolder);

					}

					// auf jeden Fall gefundenen Ordner der Liste anfügen
					int SortNr = SucheIn.node(childFolder).getInt("SortNr", 0);
					tmpList.add(new OrdnerStruktur(SortNr, childFolder, child.absolutePath(), makeFolderList(child)));
				} else {

					// ist Mail, daher keine weiteren Schritte hier
				}

			}
		} catch (BackingStoreException e) {
			if (Configuration.isDebug())
				e.printStackTrace();
		}

		// ArrayList.sort(tmpList);

		// tmpList.sort(new Comparable<OrdnerStruktur>);

		// List<Contact> contacts = new ArrayList<Contact>();
		// Fill it.

		Collections.sort(tmpList);

		if (gewaehlterMailOrdner.equals("[" + Configuration.getFolders().name() + "]"))
			aktFolder = Configuration.getFolders();

		return tmpList;
	}

	/**
	 * Gibt die Ordner-Liste zurück für andere Methoden/KLassen
	 * 
	 * @return Ordner-Liste
	 */
	public ArrayList<OrdnerStruktur> getFolderList() {
		FolderList = makeFolderList(null);
		return FolderList;

	}

	/**
	 * Ändert den gewählten Ordner
	 * 
	 * @param gewaehlterMailOrdner
	 *            Name des neuen Ordners (Pfad in Baumstruktur)
	 */
	public void setGewaehlterMailOrdner(String gewaehlterMailOrdner) {

		if (gewaehlterMailOrdner == null)
			this.gewaehlterMailOrdner = "[" + Configuration.getNameRootFolder() + ", "
					+ Configuration.getNamePosteingang() + "]";
		else
			this.gewaehlterMailOrdner = gewaehlterMailOrdner;

		if (Configuration.isDebug())
			System.out.println("-> Öffne Ordner: " + this.gewaehlterMailOrdner);

		FolderList = makeFolderList(null);
	}

}
