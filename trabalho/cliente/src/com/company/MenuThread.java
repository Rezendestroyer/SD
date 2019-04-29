package com.company;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MenuThread implements Runnable {
    private static String server;
    private static int port;
    private static InetAddress serverIpAddress;

    private Socket socket;
    private Scanner scn;
    private String command;
    private ObjectOutputStream outputStream;

    private Config config = new Config();

    public void run() {
        config.load();
        if (config.getIsLoaded()) {
            scn = new Scanner(System.in);

            server = config.getServer();
            port = config.getPort();

            try {
                serverIpAddress = InetAddress.getByName(server);
                socket = new Socket(serverIpAddress, port);

                Thread response = new Thread(new ResponseThread(socket));
                response.setDaemon(true);
                response.start();

                outputStream = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    System.out.println("\n----------- Lista de Comandos ------------");
                    System.out.println("1. Insert <id> value <value>");
                    System.out.println("2. Select <id>");
                    System.out.println("3. Update <id> value <values>");
                    System.out.println("4. Delete <id>");
                    System.out.println("5. Exit");
                    System.out.println("------------------------------------------");
                    command = scn.nextLine();

                    if (command.toLowerCase().equals("exit")) {
                        Thread.sleep(5000);
                        break;
                    } else {
                        if (validation(command)) {
                            outputStream.flush();
                            outputStream.writeObject(command);
                            Thread.sleep(1000);
                        } else System.out.println("\nComando inválido!");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Ocorreu uma falha!\nPor favor, verifique a disponibilidade do servidor.");
                e.printStackTrace();
            }
            scn.close();
        }
    }

    public boolean validation(String text) {
        char c;
        String command = "";

        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            if (c == ' ') break;
            command += c;
        }

        switch (command.toLowerCase()) {
            case "insert":
            case "update":
                String[] elements = text.split("(?i)value");
                if (elements.length < 2) return false;
                elements =  elements[0].split(" ");
                if (elements.length < 2) return false;
                try {
                    BigInteger b = new BigInteger(elements[1]);
                } catch (NumberFormatException | NullPointerException nfe) {
                    return false;
                }
                break;
            case "select":
            case "delete":
                elements =  text.split(" ");
                if (elements.length < 2) return false;
                try {
                    BigInteger b = new BigInteger(elements[1]);
                } catch (NumberFormatException | NullPointerException nfe) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public static void main(String argv[]) {
        Thread menu = new Thread(new MenuThread());
        menu.start();
    }
}
