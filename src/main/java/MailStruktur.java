import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.table.AbstractTableModel;

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
	private Boolean Gesendet;
	private String CC;
	private String BCC;
	//private boolean doSend = false;
	
	  /**
	   * Generiert eine neue ID (um Mails abzulegen)
	 * @return
	 * ID-Value
	 */
	public static String newID() {
	    return new BigInteger(130, new SecureRandom()).toString(20);
	  }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override 
	public String toString(){
		return "Betreff: " + Betreff +"; " +
				 "Absender: " + Absender +"; " +
				 "Empfaenger: " + Empfaenger +"; " +
				 "Nachricht: " + Nachricht +"; " +
				 "CC: " + CC +"; " +
				 "BCC: " + BCC +"; " +
				
				
	""
	;
		
	}
	  
	/**
	 * Konstruktor
	 * @param datum
	 * @param sender
	 * @param betreff
	 * @param nachricht
	 * @param ID
	 * @param hk
	 */
	public MailStruktur(Date datum, String sender, String betreff, String nachricht, String ID, Preferences hk ) {
		this.Datum = datum;
		this.Absender = sender;
		this.Betreff = betreff;
		this.Nachricht = nachricht;
		this.ID = ID;
		this.Herkunft = hk;
		//this.setDoSend(doSend);
	}

	/**
	 * @return
	 */
	public Date getDatum() {
		return Datum;
	}

	/**
	 * @return
	 */
	public String getAbsender() {
		if(Absender == null) Absender = "";
		return Absender;
	}

	/**
	 * @return
	 */
	public String getBetreff() {
		if(Betreff == null) Betreff = "";
		return Betreff;
	}

	/**
	 * @return
	 */
	public String getNachricht() {
		if(Nachricht == null) Nachricht = "";
		return Nachricht;
	}

	/**
	 * @return
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @return
	 */
	public Preferences getHerkunft(){
		return Herkunft;
	}

/**
 * @param cc
 */
public void setCC(String cc){
	this.CC = cc;
}
	/**
	 * @return
	 */
	public String getCC() {
		if(CC == null) CC = "";
		return CC;
	}


	/**
	 * @return
	 */
	public String getBCC() {
		if(BCC == null) BCC = "";
		return BCC;
	}



	/**
	 * @param bCC
	 */
	public void setBCC(String bCC) {
		BCC = bCC;
	}



	/**
	 * @return
	 */
	public String getEmpfaenger() {
		return Empfaenger;
	}



	/**
	 * @param empfaenger
	 */
	public void setEmpfaenger(String empfaenger) {
		Empfaenger = empfaenger;
	}

	
	
	
		
}
