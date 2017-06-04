import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Handlerklasse für alle Mail-Operateionen
 * - Liste aller Mails in Ordner
 * - Mail zu Ordner hinzu
 * - Mail von Ordner weg
 * @author ahaen
 *
 */
public class MailHandler {

    private Preferences OffenerOrnder = null;
	
	private ArrayList<MailStruktur> MailList = new ArrayList<MailStruktur>();

	
	
	/**
	 * Löscht ein Mail in einem Ordner
	 * @param mail
	 * 		Mail welches zu löschen ist
	 * @return
	 * 		Erfolg true|false
	 */
	public boolean removeMail(Preferences mail){
		boolean result = false;
		
		if(mail != null){
		try {
			mail.clear();
			mail.removeNode();
			OffenerOrnder.flush();
			OffenerOrnder.sync();
			result = true;
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		}
		return result;
	}
	
	
	/**
	 * Füg x Mails einem Ordner hinzu
	 * Wrapper für Einzelmail hinzu
	 * @param mails
	 * Liste der Mails die einem Ordner hinzugefügt werden sollen
	 * @param folder
	 * Ordner zum Mail hinzufügen
	 * @return
	 * Erfolg true|false
	 */
	public static boolean addMailToFolder(ArrayList<MailStruktur> mails, Preferences folder){
		boolean result = false;
		
		for(MailStruktur mail : mails){
			result = addMailToFolder(mail, folder);
		}
		
		return result;
	}
	/**
	 * Fügt einem Ordner ein Mail hinzu (Einzeln)
	 * @param mail
	 * Mail zum hinzufügen
	 * @param folder
	 * Order wo Mail rein kommt
	 * @return
	 * Erfolg wahr|falsch
	 */
	public static boolean addMailToFolder(MailStruktur mail, Preferences folder){
		boolean result = false;
			
		
		System.out.println("Mail try add:");
		
		
		
		try {
			String msg_name = "";
			
			if(mail.getID() == null) msg_name =  mail.newID();
			else msg_name = mail.getID();
			
			if(!msg_name.startsWith("msg_")) msg_name = "msg_" + msg_name;
		
			Configuration.createFolder(msg_name, 0, folder);
			
			
			
			Preferences newNachricht = folder.node(msg_name);
			newNachricht.put("Absender", mail.getAbsender());
			newNachricht.put("Betreff", mail.getBetreff());
			newNachricht.putLong("Datum", mail.getDatum().getTime());
			newNachricht.put("Nachricht", mail.getNachricht());
			System.out.println("hier);");
			newNachricht.put("Empfaenger", mail.getEmpfaenger());
			newNachricht.put("CC", mail.getCC());
			newNachricht.put("BCC", mail.getBCC());
			
			System.out.println(mail);
			
		//	newNachricht.putBoolean("doSend", mail.isDoSend());
			
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
	

	/**
	 * Liest alle Mails neu aus in Ordner
	 * 
	 * @param quelle
	 * Ordner zum Mails laden
	 */
	public void resetData(Preferences quelle) {
		
		MailList = new ArrayList<MailStruktur>();
		OffenerOrnder = quelle;
		
		try {
			OffenerOrnder.sync();
			
			
			for (String childFolder : OffenerOrnder.childrenNames()) {
				Preferences child = Configuration.getOrdner(childFolder, OffenerOrnder); //parent.node(childFolder);
				
				// startet mit "msg_" ist also Nachricht :)
				if (child.name().startsWith("msg_")) {
					addMail(child);
				}
			}
			
			
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * Konstruktor
	 * Default mit Posteingang gem. Configuration.getEingang()
	 */
	public MailHandler() {
		resetData(Configuration.getEingang());
	
	}

	/**
	 * Konstruktor mit spezifischem Ordner
	 * @param Ordner
	 * Ordner wo Mails gelesen werden sollen
	 */
	public MailHandler(Preferences Ordner) {
	
		resetData(Ordner);

	}
	
	/**
	 * Füge Mail der internen Mailliste hinzu
	 * 
	 * @param mail
	 * Mail zum in der LIste ergänzen
	 */
	private void addMail(Preferences mail) {
		Date Datum = new Date();
		String Absender;
		String Betreff;
		String Nachricht;

		//System.out.println(mail.absolutePath());

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
		MailStruktur ms = new MailStruktur(Datum, Absender, Betreff, Nachricht, mail.name(), mail);
		ms.setBCC(mail.get("BCC", null));
		ms.setEmpfaenger(mail.get("Empfaenger", null));
		ms.setCC(mail.get("CC", null));
		
		MailList.add(ms);
		// System.out.println("added Mail");
	}

	


	/**
	 * Gibt alle Mails im Order zurück
	 * @return
	 * Liste aller Mails
	 */
	public ArrayList<MailStruktur> getMailList() {
		return MailList;
	}



}
