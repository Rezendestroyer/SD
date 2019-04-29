package com.company;

import java.io.*;
import java.util.concurrent.BlockingQueue;

public class Log {
    private String file = "log.txt";
    private FileWriter fileWriter;
    private FileReader fileReader;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private boolean isOpen = false;

    public void open() {
        try {
            if (!isOpen) {
                fileWriter = new FileWriter(file,true);
                printWriter = new PrintWriter(fileWriter);
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);


                isOpen = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (isOpen) {
                fileWriter.close();
                fileReader.close();
                isOpen = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String content) {
       if (isOpen) {
           printWriter.printf(content + "%n");
           printWriter.flush();
           System.out.println("Comando gravado no arquivo log.");
       }
       else System.out.println("O arquivo " + file + " não está aberto!");
    }

    public void read(BlockingQueue<Input> queue) {
        if (isOpen) {
            try {
                String line = bufferedReader.readLine();

                while (line != null) {
                    queue.add(new Input(null, line));
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
