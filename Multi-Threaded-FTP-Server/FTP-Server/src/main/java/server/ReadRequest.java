package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class ReadRequest extends Thread {
	private static Object Locked = new Object();
	private Random gen = new Random();

	private Socket socket;
	public InputStream sockIn;
	public static BufferedReader sockReader;
	public static ExecutorService FTP_service;
	public Semaphore serverAcceptedFiles;
	int id = 0;
	private Response response = null;

	public ReadRequest(Socket s, InputStream sockIn, BufferedReader sockReader, ExecutorService FTP_service,
			Semaphore serverAcceptedFiles) {
		this.socket = s;
		this.sockIn = sockIn;
		this.sockReader = sockReader;
		this.FTP_service = FTP_service;
		this.serverAcceptedFiles = serverAcceptedFiles;
	}

	@Override
	public void run() {
		super.run();

		try {
			process();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void process() throws IOException {
		String returnRequest = null;
		String requestLine = null;
		String line = "";
		String c = "";
		if (sockReader.ready()) {
			requestLine = "";
			int counter = 0;
			while ((c = sockReader.readLine()) != null) {
				if (c.length() != 0) {
					try {
						id = Integer.parseInt(c);

					} catch (NumberFormatException e) {
						if (!c.equals("------------")) {
							line = c;
							requestLine += "\n";
							requestLine += line;
						} else {
							counter++;
							returnRequest = requestLine;

							System.out.println("\nRequest Received:  " + id + " \n\t" + returnRequest);
							requestLine = "";

							if (returnRequest.contains("Sending a file")) {
								response = new Response(socket, id, "Upload File");
							} else if (returnRequest.contains("Retrieving a file")) {
								response = new Response(socket, id, "Retrieved File");
							}
							FTP_service.submit(() -> {
								try {
									serverAcceptedFiles.acquire();
								} catch (InterruptedException ex) {
									ex.printStackTrace();
								}
								response.start();
								serverAcceptedFiles.release();
							});

						}
					}

				}

			}
		}
	}
}
