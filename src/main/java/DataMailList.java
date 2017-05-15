import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.prefs.Preferences;

public class DataMailList {
	private Date Datum;
	private String Absender;
	private String Betreff;
	private String Nachricht;
	private String ID;
	private Preferences Herkunft;

	  public static String newID() {
	    return new BigInteger(130, new SecureRandom()).toString(20);
	  }
	
	
	  
	public DataMailList(Date datum, String sender, String betreff, String nachricht, String ID, Preferences hk) {
		this.Datum = datum;
		this.Absender = sender;
		this.Betreff = betreff;
		this.Nachricht = nachricht;
		this.ID = ID;
		this.Herkunft = hk;
	}

	public Date getDatum() {
		return Datum;
	}

	public String getAbsender() {
		return Absender;
	}

	public String getBetreff() {
		return Betreff;
	}

	public String getNachricht() {
		return Nachricht;
	}

	public String getID() {
		return ID;
	}
	public Preferences getHerkunft(){
		return Herkunft;
	}


}
