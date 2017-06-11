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
	 * @return ID-Value
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
	 * Konstruktor
	 * 
	 * @param datum
	 * @param Absender
	 * @param betreff
	 * @param nachricht
	 * @param ID
	 * @param hk
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
	 * Gib das Absendedatum
	 * 
	 * @return
	 */
	public Date getDatum() {
		return Datum;
	}

	/**
	 * Gib den Absender
	 * 
	 * @return
	 */
	public String getAbsender() {
		if (Absender == null)
			Absender = "";
		return Absender;
	}

	/**
	 * Gib den Betreff
	 * 
	 * @return
	 */
	public String getBetreff() {
		if (Betreff == null)
			Betreff = "";
		return Betreff;
	}

	/**
	 * Gib den Mailtext
	 * 
	 * @return
	 */
	public String getNachricht() {
		if (Nachricht == null)
			Nachricht = "";
		return Nachricht;
	}

	/**
	 * Gib die ID der Ablage
	 * 
	 * @return
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Gib den Herkunftsordner
	 * 
	 * @return
	 */
	public Preferences getHerkunft() {
		return Herkunft;
	}

	/**
	 * Setze das CC
	 * 
	 * @param cc
	 */
	public void setCC(String cc) {
		this.CC = cc;
	}

	/**
	 * Gib das CC
	 * 
	 * @return
	 */
	public String getCC() {
		if (CC == null)
			CC = "";
		return CC;
	}

	/**
	 * Gib das BCC
	 * 
	 * @return
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
	 */
	public void setBCC(String bCC) {
		BCC = bCC;
	}

	/**
	 * Gib den Empaenger
	 * 
	 * @return
	 */
	public String getEmpfaenger() {
		return Empfaenger;
	}

	/**
	 * Setze den Empfaenger
	 * 
	 * @param empfaenger
	 */
	public void setEmpfaenger(String empfaenger) {
		Empfaenger = empfaenger;
	}

}
