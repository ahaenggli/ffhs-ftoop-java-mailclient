import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public final class aMailClientSettings
{
	// Retrieve the user preference node for the package com.mycompany

	private final static Preferences prefs = Preferences.userRoot().node("/ch/ahaenggli/MailClient");
    public final static Preferences getPrefs() {return prefs;}
	// pop3
	private final static String POP3Server = "POP3Server";
	private final static String POP3User = "POP3User";
	private final static String POP3PW = "POP3PW";
	private final static String POP3Port = "POP3Port";

	//smtp
	private final static String smtpServer = "smtpServer";
	private final static String smtpUser = "smtpUser";
	private final static String smtpPW = "smtpPW";
	private final static String smtpPort = "smtpPort";
	
	//private final String POP3Benuter = "POP3Benuter";
	//private final String POP3Passwort = "POP3Passwort";
	
	public final static String getValue(String Name) {
		

		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  prefs.get(Name, ""); 
	}
	public final static boolean setValue(String Name, String Value){
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
	
	// pop3
	public final static String  getPop3Server(){return getValue(POP3Server);}
	public final static boolean setPop3Server(String newValue){return setValue(POP3Server, newValue);}	
	public final static String  getPop3User(){return getValue(POP3User);}
	public final static boolean setPop3User(String newValue){return setValue(POP3User, newValue);}	
	public final static String  getPop3PW(){return getValue(POP3PW);}
	public final static boolean setPop3PW(String newValue){return setValue(POP3PW, newValue);}	
	public final static String  getPop3Port(){return getValue(POP3Port);}
	public final static boolean setPop3Port(String newValue){return setValue(POP3Port, newValue);}
	
	// smtp
	public final static String  getsmtpServer(){return getValue(smtpServer);}
	public final static boolean setsmtpServer(String newValue){return setValue(smtpServer, newValue);}
	public final static String  getsmtpUser(){return getValue(smtpUser);}
	public final static boolean setsmtpUser(String newValue){return setValue(smtpUser, newValue);}
	public final static String  getsmtpPW(){return getValue(smtpPW);}
	public final static boolean setsmtpPW(String newValue){return setValue(smtpPW, newValue);}
	public final static String  getsmtpPort(){return getValue(smtpPort);}
	public final static boolean setsmtpPort(String newValue){return setValue(smtpPort, newValue);}


	
}