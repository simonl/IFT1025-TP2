package client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import server.Server;
import server.models.Course;

import java.io.IOException;
import java.util.List;

public class HelloController {

    HelloApplication view;
    public HelloController(HelloApplication view) {
        this.view = view;
        view.charger.setOnAction(event -> displayCourses(view.periode));
        view.envoyer.setOnAction(event -> register());


    }
    public void displayCourses(ComboBox choixPeriode){
        this.view.choixPeriodes = (String) choixPeriode.getValue();

        try {
            List<Course> listeCours = Console.Load((String) view.periode.getValue());
            view.tableView.getItems().clear();
            view.tableView.getItems().addAll(listeCours);
        } catch (Exception exception){

        }
    }

    public void register(){
        try{
            Console.Register(view.getters());
        } catch (Exception exception){

        }
    }




}
