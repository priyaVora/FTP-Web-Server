package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

public class Response extends Thread {
	private static Object Locked = new Object();
	private Random gen = new Random();
	private String filePath = "C:\\Users\\Parker\\Workspaces\\ProcessModeling\\FTP-Web-Server\\Multi-Threaded-FTP-Server\\FTP-Server\\src\\main\\java\\serverFiles\\";

	private String header = "";
	private String fileName = "";
	private String fileType = "";
	private String responseType = "";
	private String fileBody = "";
	private Socket socket;
	int id = 0;

	//PULL / RETREIVE RESPONSE
	public Response(Socket s, int id, String response) {
		this.socket = s;
		this.id = id;
		this.responseType = response;
	}
	
	//PUSH / UPLOAD RESPONSE
	public Response(Socket s, int id, String body, String response) {
		this.socket = s;
		this.id = id;
		this.fileBody = body;
		this.responseType = response;
	}

	@Override
	public void run() {
		synchronized (Locked) {
			try {
				sendResponse();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Locked = true;
	}

	public String makeResponse() {
		String response = "";
		
		//Upload
		if(responseType.equals("UPLOAD")) {
			//Save fileBody to server
			response = "Server Response Header: (" + id + ")- " + responseType + " "+ header + "\n\tFile name: " + fileName
					+ "\n\tType: " + fileType +  "\nUpload Successful\n";
			
		}
		//Retrieve
		else {
			//Find file, return body
			File returnFile = new File(filePath + fileName);
			if(returnFile.exists()) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(returnFile));
				} catch (FileNotFoundException e) {e.printStackTrace();}
				
				String st;
				String readFileBody = null;
				try {
					while ((st = br.readLine()) != null) {
						readFileBody += st;
					 }
					System.out.println("RETURN FILE BODY: " + readFileBody);
				} catch (IOException e) {e.printStackTrace();} 
			}
			else {
				System.out.println("FILE " + returnFile.getAbsolutePath() + " DOES NOT EXIST");
			}
			response = "Server Response Header: (" + id + ")- " + responseType + " " + header + "\n\tFile name: " + fileName
					+ "\n\tType: " + fileType + "\n\tBody: "; //Get File Body here
		}
		return response;
	}

	public void sendResponse() throws IOException {
		OutputStream output = this.socket.getOutputStream();
		output.write("\n".getBytes());
		String response = makeResponse();
		output.write(response.getBytes());
		// response = makeResponse();
	}
	
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
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
