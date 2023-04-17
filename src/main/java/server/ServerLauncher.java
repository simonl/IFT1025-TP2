package server;

/**
 * La classe ServerLauncher permet d'exécuter le server.
 */
public class ServerLauncher {
    /**
     * Port 1337 de connexion au serveur, connu des clients.
     */
    public final static int PORT = 1337;

    /**
     * La méthode main permet de lancer l'application serveur.
     *
     * @param args Aucun arguments ne sont attendus sur la ligne de commande
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            // L'application bloque ici jusqu'à ce que le processus soit interrompu
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}