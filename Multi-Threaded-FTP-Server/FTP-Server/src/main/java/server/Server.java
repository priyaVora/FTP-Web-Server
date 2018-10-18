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
import java.nio.channels.SocketChannel;
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
				System.out.println("Server Receives Request");

				InputStream sockIn = s.getInputStream();
				String request = readRequest(sockIn);
				if (request != null) {
					System.out.println("Request Received:\n\t  " + request);
				}

				sleep();
//				sockOut.flush();
//				System.out.println("Closing connection");
//				servSocket.close();
			}
		}
	}

	public static String readRequest(InputStream sockIn) throws IOException {
		BufferedReader sockReader = new BufferedReader(new InputStreamReader(sockIn));
		String requestLine = "";
		String line = "";
		boolean run = true;
		line = sockReader.readLine();
		requestLine += line;

		return requestLine;
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