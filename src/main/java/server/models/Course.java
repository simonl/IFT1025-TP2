package server.models;

import java.io.Serializable;

/**
 * Cet enregistrement répresente un cours auquel les étudiants peuvent s'inscrire.
 * Le code de chaque cours doit être unique.
 * Les sessions sont typiquement "Automne", "Hiver", ou "Ete".
 */
public class Course implements Serializable {

    private String name;
    private String code;
    private String session;

    /**
     * Créer un cours contenant exactement les valeurs données.
     *
     * @param name Nom descriptif du cours
     * @param code Code unique du cours
     * @param session Période de l'année où le cours est donné
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * @return Le nom descriptif du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Changer le nom descriptif du cours
     *
     * @param name Le nouveau nom descriptif
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Le code unique du cours
     */
    public String getCode() {
        return code;
    }

    /**
     * Changer le code unique identifiant ce cours.
     *
     * @param code Le nouveau code unique pour ce cours
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return La période de l'année où le cours est donné
     */
    public String getSession() {
        return session;
    }

    /**
     * Changer la période de temps où le cours sera donné.
     *
     * @param session La nouvelle période de temps où le cours sera donné
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * @return Une représentation textuelle de ce cours, utile pour le déboguage.
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
