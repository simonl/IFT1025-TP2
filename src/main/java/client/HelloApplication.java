package client;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        GridPane grille = new GridPane();

        // Grille Formulaire -------------------------------------------------------------------------------------------

        GridPane grilleFormulaire = new GridPane();

        grilleFormulaire.setPadding(new Insets(10, 10, 10, 10));
        grilleFormulaire.setHgap(10);
        grilleFormulaire.setVgap(10);

        Label prenom = new Label("Prénom");
        GridPane.setConstraints(prenom, 0, 0);

        TextField champPrenom = new TextField();
        GridPane.setConstraints(champPrenom, 1, 0);

        Label nom = new Label("Nom");
        GridPane.setConstraints(nom, 0, 1);

        TextField champNom = new TextField();
        GridPane.setConstraints(champNom, 1, 1);

        Label mail = new Label("E-mail");
        GridPane.setConstraints(mail, 0, 2);

        TextField champMail = new TextField();
        GridPane.setConstraints(champMail, 1, 2);

        Label matricule = new Label("Matricule");
        GridPane.setConstraints(matricule, 0, 3);

        TextField champMatricule = new TextField();
        GridPane.setConstraints(champMatricule, 1, 3);

        Button envoyer = new Button("envoyer");
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

        TableView tableView = new TableView<String>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn code = new TableColumn("Code");
        TableColumn cours = new TableColumn("Cours");
        tableView.getColumns().addAll(code, cours);
        GridPane.setConstraints(tableView, 0,1);


        GridPane grilleBouton = new GridPane();
        ColumnConstraints cButton = new ColumnConstraints();
        cButton.setPercentWidth(50);

        Button charger = new Button("Charger");
        GridPane.setConstraints(charger, 1, 0);

        //GridPane.setHalignment(charger, HPos.CENTER);

        ObservableList<String> periodes =
                FXCollections.observableArrayList(
                        "Hiver",
                        "Été",
                        "Automne"
                );

        ComboBox periode = new ComboBox(periodes);
        GridPane.setConstraints(periode,0,0);

        charger.setOnAction(e -> setText(periode));

        grilleBouton.getColumnConstraints().add(cButton);

        grilleBouton.getChildren().addAll(charger, periode);

        grilleCours.getRowConstraints().add(rConst);

        grilleCours.getChildren().addAll(text2, tableView, grilleBouton);

        GridPane.setConstraints(grilleBouton, 0,2);
        GridPane.setConstraints(grilleCours, 0, 0);
        GridPane.setConstraints(grilleInscription, 1, 0);
        grille.getChildren().addAll(grilleCours ,grilleInscription);

        Scene scene = new Scene(grille, 700, 500);
        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();



    }
    String choixPeriodes;
    TableColumn cours;
    TableColumn codeCours;
    public void setText(ComboBox choixPeriode){
        this.choixPeriodes = (String) choixPeriode.getValue();
    }

    public String getChoixPeriodes() {
        return choixPeriodes;
    }

    public void setTableView(TableColumn cours, TableColumn codeCours){
        this.cours = cours;
        this.codeCours = codeCours;
    }
    public void displayCourses(String codeCours, String cours){
        this.cours.setCellFactory(new PropertyValueFactory<Console, String>(codeCours));
    }


    public static void main(String[] args) {
        launch(args);
    }
}