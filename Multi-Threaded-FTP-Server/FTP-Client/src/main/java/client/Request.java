package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Request extends Thread {
	private String header = "";
	private String fileName = "";
	private String fileType = "";
	private String fileContent = "";
	private long fileSize = 0;

	private static Object Locked = new Object();
	private static Random gen = new Random();
	boolean sessionEnded = false;
	private Socket socket;

	List<String> listOfFilePaths = new ArrayList<String>();

	List<String> listOfFileNames = new ArrayList<String>();

	public Request(Socket s) {
		this.socket = s;
		setFilePaths();

	}

	@Override
	public void run() {
		synchronized (Locked) {
			try {
				requestSession();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Locked = true;
	}

	private void requestSession() throws IOException {
		sessionEnded = false;
		String request = makeRequest();

		if (request != null && request != "") {
			try (Socket sock = new Socket("localhost", 2500)) {
				try (PrintStream out = new PrintStream(sock.getOutputStream())) {
					out.println(request);
					BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					String line = in.readLine();
					System.out.println("Server said: \"" + line + "\"");
				}
			}
		}
		sessionEnded = true;
	}

	private String pushFile() {
		header = "Sending a file.";
		long currentTime = System.currentTimeMillis();
		fileName = "NewFile- " + currentTime;
		File file = new File(
				"/home/priya/Personal Workspace/FTP-Web-Server/Multi-Threaded-FTP-Server/FTP-Server/src/main/java/serverFiles/"
						+ fileName);
		fileType = ".txt";
		fileContent = "Current time is : " + System.currentTimeMillis();
		fileSize = fileContent.length();
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String request = "Header: " + header + "\n\tFile name: " + fileName + "\n\tFile type: " + fileType
				+ "\n\tFile size: " + fileSize + "\n\n\tBody: \n\t\t" + fileContent;
		return request;
	}

	private String pullFile() {
		header = "Retrieving a file";
		String request = "Header: " + header + "\n\t\tFile name: " + fileName + "\n\t\tFile type: " + fileType;
		return request;
	}

	private void setFilePaths() {
		File folder = new File(
				"/home/priya/Personal Workspace/FTP-Web-Server/Multi-Threaded-FTP-Server/FTP-Server/src/main/java/serverFiles/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				listOfFilePaths.add(i, listOfFiles[i].getPath());
				listOfFileNames.add(i, listOfFiles[i].getName());
			}
		}
	}

	public String makeRequest() throws IOException {
		int randomRequest = gen.nextInt(2);
		String request = "Invalid Request";
		int randomFile = gen.nextInt(listOfFileNames.size());

		if (randomRequest == 0) {
			// Request is push file

			request = pushFile();
		} else if (randomRequest == 1) {
			fileName = listOfFileNames.get(randomFile);
			fileType = ".txt";
			request = pullFile();
		}
		System.out.println("Request: (" + this.getId() + ") \n\t" + request);
		return request;
	}
}