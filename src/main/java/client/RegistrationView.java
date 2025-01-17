package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import server.models.Course;
import server.models.RegistrationForm;


import java.io.IOException;
import java.util.List;

/**
 * La classe RegistrationView représence la classe View dans le model MVC.
 * Elle permet donc de créer et de modéliser l'interface graphique GUI.
 * Elle ne contient que les éléments graphique du projet.
 */
public class RegistrationView extends Application {
    // Tableview contenant la liste des cours.
    private TableView tableView;
    // Boutons charger et envoyer.
    private Button loadButton = new Button("charger");
    private Button sendButton = new Button("envoyer");
    // ComboBox contenant les sessions.
    private ComboBox sessions;
    // Champs des informations à remplir.
    private TextField lastNameField, firstNameField, mailField, identifierField;

    // Écran d'erreur et de confirmation d'inscription.
    private Alert errorScreen;
    private Alert confirmationScreen;

    // Controleur
    private RegistrationController controller;

    /**
     * La méthode start permet d'afficher les éléments de l'interface GUI.
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        controller = new RegistrationController(this);

        // Grille contenant toutes les autres grilles.
        GridPane grille = new GridPane();
        grille.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Grille contenant les éléments d'affichage pour la partie correspondant aux champs à compléter.
        GridPane grilleFormulaire = new GridPane();
        grilleFormulaire.setPadding(new Insets(10, 10, 10, 10));
        grilleFormulaire.setHgap(10);
        grilleFormulaire.setVgap(10);

        // Création des champs à compléter par l'utilisateur.
        Label firstName = new Label("Prénom");
        GridPane.setConstraints(firstName, 0, 0);

        lastNameField = new TextField();
        GridPane.setConstraints(lastNameField, 1, 0);

        Label lastName = new Label("Nom");
        GridPane.setConstraints(lastName, 0, 1);

        firstNameField = new TextField();
        GridPane.setConstraints(firstNameField, 1, 1);

        Label mail = new Label("Email");
        GridPane.setConstraints(mail, 0, 2);

        mailField = new TextField();
        GridPane.setConstraints(mailField, 1, 2);

        Label identifier = new Label("Matricule");
        GridPane.setConstraints(identifier, 0, 3);

        identifierField = new TextField();
        GridPane.setConstraints(identifierField, 1, 3);

        sendButton = new Button("envoyer");
        GridPane.setConstraints(sendButton, 1, 4);

        grilleFormulaire.getChildren().addAll(
                firstName, lastNameField,
                lastName, firstNameField,
                mail, mailField,
                identifier, identifierField,
                sendButton
        );

        // Grille permettant l'affichage du titre de la section ainsi que des champs à compléter.
        GridPane grilleInscription = new GridPane();
        grilleInscription.setPadding(new Insets(10, 10, 10, 10));

        Label text1 = new Label("Formulaire d'inscription");
        text1.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        text1.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(text1, HPos.CENTER);
        GridPane.setConstraints(text1, 0, 0);

        GridPane.setConstraints(grilleFormulaire, 0, 1);

        grilleInscription.getChildren().addAll(text1, grilleFormulaire);

        // Grille contenant les èléments d'affichage pour la section de choix de cours.
       GridPane grilleCours = new GridPane();
        grilleCours.setPadding(new Insets(10, 10, 10, 10));
       grilleCours.setHgap(10);
       grilleCours.setVgap(10);
       RowConstraints rConst = new RowConstraints();
       rConst.setPercentHeight(0);

        // Titre de la section
        Label text2 = new Label("Liste de Cours");
        text2.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        text2.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(text2, HPos.CENTER);
        GridPane.setConstraints(text2, 0, 0);

        // Création de la table contenant la liste des cours.
        tableView = new TableView<String>();
        tableView.setEditable(false);
        tableView.setPrefHeight(300);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn code = new TableColumn("Code");
        code.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
        TableColumn cours = new TableColumn("Cours");
        cours.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        tableView.getColumns().addAll(code, cours);
        GridPane.setConstraints(tableView, 0,1);

        // Grille permettant l'affichage du bouton charger.
        GridPane grilleBouton = new GridPane();
        ColumnConstraints cButton = new ColumnConstraints();
        cButton.setPercentWidth(50);

        // Bouton charger
        loadButton = new Button("Charger");
        GridPane.setConstraints(loadButton, 1, 0);

        // ComboBox affichant les périodes disponibles.
        ObservableList<String> sessionsList =
                FXCollections.observableArrayList(
                        "Hiver",
                        "Ete",
                        "Automne"
                );
        sessions = new ComboBox(sessionsList);
        GridPane.setHalignment(sessions, HPos.CENTER);
        GridPane.setConstraints(sessions,0,0);

        // Ajout des éléments dans les grilles et fixation des contraintes.
        grilleBouton.getColumnConstraints().add(cButton);
        grilleBouton.getChildren().addAll(loadButton, sessions);
        grilleCours.getRowConstraints().add(rConst);
        grilleCours.getChildren().addAll(text2, tableView, grilleBouton);

        GridPane.setConstraints(grilleBouton, 0,2);
        GridPane.setConstraints(grilleCours, 0, 0);
        GridPane.setConstraints(grilleInscription, 1, 0);
        grille.getChildren().addAll(grilleCours ,grilleInscription);

        // EventHandler lorsque l'utilisateur click sur les boutons charger et envoyer.
        setOnLongAction(loadButton, event -> controller.displayCourses());
        setOnLongAction(sendButton, event -> controller.validateInputs());

        errorScreen = new Alert(Alert.AlertType.ERROR);
        errorScreen.setTitle("Error");
        errorScreen.setHeaderText("Error");

        confirmationScreen = new Alert(Alert.AlertType.INFORMATION);
        confirmationScreen.setTitle("Message");
        confirmationScreen.setHeaderText("Message");

        Scene scene = new Scene(grille, 550, 350);
        scene.setFill(Color.RED);
        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Ajouter un EventHandler au click d'un bouton, sans qu'il ne bloque l'interface durant son exécution.
     *
     * @param button Le bouton auquel on veut gérer l'événement click.
     * @param handler Le EventHandler qui peut prendre beaucoup de temps à exécuter.
     */
    private static void setOnLongAction(Button button, javafx.event.EventHandler<ActionEvent> handler) {
        button.setOnAction(event -> {
            // On empêche l'utilisateur de re-clicker sur le bouton durant l'exécution
            button.setDisable(true);

            // Exécuter l'événement sur un nouveau Thread pour ne pas bloquer l'interface utilisateur
            Thread thread = new Thread(() -> {
                try {
                    // Cette étape peut prendre beaucoup de temps
                    handler.handle(event);
                } catch (Exception error) {
                    // On ignore les erreurs ici
                } finally {
                    Platform.runLater(() -> {
                        // On permet à l'utilisateur de clicker à nouveau
                        button.setDisable(false);
                    });
                }
            });

            thread.start();
        });
    }

