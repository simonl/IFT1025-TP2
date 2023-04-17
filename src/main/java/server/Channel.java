package server;

/**
 * L'interface Channel représente un canal de communication de haut niveau,
 * qui permet d'échanger des objets avec un client/serveur.
 * <br>
 * Tous les types d'objet transmis avec un Channel doivent implémenter Serializable.
 * <br>
 * L'ordre d'appel des méthodes et les types à utiliser, sont dictés par le protocol de chaque implémentation.
 */
public interface Channel {
    /**
     * Recoît un objet de type T envoyé par le pair de connection.
     * <br>
     * Cette méthode bloque le fil d'exécution jusqu'à ce qu'un object soit reçu.
     *
     * @return L'objet envoyé par le pair de connection.
     * @param <T> Le type d'objet attendu à cette étape du protocol.
     * @throws Exception Si un objet ne peut pas être reçu.
     */
    <T> T read() throws Exception;

    /**
     * Envoie un objet de type T au pair de connection.
     *
     * @param <T> Le type d'objet attendu à cette étape du protocol.
     * @param o L'objet qui doit être reçu par le pair de connection.
     * @throws Exception Si un objet ne peut pas être envoyé.
     */
    <T> void write(T o) throws Exception;
}
