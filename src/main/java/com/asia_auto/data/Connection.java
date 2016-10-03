package com.asia_auto.data;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Класс обеспечивет соединение клиент-сервер.
 */
public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws Exception {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(Message message) throws Exception {
        out.writeObject(message);
    }

    public Message receive() throws Exception {
        Message mess = (Message) in.readObject();
        return mess;
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
