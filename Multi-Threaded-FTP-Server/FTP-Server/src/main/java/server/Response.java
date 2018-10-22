package server;

import java.io.IOException;
import java.io.OutputStream;
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
	
	public String response = null;

	public Response(Socket s, String responseToClient) {
		this.socket = s;
		this.response = responseToClient;
	}

	@Override
	public void run() {
		synchronized (Locked) {
			System.out.println("Response Run Starts...");
			try {
				responseSession();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Locked = true;
	}

	private void responseSession() throws IOException {
		session = false;
		sendResponse();
		session = true;
	}

	public String makeResponse() {
		String response = "Invalid Request was sent";
		String request = "\nHeader: " + header + "\n\tFile name: " + fileName;
		System.out.println("Server Response: " + request);

		return request;
	}

	public void sendResponse() throws IOException {
		System.out.println("Send Response Entered ...");
		OutputStream output = this.socket.getOutputStream();
		output.write("\n".getBytes());
		output.write("Server's Response\n".getBytes());
		String response = makeResponse();
		//output.write("\n".getBytes());
	}
}
