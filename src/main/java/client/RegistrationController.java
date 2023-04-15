package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe RegistrationController représente la classe controleur du modèle MVC.
 * Elle contient le code qui permet de faire la liaison entre la classe RegistrationView
 * et la classe Console.
 * Donc, les actions à éffectuer lorsqu'on click sur un boutton.
 */
public class RegistrationController {

    // Déclaration de la vue.
    private RegistrationView view;

    // Variable contenant la liste des cours.
    List<Course> coursesList;
    public RegistrationController(RegistrationView view) {
        this.view = view;
    }

    /**
     * La méthode displayCourse permet de récupérer les cours de la session choisie par l'utilisateur et de les
     * afficher dans l'interface graphique.
     */
    public void displayCourses(){
        try {
            // Récupération de la liste de cours.
            this.coursesList = Console.Load(view.getSession());

            // Ajout des éléments dans le tableview du GUI.
            view.fillCoursesList(this.coursesList);
        } catch (Exception error){
            view.showErrors(List.of(error.getMessage()));
        }
    }

    /**
     * La méthode validateInputs permet de valider les informations entrées par l'utilisateur.
     * Elles affichent dans le GUI un message d'erreur si les informations sont erronées ou un message de confirmation
     * d'inscription si elles sont corrects.
     */
    public void validateInputs(){
        // Liste qui contiendra les méssages d'erreur
        ArrayList<String> errorList = new ArrayList<>();
        errorList.add("Le formulaire est invalide.\n");

        // Récupération du matricule et du cours choisi par l'utilisateur
        RegistrationForm inputs = view.getRegistration();
        String identifier = inputs.getMatricule();
        Course choosedCourse = inputs.getCourse();

        // Vérification des entrées de l'utisateur
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
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception){
            errorList.add("Le champ 'Matricule' est invalide!\n");
        }

        try {
            if (!coursesList.contains(choosedCourse) && choosedCourse != null){
                throw new Exception();
            }
        } catch (Exception exception){
            errorList.add("Le cours choisi est invalide!\n");
        }

        // Appel de la méthode register s'il n'y a pas d'erreur. Au cas contraire, afficher l'erreur
        if (errorList.size() == 1){
            register(inputs);
        } else {
            view.showErrors(errorList);
        }

    }

    /**
     * La méthode register permet d'inscrire un étudiant et affiche le méssage de confirmation.
     * @param inputs RegistrationForm contenant les informations de l'utilisateur.
     */
    public void register(RegistrationForm inputs){
        try {
            // Enrégistrement de l'utilisateur
            Console.Register(inputs);

            // Affichage du message de confirmation
            view.showConfirmation("Félicitation! " + inputs.getNom() + " " + inputs.getPrenom() + " est inscrit(e)\n" +
                    "avec succès pour le cours " + inputs.getCourse().getCode());
        } catch (Exception error){
            view.showErrors(List.of(error.getMessage()));
        }
    }

}
