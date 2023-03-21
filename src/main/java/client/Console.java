package client;

import server.Server;
import server.ServerLauncher;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Console {
    public final static String[] SESSIONS = new String[] { "Automne", "Hiver", "Ete" };

    public static void main(String[] args) {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        System.out.println();

        try (Scanner userIn = new Scanner(System.in)) {
            while (true) {
                try {
                    InteractUser(userIn);
                } catch (Exception ex) {
                    System.out.println(ex);
                    System.out.println();
                }
            }
        }
    }

    private static void InteractUser(Scanner userIn) throws Exception {
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        for (int index = 0; index < SESSIONS.length; index++) {
            System.out.println((index + 1) + ". " + SESSIONS[index]);
        }

        System.out.print("> Choix: ");
        int sessionChoice = Integer.parseInt(userIn.nextLine());
        String sessionName = SESSIONS[sessionChoice - 1];
        System.out.println();

        List<Course> courses = (List<Course>) Request(Server.LOAD_COMMAND, sessionName);

        System.out.println("Veuillez choisir un cours auquel vous inscrire pour la session d'" + sessionName.toLowerCase() + ":");
        for (int index = 0; index < courses.size(); index++) {
            Course course = courses.get(index);
            System.out.println((index + 1) + ". " + course.getCode() + "\t" + course.getName());
        }
        System.out.println("0. Consulter les cours offerts pour une autre session");

        System.out.print("> Choix: ");
        int courseChoice = Integer.parseInt(userIn.nextLine());
        System.out.println();

        if (courseChoice == 0) {
            return;
        }

        String courseCode = courses.get(courseChoice - 1).getCode();

        System.out.print("> Veuillez saisir votre prénom: ");
        String firstName = userIn.nextLine();
        System.out.print("> Veuillez saisir votre nom: ");
        String lastName = userIn.nextLine();
        System.out.print("> Veuillez saisir votre email: ");
        String email = userIn.nextLine();
        System.out.print("> Veuillez saisir votre matricule: ");
        String matricule = userIn.nextLine();
        System.out.println();

        RegistrationForm form = new RegistrationForm(firstName, lastName, email, matricule, new Course(null, courseCode, sessionName));
        String message = (String) Request(Server.REGISTER_COMMAND, form);

        if (message.equals("OK")) {
            System.out.println("Félicitations! Inscription réussie de " + form.getPrenom() + " au cours " + form.getCourse().getCode());
            System.out.println();
        } else {
            System.out.println("L'inscription n'a pas réussie: " + message);
            System.out.println();
        }
    }

    private static Object Request(String command, Object arg) throws Exception {
        try (Socket client = new Socket("localhost", ServerLauncher.PORT)) {
            ObjectOutputStream serverOut = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(client.getInputStream());

            switch (command) {
                case Server.LOAD_COMMAND: {
                    serverOut.writeObject(command + " " + arg);
                    serverOut.flush();

                    return serverIn.readObject();
                }
                case Server.REGISTER_COMMAND: {
                    serverOut.writeObject(command);
                    serverOut.flush();

                    serverOut.writeObject((RegistrationForm) arg);
                    serverOut.flush();

                    return serverIn.readObject();
                }
                default:
                    throw new IllegalArgumentException(command);
            }
        }
    }
}
