package server;

import java.util.Random;

public class Response extends Thread {
	private static Object Locked = new Object();
	private static Random gen = new Random();
	
	private static String header = "";
	private static String fileName = "";
	private static boolean session = false;

	public static void main(String[] args) {
		responseSession();
	}

	@Override
	public void run() {
		synchronized (Locked) {
			responseSession();
		}
		Locked = true;
	}

	public static void responseSession() {
		session = false;
		makeResponse();
		session = true;
	}
	
	public static void readFile(String filename) { 
		
	}

	public static String respond() {
		String response = "";
		response = makeResponse();
		return response;
		
	}

	public static String makeResponse() {
		String response = "Invalid Request was sent";
		String request = "\nHeader: " + header + "\n\tFile name: " + fileName;
		System.out.println("Server Response: " + request);
		return request;
	}
}
