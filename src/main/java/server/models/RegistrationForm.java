package server.models;

import java.io.Serializable;

/**
 * Cet enregistrement représente une demande d'inscription d'un étudiant à un des cours disponibles.
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Créer une demande d'inscription d'un étudiant au cours donné.
     *
     * @param prenom Le prénom de l'étudiant.
     * @param nom Le nom de famille de l'étudiant.
     * @param email L'address courriel de l'étudiant.
     * @param matricule Le numéro d'identification de l'étudiant.
     * @param course Le cours (d'une session en particulière) auquel inscrire l'étudiant.
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Retourne le prénom de l'étudiant.
     *
     * @return Le prénom de l'étudiant.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Changer le prénom de l'étudiant.
     *
     * @param prenom Le nouveau prénom de l'étudiant.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne le nom de famille de l'étudiant.
     *
     * @return Le nom de famille de l'étudiant.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Changer le nom de famille de l'étudiant.
     *
     * @param nom Le nouveau nom de famille de l'étudiant.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne l'adresse courriel de l'étudiant.
     *
     * @return L'adresse courriel de l'étudiant.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changer l'adresse courriel de l'étudiant.
     *
     * @param email La nouvelle adresse courriel de l'étudiant.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le matricule identifiant de l'étudiant.
     *
     * @return Le matricule identifiant de l'étudiant.
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Changer le matricule identifiant de l'étudiant.
     *
     * @param matricule Le nouveau matricule identifiant de l'étudiant.
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Retourne le cours auquel l'étudiant veut s'inscrire.
     *
     * @return Le cours auquel l'étudiant veut s'inscrire.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Changer le cours auquel inscrire l'étudiant.
     *
     * @param course Le nouveau cours auquel l'étudiant veut s'inscrire.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @return Une représentation textuelle de cette demande d'inscription, utile pour le déboguage.
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
