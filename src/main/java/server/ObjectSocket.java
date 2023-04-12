package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectSocket implements Channel, AutoCloseable {
    private final Socket client;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ObjectSocket(Socket client) throws IOException {
        this.client = client;

        // Le ObjectOutputStream doit être instancié en premier de chaque côté (client/serveur),
        // car il écrit un header qui est attendu par le ObjectInputStream de l'autre côté (serveur/client).
        this.objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        this.objectOutputStream.flush();

        this.objectInputStream = new ObjectInputStream(client.getInputStream());
    }

    public <T> T read() throws Exception {
        return (T) this.objectInputStream.readObject();
    }

    public <T> void write(T o) throws Exception {
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
    }

    @Override
    public void close() throws Exception {
        this.objectOutputStream.close();
        this.objectInputStream.close();
        this.client.close();
    }
}
