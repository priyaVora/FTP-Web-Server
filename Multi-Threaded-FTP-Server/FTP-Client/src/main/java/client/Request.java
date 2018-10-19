package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Files;
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
		sendRequestToServer(request);

		sessionEnded = true;
	}

	private void sendRequestToServer(String request) throws IOException {
		System.out.println("Sending file to Server");
		OutputStream output = socket.getOutputStream();
			output.write(request.getBytes());
	}

	private String pushFile() {
		header = "Sending a file.";
		long currentTime = System.currentTimeMillis();
//		fileName = "NewFile- " + currentTime;
		fileName = "Elephants";
		File file = new File(
				"C:\\Users\\Parker\\Workspaces\\ProcessModeling\\FTP-Web-Server\\Multi-Threaded-FTP-Server\\FTP-Server\\src\\main\\java\\serverFiles\\Elephants");
		fileType = ".txt";
		fileContent = fileToByteArray(file);
		fileSize = fileContent.length();

		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		String request = "Header:" + header + "||File name:" + fileName + "||File type:" + fileType
				+ "||File size:" + fileSize + "||Body:" + fileContent;
		return request;
	}

	private String fileToByteArray(File file) {	
		List<String> readFileContent = null;
		try {
			readFileContent = Files.readAllLines(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File byte size: " + file.length());
		String content = "";
		for(String s : readFileContent) {
			content += s;
			content += "\r\n";
		}
		System.out.println("File content: " + content);
		return content;
	}

	private String pullFile() {
		header = "Retrieving a file";
		String request = "Header: " + header + "\n\t\tFile name: " + fileName + "\n\t\tFile type: " + fileType;
		return request;
	}

	private void setFilePaths() {
		File folder = new File(
				"C:\\Users\\Parker\\Workspaces\\ProcessModeling\\FTP-Web-Server\\Multi-Threaded-FTP-Server\\FTP-Server\\src\\main\\java\\serverFiles\\");
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
		randomRequest = 0;

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