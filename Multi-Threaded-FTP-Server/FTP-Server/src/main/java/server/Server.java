package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Server {

	final static int CLIENTS = 20;
	final static int MAX_FILE_PROCESS = 25;

	private static ExecutorService FTP_service;
	private static Semaphore serverAcceptedFiles = new Semaphore(MAX_FILE_PROCESS);

	public static Random random = new Random();

	public static void main(String[] args) throws IOException, InterruptedException {
		process();
	}

	public static void process() throws IOException {
		ServerSocket servSocket = new ServerSocket(2500);
		FTP_service = Executors.newFixedThreadPool(MAX_FILE_PROCESS);
		System.out.println("Starting Up...");

		while (true) {
			try (Socket s = servSocket.accept()) {
				System.out.println("Server Accepts Connection");
				System.out.println("Receives Request");
				System.out.println();

				String line = readRequest(s);

				System.out.println("Reading File: \n\n");
				String fileContent = readFile(
						"/home/priya/Personal Workspace/FTP-Web-Server/Multi-Threaded-FTP-Server/FTP-Server/src/main/java/serverFiles/file");
				System.out.println("File Content: \n\n" + fileContent);
				sleep();
//				sockOut.flush();

//				System.out.println("Closing connection");
//				servSocket.close();
			}
		}
	}

	public static String readRequest(Socket s) {
		InputStream sockIn = null;
		try {
			sockIn = s.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader sockReader = new BufferedReader(new InputStreamReader(sockIn));
		String line = null;
		try {
			line = sockReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}

	public static String readFile(String filePath) {
		String fileContent = "";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				fileContent += sCurrentLine;
				fileContent += "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileContent;
	}

	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}