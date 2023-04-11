package client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import server.Server;
import server.models.Course;
import javafx.fxml.Initializable;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.List;

public class HelloController {

    HelloApplication view;
    RegistrationForm inputs;
    List<Course> coursesList;
    public HelloController(HelloApplication view) {
        this.view = view;
        view.charger.setOnAction(event -> displayCourses());
        view.envoyer.setOnAction(event -> validateInputs());

    }
    public void displayCourses(){
        try {
            this.coursesList = Console.Load((String) view.periode.getValue());
            view.tableView.getItems().clear();
            view.tableView.getItems().addAll(coursesList);
        } catch (Exception exception){

        }
    }


    public void validateInputs(){
        try{
            RegistrationForm inputs = view.getters();
            String identifier = inputs.getMatricule();
            Course choosedCourse = inputs.getCourse();
            String message = new String("Le formulaire est invalide.");
            view.errorScreen.setTitle("Erreur");
            view.errorScreen.setHeaderText(message);

            try {

                if (inputs.getEmail().contains("@")){
                    int i = inputs.getEmail().indexOf("@");
                    String substring1 = inputs.getEmail().substring(0,i);
                    String substring2 = inputs.getEmail().substring(i);
                    if (substring2.length() < 2 || substring1.length() < 1){
                        throw new Exception();
                    }
                } else {
                    throw new Exception();

                }

                Integer.parseInt(identifier);

                if (identifier.length() != 6) {
                    throw new ArithmeticException();
                }

                if (coursesList.contains(choosedCourse)){
                    register(inputs);
                }

            } catch (ArithmeticException exception){

                view.errorScreen.setContentText("Le matricule est invalide.");
                view.errorScreen.show();

            } catch (NumberFormatException exception){

                view.errorScreen.setContentText("Le matricule est invalide.");
                view.errorScreen.show();

            } catch (Exception exception){

                view.errorScreen.setContentText("L'adresse Email est invalide.");
                view.errorScreen.show();

            }

        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void register(RegistrationForm inputs){
        try {

            Console.Register(inputs);

        } catch (Exception exception){

        }
    }


}
