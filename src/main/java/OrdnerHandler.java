import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *Handlerklasse für alle Ordner-Operationen
 * - Liste aller Ordner
 * - aktuelle gewählter Ordner
 * - Ordner-Wechseln
 * @author ahaen
 *
 */
public class OrdnerHandler {

	// Liste aller Ordner
	private ArrayList<OrdnerStruktur> FolderList = new ArrayList<OrdnerStruktur>();
	
	// Default = Posteingang
	private String gewaehlterMailOrdner = "[" + Configuration.getNameRootFolder() +", "+Configuration.getNamePosteingang() +"]";
	private Preferences aktFolder = null;
	
	
	/**
	 * Konstruktor
	 */
	public void OrdnerHandler(){
		//this.gewaehlterMailOrdner = 	
		
	}
	
	
	/**
	 * Gibt den aktuell gewählten Ordner zurück
	 * @return
	 * Selektierter Ordner
	 */
	public Preferences getAktFolder(){
		return aktFolder;
	}
	
	/**
	 * Ermittelt alle Ordner (interne Methode)
	 * @param parent
	 * Root-Order um alle Unterordner zu ermitteln
	 * Bei "null" wird komplette Struktur zurückgegeben
	 * @return
	 * Liste aller Ordner
	 */
	private ArrayList<OrdnerStruktur> makeFolderList(Preferences parent)  {

		// Ist kein Start-Ordner definiert, nimm Hauptordner mit allem drin
		if(parent == null){ 
			parent = Configuration.getFolders();
			System.out.println("-> Absoluter Pfad: " + parent.absolutePath());
						
		} else {
			System.out.println("--> Absoluter Pfad: " + parent.absolutePath());			
		}
		
		ArrayList<OrdnerStruktur> tmpList = new ArrayList<OrdnerStruktur>();
		try {
			for (String childFolder : parent.childrenNames()) {
				Preferences child = Configuration.getOrdner(childFolder, parent); //parent.node(childFolder);
				String p = child.absolutePath().replace(Configuration.getPrefs().absolutePath() + "/", "");
				
				
				p = "["+p.replace("/", ", ") + "]";

				System.out.println("--> Vergleichspfad: " + p);
				System.out.println("--> Suchenderpfad : " +gewaehlterMailOrdner);
							
				// startet nicht mit "msg_", somit Ordner und weitermachen				
				if (!child.name().startsWith("msg_")) {
					
					// ist unser gesuchter Ordner, daher aktFolder setzen
					if (gewaehlterMailOrdner.equals(p)) {						
						System.out.println("---> Ordner gefunden");
						aktFolder = child;
					}
								
					// auf jeden Fall gefundenen Ordner der Liste anfügen
					int SortNr = parent.node(childFolder).getInt("SortNr", 0);
					tmpList.add(new OrdnerStruktur(SortNr, childFolder, child.absolutePath(), makeFolderList(child)));
				} else {
					
					// ist Mail, daher keine weiteren Schritte hier
				}
				
				System.out.println("");
				
				
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tmpList.sort(null);

		/*
		 * for(DataFolderList myList: tmpList){
		 * 
		 * System.out.print(myList.getSortNr());
		 * System.out.print(myList.getName()); System.out.println(""); }
		 */

		if(gewaehlterMailOrdner.equals("["+Configuration.getFolders().name()+"]")) aktFolder = Configuration.getFolders();
		
		return tmpList;
	}

	
	/**
	 * Gibt die Ordner-Liste zurück für andere Methoden/KLassen
	 * @return
	 * Ordner-Liste
	 */
	public ArrayList<OrdnerStruktur> getFolderList() {
		FolderList = makeFolderList(null);
		return FolderList;

	}
	
	/**
	 * Ändert den gewählten Ordner
	 * @param gewaehlterMailOrdner
	 * Name des neuen Ordners (Pfad in Baumstruktur)
	 */
	public void setGewaehlterMailOrdner(String gewaehlterMailOrdner) {
		
		if(gewaehlterMailOrdner == null) gewaehlterMailOrdner = "[" + Configuration.getNameRootFolder() +", "+Configuration.getNamePosteingang() +"]";
		
		
		this.gewaehlterMailOrdner = gewaehlterMailOrdner;
		System.out.println("-> Öffne Ordner: " + this.gewaehlterMailOrdner );	
		FolderList = makeFolderList(null);
	}
	
	
	
}
