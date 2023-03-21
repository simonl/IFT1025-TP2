package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public final static String DATABASE_PATH = "./src/main/java/server/data";
    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        try {
            List<Course> courses = loadCourseList(arg);
            objectOutputStream.writeObject(courses);
            objectOutputStream.flush();
        } catch (Exception e) {
            // Abandonner l'opération.
            // Le client ne recevra pas de résultat.
        }
    }

    private List<Course> loadCourseList(String sessionFilter) throws Exception {
        File database = new File(DATABASE_PATH + "/cours.txt");
        try (Scanner scanner = new Scanner(new FileInputStream(database))) {
            return parseCourseList(scanner, sessionFilter);
        }
    }

    private List<Course> parseCourseList(Scanner scanner, String sessionFilter) {
        List<Course> courses = new ArrayList<Course>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Course course = parseCourse(line);
            if (course.getSession().equals(sessionFilter)) {
                courses.add(course);
            }
        }
        return courses;
    }

    private Course parseCourse(String line) {
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\t");

        String code = scanner.next();
        String name = scanner.next();
        String session = scanner.next();

        return new Course(name, code, session);
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {
            RegistrationForm form = (RegistrationForm) objectInputStream.readObject();
            String message = RegisterCourse(form);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (Exception e) {
            // Abandonner l'opération.
            // Le client ne recevra pas de résultat.
        }
    }

    private String RegisterCourse(RegistrationForm form) throws Exception {
        if (CourseExists(form.getCourse())) {
            SaveRegistration(form);
            return "OK";
        }
        return "NO";
    }

    private void SaveRegistration(RegistrationForm form) throws IOException {
        boolean append = true;
        File database = new File(DATABASE_PATH + "/inscription.txt");
        try (PrintWriter out = new PrintWriter(new FileWriter(database, append))) {
            String line = serializeRegistration(form);
            out.println(line);
            out.flush();
        }
    }

    private boolean CourseExists(Course chosenCourse) throws Exception {
        List<Course> availableCourses = loadCourseList(chosenCourse.getSession());
        for (Course availableCourse : availableCourses) {
            if (availableCourse.getCode().equals(chosenCourse.getCode())) {
                return true;
            }
        }
        return false;
    }

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

