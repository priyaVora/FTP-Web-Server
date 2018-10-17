package client;

import java.io.BufferedReader;
import java.util.Random;

public class Request extends Thread {
	private String header = "";
	private String fileName = "";
	private String fileType = "";
	private String fileSize = "";

	private static Object Locked = new Object();
	private static Random gen = new Random();
	boolean sessionEnded = false;

	@Override
	public void run() {
		synchronized (Locked) {
			requestSession();
		}
		Locked = true;
	}

	private void requestSession() {
		sessionEnded = false;
		makeRequest();
		sessionEnded = true;
	}

	private String pushFile() {
		header = "Sending a file.";
		String request = "Header: " + header + "\n\tFile name: " + fileName + "\n\tFile type: " + fileType
				+ "\n\tFile size: " + fileSize;
		return request;
	}

	private String pullFile() {
		header = "Retrieving a file";
		String request = "Header: " + header + "\n\tFile name: " + fileName + "\n\tFile type: " + fileType
				+ "\n\tFile size: " + fileSize;
		return request;
	}

	public String makeRequest() {
		int randomRequest = gen.nextInt(2);
		String request = "Invalid Request";
		if (randomRequest == 0) {
			// Request is push file
			request = pushFile();
		} else if (randomRequest == 1) {
			request = pullFile();
		}
		System.out.println("Request: (" + this.getId() + ") " + request);
		return request;
	}
}