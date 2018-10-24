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

	final static int MAX_FILE_PROCESS = 2000;

	private static ExecutorService FTP_service;
	private static Semaphore serverAcceptedFiles = new Semaphore(MAX_FILE_PROCESS);

	public static Random random = new Random();

	public InputStream sockIn;
	public static BufferedReader sockReader;

	public static void main(String[] args) throws IOException, InterruptedException {
		process();
	}

	public static void process() throws IOException {
		ServerSocket servSocket = new ServerSocket(2500);
		FTP_service = Executors.newFixedThreadPool(MAX_FILE_PROCESS);
		System.out.println("Starting Up...");
		
		while(true) {
			Socket s;
			s = servSocket.accept();
			System.out.println("Server Accepts Connection");
	
			
			FTP_service.submit(() -> {
				InputStream sockIn = null;
				try {
					sockIn = s.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sockReader = new BufferedReader(new InputStreamReader(sockIn));
		
				ReadRequest readRequest = new ReadRequest(s, sockIn, sockReader, FTP_service, serverAcceptedFiles);
				
					sleep(4500);
					readRequest.start();
					sleep(4500);
				});
		}
	}

	private static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}