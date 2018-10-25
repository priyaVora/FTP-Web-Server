package test;

import static org.junit.Assert.*;
import org.junit.Test;
import client.Client;

public class ServerTests {

	@Test
	public void testServerTenRequests() {
		Client c = new Client(10);
		try {
			c.run();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testServerHundredRequests() {
		Client c = new Client(100);
		try {
			c.run();
		} catch(Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testServerThousandRequests() {
		Client c = new Client(1000);
		try {
			c.run();
		} catch(Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testServerTenThousandRequests() {
		Client c = new Client(10000);
		try {
			c.run();
		} catch(Exception e) {
			fail();
			e.printStackTrace();
		}
	}

}
