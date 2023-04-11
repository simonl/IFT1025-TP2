package client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import server.models.Course;
import server.models.RegistrationForm;


import java.io.IOException;

/**
 *
 */
public class RegistrationView extends Application {
    //String choixPeriodes;
    TableView tableView;
    //Button charger, envoyer;
    Button charger = new Button("charger");
    Button envoyer = new Button("envoyer");
    ComboBox periode;
    TextField champPrenom, champNom, champMail, champMatricule;
    Alert errorScreen;
    Alert confirmationScreen;
    RegistrationController controler;

    @Override
    public void start(Stage stage) throws IOException {

        controler = new RegistrationController(this);

        GridPane grille = new GridPane();

        // Grille Formulaire -------------------------------------------------------------------------------------------

        GridPane grilleFormulaire = new GridPane();

        grilleFormulaire.setPadding(new Insets(10, 10, 10, 10));
        grilleFormulaire.setHgap(10);
        grilleFormulaire.setVgap(10);

        Label prenom = new Label("Prénom");
        GridPane.setConstraints(prenom, 0, 0);

        champPrenom = new TextField();
        GridPane.setConstraints(champPrenom, 1, 0);

        Label nom = new Label("Nom");
        GridPane.setConstraints(nom, 0, 1);

        champNom = new TextField();
        GridPane.setConstraints(champNom, 1, 1);

        Label mail = new Label("Email");
        GridPane.setConstraints(mail, 0, 2);

        champMail = new TextField();
        GridPane.setConstraints(champMail, 1, 2);

        Label matricule = new Label("Matricule");
        GridPane.setConstraints(matricule, 0, 3);

        champMatricule = new TextField();
        GridPane.setConstraints(champMatricule, 1, 3);

        envoyer = new Button("envoyer");
        GridPane.setConstraints(envoyer, 1, 4);


        grilleFormulaire.getChildren().addAll(
                prenom, champPrenom,
                nom, champNom,
                mail, champMail,
                matricule, champMatricule,
                envoyer
        );

        // Grille Insccription -----------------------------------------------------------------------------------------
        GridPane grilleInscription = new GridPane();
        grilleInscription.setPadding(new Insets(10, 10, 100, 100));

        Label text1 = new Label("Formulaire d'inscription");
        text1.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        text1.setAlignment(Pos.CENTER);
        GridPane.setConstraints(text1, 0, 0);

        GridPane.setConstraints(grilleFormulaire, 0, 1);

        grilleInscription.getChildren().addAll(text1, grilleFormulaire);

//----------------------------------------------------------------------------------------------------------------------

       GridPane grilleCours = new GridPane();
       grilleCours.setPadding(new Insets(10,10,10,10));
       RowConstraints rConst = new RowConstraints();
       rConst.setPercentHeight(0);

        Label text2 = new Label("Liste de Cours");
        text2.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        //grilleCours.setAlignment(text2, Pos.CENTER);
        GridPane.setConstraints(text2, 0, 0);

        tableView = new TableView<String>();
        tableView.setEditable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn code = new TableColumn("Code");
        code.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
        TableColumn cours = new TableColumn("Cours");
        cours.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        tableView.getColumns().addAll(code, cours);
        GridPane.setConstraints(tableView, 0,1);


        GridPane grilleBouton = new GridPane();
        ColumnConstraints cButton = new ColumnConstraints();
        cButton.setPercentWidth(50);

        charger = new Button("Charger");
        GridPane.setConstraints(charger, 1, 0);

        ObservableList<String> periodes =
                FXCollections.observableArrayList(
                        "Hiver",
                        "Ete",
                        "Automne"
                );

        periode = new ComboBox(periodes);
        GridPane.setConstraints(periode,0,0);

        grilleBouton.getColumnConstraints().add(cButton);

        grilleBouton.getChildren().addAll(charger, periode);

        grilleCours.getRowConstraints().add(rConst);

        grilleCours.getChildren().addAll(text2, tableView, grilleBouton);

        GridPane.setConstraints(grilleBouton, 0,2);
        GridPane.setConstraints(grilleCours, 0, 0);
        GridPane.setConstraints(grilleInscription, 1, 0);
        grille.getChildren().addAll(grilleCours ,grilleInscription);


        charger.setOnAction(event -> controler.displayCourses());
        envoyer.setOnAction(event -> controler.validateInputs());

        errorScreen = new Alert(Alert.AlertType.ERROR);
        confirmationScreen = new Alert(Alert.AlertType.INFORMATION);

        Scene scene = new Scene(grille, 700, 500);
        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();
    }

    public RegistrationForm getters(){
        String firstName = champPrenom.getText();
        String lastName = champNom.getText();
        String mail = champMail.getText();
        String indentifier = champMatricule.getText();
        Course choosedCourse = (Course) tableView.getSelectionModel().getSelectedItem();
        RegistrationForm registrations = new RegistrationForm(firstName, lastName, mail, indentifier, choosedCourse);

        return registrations;
    }
}