import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * 
 * Datenstrukur für eine E-Mail mit allen Feldern
 * 
 * @author ahaen
 *
 */
public class MailStruktur {

	private Date Datum;
	private String Absender;
	private String Empfaenger;
	private String Betreff;
	private String Nachricht;
	private String ID;
	private Preferences Herkunft;
	private String CC;
	private String BCC;

	/**
	 * Generiert eine neue ID (um Mails abzulegen)
	 * 
	 * @return neue Zufallszahl fuer ID
	 */
	public static String newID() {
		return new BigInteger(130, new SecureRandom()).toString(20);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Betreff: " + Betreff + "; " + "Absender: " + Absender + "; " + "Empfaenger: " + Empfaenger + "; "
				+ "Nachricht: " + Nachricht + "; " + "CC: " + CC + "; " + "BCC: " + BCC + "; " +

				"";

	}

	/**
	 * Konstruktor fuer neues EMail
	 * 
	 * @param datum
	 *            Datum von Empfang/Absenden der Mail
	 * @param Absender
	 *            Absender des E-Mails
	 * @param betreff
	 *            Betreff der Nachricht
	 * @param nachricht
	 *            Nachrichtentext
	 * @param ID
	 *            ID der Nachricht in unserer Ablage
	 * @param hk
	 *            Ordner, wo unser Mail abgelegt ist
	 */
	public MailStruktur(Date datum, String Absender, String betreff, String nachricht, String ID, Preferences hk) {
		this.Datum = datum;
		this.Absender = Absender;
		this.Betreff = betreff;
		this.Nachricht = nachricht;
		this.ID = ID;
		this.Herkunft = hk;
		// this.setDoSend(doSend);
	}

	public MailStruktur() {

	}

	/**
	 * Datum ermitteln von Empfang oder versenden
	 * 
	 * @return Datum Absenden/Empfang
	 */
	public Date getDatum() {
		return Datum;
	}

	/**
	 * Ermittelt den Absender
	 * 
	 * @return Text mit Absender drin
	 */
	public String getAbsender() {
		if (Absender == null)
			Absender = "";
		return Absender;
	}

	/**
	 * Ermittelt den Betreff
	 * 
	 * @return Text mit Mailbetreff
	 */
	public String getBetreff() {
		if (Betreff == null)
			Betreff = "";
		return Betreff;
	}

	/**
	 * Ermittelt den Mailtext
	 * 
	 * @return Text mit Inhalt der Nachricht
	 */
	public String getNachricht() {
		if (Nachricht == null)
			Nachricht = "";
		return Nachricht;
	}

	/**
	 * Gibt die ID in der Ablage/Ordner
	 * 
	 * @return Text mit ID der Nachricht in DB/Ablage
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Ermittelt den Herkunftsordner
	 * 
	 * @return Preferences mit Ordner (z.B. Posteingang/Postausgang)
	 */
	public Preferences getHerkunft() {
		return Herkunft;
	}

	/**
	 * Setze das CC
	 * 
	 * @param cc
	 *            Wert fuer CC-Empfaenger
	 */
	public void setCC(String cc) {
		this.CC = cc;
	}

	/**
	 * Gibt das CC
	 * 
	 * @return Text mit CC-Empfaenger
	 */
	public String getCC() {
		if (CC == null)
			CC = "";
		return CC;
	}

	/**
	 * Gibt das BCC
	 * 
	 * @return Text mit BCC-Empfaenger
	 */
	public String getBCC() {
		if (BCC == null)
			BCC = "";
		return BCC;
	}

	/**
	 * Setze das BCC
	 * 
	 * @param bCC
	 *            Wert fuer BCC-Empfaenger
	 */
	public void setBCC(String bCC) {
		BCC = bCC;
	}

	/**
	 * Gibt den Empaenger
	 * 
	 * @return Text mit Mailempfaenger
	 */
	public String getEmpfaenger() {
		return Empfaenger;
	}

	/**
	 * Setze den Empfaenger
	 * 
	 * @param empfaenger
	 *            Wert fuer Mailempfaenger
	 */
	public void setEmpfaenger(String empfaenger) {
		Empfaenger = empfaenger;
	}

}
