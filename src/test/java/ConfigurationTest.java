import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigurationTest {

//	@Test
//	public void testCreateFolder() {
//		fail("Not yet implemented"); // TODO
//	}

//	@Test
//	public void testDeleteConfig() {
//		fail("Not yet implemented"); // TODO
//	}

	@Test
	public void testGetMailClientName() {
		
		
		final String MailClientName = "Bananaa MailClient";
		String expected = "Bananaa MailClient";
		String result = Configuration.getMailClientName();
		
		
		assertEquals(expected,result);
		
		//fail("Not yet implemented"); // TODO
	}


//
//	@Test
//	public void testGetDebug() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetPrefs() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetFolders() {
//		fail("Not yet implemented"); // TODO
//	}
//
	@Test
	public void testGetNamePostausgang() {
		
	final String NamePostausgang = "Gesendet";
	String expected = "Gesendet";
	
	String result = Configuration.getNamePostausgang();
	assertEquals(expected,result);

}

	@Test
	public void testGetNamePosteingang() {
	final String NamePosteingang = "Neue Mails";
	String expected = "Neue Mails";
	
	String result = Configuration.getNamePosteingang();
	assertEquals(expected,result);
	}

	@Test
	public void testGetNameRootFolder() {
		final String NameRootFolder = "Folders";
		String expected = "Folders";
		
		String result = Configuration.getNameRootFolder();
		assertEquals(expected,result);
	}

	@Test
	public void testGetPop3Server() {
		final String Pop3Server = "pop.gmail.com";
		String expected = "pop.gmail.com";
		
		String result = Configuration.getPop3Server();
		assertEquals(expected,result);	
	}

//	@Test
//	public void testSetPop3Server() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetPop3User() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetPop3User() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetPop3PW() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetPop3PW() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetPop3Port() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetPop3Port() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetsmtpServer() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetsmtpServer() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetsmtpUser() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetsmtpUser() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetsmtpPW() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetsmtpPW() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetsmtpPort() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSetsmtpPort() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetOrdner() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetEingang() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetGesendet() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testObject() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetClass() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testHashCode() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testEquals() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testClone() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testToString() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testNotify() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testNotifyAll() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testWaitLong() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testWaitLongInt() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testWait() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testFinalize() {
//		fail("Not yet implemented"); // TODO
//	}
//
}
