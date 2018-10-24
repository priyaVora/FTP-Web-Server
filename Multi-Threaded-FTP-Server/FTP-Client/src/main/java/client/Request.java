package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	private String filePath = "C:\\Users\\Parker\\Workspaces\\ProcessModeling\\FTP-Web-Server\\Multi-Threaded-FTP-Server\\FTP-Server\\src\\main\\java\\serverFiles\\";
	// "/home/priya/Personal Workspace/MultiServer/FTP-Web-Server/Multi-Threaded-FTP-Server/FTP-Server/src/main/java/serverFiles/"
	private static Object Locked = new Object();
	private static Random gen = new Random();
	boolean sessionEnded = false;
	private Socket socket;
	String id = "";

	List<String> listOfFilePaths = new ArrayList<String>();

	List<String> listOfFileNames = new ArrayList<String>();

	public Request(Socket s) {
		this.socket = s;
		setFilePaths();
		this.id = this.getId() + "";
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
		sessionEnded = true;
	}

	public void write(String request) throws IOException {
		OutputStream output = socket.getOutputStream();
		output.write(request.getBytes());
		output.write("\n".getBytes());
	}

	private String pushFile() throws IOException {
		header = "Sending a file";
		long currentTime = System.currentTimeMillis();
		fileName = "NewFile- " + currentTime;
		File file = new File(filePath + fileName);
		fileType = ".txt";
		fileContent = "Current time is : " + System.currentTimeMillis();
		fileSize = fileContent.length();
		try {
			if (file.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String headerLine = "\nHeader: (" + this.getId() + ") " + header;
		String fileNameLine = "\n\tFile name: " + fileName;
		String fileTypeLine = "\n\tFile type: " + fileType;
		String fileSizeLine = "\n\tFile size: " + fileSize;
		String fileContentLine = "\n\t\tBody: " + fileContent;
		write(id);
		write(headerLine);
		write(fileNameLine);
		write(fileTypeLine);
		write(fileSizeLine);
		write(fileContentLine);
		write("------------");
		
		String request = id + headerLine + fileNameLine + fileTypeLine + fileSizeLine + fileContentLine;
//		String request = "\nHeader: (" + this.getId() + ") " + header + "\n\tFile name: " + fileName + "\n\tFile type: "
//				+ fileType + "\n\tFile size: " + fileSize + "\n\n\tBody: \n\t\t" + fileContent;

		return request;
	}

	private String pullFile() throws IOException {
		header = "Retrieving a file";
		String headerLine = "\nHeader: (" + this.getId() + ") " + header;
		String fileNameLine = "\n\tFile name: " + fileName;
		String fileTypeLine = "\n\tFile type:" + fileType;
		write(id);
		write(headerLine);
		write(fileNameLine);
		write(fileTypeLine);
		write("------------");

		String request = id + headerLine + fileNameLine + fileTypeLine;
//		String request = "\nHeader: (" + this.getId() + ") " + header + "\n\t\tFile name: " + fileName
//				+ "\n\t\tFile type: " + fileType;
		return request;
	}

	private void setFilePaths() {
		File folder = new File(filePath);
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
		System.out.println("Request: (" + this.getId() + ")" + request);
		return request;
	}
	
	
	public String getRequestID() {
		return id;
	}
}