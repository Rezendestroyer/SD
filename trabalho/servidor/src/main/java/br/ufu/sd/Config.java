package br.ufu.sd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    private static String config = "config.txt";

    private boolean isLoaded = false;

    private static String server;
    private static int port;

    public void load() {
        if (!isLoaded) {
            try {
                FileReader fileReader = new FileReader(config);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line = bufferedReader.readLine();

                String[] fields;

                while (line != null) {
                    fields = line.split(":");
                    if (fields.length > 1 && fields[0].equals("Porta")) port = Integer.parseInt(fields[1].replace(" ", ""));
                    if (fields.length > 1 && fields[0].equals("Endereco")) server = fields[1].replace(" ", "");
                    line = bufferedReader.readLine();
                }

                fileReader.close();

                isLoaded = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Não foi possível carregar as configurações!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Não foi possível carregar as configurações!");
            }
        }
    }

    public boolean getIsLoaded() {
        return this.isLoaded;
    }

    public String getServer() {
        if (!isLoaded) System.out.println("Atenção! As configurações ainda não foram carregadas.");
        return this.server;
    }

    public int getPort() {
        if (!isLoaded) System.out.println("Atenção! As configurações ainda não foram carregadas.");
        return this.port;
    }
}
