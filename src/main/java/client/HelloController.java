package client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import server.Server;
import server.models.Course;

import java.io.IOException;
import java.util.List;

public class HelloController {

    HelloApplication vue;
    Console console;

    public HelloController(HelloApplication vue, Console console) {
        this.vue = vue;
        this.console = console;

        vue.charger.setOnAction(e -> getCourses());
    }

    public void getCourses(){
        try {
            List<Course> listeCours = Console.Load((String) vue.periode.getValue());
            vue.tableView.getItems().addAll(listeCours);
            //vue.displayCourses((String) listeCours.get(1), );
        } catch (Exception exception){

        }



    }

}
