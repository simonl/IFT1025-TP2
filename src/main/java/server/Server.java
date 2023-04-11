package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(Channel client, String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(client, cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                Socket client = server.accept();
                System.out.println("Connecté au client: " + client);
                try (ObjectSocket channel = new ObjectSocket(client)) {
                    listen(channel);
                }
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen(Channel client) throws Exception {
        String line;
        if ((line = client.<Object>read().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(client, cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void handleEvents(Channel client, String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration(client);
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(client, arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(Channel client, String arg) {
        try {
            List<Course> courses = Database.loadCourseList(arg);
            client.write(courses);
        } catch (Exception e) {
            // Abandonner l'opération.
            // Le client ne recevra pas de résultat.
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gère les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration(Channel client) {
        try {
            RegistrationForm form = client.read();
            String message = Database.registerCourse(form);
            client.write(message);
        } catch (Exception e) {
            // Abandonner l'opération.
            // Le client ne recevra pas de résultat.
        }
    }
}

