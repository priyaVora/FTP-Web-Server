package server;

import java.util.Random;

public class Response extends Thread {
	private static Object Locked = new Object();
	private Random gen = new Random();

	private String header = "";
	private String fileName = "";
	private boolean session = false;

	@Override
	public void run() {
		synchronized (Locked) {
			responseSession();
		}
		Locked = true;
	}

	private void responseSession() {
		session = false;
		makeResponse();
		session = true;
	}

	public void readFile(String filename) {

	}

	public String respond() {
		String response = "";
		response = makeResponse();
		return response;

	}

	public String makeResponse() {
		String response = "Invalid Request was sent";
		String request = "\nHeader: " + header + "\n\tFile name: " + fileName;
		System.out.println("Server Response: " + request);
		return request;
	}
}
