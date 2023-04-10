package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {
    public final static String SUCCESS_MESSAGE = "OK";
    private final static String DATABASE_PATH = "./src/main/java/server/data";

    public static List<Course> loadCourseList(String sessionFilter) throws IOException {
        File database = new File(DATABASE_PATH + "/cours.txt");
        try (Scanner scanner = new Scanner(new FileInputStream(database))) {
            return parseCourseList(scanner, sessionFilter);
        }
    }

    private static List<Course> parseCourseList(Scanner scanner, String sessionFilter) {
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

    private static Course parseCourse(String line) {
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\t");

        String code = scanner.next();
        String name = scanner.next();
        String session = scanner.next();

        return new Course(name, code, session);
    }

    public static String registerCourse(RegistrationForm form) throws IOException {
        if (courseExists(form.getCourse())) {
            SaveRegistration(form);
            return SUCCESS_MESSAGE;
        }
        return "Selected course does not exist in the given session!";
    }

    private static void SaveRegistration(RegistrationForm form) throws IOException {
        boolean append = true;
        File database = new File(DATABASE_PATH + "/inscription.txt");
        try (PrintWriter out = new PrintWriter(new FileWriter(database, append))) {
            String line = serializeRegistration(form);
            out.println(line);
            out.flush();
        }
    }

    private static boolean courseExists(Course chosenCourse) throws IOException {
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
