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

/**
 * <pre>
 * La classe Server permet de recevoir des commandes par connexions TCP, pour l'inscription d'étudiants à leurs cours.
 *
 * Le protocole est basé sur la sérialisation d'objets en format natif à Java (ObjectInputStream, ObjectOutputStream).
 * Une fois la connexion établie, le client doit envoyer un object de type String, qui est l'entête de sa commande.
 * Le serveur ne traite qu'une seule commande par connexion client.
 *
 * Pour les détails de chaque commande, voyez Server.LOAD_COMMAND et Server.REGISTER_COMMAND.
 * </pre>
 **/
public class Server {
    /**
     * Nom indiquant une commande d'inscription.
     * <pre>
     * L'entête ne doit contenir que le nom de commande.
     * Suite à cet entête, le client doit envoyer un objet de type RegistrationForm.
     * En résultat, le serveur envoie un message de confirmation de type String.
     * </pre>
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";

    /**
     * Nom indiquant une requête de chargement de la liste des cours.
     * <pre>
     * L'entête doit contenir le nom de requête, un charactère espace, et puis un nom de session parmi AUTOMNE, HIVER, ETE.
     * Ensuite, le serveur envoie en résultat un objet de type List&lt;Course&gt;.
     * La liste ne contient que les cours disponibles durant la session choisi.
     * </pre>
     */
    public final static String LOAD_COMMAND = "CHARGER";

    // Membres pour gérer les connexions réseaux
    private final ServerSocket server;

    // Liste des implémentations de commandes
    private final ArrayList<EventHandler> handlers;

    /**
     * Ce constructeur occupe le port donné en tant que ServerSocket TCP.
     * Aucune commande ne sera traitée avant d'appeler la méthode run.
     *
     * @param port Le port auquel les clients vont se connecter.
     * @throws IOException Si la création du serveur échoue.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajouter l'implémentation d'une commande à ce serveur.
     * Lorsqu'un client se connecte, tous les EventHandler ont l'opportunité de traiter la commande.
     *
     * @param h Un EventHandler qui sera appelé à chaque connexion d'un client.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * On défère le traitement d'une commande aux EventHandlers attachés auparavant.
     *
     * @param client Le canal de communication au client pour cette commande.
     * @param cmd Le nom de la commande à exécuter.
     * @param arg Des données additionnelles requises pour le traitement de la commande.
     */
    private void alertHandlers(Channel client, String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(client, cmd, arg);
        }
    }

    /**
     * La méthode run bloque le fil d'exécution et traitera des nouvelles connexions client indéfiniment.
     * Si plusieurs clients se connectent en même temps, leurs commandes seront traitées simultanément.
     */
    public void run() {
        while (true) {
            try {
                // Le serveur bloque ici jusqu'à ce qu'un client se connecte
                Socket client = server.accept();

                Thread thread = new Thread(() -> {
                    try {
                        System.out.println("Connecté au client: " + client);
                        // L'initialisation des ObjectStram est importante à faire tôt,
                        // car des entêtes du protocol y sont écrites, de chaque côté.
                        try (ObjectSocket channel = new ObjectSocket(client)) {
                            // On traite la commande
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

    /**
     * La méthode listen décode l'entête de la connexion du client,
     * et envoie la commande aux EventHandlers attachés auparavant.
     *
     * @param client Le canal de communication au client pour cette commande.
     * @throws IOException Si la communication avec le client échoue.
     * @throws ClassNotFoundException Si l'objet envoyé par le client est de classe inconnue.
     */
    public void listen(Channel client) throws Exception {
        String line;
        if ((line = client.<String>read()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(client, cmd, arg);
        }
    }

    /**
     * La méthode processCommandLine sépare le nom de la commande des autres données dans l'entête de la commande.
     *
     * @param line L'entête de la commande envoyée par un client.
     * @return Une paire contenant le nom de la commande et les données additionnelles.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * La méthode handleEvents implémente la fonctionnalité de recherche et d'enregistrement des cours.
     * Une inscription ne peut seulement être faite qu'à un cours qui est dans la liste retournée par le serveur.
     *
     * @param client Le canal de communication au client pour cette commande.
     * @param cmd Le nom de la commande à exécuter.
     * @param arg Les données additionnelles associées à ce type de commande.
     */
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
     @param client Le canal de communication au client pour cette commande.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(Channel client, String arg) {
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
     @param client Le canal de communication au client pour cette commande.
     */
    public void handleRegistration(Channel client) {
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

