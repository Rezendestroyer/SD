package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ExecutionThread implements Runnable {
    private final BlockingQueue<Input> queue;
    private Map<BigInteger, byte[]> dataBase = new HashMap();;
    private String command;

    public ExecutionThread(BlockingQueue<Input> _queue) {
        this.queue = _queue;
    }

    public void run() {
        try {
            while (true) {
                Input input = queue.take();

                ObjectOutputStream outputStream = input.getOutputStream();
                command = input.getCommand();

                try {
                    if (command.toLowerCase().contains("insert")) {
                        String[] elements = command.split("(?i)value");
                        BigInteger id = new BigInteger(elements[0].split(" ")[1].trim());
                        dataBase.putIfAbsent(id, elements[1].trim().getBytes());
                        if (outputStream != null) {
                            outputStream.flush();
                            outputStream.writeObject("Comando executado com sucesso!");
                        }
                        System.out.println("Comando executado: " + command);
                    } else if (command.toLowerCase().contains("update")) {
                        String[] elements = command.split("(?i)value");
                        dataBase.replace(new BigInteger(elements[0].split(" ")[1].trim()), elements[1].trim().getBytes());
                        if (outputStream != null) {
                            outputStream.flush();
                            outputStream.writeObject("Comando executado com sucesso!");
                        }
                        System.out.println("Comando executado: " + command);
                    } else if (command.toLowerCase().contains("delete")) {
                        String[] elements = command.split(" ");
                        BigInteger id = new BigInteger(elements[1].trim());
                        if (dataBase.containsKey(id)) {
                            dataBase.remove(id);
                            if (outputStream != null) {
                                outputStream.flush();
                                outputStream.writeObject("Comando executado com sucesso!");
                            }
                            System.out.println("Comando executado: " + command);
                        } else {
                            if (outputStream != null) {
                                outputStream.flush();
                                outputStream.writeObject("Registro não encontrado!");
                            }
                        }
                    } else if (command.toLowerCase().contains("select")) {
                        String[] elements = command.split(" ");
                        if (outputStream != null) {
                            outputStream.flush();
                            System.out.println(elements[1].trim());
                            BigInteger id = new BigInteger(elements[1].trim());
                            if (dataBase.containsKey(id)) {
                                if (outputStream != null) {
                                    outputStream.flush();
                                    outputStream.writeObject(new String(dataBase.get(id)));
                                }
                                System.out.println("Comando executado: " + command);
                            } else {
                                if (outputStream != null) {
                                    outputStream.flush();
                                    outputStream.writeObject("Registro não encontrado!");
                                }
                            }
                        }
                    } else {
                        if (outputStream != null) {
                            outputStream.flush();
                            outputStream.writeObject("Não foi possível executar o comando!");
                        }
                    }
                } catch (Exception e) {
                    outputStream.writeObject("Não foi possível executar o comando!");
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
