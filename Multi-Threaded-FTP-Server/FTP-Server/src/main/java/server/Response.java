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
	private String fileType = "";
	private String responseType = "";
	private boolean session = false;
	private Socket socket;
	int id = 0;

	public Response(Socket s, int id, String response) {
		this.socket = s;
		this.id = id;
		this.responseType = response;
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
		response = "Server Response Header: (" + id + ")- " + responseType + " " + header + "\n\tFile name: " + fileName
				+ "\n\tType: " + fileType;

		return response;
	}

	public void sendResponse() throws IOException {
		OutputStream output = this.socket.getOutputStream();
		output.write("\n".getBytes());
		String response = makeResponse();
		output.write(response.getBytes());
		// response = makeResponse();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	
}
