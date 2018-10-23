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

	public Response(Socket s) {
		this.socket = s;
	}

	@Override
	public void run() {
		synchronized (Locked) {
			try {
				responseSession();
			} catch (IOException e) {
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
		response = "\nHeader: " + header + "\n\tFile name: " + fileName;

		return response;
	}

	public void sendResponse() throws IOException {
		OutputStream output = this.socket.getOutputStream();
		output.write("\n".getBytes());
		output.write("Server's Response\n".getBytes());
		String response = makeResponse();
	}
}
