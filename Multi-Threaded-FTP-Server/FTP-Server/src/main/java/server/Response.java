package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

public class Response extends Thread {
	private static Object Locked = new Object();
	private Random gen = new Random();

	private String header = "";
	private String fileName = "";
	private boolean session = false;
	private Socket socket;

	public Response(Socket s) {
		this.socket = s;
	}

	@Override
	public void run() {
		synchronized (Locked) {
			responseSession();
		}
		Locked = true;
	}

	private void responseSession() {
		session = false;
		respond();
		session = true;
	}

	public void readFile(String filename) {

	}

	public void respond() {
		String response = "";
		sendResponse();
	}

	public String makeResponse() {

		String response = "Invalid Request was sent";
		String request = "\nHeader: " + header + "\n\tFile name: " + fileName;
		System.out.println("Server Response: " + request);

		return request;
	}

	public void sendResponse() {
		PrintStream sockOut = null;
		try {
			sockOut = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sockOut.println(makeResponse());
	}
}