    /**
     * La méthode getSession permet de récupérer la session choisie pour filtrer la liste de cours.
     * Les sessions sont identifiées par leur nom de saison en texte ("AUTOMNE", "HIVER", "ETE").
     *
     * @return La session à laquelle l'étudiant veut s'inscrire
     */
    public String getSession() {
        return (String) this.sessions.getValue();
    }
    /**
     * Remplir la liste de cours dans l'interface utilisateur, avec l'information provenant du serveur.
     * La liste sera remplie sur le Thread de l'interface utilisateur au prochain moment possible.
     *
     * @param coursesList La liste des cours retrouvés du serveur
     */
    public void fillCoursesList(List<Course> coursesList) {
        Platform.runLater(() -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(coursesList);
        });
    }

    /**
     * La méthode getRegistration permet de récupérer les informations entrées par l'utilisateur ainsi que le cours choisi.
     *
     * @return Retourne un RegistrationForm qui contient des strings des informations que l'utilisateur a entré ainsi
     * que le cours choisi.
     */
    public RegistrationForm getRegistration(){
        String firstName = lastNameField.getText();
        String lastName = firstNameField.getText();
        String mail = mailField.getText();
        String indentifier = identifierField.getText();
        Course choosedCourse = (Course) tableView.getSelectionModel().getSelectedItem();
        RegistrationForm registrations = new RegistrationForm(firstName, lastName, mail, indentifier, choosedCourse);

        return registrations;
    }

    /**
     * Afficher des messages d'erreur dans une boîte de dialogue.
     * Les messages seront affichés sur le Thread de l'interface utilisateur.
     *
     * @param errorList Les messages à afficher
     */
    public void showErrors(List<String> errorList) {
        Platform.runLater(() -> {
            String list = "";
            for(int i = 0; i < errorList.size(); ++i){
                list += errorList.get(i);
            }

            errorScreen.setContentText(list);
            errorScreen.show();
        });
    }

    /**
     * Afficher un message de confirmation dans une boîte de dialogue.
     * Le message sera affiché sur le Thread de l'interface utilisateur.
     *
     * @param message Le message à afficher
     */
    public void showConfirmation(String message) {
        // Affichage du méssage de confirmation sur le Thread de l'interface
        Platform.runLater(() -> {
            confirmationScreen.setContentText(message);
            confirmationScreen.show();
        });
    }
}
