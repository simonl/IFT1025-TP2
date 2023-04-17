package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * La classe ObjectSocket encapsule la communication TCP à base d'objets sérialisés.
 */
public class ObjectSocket implements Channel, AutoCloseable {
    private final Socket client;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    /**
     * Le constructeur écrit immédiatement un entête sur la connexion TCP donnée, pour établir le protocol ObjectStream.
     * On s'attend aussi à ce que le pair ait fait pareil, et on lit l'entête provenant du socket.
     *
     * @param socket La connexion TCP déjà établie avec un pair
     * @throws IOException Si l'établissement du protocol échoue
     */
    public ObjectSocket(Socket socket) throws IOException {
        this.client = socket;

        // Le ObjectOutputStream doit être instancié en premier de chaque côté (socket/serveur),
        // car il écrit un header qui est attendu par le ObjectInputStream de l'autre côté (serveur/socket).
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectOutputStream.flush();

        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Lire un object d'un type donné provenant de la connexion.
     *
     * @return Un object désérialisé de type T
     * @param <T> Le type d'objet qu'on s'attend à recevoir
     * @throws Exception Si la lecture échoue ou on reçoit un objet inattendu
     */
    public <T> T read() throws Exception {
        return (T) this.objectInputStream.readObject();
    }

    /**
     * Écrire un objet en format sérialisé sur la connexion.
     *
     * @param o L'objet qu'on veut transmettre
     * @param <T> Le type de l'objet
     * @throws IOException Si l'écriture échoue
     */
    public <T> void write(T o) throws IOException {
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
    }

    /**
     * Fermer la connexion TCP proprement.
     *
     * @throws IOException Si une erreur I/O
     */
    @Override
    public void close() throws IOException {
        this.objectOutputStream.close();
        this.objectInputStream.close();
        this.client.close();
    }
}
