package server;

import static org.junit.Assert.assertTrue;

import javafx.util.Pair;
import org.junit.Test;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public static void main(String[] args) throws Exception {
        try (Scanner userIn = new Scanner(System.in)) {
            while (true) {
                System.out.print(">>> ");
                String commandLine = userIn.nextLine();
                String[] words = commandLine.split(" ");

                try (Socket client = new Socket("localhost", ServerLauncher.PORT)) {
                    ObjectOutputStream serverOut = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream serverIn = new ObjectInputStream(client.getInputStream());

                    switch (words[0]) {
                        case Server.LOAD_COMMAND: {
                            serverOut.writeObject(words[0] + " " + words[1]);
                            serverOut.flush();

                            List<Course> courses = (List<Course>) serverIn.readObject();
                            for (Course course : courses) {
                                System.out.println(course.getCode() + " " + course.getName() + " " + course.getSession());
                            }

                            break;
                        }
                        case Server.REGISTER_COMMAND: {
                            serverOut.writeObject(words[0]);
                            serverOut.flush();

                            RegistrationForm form = new RegistrationForm(words[1], words[2], words[3], words[4], new Course(null, words[5], words[6]));
                            serverOut.writeObject(form);
                            serverOut.flush();

                            String message = (String) serverIn.readObject();
                            System.out.println(message);

                            break;
                        }
                        default:
                            throw new IllegalArgumentException(words[0]);
                    }
                }
            }
        }
    }
}
