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

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
    }

    public void run() {
        while (true) {
            try {
                Socket client = server.accept();

                Thread thread = new Thread(() -> {
                    try {
                        System.out.println("Connecté au client: " + client);
                        try (ObjectSocket channel = new ObjectSocket(client)) {
                            listen(channel);
                        }
                        System.out.println("Client déconnecté!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void listen(Channel client) throws Exception {
        String line;
        if ((line = client.<String>read()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            handleEvents(client, cmd, arg);
        }
    }

    public static Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public static void handleEvents(Channel client, String cmd, String arg) {
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
    public static void handleLoadCourses(Channel client, String arg) {
        try {
            List<Course> courses = Database.loadCourseList(arg);
            client.write(courses);
        } catch (Exception e) {
            // Abandonner l'opération.
            // Le client ne recevra pas de résultat.
            e.printStackTrace();
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gère les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public static void handleRegistration(Channel client) {
        try {
            RegistrationForm form = client.read();
            String message = Database.registerCourse(form);
            client.write(message);
        } catch (Exception e) {
            // Abandonner l'opération.
            // Le client ne recevra pas de résultat.
            e.printStackTrace();
        }
    }
}

