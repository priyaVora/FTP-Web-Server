package server;

import java.io.BufferedReader;
import java.io.File;
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
	final static int MAX_FILE_PROCESS = 3;

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

				InputStream sockIn = s.getInputStream();
				BufferedReader sockReader = new BufferedReader(new InputStreamReader(sockIn));
				String line = sockReader.readLine();
				System.out.println("Read the line \"" + line + "\"");

				PrintStream sockOut = new PrintStream(s.getOutputStream());
				sockOut.println(line);
				System.out.println("Echoed the line back to the client");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sockOut.flush();

//				System.out.println("Closing connection");
//				servSocket.close();
			}
		}
	}

	public static void readRequest() {

	}
}