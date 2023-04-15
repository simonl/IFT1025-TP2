package server;

/**
 * La classe ServerLauncher permet d'exécuter le server.
 */
public class ServerLauncher {
    // Port de connexion au serveur
    public final static int PORT = 1337;

    /**
     * La méthode main permet de lancer le serveur sur le port 1337.
     *
     * @param args Aucun arguments ne sont attendus sur la ligne de commande
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}