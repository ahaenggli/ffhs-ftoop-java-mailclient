import java.util.Date;

public class DataMailList {
	private Date Datum;
	private String Absender;
	private String Betreff;
	private String Nachricht;
	private String ID;
	
	public DataMailList(Date datum, String sender, String betreff, String nachricht, String ID){
		this.Datum = datum;
		this.Absender = sender;
		this.Betreff = betreff;
		this.Nachricht = nachricht;
		this.ID = ID;
	}
	public Date getDatum() {
		return Datum;
	}
	public void setDatum(Date datum) {
		Datum = datum;
	}
	public String getAbsender() {
		return Absender;
	}
	public void setAbsender(String sender) {
		Absender = sender;
	}
	public String getBetreff() {
		return Betreff;
	}
	public void setBetreff(String betreff) {
		Betreff = betreff;
	}
	public String getNachricht() {
		return Nachricht;
	}
	public void setNachricht(String nachricht) {
		Nachricht = nachricht;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	
	
}
