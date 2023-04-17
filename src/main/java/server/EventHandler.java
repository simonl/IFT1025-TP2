package server;

/**
 * L'interface EventHandler représente une implémentation d'une commande du serveur.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * La méthode handle sera appelée pour chaque connexion d'un client, avec une commande et argument.
     * L'implémentation devrait faire attention de ne pas bloquer l'exécution, et de ne pas lancer d'exceptions.
     *
     * @param cmd Le nom de la commande demandée par le client.
     * @param arg Les données additionnelles nécessaires pour l'exécution de la commande.
     */
    void handle(String cmd, String arg);
}
