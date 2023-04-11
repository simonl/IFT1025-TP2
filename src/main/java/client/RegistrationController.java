package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationController {

    RegistrationView view;
    RegistrationForm inputs;
    List<Course> coursesList;
    public RegistrationController(RegistrationView view) {
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

        ArrayList<String> errorList = new ArrayList<>();
        errorList.add("Le formulaire est invalide.\n");
        RegistrationForm inputs = view.getters();
        String identifier = inputs.getMatricule();
        Course choosedCourse = inputs.getCourse();
        view.errorScreen.setTitle("Erreur");
        view.errorScreen.setHeaderText("Erreur");

        try {
            if (inputs.getCourse() == null) {
                throw new IOException();
            }
        } catch (IOException exception){
            errorList.add("Vous devez choisir un cours!\n");
        }

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
        } catch (Exception exception){
            errorList.add("Le champ 'Email' est invalide!\n");
        }

        try {
            Integer.parseInt(identifier);
            if (identifier.length() != 6) {
                throw new ArithmeticException();
            }
        } catch (ArithmeticException exception){
            errorList.add("Le champ 'Matricule' est invalide!\n");
        }

        try {
            if (!coursesList.contains(choosedCourse) && choosedCourse != null){
                throw new Exception();
            }
        } catch (Exception exception){
            errorList.add("Le cours choisi est invalide!\n");
        }

        if (errorList.size() == 1){
            register(inputs);
        } else {
            String list = "";
            for(int i = 0; i < errorList.size(); ++i){
                list += errorList.get(i);
            }
            view.errorScreen.setContentText(list);
            view.errorScreen.show();
        }

    }

    public void register(RegistrationForm inputs){
        try {

            Console.Register(inputs);
            view.confirmationScreen.setHeaderText("Message");
            view.confirmationScreen.setContentText("Félicitation! " + inputs.getNom() + " " + inputs.getPrenom() + " est inscrit(e)\n" +
                    " avec succès pour le cours " + inputs.getCourse().getCode());
            view.confirmationScreen.show();

        } catch (Exception exception){

        }
    }

}
