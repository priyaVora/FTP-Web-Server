package server;

import java.util.Random;

public class Request extends Thread {
	private static Object Locked = new Object();
	
	public static int fileSubmission = 0;
	private static Random gen = new Random();
	int criticalSessionTime = 0;
	boolean sessionEnd = false;

	@Override
	public void run() {
		synchronized (Locked) {
			fileRequestSession();
		}
		Locked = true;
	}

	private void fileRequestSession() {
		System.out.println("# " + this.getId() + " - request is processed.");
		sessionEnd = true;
	}

}
