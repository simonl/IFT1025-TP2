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
    }

    public void getCourses(){
        try {
            List<Course> listeCours = console.Load(vue.getChoixPeriodes());
        } catch (Exception exception){

        }



    }

}
