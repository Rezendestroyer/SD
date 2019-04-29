package com.company;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Input {
    private ObjectOutputStream outputStream;
    private String command;

    public Input(ObjectOutputStream _outputStream, String _command) {
        this.outputStream = _outputStream;
        this.command = _command;
    }

    public void getOutputStream(ObjectOutputStream _outputStream) {
        this.outputStream = _outputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setCommand(String _command) {
        this.command = _command;
    }

    public String getCommand() {
        return this.command;
    }
}
