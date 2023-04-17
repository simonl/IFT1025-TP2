package client;

import server.ObjectSocket;
import server.Server;
import server.ServerLauncher;
import server.models.Course;
import server.models.RegistrationForm;

import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * La classe Console représente le client de ligne de commande simple permettant de vérifier la fonctionnalité du
 * serveur.
 */
public class Console {
    /**
     * Les noms de sessions attendus lorsqu'on charge la liste de cours.
     */
    public final static String[] SESSIONS = new String[] { "Automne", "Hiver", "Ete" };

    /**
     * Cette méthode affiche le méssage de bienvenue dans la console et demande aussi l'input de l'utilisateur.
     *
     * @param args Aucun arguments ne sont attendus sur la ligne de commande
     */
    public static void main(String[] args) {
        // Affichage du méssage de bienvenue
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        System.out.println();

        // Récupération de l'input de l'utilisateur et validation de l'input
        try (Scanner userIn = new Scanner(System.in)) {
            while (true) {
                try {
                    interactUser(userIn);
                } catch (Exception ex) {
                    System.out.println(ex);
                    System.out.println();
                }
            }
        }
    }

    /**
     * La méthode interactUser affiche les différentes sessions ainsi que les cours disponibles à la session choisie
     * par l'utilisateur.
     * Elle demande également à lUtilisateur d'entrer les informations nécéssaires à l'inscription et affiche le méssage
     * de confirmation d'inscription.
     * @param userIn Inputs de l'utilisateur.
     * @throws Exception
     */
    private static void interactUser(Scanner userIn) throws Exception {
        // Affichage du méssage de bienvenue et des sessions
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        for (int index = 0; index < SESSIONS.length; index++) {
            System.out.println((index + 1) + ". " + SESSIONS[index]);
        }

        // Affichage de "Choix" et récupération de la session choisie par l'utilisateur
        System.out.print("> Choix: ");
        int sessionChoice = Integer.parseInt(userIn.nextLine());
        String sessionName = SESSIONS[sessionChoice - 1];
        System.out.println();

        List<Course> courses = Load(sessionName);

        // Affichage des cours disponibles dans la période choisie par l'utilisateur et de l'option de retour au choix
        // de la période.
        System.out.println("Veuillez choisir un cours auquel vous inscrire pour la session d'" + sessionName.toLowerCase() + ":");
        for (int index = 0; index < courses.size(); index++) {
            Course course = courses.get(index);
            System.out.println((index + 1) + ". " + course.getCode() + "\t" + course.getName());
        }
        System.out.println("0. Consulter les cours offerts pour une autre session");

        // Affichage de "Choix" et récupération du cours choisie par l'utilisateur
        System.out.print("> Choix: ");
        int courseChoice = Integer.parseInt(userIn.nextLine());
        System.out.println();

        if (courseChoice == 0) {
            return;
        }

        String courseCode = courses.get(courseChoice - 1).getCode();

        // Affichage des champs à remplir et demande des informations de l'utilisateur
        System.out.print("> Veuillez saisir votre prénom: ");
        String firstName = userIn.nextLine();
        System.out.print("> Veuillez saisir votre nom: ");
        String lastName = userIn.nextLine();
        System.out.print("> Veuillez saisir votre email: ");
        String email = userIn.nextLine();
        System.out.print("> Veuillez saisir votre matricule: ");
        String matricule = userIn.nextLine();
        System.out.println();

        // Création d'un RegistrationForm contenant les informations de l'utilisateur et inscription de l'utilisateur
        RegistrationForm form = new RegistrationForm(firstName, lastName, email, matricule, new Course(null, courseCode, sessionName));
        String message = Register(form);

        // Vérification si l'utilisateur a bien été inscrit ou non et affichage du méssage en conséquence
        if (message.equals("OK")) {
            System.out.println("Félicitations! Inscription réussie de " + form.getPrenom() + " au cours " + form.getCourse().getCode());
            System.out.println();
        } else {
            System.out.println("L'inscription n'a pas réussie: " + message);
            System.out.println();
        }
    }

    /**
     * Demander au Serveur la liste de cours disponible durant la session donnée.
     *
     * @param session la session pour laquelle on veut récupérer la liste des cours
     * @return la liste de cours disponibles durant la session donnée
     * @throws Exception si une erreur se produit avec la communication réseau
     */
    public static List<Course> Load(String session) throws Exception {
        return (List<Course>) Request(Server.LOAD_COMMAND, session);
    }

    /**
     * Demander au Serveur d'inscrire un étudiant à un cours.
     * Le cours choisi doit déjà exister dans la liste de cours pour la session donnée.
     *
     * @param form les détails de l'étudiant et du cours choisi
     * @return Un message de succès "OK" ou un message d'erreur provenant du Serveur
     * @throws Exception si une erreur se produit avec la communication réseau
     */
    public static String Register(RegistrationForm form) throws Exception {
        return (String) Request(Server.REGISTER_COMMAND, form);
    }

    private static Object Request(String command, Object arg) throws Exception {
        Socket socket = new Socket("localhost", ServerLauncher.PORT);
        try (ObjectSocket client = new ObjectSocket(socket)) {
            switch (command) {
                case Server.LOAD_COMMAND: {
                    client.write(command + " " + arg);

                    return client.read();
                }
                case Server.REGISTER_COMMAND: {
                    client.write(command);
                    client.write(arg);

                    return client.read();
                }
                default:
                    throw new IllegalArgumentException(command);
            }
        }
    }
}
