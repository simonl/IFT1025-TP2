package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * La classe Database gère l'accès aux fichiers contenant la liste des cours disponibles, et la liste des inscriptions des élèves.
 */
public class Database {
    /**
     * Le client peut reconnaitre que sa demande d'inscription est acceptée après avoir reçu ce message de confirmation.
     */
    public final static String SUCCESS_MESSAGE = "OK";

    /**
     * Si le cours demandé dans une inscription n'est pas reconnu, ce message de confirmation est renvoyé.
     */
    public final static String INVALID_COURSE_MESSAGE = "Le cours choisi est invalide!";

    /**
     * Chemin dans le système de fichiers vers la liste de cours et la liste d'inscriptions.
     */
    private final static String DATABASE_PATH = "./src/main/java/server/data";

    /**
     * Charger la liste de cours du système de fichiers. Seulement les cours de la session spécifiée sont retournés.
     *
     * @param sessionFilter Le nom de la session d'intérêt au client.
     * @return La liste des cours disponibles durant la session spécifiée.
     * @throws IOException Si on échoue durant la lecture du fichier contenant la liste de cours.
     */
    public static List<Course> loadCourseList(String sessionFilter) throws IOException {
        File database = new File(DATABASE_PATH + "/cours.txt");
        try (Scanner scanner = new Scanner(new FileInputStream(database))) {
            return parseCourseList(scanner, sessionFilter);
        }
    }

    /**
     * Décoder la liste des cours de leur format textuel.
     *
     * @param scanner L'objet qui nous permet de décoder la liste de cours, pas à pas.
     * @param sessionFilter Le nom de la session d'intérêt au client.
     * @return La liste des cours disponibles durant la session spécifiée.
     */
    private static List<Course> parseCourseList(Scanner scanner, String sessionFilter) {
        List<Course> courses = new ArrayList<Course>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Course course = parseCourse(line);
            // On ignore les cours des autres sessions
            if (course.getSession().equals(sessionFilter)) {
                courses.add(course);
            }
        }
        return courses;
    }

    /**
     * Décoder un seul cours de son format textuel.
     *
     * @param line Une ligne de texte représentant un cours.
     * @return Le cours en tant qu'objet.
     */
    private static Course parseCourse(String line) {
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\t");

        String code = scanner.next();
        String name = scanner.next();
        String session = scanner.next();

        return new Course(name, code, session);
    }

    /**
     * Enregistre l'inscription d'un élève à un cours dans le système de fichiers.
     * Pour accepter l'inscription, on vérifie que le cours est valide pour sa session spécifiée.
     *
     * @param form Le formulaire qui identifie l'élève et son cours choisi.
     * @return Un message de confirmation si le cours est valide et l'inscription acceptée.
     * @throws IOException Si on échoue durant l'écriture du fichier des inscriptions.
     */
    public static String registerCourse(RegistrationForm form) throws IOException {
        if (courseExists(form.getCourse())) {
            SaveRegistration(form);
            return SUCCESS_MESSAGE;
        }
        return INVALID_COURSE_MESSAGE;
    }

    /**
     * Enregistre l'inscription d'un élève à un cours dans le système de fichiers.
     * On s'assure que seulement un Thread à la fois peut écrire dans le fichier d'inscriptions.
     *
     * @param form Le formulaire qui identifie l'élève et son cours choisi.
     * @throws IOException Si on échoue durant l'écriture du fichier des inscriptions.
     */
    private static synchronized void SaveRegistration(RegistrationForm form) throws IOException {
        boolean append = true;
        File database = new File(DATABASE_PATH + "/inscription.txt");
        try (PrintWriter out = new PrintWriter(new FileWriter(database, append))) {
            String line = serializeRegistration(form);
            out.println(line);
            out.flush();
        }
    }

    /**
     * Vérifier que le cours donné existe dans la liste de cours disponibles.
     *
     * @param chosenCourse Le cours choisi pour une inscription.
     * @return Si le cours est valide.
     * @throws IOException Si on échoue durant la lecture du fichier contenant la liste de cours.
     */
    private static boolean courseExists(Course chosenCourse) throws IOException {
        // On cherche un cours connu avec le même code et durant la même session
        List<Course> availableCourses = loadCourseList(chosenCourse.getSession());
        for (Course availableCourse : availableCourses) {
            if (availableCourse.getCode().equals(chosenCourse.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Écrire une d'inscription dans son format textuel, pour être écrit dans le système de fichiers.
     *
     * @param form Le formulaire qui identifie l'élève et son cours choisi.
     * @return L'inscription en format textuel.
     */
    private static String serializeRegistration(RegistrationForm form) {
        String[] fields = new String[] {
                form.getCourse().getSession(),
                form.getCourse().getCode(),
                form.getMatricule(),
                form.getPrenom(),
                form.getNom(),
                form.getEmail()
        };

        return String.join("\t", fields);
    }
}
