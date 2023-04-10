package client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import server.Server;
import server.models.Course;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.util.List;

public class HelloController {

    HelloApplication view;
    public HelloController(HelloApplication view) {
        this.view = view;
        //view.charger.setOnAction(event1 -> displayCourses(view.periode));
        //view.envoyer.setOnAction(event2 -> register());

    }
    public void displayCourses(ComboBox choixPeriode){
        //String ChoixPeriodes = (String) choixPeriode.getValue();
        try {
            List<Course> listeCours = Console.Load((String) view.periode.getValue());
            view.tableView.getItems().clear();
            view.tableView.getItems().addAll(listeCours);
        } catch (Exception exception){

        }
    }

    public void register(){
        try{
            String message = Console.Register(view.getters());
            System.out.println(message);
        } catch (Exception exception){

        }
    }


}
