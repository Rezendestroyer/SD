package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private static int port;
    private static Config config = new Config();
    private static BlockingQueue<Input> queue1 = new LinkedBlockingQueue();
    private static BlockingQueue<Input> queue2 = new LinkedBlockingQueue();
    private static BlockingQueue<Input> queue3 = new LinkedBlockingQueue();

    private static void initialization() {
        Log log = new Log();
        log.open();
        log.read(queue3);
        log.close();
    }

    public static void main(String argv[]) {
        config.load();

        if (config.getIsLoaded()) {
            port = config.getPort();

            try {
                initialization();

                Thread organizer = new Thread(new OrganizerThread(queue1, queue2, queue3));
                organizer.setDaemon(true);
                organizer.start();

                Thread execution = new Thread(new ExecutionThread(queue3));
                execution.setDaemon(true);
                execution.start();

                Thread log = new Thread(new LogThread(queue2));
                log.setDaemon(true);
                log.start();

                ServerSocket welcomeSocket = new ServerSocket(port);
                System.out.println("Servidor ouvindo a porta " + port);


                while (true) {
                    Socket connectionSocket = welcomeSocket.accept();
                    System.out.println("Cliente conectado: " + connectionSocket.getInetAddress().getHostAddress());

                    ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());
                    ObjectOutputStream outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());

                    Thread reception = new Thread(new ReceptionThread(inputStream, outputStream, queue1));
                    reception.setDaemon(true);
                    reception.start();

                    outputStream.flush();
                    outputStream.writeObject("Conexão estabelecida!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
